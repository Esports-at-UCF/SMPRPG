package xyz.devvydont.smprpg.gui.items

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.CustomModelData
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Keyed
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.*
import org.bukkit.inventory.RecipeChoice.ExactChoice
import org.bukkit.inventory.RecipeChoice.MaterialChoice
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.scheduler.BukkitTask
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.SMPRPG.Companion.broadcastToOperatorsCausedBy
import xyz.devvydont.smprpg.gui.InterfaceUtil.getNamedItem
import xyz.devvydont.smprpg.gui.InterfaceUtil.getNamedItemWithDescription
import xyz.devvydont.smprpg.gui.base.MenuBase
import xyz.devvydont.smprpg.gui.base.MenuButtonClickHandler
import xyz.devvydont.smprpg.items.interfaces.ICraftable
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.services.RecipeService.Companion.getRecipesFor
import xyz.devvydont.smprpg.util.formatting.ComponentUtils
import xyz.devvydont.smprpg.util.formatting.Symbols
import xyz.devvydont.smprpg.util.time.TickTime
import java.util.List
import java.util.function.Consumer

class MenuRecipeViewer(
    player: Player,
    parentMenu: MenuBase?,
    private val recipes: MutableList<Recipe>,
    result: ItemStack?
) : MenuBase(player, ROWS, parentMenu) {
    // The index of the recipe we want to show if there is more than one recipe.
    private var currentRecipe = 0

    private val result: ItemStack

    // Integer that will allow us to continuously flip through different options a recipe provides.
    private var recipeChoiceIndex = 0

    /**
     * Default constructor initialized from within the MenuItemBrowser typically when a craftable item was clicked.
     * 
     * @param player The player viewing the recipe.
     * @param parentMenu The menu calling this menu. Typically, the MenuItemBrowser.
     * @param result The result from crafting.
     */
    init {
        this.result =
            ItemService.clean(result) // This may seem silly, but it's necessary to remove the "craftable" lore.
    }

    override fun handleInventoryOpened(event: InventoryOpenEvent) {
        super.handleInventoryOpened(event)
        event.titleOverride(ComponentUtils.merge(ComponentUtils.create("Recipes for: "), result.displayName()))
        this.render(event)
        Bukkit.getScheduler().runTaskTimer(SMPRPG.getInstance(), Consumer { task: BukkitTask? ->

            // If nobody is viewing us, we can stop the task.
            if (this.inventory.getViewers().isEmpty()) {
                task!!.cancel()
                return@runTaskTimer
            }

            render(event)
            recipeChoiceIndex++
        }, TickTime.HALF_SECOND, TickTime.HALF_SECOND)
    }

    override fun handleInventoryClicked(event: InventoryClickEvent) {
        super.handleInventoryClicked(event)
        event.setCancelled(true)
        //this.playInvalidAnimation();
    }

    private fun getItemFromRecipeChoice(choice: RecipeChoice?): ItemStack {
        // Yes, this is possible... LMAO

        if (choice == null) return ItemStack.of(Material.AIR)

        var item: ItemStack? = null
        if (choice is ExactChoice) item =
            ItemService.clean(choice.getChoices().get(recipeChoiceIndex % choice.getChoices().size))

        if (choice is MaterialChoice) item =
            ItemService.generate(choice.getChoices().get(recipeChoiceIndex % choice.getChoices().size))

        // This will only occur if Spigot/Paper ever add a new child of RecipeChoice in a future update.
        if (item == null) {
            broadcastToOperatorsCausedBy(
                this.player,
                ComponentUtils.create("Unknown recipe choice candidate: " + choice.javaClass.getSimpleName())
            )
            throw IllegalStateException("Unknown choice type: " + choice.javaClass.getSimpleName())
        }

        ItemService.blueprint(item).updateItemData(item)

        // If this item is deemed craftable by us, inject lore explaining that they can click it to go deeper.
        if (!getRecipesFor(item).isEmpty()) {
            val lore = ArrayList<Component?>(
                List.of<TextComponent?>(
                    ComponentUtils.EMPTY,
                    ComponentUtils.create("Click to view recipe!", NamedTextColor.YELLOW),
                    ComponentUtils.EMPTY
                )
            )
            if (item.lore() != null) lore.addAll(item.lore()!!)
            item.lore(lore)
        }

        return item
    }

    /**
     * When an ingredient is clicked, if the blueprint of that ingredient is craftable itself as well then open
     * another layer of this menu with its recipe.
     * 
     * @param itemStack The ItemStack clicked as a crafting component
     */
    private fun handleIngredientClick(itemStack: ItemStack) {
        // Retrieve the blueprint of this item. If it is craftable, enter another recipe layer

        val recipesFor = getRecipesFor(ItemService.blueprint(itemStack).generate())
        if (recipesFor.isEmpty() || itemStack.getType() == Material.AIR) {
            //this.playInvalidAnimation();
            return
        }

        this.openSubMenu(MenuRecipeViewer(player, this, recipesFor, itemStack))
    }

    /**
     * Renders the recipe completely
     */
    fun renderRecipe(event: InventoryOpenEvent) {
        if (recipes.isEmpty()) {
            broadcastToOperatorsCausedBy(
                this.player,
                ComponentUtils.create(
                    "Achieved impossible recipe viewer state (no recipes to show): ",
                    NamedTextColor.RED
                ).append(result.displayName())
            )
            return
        }

        if (currentRecipe >= recipes.size) currentRecipe = 0
        if (currentRecipe < 0) currentRecipe = recipes.size - 1

        val recipe = recipes.get(currentRecipe)
        when (recipe) {
            -> renderCookingRecipe(cooking, event)
            -> renderShapelessRecipe(shapeless, event)
            -> renderShapedRecipe(shaped, event)
            -> renderTransmuteRecipe(transmute, event)
            -> renderSmithingTransformRecipe(smithing, event)
            -> renderStonecuttingRecipe(stonecutting, event)
            -> renderComplexRecipe(complex)
            else -> broadcastToOperatorsCausedBy(
                this.player,
                ComponentUtils.create("Unknown recipe handler: " + recipe.getClass().getSimpleName())
            )
        }
    }

    private fun renderComplexRecipe(complex: ComplexRecipe) {
        setSlot(
            CORNER + 10, getNamedItemWithDescription(
                Material.BARRIER,
                ComponentUtils.create("Complex Recipe", NamedTextColor.RED),
                ComponentUtils.EMPTY,
                ComponentUtils.create("This recipe is considered 'complex',"),
                ComponentUtils.create("which means that we can't"),
                ComponentUtils.create("determine the process to craft the"),
                ComponentUtils.create("result. There's not much we can provide"),
                ComponentUtils.create("you in this interface about this recipe!"),
                ComponentUtils.EMPTY,
                ComponentUtils.merge(
                    ComponentUtils.create("Recipe Key: "),
                    ComponentUtils.create(complex.getKey().asString(), NamedTextColor.RED)
                )
            )
        )
    }

    private fun renderStonecuttingRecipe(stonecutting: StonecuttingRecipe, event: InventoryOpenEvent) {
        event.titleOverride(
            ComponentUtils.merge(
                ComponentUtils.create(Symbols.OFFSET_NEG_1 + Symbols.STONECUTTER_RECIPE_MENU, NamedTextColor.WHITE),
                ComponentUtils.create(
                    Symbols.OFFSET_NEG_128 + Symbols.OFFSET_NEG_32 + Symbols.OFFSET_NEG_2 + "Recipes for: " + result.getI18NDisplayName(),
                    NamedTextColor.BLACK
                )
            )
        )

        val input = getItemFromRecipeChoice(stonecutting.getInputChoice())
        setButton(
            CORNER + 10,
            input,
            MenuButtonClickHandler { ev: InventoryClickEvent? -> handleIngredientClick(input) })
        setSlot(
            CORNER + 36 + 1,
            getNamedItem(Material.STONECUTTER, ComponentUtils.create("Stonecutter Recipe", NamedTextColor.GOLD))
        )
    }

    private fun renderSmithingTransformRecipe(smithing: SmithingRecipe, event: InventoryOpenEvent) {
        event.titleOverride(
            ComponentUtils.merge(
                ComponentUtils.create(Symbols.OFFSET_NEG_1 + Symbols.SMITHING_RECIPE_MENU, NamedTextColor.WHITE),
                ComponentUtils.create(
                    Symbols.OFFSET_NEG_128 + Symbols.OFFSET_NEG_32 + Symbols.OFFSET_NEG_2 + "Recipes for: " + result.getI18NDisplayName(),
                    NamedTextColor.BLACK
                )
            )
        )

        if (smithing is SmithingTransformRecipe) {
            var input = getItemFromRecipeChoice(smithing.getTemplate())
            if (input.getType() == Material.AIR) input =
                getNamedItem(Material.BARRIER, ComponentUtils.create("No template needed!", NamedTextColor.GREEN))
            val finalInput = input
            setButton(
                CORNER + 9,
                input,
                MenuButtonClickHandler { ev: InventoryClickEvent? -> handleIngredientClick(finalInput) })
        }

        var base = getItemFromRecipeChoice(smithing.getBase())
        var addition = getItemFromRecipeChoice(smithing.getAddition())

        if (addition.getType() == Material.AIR) addition =
            getNamedItem(Material.BARRIER, ComponentUtils.create("No additional item needed!", NamedTextColor.GREEN))

        if (base.getType() == Material.AIR) base =
            getNamedItem(Material.BARRIER, ComponentUtils.create("No base item needed!", NamedTextColor.GREEN))

        val finalBase = base
        val finalAddition = addition
        setButton(
            CORNER + 10,
            base,
            MenuButtonClickHandler { ev: InventoryClickEvent? -> handleIngredientClick(finalBase) })
        setButton(
            CORNER + 11,
            addition,
            MenuButtonClickHandler { ev: InventoryClickEvent? -> handleIngredientClick(finalAddition) })

        setSlot(
            CORNER + 37,
            getNamedItem(Material.SMITHING_TABLE, ComponentUtils.create("Smithing Recipe", NamedTextColor.GOLD))
        )
    }

    private fun renderShapelessRecipe(shapeless: ShapelessRecipe, event: InventoryOpenEvent) {
        var x = 0
        var y = 0
        event.titleOverride(
            ComponentUtils.merge(
                ComponentUtils.create(Symbols.OFFSET_NEG_1 + Symbols.SHAPELESS_RECIPE_MENU, NamedTextColor.WHITE),
                ComponentUtils.create(
                    Symbols.OFFSET_NEG_128 + Symbols.OFFSET_NEG_32 + Symbols.OFFSET_NEG_2 + "Recipes for: " + result.getI18NDisplayName(),
                    NamedTextColor.BLACK
                )
            )
        )

        // Loop through all the choices. These are essentially just the ingredients.
        for (choice in shapeless.getChoiceList()) {
            val item = getItemFromRecipeChoice(choice)
            this.setButton(
                y * 9 + x + CORNER,
                item,
                MenuButtonClickHandler { ev: InventoryClickEvent? -> handleIngredientClick(item) })

            x += 1
            // If we are out of bounds, go to the next row.
            if (x >= 3) {
                x = 0
                y += 1
            }
        }
        this.setSlot(
            CORNER + 36 + 1,
            getNamedItem(
                Material.CRAFTING_TABLE,
                ComponentUtils.create("Shapeless Crafting Recipe", NamedTextColor.GOLD)
            )
        )
    }

    private fun renderShapedRecipe(shaped: ShapedRecipe, event: InventoryOpenEvent) {
        var x = 0
        var y = 0
        event.titleOverride(
            ComponentUtils.merge(
                ComponentUtils.create(Symbols.OFFSET_NEG_1 + Symbols.SHAPED_RECIPE_MENU, NamedTextColor.WHITE),
                ComponentUtils.create(
                    Symbols.OFFSET_NEG_128 + Symbols.OFFSET_NEG_32 + Symbols.OFFSET_NEG_2 + "Recipes for: " + result.getI18NDisplayName(),
                    NamedTextColor.BLACK
                )
            )
        )
        for (row in shaped.getShape()) {
            for (ingredient in row.toCharArray()) {
                val choice = shaped.getChoiceMap().get(ingredient)
                val item = getItemFromRecipeChoice(choice)
                this.setButton(
                    y * 9 + x + CORNER,
                    item,
                    MenuButtonClickHandler { ev: InventoryClickEvent? -> handleIngredientClick(item) })
                x += 1
            }
            y += 1
            x = 0
        }
        this.setSlot(
            CORNER + 36 + 1,
            getNamedItem(Material.CRAFTING_TABLE, ComponentUtils.create("Shaped Crafting Recipe", NamedTextColor.GOLD))
        )
    }

    /**
     * Renders a transmute recipe on the interface.
     * Transmute recipes are pretty simple, it is just two items that turn an item into another, similar to shapeless.
     * @param transmute The transmute recipe.
     */
    private fun renderTransmuteRecipe(transmute: TransmuteRecipe, event: InventoryOpenEvent?) {
        val input = getItemFromRecipeChoice(transmute.getInput())
        val transmuter = getItemFromRecipeChoice(transmute.getMaterial())
        this.setButton(
            CORNER + 9 + 1,
            input,
            MenuButtonClickHandler { ev: InventoryClickEvent? -> handleIngredientClick(input) })
        this.setButton(
            CORNER + 9 + 2,
            transmuter,
            MenuButtonClickHandler { ev: InventoryClickEvent? -> handleIngredientClick(transmuter) })
        this.setSlot(
            CORNER + 27 + 1,
            getNamedItem(
                Material.CRAFTING_TABLE,
                ComponentUtils.create("Transmute Crafting Recipe", NamedTextColor.GOLD)
            )
        )
    }

    private fun renderCookingRecipe(cooking: CookingRecipe<*>, event: InventoryOpenEvent) {
        val top: Int = CORNER + 1
        val middle = top + 9
        val bottom = middle + 9
        val smelterSlot = middle + 27

        event.titleOverride(
            ComponentUtils.merge(
                ComponentUtils.create(Symbols.OFFSET_NEG_1 + Symbols.FURNACE_RECIPE_MENU, NamedTextColor.WHITE),
                ComponentUtils.create(
                    Symbols.OFFSET_NEG_128 + Symbols.OFFSET_NEG_32 + Symbols.OFFSET_NEG_2 + "Recipes for: " + result.getI18NDisplayName(),
                    NamedTextColor.BLACK
                )
            )
        )

        val smelter: ItemStack?

        if (cooking is FurnaceRecipe) smelter =
            getNamedItem(Material.FURNACE, ComponentUtils.create("Furnace Recipe", NamedTextColor.GOLD))
        else if (cooking is BlastingRecipe) smelter =
            getNamedItem(Material.BLAST_FURNACE, ComponentUtils.create("Blasting Recipe", NamedTextColor.GOLD))
        else if (cooking is SmokingRecipe) smelter =
            getNamedItem(Material.SMOKER, ComponentUtils.create("Smoker Recipe", NamedTextColor.GOLD))
        else if (cooking is CampfireRecipe) smelter =
            getNamedItem(Material.CAMPFIRE, ComponentUtils.create("Campfire Recipe", NamedTextColor.GOLD))
        else smelter = getNamedItem(
            Material.BARRIER,
            ComponentUtils.create("Unknown Smelter: " + cooking.getClass().getSimpleName(), NamedTextColor.RED)
        )

        smelter.lore(
            ComponentUtils.cleanItalics(
                List.of<Component?>(
                    ComponentUtils.EMPTY,
                    ComponentUtils.merge(
                        ComponentUtils.create("Cook in a "),
                        Component.translatable(smelter.translationKey()).color(NamedTextColor.YELLOW),
                        ComponentUtils.create(" block")
                    ),
                    ComponentUtils.merge(
                        ComponentUtils.create("for "),
                        ComponentUtils.create((cooking.getCookingTime() / 20).toString() + "s", NamedTextColor.GREEN)
                    )
                )
            )
        )

        val fire = BORDER_NORMAL.clone()
        fire.setData<CustomModelData?>(
            DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                .addString("smprpg:furnace_burn")
                .build()
        )

        val ingredient = getItemFromRecipeChoice(cooking.getInputChoice())
        this.setButton(
            top,
            ingredient,
            MenuButtonClickHandler { ev: InventoryClickEvent? -> handleIngredientClick(ingredient) })
        this.setSlot(smelterSlot, smelter)
        this.setSlot(middle, fire)
        this.setSlot(bottom, getNamedItem(Material.COAL, ComponentUtils.EMPTY))
    }

    /**
     * Generates an item stack that shows what "requirements" an item has in order to craft. This is subject to change,
     * but for now it simply displays what item "discovers" the recipe upon picking up. A common example is how the
     * recipe for a copper chestplate is discovered when we pick up a copper ingot.
     * 
     * @param requirements A collection of item stacks that is "required" to discover this recipe.
     * @return an item stack to be used as a display in the interface
     */
    fun getRequirements(requirements: MutableCollection<ItemStack>): ItemStack {
        val paper = createNamedItem(Material.PAPER, ComponentUtils.create("Crafting Requirements", NamedTextColor.RED))
        paper.editMeta(Consumer { meta: ItemMeta? ->
            val lore: MutableList<Component?> = ArrayList<Component?>()
            lore.add(ComponentUtils.EMPTY)
            lore.add(ComponentUtils.create("The following items must be"))
            lore.add(ComponentUtils.create("discovered to unlock this recipe"))
            lore.add(ComponentUtils.EMPTY)
            for (item in requirements) lore.add(ComponentUtils.create("- ").append(item.displayName()))
            meta!!.lore(ComponentUtils.cleanItalics(lore))
            meta.setEnchantmentGlintOverride(true)
        })
        SMPRPG.getService<ItemService?>(ItemService::class.java)!!.setIgnoreMetaUpdate(paper)
        return paper
    }

    private fun changePage(delta: Int, event: InventoryOpenEvent) {
        currentRecipe += delta

        if (delta < 0) this.sounds.playPagePrevious()
        else this.sounds.playPageNext()

        render(event)
    }

    fun render(event: InventoryOpenEvent) {
        this.clear()
        this.setBorderFull()

        this.renderRecipe(event)

        // Misc buttons
        this.setSlot(RESULT, result)

        val blueprint = ItemService.blueprint(result)
        if (blueprint is ICraftable) this.setSlot(REQUIREMENTS, getRequirements(blueprint.unlockedBy()))

        if (recipes.size() > 1) {
            this.setButton(
                46,
                getNamedItem(
                    Material.ARROW,
                    ComponentUtils.create(
                        "Previous Page " + (currentRecipe + 1) + "/" + recipes.size(),
                        NamedTextColor.GOLD
                    )
                ),
                MenuButtonClickHandler { ev: InventoryClickEvent? -> changePage(-1, event) })
            this.setButton(
                48,
                getNamedItem(
                    Material.ARROW,
                    ComponentUtils.create(
                        "Next Page " + (currentRecipe + 1) + "/" + recipes.size(),
                        NamedTextColor.GOLD
                    )
                ),
                MenuButtonClickHandler { ev: InventoryClickEvent? -> changePage(1, event) })
        }

        if (this.player.isOp()) {
            var key = "?"
            if (recipes.get(currentRecipe) is Keyed) key = keyed.getKey().asString()
            this.setSlot(
                0, getNamedItemWithDescription(
                    Material.OAK_SIGN,
                    ComponentUtils.create("Info", NamedTextColor.RED),
                    ComponentUtils.create("Key: " + key, NamedTextColor.YELLOW)
                )
            )
        }

        // Utility buttons
        this.setButton(
            (ROWS - 1) * 9 + 4,
            BUTTON_BACK,
            MenuButtonClickHandler { e: InventoryClickEvent? -> this.openParentMenu() })
    }

    companion object {
        const val ROWS: Int = 6

        // Hard set positions
        const val CORNER: Int = 10
        const val RESULT: Int = 24
        const val REQUIREMENTS: Int = 26
    }
}
