package xyz.devvydont.smprpg.market.bazaar

import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.entity.Player
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint
import xyz.devvydont.smprpg.items.base.CustomCompressableBlueprint
import xyz.devvydont.smprpg.items.base.VanillaItemBlueprint
import xyz.devvydont.smprpg.items.blueprints.resources.SellableResource
import xyz.devvydont.smprpg.market.MarketConstants
import xyz.devvydont.smprpg.market.storage.MarketDataStore
import xyz.devvydont.smprpg.services.EconomyService
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.crafting.CompressionRecipeMember
import xyz.devvydont.smprpg.util.crafting.MaterialWrapper
import xyz.devvydont.smprpg.util.formatting.ComponentUtils
import kotlin.math.min
import kotlin.math.roundToLong

/**
 * Represents a single item entry in a bulk sell-all transaction.
 * For compressed items, [tier] and [compressionAmount] describe the compression tier being sold.
 * Stock adjustments are always at the base item level (quantity * compressionAmount).
 */
data class BazaarSellEntry(
    val bazaarItem: BazaarItem,
    val displayName: String,
    val quantity: Int,
    val payout: Long,
    val tier: CompressionRecipeMember? = null,
    val compressionAmount: Int = 1
)

/**
 * Represents a resolved bazaar item: either a custom plugin item or a vanilla Minecraft material.
 */
private sealed class ResolvedItem {
    data class Custom(val type: CustomItemType) : ResolvedItem()
    data class Vanilla(val material: Material) : ResolvedItem()
}

private fun resolveItem(key: String): ResolvedItem? {
    val customType = CustomItemType.entries.find { it.getKey() == key }
    if (customType != null) return ResolvedItem.Custom(customType)
    return try {
        ResolvedItem.Vanilla(Material.valueOf(key.uppercase()))
    } catch (_: IllegalArgumentException) {
        null
    }
}

/**
 * Core bazaar logic: buy, sell, price queries.
 * Items are auto-generated from CustomItemType entries with Worth > 0 and SellableResource handler.
 */
class BazaarManager(private val dataStore: MarketDataStore) {

    private val economy get() = SMPRPG.getService(EconomyService::class.java)
    private var compressionFlowCache: Map<String, List<CompressionRecipeMember>>? = null
    private var categoryTreeCache: List<BazaarCategoryNode>? = null

    fun findCompressionFlow(bazaarKey: String): List<CompressionRecipeMember>? {
        val cache = compressionFlowCache ?: buildCompressionFlowCache().also { compressionFlowCache = it }
        return cache[bazaarKey]
    }

    private fun buildCompressionFlowCache(): Map<String, List<CompressionRecipeMember>> {
        val itemService = SMPRPG.getService(ItemService::class.java)
        val result = mutableMapOf<String, List<CompressionRecipeMember>>()
        val seenHandlers = mutableSetOf<Class<*>>()

        for (type in CustomItemType.entries) {
            if (!seenHandlers.add(type.Handler)) continue
            val blueprint = itemService.getBlueprint(type)
            if (blueprint !is CustomCompressableBlueprint) continue

            val flow = blueprint.compressionFlow
            val baseKey = flow.firstOrNull()?.material?.key() ?: continue
            result[baseKey] = flow
        }
        return result
    }

    // ── Category classification (private bootstrap helper) ──────────────

    private fun classifyItem(name: String): String {
        val lower = name.lowercase()
        for ((path, keywords) in SUBCATEGORY_KEYWORDS) {
            if (keywords.any { lower.contains(it) }) return path
        }
        for ((path, keywords) in CATEGORY_KEYWORDS) {
            if (keywords.any { lower.contains(it) }) return path
        }
        return DEFAULT_CATEGORY
    }

    // ── Initialization & migration ──────────────────────────────────────

