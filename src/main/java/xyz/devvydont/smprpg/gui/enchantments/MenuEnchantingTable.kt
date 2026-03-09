package xyz.devvydont.smprpg.gui.enchantments

import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent
import io.papermc.paper.registry.RegistryKey
import io.papermc.paper.registry.TypedKey
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.SMPRPG.Companion.plugin
import xyz.devvydont.smprpg.enchantments.CustomEnchantment
import xyz.devvydont.smprpg.gui.InterfaceUtil.getNamedItem
import xyz.devvydont.smprpg.gui.base.MenuBase
import xyz.devvydont.smprpg.gui.items.MenuReforge.Companion.BUTTON_SLOT
import xyz.devvydont.smprpg.gui.items.MenuReforge.Companion.INPUT_SLOT
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.blueprints.resources.scrolls.DynamicEnchantingScroll
import xyz.devvydont.smprpg.reforge.ReforgeType
import xyz.devvydont.smprpg.services.EconomyService
import xyz.devvydont.smprpg.services.EconomyService.Companion.formatMoney
import xyz.devvydont.smprpg.services.EnchantmentService
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.extensions.takeIfPresent
import xyz.devvydont.smprpg.util.formatting.ComponentUtils
import xyz.devvydont.smprpg.util.formatting.Symbols
import java.util.function.Consumer

const val ITEM_SLOT = 13;
const val SCROLL_SLOT = 10;
val       INGREDIENT_SLOTS = intArrayOf(30, 31, 32, 39, 40, 41);

val       WHITELISTED_SLOTS = intArrayOf(ITEM_SLOT, SCROLL_SLOT)
const val ACTION_SLOT = 16

enum class ActionButtonState {
    CALCULATING,
    DISABLED,
    ENABLED
}

class MenuEnchantingTable(owner: Player) : MenuBase(owner, 5) {
    private var actionButtonState = ActionButtonState.DISABLED

    init {
        this.sounds.setMenuOpen(Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1f, 1f)
        this.sounds.setMenuClose(Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 1f, 1f)
    }

    override fun handleInventoryOpened(event: InventoryOpenEvent) {
        // Prepare the inventory
        event.titleOverride(
            ComponentUtils.merge(
                ComponentUtils.create(Symbols.OFFSET_NEG_1 + Symbols.ENCHANTING_MENU, NamedTextColor.WHITE),
                ComponentUtils.create(
                    Symbols.OVERLAY_BG_OFFSET_STANDARD + "Enchant",
                    NamedTextColor.BLACK
                )
            )
        )

        // Render the UI
        this.render()
        this.setMaxStackSize(100)
    }

    override fun handleInventoryClicked(event: InventoryClickEvent) {
        super.handleInventoryClicked(event)
        val slot = event.slot

        // TODO: Do shift click logic
        if (event.isShiftClick) {
            event.isCancelled = true
            return
        }
        if (event.clickedInventory == null) return
        if (event.clickedInventory != this.inventory) return

        // If we are clicking in the player inventory allow it to happen. We need to allow them to manage items.
        if (event.clickedInventory!!.type == InventoryType.PLAYER) {
            event.isCancelled = false
            return
        }

        if (slot == ACTION_SLOT) {
            if (actionButtonState != ActionButtonState.ENABLED) {
                event.isCancelled = true
                return
            }
            else {
                doEnchant()
            }
        }

        if (slot !in WHITELISTED_SLOTS) {
            event.isCancelled = true
            return
        }

        // Ingredient slots are not player accessible, they are there only to show cost.
        if (slot in INGREDIENT_SLOTS) {
            event.isCancelled = true
            return
        }

        // Scroll slot should ONLY accept dynamic scrolls.
        if (slot == SCROLL_SLOT) {
            val itemKey = SMPRPG.getService(ItemService::class.java).itemTypeKey
            val cursor = event.cursor
            if (!cursor.isEmpty) {
                if (cursor.persistentDataContainer.getOrDefault(
                        itemKey,
                        PersistentDataType.STRING,
                        "ERROR"
                    ) != CustomItemType.ENCHANTING_SCROLL.name.lowercase())
                {
                    actionButtonState = ActionButtonState.CALCULATING
                    Bukkit.getScheduler()
                        .runTaskLater(plugin, Runnable { setSlot(ACTION_SLOT, generateEnchantButton()) }, 0L)
                    event.isCancelled = true
                    return
                }
            }
            val enchantItem = event.inventory.getItem(ITEM_SLOT)
            actionButtonState = ActionButtonState.CALCULATING
            Bukkit.getScheduler()
                .runTaskLater(plugin, Runnable { setSlot(ACTION_SLOT, generateEnchantButton()) }, 0L)
        }

        // Item slot accepts any item, but we need to determine if the enchantment in the scroll slot is valid.
        if (slot == ITEM_SLOT) {
            // Use our cursor item for logic, since it's what actually is going to be used for the enchantment
            actionButtonState = ActionButtonState.CALCULATING
            Bukkit.getScheduler().runTaskLater(plugin, Runnable { setSlot(ACTION_SLOT, generateEnchantButton()) }, 0L)
        }
    }

