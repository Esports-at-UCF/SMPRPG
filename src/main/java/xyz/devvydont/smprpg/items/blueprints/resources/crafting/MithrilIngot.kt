package xyz.devvydont.smprpg.items.blueprints.resources.crafting

import net.kyori.adventure.key.Key
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.RecipeChoice.ExactChoice
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden
import xyz.devvydont.smprpg.items.interfaces.ISellable
import xyz.devvydont.smprpg.items.interfaces.ISmeltable
import xyz.devvydont.smprpg.items.interfaces.ISmeltable.RecipeType
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.services.ItemService.Companion.generate
import xyz.devvydont.smprpg.util.time.TickTime

class MithrilIngot(itemService: ItemService, type: CustomItemType) : CustomItemBlueprint(itemService, type), ISmeltable,
    ISellable, IModelOverridden {
    override val itemClassification: ItemClassification get() = ItemClassification.MATERIAL

    /**
     * Get the ingredient that is used to smelt this item.
     *
     * @return The [RecipeChoice] that will turn into this item when cooked.
     */
    override fun getIngredient(): RecipeChoice { return ExactChoice(generate(CustomItemType.RAW_MITHRIL)) }

    /**
     * The vanilla Minecraft experience that is awarded as a result for cooking this item.
     *
     * @return The vanilla Minecraft experience.
     */
    override fun getExperience(): Float { return 5f }

    /**
     * The cooking time in ticks in order to cook this item.
     *
     * @return The time in ticks.
     */
    override fun getCookingTime(): Long { return TickTime.seconds(20) }

    /**
     * Gets the recipe type for this furnace.
     *
     * @return The type of smelting recipe.
     */
    override fun getRecipeType(): RecipeType { return RecipeType.BLASTING }

    /**
     * Given this item stack, how much should it be able to sell for?
     * Keep in mind that the size of the stack needs to considered as well!
     *
     * @param item The item that can be sold.
     * @return The worth of the item.
     */
    override fun getWorth(item: ItemStack): Int { return 150 * item.amount }

    override fun getDisplayKey(): Key { return IModelOverridden.ofItemTypeInDirectory(customItemType, "materials") }
}