    fun initializeDefaults() {
        if (dataStore.bazaarData.items.isNotEmpty()) return

        for (type in CustomItemType.entries) {
            if (type.Worth <= 0) continue
            if (type.Handler != SellableResource::class.java) continue

            val key = type.getKey()
            val minPrice = (type.Worth * MarketConstants.BAZAAR_MIN_PRICE_MULTIPLIER).roundToLong().coerceAtLeast(1)
            val maxPrice = (type.Worth * MarketConstants.BAZAAR_MAX_PRICE_MULTIPLIER).roundToLong().coerceAtLeast(minPrice + 1)
            val maxStock = MarketConstants.BAZAAR_DEFAULT_MAX_STOCK
            val startingStock = (maxStock * MarketConstants.BAZAAR_DEFAULT_STARTING_STOCK_RATIO).toInt()

            val category = classifyItem(type.name)

            dataStore.bazaarData.items[key] = BazaarItem(
                key = key,
                displayName = type.ItemName,
                category = category,
                minPrice = minPrice,
                maxPrice = maxPrice,
                maxStock = maxStock,
                currentStock = startingStock
            )
        }

        dataStore.saveBazaar()
        SMPRPG.plugin.logger.info("Initialized ${dataStore.bazaarData.items.size} default bazaar items")
    }

    /**
     * Detects legacy enum-style category values (all-uppercase with underscores)
     * and re-classifies them using keyword matching. Preserves stock and pricing.
     */
    fun migrateIfNeeded() {
        val legacyPattern = Regex("^[A-Z_]+$")
        var changed = false

        for (item in dataStore.bazaarData.items.values.toList()) {
            if (!legacyPattern.matches(item.category)) continue

            val newCategory = classifyItem(item.key)
            dataStore.bazaarData.items[item.key] = item.copy(category = newCategory)
            changed = true
        }

        if (changed) {
            dataStore.saveBazaar()
            SMPRPG.plugin.logger.info("Migrated bazaar items from legacy enum categories to path-based categories")
        }
    }

    // ── Category tree ───────────────────────────────────────────────────

    fun getRootCategories(): List<BazaarCategoryNode> {
        return categoryTreeCache ?: BazaarCategoryNode.buildTree(getAllItems(), CATEGORY_DISPLAY_ORDER)
            .also { categoryTreeCache = it }
    }

    fun getItemsByPath(path: String): List<BazaarItem> {
        return dataStore.bazaarData.items.values.filter { it.category == path }
    }

    fun invalidateCategoryTree() {
        categoryTreeCache = null
    }

    // ── Item queries ────────────────────────────────────────────────────

    fun getAllItems(): List<BazaarItem> {
        return dataStore.bazaarData.items.values.toList()
    }

    fun getItem(key: String): BazaarItem? {
        return dataStore.bazaarData.items[key]
    }

    fun searchItems(query: String): List<BazaarItem> {
        val lowerQuery = query.lowercase().replace(" ", "").replace("_", "")
        return getAllItems().filter {
            it.displayName.lowercase().replace(" ", "").contains(lowerQuery)
        }
    }

    // ── Buy / sell ──────────────────────────────────────────────────────

    /**
     * Buy items from the bazaar.
     * Returns null on success, or an error message string.
     */
    fun buyItems(buyer: Player, bazaarItem: BazaarItem, quantity: Int): String? {
        if (quantity <= 0 || quantity > MarketConstants.BAZAAR_MAX_TRANSACTION_QUANTITY) {
            return "Invalid quantity! (1-${MarketConstants.BAZAAR_MAX_TRANSACTION_QUANTITY})"
        }

        val availableStock = if (bazaarItem.canGoOutOfStock) bazaarItem.currentStock else quantity
        val actualQuantity = quantity.coerceAtMost(availableStock)

        if (actualQuantity <= 0) {
            return "This item is out of stock!"
        }

        val totalCost = BazaarPricingEngine.calculateBulkBuyCost(bazaarItem, actualQuantity)
        if (economy.getMoney(buyer) < totalCost) {
            return "You need ${EconomyService.formatMoney(totalCost)} to buy $actualQuantity!"
        }

        val resolved = resolveItem(bazaarItem.key) ?: return "Item type not found!"

        economy.takeMoney(buyer, totalCost.toDouble())
        bazaarItem.currentStock = if (bazaarItem.canGoOutOfStock) {
            (bazaarItem.currentStock - actualQuantity).coerceAtLeast(0)
        } else {
            bazaarItem.currentStock - actualQuantity
        }

        when (resolved) {
            is ResolvedItem.Custom -> giveItems(buyer, resolved.type, actualQuantity)
            is ResolvedItem.Vanilla -> giveVanillaItems(buyer, resolved.material, actualQuantity)
        }

        buyer.sendMessage(
            ComponentUtils.success(
                ComponentUtils.merge(
                    ComponentUtils.create("Bought ${actualQuantity}x "),
                    ComponentUtils.create(bazaarItem.displayName, NamedTextColor.AQUA),
                    ComponentUtils.create(" for "),
                    ComponentUtils.create(EconomyService.formatMoney(totalCost), NamedTextColor.GOLD)
                )
            )
        )

        invalidateCategoryTree()
        dataStore.saveBazaar()
        return null
    }