    override fun handleInventoryClosed(event: InventoryCloseEvent) {
        super.handleInventoryClosed(event)
        giveItemToPlayer(ITEM_SLOT, true)
        giveItemToPlayer(SCROLL_SLOT, true)
    }

    fun generateEnchantButton(): ItemStack {
        val input = getItem(ITEM_SLOT)
        val scroll = getItem(SCROLL_SLOT)
        val book = getNamedItem(
            Material.BOOK,
            ComponentUtils.create("Enchant Item", NamedTextColor.DARK_AQUA).decoration(TextDecoration.ITALIC, false)
        )
        val valid = validateEnchant(input, scroll)
        val lore: MutableList<Component?> = ArrayList()

        actionButtonState = ActionButtonState.DISABLED
        // Check our validity cases.
        if (!valid) {
            clearIngredientDisplay()
            // Input item is missing
            if (input == null || input.type == Material.AIR) {
                lore.add(ComponentUtils.EMPTY)
                lore.add(ComponentUtils.create("Input an item to enchant.", NamedTextColor.GRAY))
                book.editMeta(Consumer { meta: ItemMeta? ->
                    meta!!.lore(ComponentUtils.cleanItalics(lore))
                })
                return book
            }

            // Input exists at this point, is a scroll missing?
            if (scroll == null || scroll.type == Material.AIR) {
                lore.add(ComponentUtils.EMPTY)
                lore.add(ComponentUtils.create("You need a Scroll of Imbuement to enchant!", NamedTextColor.RED))
                book.editMeta(Consumer { meta: ItemMeta? ->
                    meta!!.lore(ComponentUtils.cleanItalics(lore))
                })
                return book.withType(Material.BARRIER)
            }

            // Scroll and Input both exist at this point, only case left for invalid check is invalid enchantment
            lore.add(ComponentUtils.EMPTY)
            lore.add(ComponentUtils.create("This enchantment cannot be applied to this item!", NamedTextColor.RED))
            book.editMeta(Consumer { meta: ItemMeta? ->
                meta!!.lore(ComponentUtils.cleanItalics(lore))
            })
            return book.withType(Material.BARRIER)
        }

        // Populate our recipe components
        val enchant = getEnchant(scroll)!!
        val enchantLevel = input!!.getEnchantmentLevel(enchant) + 1
        var recipe = SMPRPG.getService(EnchantmentService::class.java).getEnchantment(enchant)?.getRecipe(enchantLevel)
        if (recipe == null) {
            // Either this recipe isn't implemented, or we are max level. Assume latter.
            lore.add(ComponentUtils.EMPTY)
            lore.add(ComponentUtils.create("This enchantment has already reached its maximum potential!", NamedTextColor.RED))
            book.editMeta(Consumer { meta: ItemMeta? ->
                meta!!.lore(ComponentUtils.cleanItalics(lore))
            })
            return book.withType(Material.BARRIER)
        }
        var i = 0
        val ingredients = recipe.ingredients!!
        if (ingredients.size > 6)
            throw IllegalStateException("Enchanting recipes cannot be longer than 6 ItemStacks.")
        for (ingredient in ingredients) {
            setSlot(INGREDIENT_SLOTS[i], ingredient)
            i++
        }

        // Do we have the required materials to enchant?
        for (ingredient in ingredients) {
            if (!(player.inventory.containsAtLeast(ingredient, ingredient.amount))) {
                lore.add(ComponentUtils.EMPTY)
                lore.add(ComponentUtils.create("This enchantment is missing reagents!", NamedTextColor.RED))
                book.editMeta(Consumer { meta: ItemMeta? ->
                    meta!!.lore(ComponentUtils.cleanItalics(lore))
                })
                return book.withType(Material.BARRIER)
            }
        }

        actionButtonState = ActionButtonState.ENABLED
        // Valid item
        lore.add(ComponentUtils.EMPTY)
        lore.add(ComponentUtils.create("Click to enchant this item!", NamedTextColor.GRAY))
        book.editMeta(Consumer { meta: ItemMeta? ->
            meta!!.lore(ComponentUtils.cleanItalics(lore))
            meta.setEnchantmentGlintOverride(true)
        })

        return book
    }

