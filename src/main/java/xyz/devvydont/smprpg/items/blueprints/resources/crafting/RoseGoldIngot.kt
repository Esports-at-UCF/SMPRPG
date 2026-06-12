package xyz.devvydont.smprpg.items.blueprints.resources.crafting

import net.kyori.adventure.key.Key
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.CraftingRecipe
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapelessRecipe
import org.bukkit.inventory.recipe.CraftingBookCategory
import xyz.devvydont.smprpg.SMPRPG.Companion.plugin
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint
import xyz.devvydont.smprpg.items.blueprints.craftengine.CraftEngineBlueprint
import xyz.devvydont.smprpg.items.interfaces.ICraftable
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden
import xyz.devvydont.smprpg.items.interfaces.ISellable
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.services.ItemService.Companion.generate
import java.util.List

class RoseGoldIngot(itemService: ItemService, type: CustomItemType) : CraftEngineBlueprint(itemService, type), ISellable,
    ICraftable, IModelOverridden {
    override val itemClassification: ItemClassification get() = ItemClassification.MATERIAL

    override fun getRecipeKey(): NamespacedKey { return NamespacedKey(plugin, this.customItemType.getKey() + "_recipe") }

    override fun getCustomRecipe(): CraftingRecipe {
        val recipe = ShapelessRecipe(getRecipeKey(), generate())
        recipe.addIngredient(4, generate(Material.COPPER_INGOT))
        recipe.addIngredient(4, generate(Material.GOLD_INGOT))
        recipe.setCategory(CraftingBookCategory.MISC)
        return recipe
    }

    /**
     * A collection of items that will unlock the recipe for this item. Typically will be one of the components
     * of the recipe itself, but can be set to whatever is desired
     *
     * @return
     */
    override fun unlockedBy(): MutableCollection<ItemStack?> {
        return mutableListOf(
            generate(Material.COPPER_INGOT),
            generate(Material.GOLD_INGOT)
        )
    }

    /**
     * Given this item stack, how much should it be able to sell for?
     * Keep in mind that the size of the stack needs to considered as well!
     *
     * @param item The item that can be sold.
     * @return The worth of the item.
     */
    override fun getWorth(item: ItemStack): Int { return 350 * item.amount }

    override fun getDisplayKey(): Key { return IModelOverridden.ofItemTypeInDirectory(customItemType, "materials") }

}