    /**
     * Sell items to the bazaar.
     * Returns null on success, or an error message string.
     */
    fun sellItems(seller: Player, bazaarItem: BazaarItem, quantity: Int): String? {
        if (quantity <= 0 || quantity > MarketConstants.BAZAAR_MAX_TRANSACTION_QUANTITY) {
            return "Invalid quantity! (1-${MarketConstants.BAZAAR_MAX_TRANSACTION_QUANTITY})"
        }

        val resolved = resolveItem(bazaarItem.key) ?: return "Item type not found!"

        val available = when (resolved) {
            is ResolvedItem.Custom -> countPlayerItems(seller, resolved.type)
            is ResolvedItem.Vanilla -> countVanillaItems(seller, resolved.material)
        }
        if (available < quantity) {
            return "You only have ${available}x ${bazaarItem.displayName}!"
        }

        val totalPayout = BazaarPricingEngine.calculateBulkSellPayout(bazaarItem, quantity)
        when (resolved) {
            is ResolvedItem.Custom -> removeItems(seller, resolved.type, quantity)
            is ResolvedItem.Vanilla -> removeVanillaItems(seller, resolved.material, quantity)
        }

        bazaarItem.currentStock = if (bazaarItem.canGoOutOfStock) {
            (bazaarItem.currentStock + quantity).coerceAtMost(bazaarItem.maxStock)
        } else {
            bazaarItem.currentStock + quantity
        }
        economy.addMoney(seller, totalPayout.toDouble())

        seller.sendMessage(
            ComponentUtils.success(
                ComponentUtils.merge(
                    ComponentUtils.create("Sold ${quantity}x "),
                    ComponentUtils.create(bazaarItem.displayName, NamedTextColor.AQUA),
                    ComponentUtils.create(" for "),
                    ComponentUtils.create(EconomyService.formatMoney(totalPayout), NamedTextColor.GOLD)
                )
            )
        )

        invalidateCategoryTree()
        dataStore.saveBazaar()
        return null
    }

    /**
     * Buy compressed-tier items from the bazaar.
     * Stock is deducted at the base item level (quantity * compressionAmount).
     * Returns null on success, or an error message string.
     */
    fun buyCompressedItems(
        buyer: Player,
        bazaarItem: BazaarItem,
        tier: CompressionRecipeMember,
        compressionAmount: Int,
        quantity: Int
    ): String? {
        val baseQuantity = quantity * compressionAmount
        if (baseQuantity <= 0) return "Invalid quantity!"

        val availableStock = if (bazaarItem.canGoOutOfStock) bazaarItem.currentStock else baseQuantity
        if (availableStock < baseQuantity) return "Not enough stock!"

        val totalCost = BazaarPricingEngine.calculateBulkBuyCost(bazaarItem, baseQuantity)
        if (economy.getMoney(buyer) < totalCost) {
            return "You need ${EconomyService.formatMoney(totalCost)} to buy $quantity!"
        }

        economy.takeMoney(buyer, totalCost.toDouble())
        bazaarItem.currentStock = if (bazaarItem.canGoOutOfStock) {
            (bazaarItem.currentStock - baseQuantity).coerceAtLeast(0)
        } else {
            bazaarItem.currentStock - baseQuantity
        }
        giveCompressedItems(buyer, tier.material, quantity)

        buyer.sendMessage(
            ComponentUtils.success(
                ComponentUtils.merge(
                    ComponentUtils.create("Bought ${quantity}x "),
                    ComponentUtils.create(tier.material.name(), NamedTextColor.AQUA),
                    ComponentUtils.create(" for "),
                    ComponentUtils.create(EconomyService.formatMoney(totalCost), NamedTextColor.GOLD)
                )
            )
        )

        invalidateCategoryTree()
        dataStore.saveBazaar()
        return null
    }