    fun validateEnchant(itemToEnchant : ItemStack?, scrollItem : ItemStack?) : Boolean {
        if (itemToEnchant == null) {
            println("itemToEnchant: $itemToEnchant")
            return false
        }
        val scrollItem = this.getItem(SCROLL_SLOT)
        println("scrollItem:  $scrollItem")
        if (scrollItem != null) {
            val enchant : Enchantment = getEnchant(scrollItem)!!
            if (enchant.canEnchantItem(itemToEnchant))
                return true
        }
        return false
    }

    fun clearIngredientDisplay() {
        for (slot in INGREDIENT_SLOTS) {
            clearSlot(slot)
        }
    }

    fun doEnchant() : Boolean {
        val scrollItem : ItemStack = this.getItem(SCROLL_SLOT)!!
        val enchant : Enchantment = getEnchant(scrollItem)!!
        val enchantItem : ItemStack = this.getItem(ITEM_SLOT)!!
        val enchantLevel = enchantItem.getEnchantmentLevel(enchant) + 1
        var recipe = SMPRPG.getService(EnchantmentService::class.java).getEnchantment(enchant)?.getRecipe(enchantLevel)
        val ingredients = recipe!!.ingredients!!

        if (player.inventory.takeIfPresent(*ingredients.toTypedArray())) {
            enchantItem.addEnchantment(enchant, enchantItem.getEnchantmentLevel(enchant) + 1)
            scrollItem.amount--
            player.playSound(player.location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1f, 1f)
            ItemService.blueprint(enchantItem).updateItemData(enchantItem)
            generateEnchantButton()
            return true
        }
        return false
    }

    fun getEnchant(scroll : ItemStack?) : Enchantment? {
        if (scroll != null) {
            val enchantId = scroll.persistentDataContainer.getOrDefault(
                DynamicEnchantingScroll.SCROLL_ENCHANT_TYPE_KEY,
                PersistentDataType.STRING, ""
            )
            val enchant: Enchantment = SMPRPG.getService(EnchantmentService::class.java).getEnchantment(
                TypedKey.create(
                    RegistryKey.ENCHANTMENT, enchantId
                )
            )
            return enchant
        }
        return null
    }

    fun render() {
        this.setBorderFull()

        for (slotIdx in INGREDIENT_SLOTS + WHITELISTED_SLOTS) {
            this.clearSlot(slotIdx)
        }
        this.setSlot(ACTION_SLOT, generateEnchantButton())
    }

    @EventHandler
    fun onPlayerInventoryChange(event : PlayerInventorySlotChangeEvent) {
        // Reset our icon if our inventory changes
        if ((event.player == player) && actionButtonState == ActionButtonState.ENABLED) {
            generateEnchantButton()
        }
    }
}