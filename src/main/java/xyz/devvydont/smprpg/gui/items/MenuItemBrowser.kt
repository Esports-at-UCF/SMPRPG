package xyz.devvydont.smprpg.gui.items

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.gui.base.MenuBase
import xyz.devvydont.smprpg.gui.base.MenuButtonClickHandler
import xyz.devvydont.smprpg.gui.items.search.ItemSearchQuery
import xyz.devvydont.smprpg.gui.items.search.SearchField
import xyz.devvydont.smprpg.gui.items.search.SearchNormalizer
import xyz.devvydont.smprpg.gui.items.search.SearchableItem
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.base.SMPItemBlueprint
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.services.RecipeService.Companion.getRecipesFor
import xyz.devvydont.smprpg.util.formatting.ComponentUtils
import xyz.devvydont.smprpg.util.formatting.Symbols

/*
 * Functions as a server side version of J/NEI. Players can view this interface to view custom items and their recipes.
 */
class MenuItemBrowser @JvmOverloads constructor(
    parent: MenuBase?,
    player: Player,
    /**
     * Retrieve the query that is currently being used for this display.
     * 
     * @return A string representing what the user input via command.
     */
    val query: String? = ""
) : MenuBase(player, ROWS) {
    private val queriedItems: MutableList<ItemStack> = ArrayList<ItemStack>()
    private var page = 0

    /**
     * Alternative constructor if the user is querying for a specific item. We only want to display items that have
     * some sort of matching string pattern with their query.
     * 
     * @param player The player who wants to view items
     * @param query The string query the player provided within the command
     */
    /**
     * Default constructor. Used for general queries when we want to just display every single item in the game.
     * 
     * @param player The player who wants to view items
     */
    init {
        // Make sure the display cache is ready. This is normally warmed at startup (see warmCache), but we build it
        // here too so the menu still works if a player opens it before the startup warm-up completes.
        buildCache()
    }

    override fun handleInventoryOpened(event: InventoryOpenEvent) {
        super.handleInventoryOpened(event)
        this.queryItems()
        this.render()
        event.titleOverride(
            ComponentUtils.merge(
                ComponentUtils.create(Symbols.OFFSET_NEG_1 + Symbols.RECIPE_MENU, NamedTextColor.WHITE),  // Background
                ComponentUtils.create(
                    Symbols.OFFSET_NEG_128 + Symbols.OFFSET_NEG_32 + Symbols.OFFSET_NEG_2 + "Item Directory: " + (if (query!!.isEmpty()) "All Items" else query),
                    Symbols.INVENTORY_TITLE_COLOR
                )
            )
        )
    }

    override fun handleInventoryClicked(event: InventoryClickEvent) {
        super.handleInventoryClicked(event)
        event.isCancelled = true
        //this.playInvalidAnimation();
    }

    /**
     * Use our currently defined query to return a fresh new list of items that we should render on the interface.
     * This only needs to be called when the query is either first set, or when we update the query.
     * After calling this method, the items can be viewed either via state or return value, as they will be the same list.
     * 
     * @return A list of item stacks representing items that we should render on the display.
     */
    private fun queryItems(): MutableList<ItemStack> {
        // First, we can throw out our old item query since we are overwriting it anyway.
        this.queriedItems.clear()

        // The cache already holds fully display-ready items with their searchable fields pre-indexed, so querying is
        // just running each item through the parsed query. We store references (not clones) since Bukkit copies items
        // on insert into the inventory, and the click handler clones before ever handing one to a player.
        val parsedQuery = ItemSearchQuery.parse(query ?: "")
        for (cached in ITEM_CACHE)
            if (parsedQuery.matches(cached))
                this.queriedItems.add(cached.displayItem)

        return queriedItems
    }

    /**
     * Determine what to do when a certain item stack is clicked. For example, if a craftable item was clicked, we
     * should display a sub menu containing the recipe of the item.
     * 
     * @param itemStack
     */
    private fun handleClick(event: InventoryClickEvent, itemStack: ItemStack) {
        this.sounds.playActionConfirm()

        // If the player is in creative mode and this is a shift click, give it to them.
        if (this.player.gameMode == GameMode.CREATIVE && event.isShiftClick) {
            this.playSound(Sound.ENTITY_ITEM_PICKUP, 1f, .5f)
            val item = itemStack.clone()
            SMPRPG.getService(ItemService::class.java).ensureItemStackUpdated(item)
            this.player.inventory.addItem(item)
            return
        }

        // Get a clean version of the item w/o modified lore so that we can properly query recipes.
        val clean = ItemService.blueprint(itemStack)
        val recipes: MutableList<Recipe> = getRecipesFor(clean.generate())
        if (recipes.isEmpty() || itemStack.type == Material.AIR) {
            //this.playInvalidAnimation();
            return
        }

        MenuRecipeViewer(this.player, this, recipes, itemStack).openMenu()
    }

    /**
     * Renders the inventory based on the state of this instance. Factors in current page and the current query
     * to decide what to display on the page.
     */
    fun render() {
        this.clear()
        this.setBorderEdge()

        // Pagination logic, do a bounds check on the current page and allow page wrapping.
        val totalItems = queriedItems.size
        val area: Int = (ROWS - 2) * 7 // Exclude top and bottom rows, 7 slots in each row
        val lastPage = totalItems / area
        var itemIndexOffset = page * area
        if (itemIndexOffset >= totalItems) {
            itemIndexOffset = 0
            page = 0
        }
        if (itemIndexOffset < 0) {
            page = lastPage
            itemIndexOffset = area * page
        }

        // Display items!
        for (slot in 0..<inventorySize) {
            // Is the item index out of bounds? This can happen on the last page.

            if (itemIndexOffset >= totalItems) break

            // Is this slot already occupied? Skip
            if (this.getItem(slot) != null) continue

            // Add the button. The item is already display-ready (lore and recipe tooltip were baked in when the
            // cache was built), so showing it is just a slot placement with no per-flip computation.
            val item = queriedItems[itemIndexOffset]
            this.setButton(
                slot,
                item
            ) { event: InventoryClickEvent -> this.handleClick(event, item) }
            itemIndexOffset++
        }

        // Extra utility buttons
        // Now set the slots for a next/prev page that increment/decrement the page and re-render
        val displayPage = page + 1
        val displayPageMax = lastPage + 1
        val previousButton = BUTTON_PAGE_PREVIOUS.clone()
        previousButton.editMeta {  meta -> meta.itemName(ComponentUtils.create("Previous Page ($displayPage/$displayPageMax)", NamedTextColor.GOLD))}
        this.setButton(
            (ROWS - 1) * 9,
            previousButton
        ) { _: InventoryClickEvent ->
            page--
            this.render()
            this.sounds.playPagePrevious()
        }

        val nextButton = BUTTON_PAGE_NEXT.clone()
        nextButton.editMeta {  meta -> meta.itemName(ComponentUtils.create("Next Page ($displayPage/$displayPageMax)", NamedTextColor.GOLD))}
        this.setButton(
            (ROWS - 1) * 9 + 8,
            nextButton
        ) { _: InventoryClickEvent ->
            page++
            this.render()
            this.sounds.playPageNext()
        }

        // Close button
        this.setButton((ROWS - 1) * 9 + 4, BUTTON_EXIT, MenuButtonClickHandler { _: InventoryClickEvent ->
            this.closeMenu()
            this.sounds.playMenuClose()
        })
    }

    companion object {
        // Serializes lore components down to plain text once, so tooltip searches operate on simple strings.
        private val PLAIN_TEXT = PlainTextComponentSerializer.plainText()

        // The browser cache: fully display-ready items, each with its searchable fields pre-indexed. Building this once
        // lets both page flips and queries avoid all per-item lore/recipe computation.
        private val ITEM_CACHE: MutableList<SearchableItem> = ArrayList()
        private val BLACKLISTED_MATERIALS = arrayOf(
            Material.STONE_AXE, Material.STONE_PICKAXE, Material.STONE_HOE, Material.STONE_SWORD, Material.STONE_SHOVEL, Material.STONE_SPEAR,
            Material.DIAMOND_AXE, Material.DIAMOND_PICKAXE, Material.DIAMOND_HOE, Material.DIAMOND_SWORD, Material.DIAMOND_SHOVEL, Material.DIAMOND_SPEAR, Material.DIAMOND_HELMET, Material.DIAMOND_BOOTS, Material.DIAMOND_LEGGINGS, Material.DIAMOND_CHESTPLATE
        )


        const val ROWS: Int = 6

        /**
         * Pre-builds the browser display cache. This is safe to call multiple times; it only does work the first time.
         * Items are not updated dynamically while browsing, so the rendered items can be reused for the server's
         * lifetime. Intended to be called once at startup (see [warmCache]) so players never pay the build cost while
         * browsing, but the menu also calls this on construction as a fallback.
         */
        @JvmStatic
        fun warmCache() {
            buildCache()
        }

        private fun buildCache() {
            if (ITEM_CACHE.isNotEmpty()) return

            val itemService = SMPRPG.getService(ItemService::class.java)
            for (type in CustomItemType.entries)
                cacheItem(itemService, ItemService.generate(type))

            for (material in Material.entries) {
                if (material.isLegacy || !material.isItem || material == Material.AIR || material in BLACKLISTED_MATERIALS) continue
                cacheItem(itemService, ItemService.generate(material))
            }
        }

        /**
         * Bakes the browser tooltip into a generated item and indexes its searchable fields, then stores it in the
         * cache. This mirrors the lore/recipe work that used to happen on every page flip inside render(), but now
         * only ever runs a single time per item.
         */
        private fun cacheItem(itemService: ItemService, item: ItemStack) {
            val blueprint = itemService.getBlueprint(item)
            val renderedLore = itemService.renderItemStackLore(item)

            // Only items with a known recipe (crafting, smelting, or custom station) get the clickable tooltip.
            if (getRecipesFor(blueprint.generate()).isNotEmpty()) {
                val displayLore = ArrayList(renderedLore)
                displayLore.addFirst(ComponentUtils.EMPTY)
                displayLore.addFirst(ComponentUtils.create("Click to view recipe!", NamedTextColor.YELLOW))
                displayLore.addFirst(ComponentUtils.EMPTY)
                item.editMeta { meta -> meta.lore(displayLore) }
            }

            ITEM_CACHE.add(buildSearchable(item, blueprint, renderedLore))
        }

        /**
         * Indexes an item's searchable fields (name, tooltip, rarity, classification) so that queries become simple
         * substring checks against pre-normalized strings.
         */
        private fun buildSearchable(
            item: ItemStack,
            blueprint: SMPItemBlueprint,
            renderedLore: List<Component?>
        ): SearchableItem {
            val name = blueprint.getItemName(item)
            val loreText = renderedLore.filterNotNull().joinToString("\n") { PLAIN_TEXT.serialize(it) }
            // The tooltip field includes the name so that '#' searches everything a player can read on the item.
            val tooltip = "$name\n$loreText"
            val rarity = blueprint.getRarity(item).name
            val classification = classificationKeywords(blueprint.itemClassification)

            val fields = mapOf(
                SearchField.NAME to SearchNormalizer.normalize(SearchField.NAME, name),
                SearchField.TOOLTIP to SearchNormalizer.normalize(SearchField.TOOLTIP, tooltip),
                SearchField.RARITY to SearchNormalizer.normalize(SearchField.RARITY, rarity),
                SearchField.CLASSIFICATION to SearchNormalizer.normalize(SearchField.CLASSIFICATION, classification)
            )
            return SearchableItem(item, fields)
        }

        /**
         * Builds the searchable keyword text for an item's classification. Beyond the specific category name (e.g.
         * "sword"), broad groupings ("weapon", "armor", "bow") are appended so a player can search by either.
         */
        private fun classificationKeywords(classification: ItemClassification): String {
            val keywords = StringBuilder(classification.name)
            if (classification.isWeapon) keywords.append(" weapon")
            if (classification.isArmor) keywords.append(" armor")
            if (classification.isBow) keywords.append(" bow")
            return keywords.toString()
        }
    }
}