    /**
     * Sell compressed-tier items to the bazaar.
     * Stock is added at the base item level (quantity * compressionAmount).
     * Returns null on success, or an error message string.
     */
    fun sellCompressedItems(
        seller: Player,
        bazaarItem: BazaarItem,
        tier: CompressionRecipeMember,
        compressionAmount: Int,
        quantity: Int
    ): String? {
        val baseQuantity = quantity * compressionAmount
        if (baseQuantity <= 0) return "Invalid quantity!"

        val available = countCompressedItems(seller, tier.material)
        if (available < quantity) {
            return "You only have ${available}x ${tier.material.name()}!"
        }

        val totalPayout = BazaarPricingEngine.calculateBulkSellPayout(bazaarItem, baseQuantity)
        removeCompressedItems(seller, tier.material, quantity)
        bazaarItem.currentStock = if (bazaarItem.canGoOutOfStock) {
            (bazaarItem.currentStock + baseQuantity).coerceAtMost(bazaarItem.maxStock)
        } else {
            bazaarItem.currentStock + baseQuantity
        }
        economy.addMoney(seller, totalPayout.toDouble())

        seller.sendMessage(
            ComponentUtils.success(
                ComponentUtils.merge(
                    ComponentUtils.create("Sold ${quantity}x "),
                    ComponentUtils.create(tier.material.name(), NamedTextColor.AQUA),
                    ComponentUtils.create(" for "),
                    ComponentUtils.create(EconomyService.formatMoney(totalPayout), NamedTextColor.GOLD)
                )
            )
        )

        invalidateCategoryTree()
        dataStore.saveBazaar()
        return null
    }

    // ── Sell-all ────────────────────────────────────────────────────────

    /**
     * Calculates what the player could sell across all bazaar items in their inventory.
     * Includes both base items and compressed tiers.
     * Returns only entries where the player has at least 1 of that item.
     */
    fun calculateSellAll(player: Player): List<BazaarSellEntry> {
        val entries = mutableListOf<BazaarSellEntry>()
        for (bazaarItem in getAllItems()) {
            val resolved = resolveItem(bazaarItem.key) ?: continue
            val baseAvailable = when (resolved) {
                is ResolvedItem.Custom -> countPlayerItems(player, resolved.type)
                is ResolvedItem.Vanilla -> countVanillaItems(player, resolved.material)
            }
            if (baseAvailable > 0) {
                val quantity = min(baseAvailable, MarketConstants.BAZAAR_MAX_TRANSACTION_QUANTITY)
                val payout = BazaarPricingEngine.calculateBulkSellPayout(bazaarItem, quantity)
                entries.add(BazaarSellEntry(bazaarItem, bazaarItem.displayName, quantity, payout))
            }

            val flow = findCompressionFlow(bazaarItem.key) ?: continue
            for (tierIndex in 1 until flow.size) {
                val tier = flow[tierIndex]
                val compAmt = calculateCompressionAmount(flow, tierIndex)
                val available = countCompressedItems(player, tier.material)
                if (available <= 0) continue

                val quantity = min(available, MarketConstants.BAZAAR_MAX_TRANSACTION_QUANTITY)
                val baseQuantity = quantity * compAmt
                val payout = BazaarPricingEngine.calculateBulkSellPayout(bazaarItem, baseQuantity)
                entries.add(BazaarSellEntry(bazaarItem, tier.material.name(), quantity, payout, tier, compAmt))
            }
        }
        return entries
    }

    /**
     * Executes a sell-all transaction for the given entries.
     * Re-validates quantities before removing items.
     * Returns null on success, or an error message string.
     */
    fun executeSellAll(player: Player, entries: List<BazaarSellEntry>): String? {
        if (entries.isEmpty()) return "Nothing to sell!"

        var totalPayout = 0L
        var totalItemsSold = 0

        for (entry in entries) {
            val isCompressed = entry.tier != null

            val available = if (isCompressed) {
                countCompressedItems(player, entry.tier!!.material)
            } else {
                val resolved = resolveItem(entry.bazaarItem.key) ?: continue
                when (resolved) {
                    is ResolvedItem.Custom -> countPlayerItems(player, resolved.type)
                    is ResolvedItem.Vanilla -> countVanillaItems(player, resolved.material)
                }
            }
            val actualQuantity = min(available, entry.quantity)
            if (actualQuantity <= 0) continue

            val baseQuantity = actualQuantity * entry.compressionAmount
            val payout = BazaarPricingEngine.calculateBulkSellPayout(entry.bazaarItem, baseQuantity)

            if (isCompressed) {
                removeCompressedItems(player, entry.tier!!.material, actualQuantity)
            } else {
                val resolved = resolveItem(entry.bazaarItem.key) ?: continue
                when (resolved) {
                    is ResolvedItem.Custom -> removeItems(player, resolved.type, actualQuantity)
                    is ResolvedItem.Vanilla -> removeVanillaItems(player, resolved.material, actualQuantity)
                }
            }

            entry.bazaarItem.currentStock = if (entry.bazaarItem.canGoOutOfStock) {
                (entry.bazaarItem.currentStock + baseQuantity).coerceAtMost(entry.bazaarItem.maxStock)
            } else {
                entry.bazaarItem.currentStock + baseQuantity
            }
            totalPayout += payout
            totalItemsSold += actualQuantity
        }

        if (totalItemsSold == 0) return "No items could be sold!"

        economy.addMoney(player, totalPayout.toDouble())
        player.sendMessage(
            ComponentUtils.success(
                ComponentUtils.merge(
                    ComponentUtils.create("Sold "),
                    ComponentUtils.create("$totalItemsSold items", NamedTextColor.AQUA),
                    ComponentUtils.create(" for "),
                    ComponentUtils.create(EconomyService.formatMoney(totalPayout), NamedTextColor.GOLD)
                )
            )
        )

        invalidateCategoryTree()
        dataStore.saveBazaar()
        return null
    }

    // ── Inventory helpers ───────────────────────────────────────────────

    fun countCompressedItems(player: Player, tierMaterial: MaterialWrapper): Int {
        return if (tierMaterial.isCustom) {
            countPlayerItems(player, tierMaterial.custom)
        } else {
            countVanillaItems(player, tierMaterial.vanilla)
        }
    }

    private fun removeCompressedItems(player: Player, tierMaterial: MaterialWrapper, quantity: Int) {
        if (tierMaterial.isCustom) {
            removeItems(player, tierMaterial.custom, quantity)
        } else {
            removeVanillaItems(player, tierMaterial.vanilla, quantity)
        }
    }

    private fun giveCompressedItems(player: Player, tierMaterial: MaterialWrapper, quantity: Int) {
        if (tierMaterial.isCustom) {
            giveItems(player, tierMaterial.custom, quantity)
        } else {
            giveVanillaItems(player, tierMaterial.vanilla, quantity)
        }
    }

    private fun countPlayerItems(player: Player, type: CustomItemType): Int {
        val itemService = SMPRPG.getService(ItemService::class.java)
        var count = 0
        for (item in player.inventory.contents) {
            if (item == null) continue
            val blueprint = itemService.getBlueprint(item)
            if (blueprint is CustomItemBlueprint && blueprint.customItemType == type) {
                count += item.amount
            }
        }
        return count
    }

    private fun removeItems(player: Player, type: CustomItemType, quantity: Int) {
        val itemService = SMPRPG.getService(ItemService::class.java)
        var remaining = quantity
        for (slot in 0 until player.inventory.size) {
            if (remaining <= 0) break
            val item = player.inventory.getItem(slot) ?: continue
            val blueprint = itemService.getBlueprint(item)
            if (blueprint !is CustomItemBlueprint || blueprint.customItemType != type) continue

            if (item.amount <= remaining) {
                remaining -= item.amount
                player.inventory.setItem(slot, null)
            } else {
                item.amount -= remaining
                remaining = 0
            }
        }
    }

    private fun giveItems(player: Player, type: CustomItemType, quantity: Int) {
        val maxStackSize = type.DisplayMaterial.maxStackSize
        var remaining = quantity
        while (remaining > 0) {
            val batchSize = remaining.coerceAtMost(maxStackSize)
            val item = ItemService.generate(type)
            item.amount = batchSize
            player.inventory.addItem(item).values.forEach { overflow ->
                player.world.dropItemNaturally(player.eyeLocation, overflow)
            }
            remaining -= batchSize
        }
    }

    private fun countVanillaItems(player: Player, material: Material): Int {
        val itemService = SMPRPG.getService(ItemService::class.java)
        var count = 0
        for (item in player.inventory.contents) {
            if (item == null || item.type != material) continue
            val blueprint = itemService.getBlueprint(item)
            if (blueprint is VanillaItemBlueprint) {
                count += item.amount
            }
        }
        return count
    }

    private fun removeVanillaItems(player: Player, material: Material, quantity: Int) {
        val itemService = SMPRPG.getService(ItemService::class.java)
        var remaining = quantity
        for (slot in 0 until player.inventory.size) {
            if (remaining <= 0) break
            val item = player.inventory.getItem(slot) ?: continue
            if (item.type != material) continue
            val blueprint = itemService.getBlueprint(item)
            if (blueprint !is VanillaItemBlueprint) continue

            if (item.amount <= remaining) {
                remaining -= item.amount
                player.inventory.setItem(slot, null)
            } else {
                item.amount -= remaining
                remaining = 0
            }
        }
    }

    private fun giveVanillaItems(player: Player, material: Material, quantity: Int) {
        val maxStackSize = material.maxStackSize
        var remaining = quantity
        while (remaining > 0) {
            val batchSize = remaining.coerceAtMost(maxStackSize)
            val item = ItemService.generate(material)
            item.amount = batchSize
            player.inventory.addItem(item).values.forEach { overflow ->
                player.world.dropItemNaturally(player.eyeLocation, overflow)
            }
            remaining -= batchSize
        }
    }

    companion object {

        private const val DEFAULT_CATEGORY = "Miscellaneous"

        private val CATEGORY_DISPLAY_ORDER = listOf(
            "Mining", "Combat", "Farming", "Fishing", "Foraging", "Crafting", "Magic",
            "Bosses", "Consumables", "Augmenting", "Decorative", "Miscellaneous"
        )

        private val SUBCATEGORY_KEYWORDS = mapOf(
            "Mining/Ores" to listOf("raw", "ore", "cobalt", "orichalcum", "tungsten", "silver", "tin"),
            "Mining/Stone" to listOf("cobblestone", "deepslate", "granite", "diorite", "andesite", "tuff", "calcite", "basalt"),
            "Mining/Gems" to listOf("sulfur", "onyx", "diamond", "emerald", "lapis", "amethyst"),
            "Combat/Overworld" to listOf("bone", "rotten", "gunpowder"),
            "Combat/Nether" to listOf("blaze", "cinderite"),
            "Combat/End" to listOf("ender", "echo"),
            "Crafting/Rare" to listOf("singularity", "hexed"),
            "Crafting/Components" to listOf("barnacle", "tendril", "resin", "erratic", "minnow", "dissipating", "midnight_hide"),
            "Crafting/Processed" to listOf("enchanted"),
        )

        private val CATEGORY_KEYWORDS = mapOf(
            "Farming" to listOf("wheat", "melon", "sugar", "cane", "potato", "carrot", "pumpkin"),
            "Fishing" to listOf("fish", "filament", "fiber", "holomoku", "shark"),
            "Foraging" to listOf("log", "wood", "plank"),
            "Magic" to listOf("displacement", "warp"),
            "Bosses" to listOf("dragon", "summoning", "heart_of_the_void", "pluto", "jupiter", "smoldering", "draconic"),
        )

        fun calculateCompressionAmount(flow: List<CompressionRecipeMember>, tierIndex: Int): Int {
            var amount = 1
            for (i in 0 until tierIndex) {
                amount *= flow[i].amount
            }
            return amount
        }
    }
}
