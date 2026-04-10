package xyz.devvydont.smprpg.items.blueprints.resources.crafting

import net.kyori.adventure.key.Key
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.CraftingRecipe
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.recipe.CraftingBookCategory
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint
import xyz.devvydont.smprpg.items.interfaces.ICraftable
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden
import xyz.devvydont.smprpg.items.interfaces.ISellable
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.services.ItemService.Companion.generate

class AmbrosiumToolShaft(itemService: ItemService, type: CustomItemType) : CustomItemBlueprint(itemService, type),
    ISellable, ICraftable, IModelOverridden {

    override val itemClassification: ItemClassification get() = ItemClassification.ITEM

    override fun getRecipeKey(): NamespacedKey { return ICraftable.getDefaultRecipeKey(customItemType) }

    override fun getCustomRecipe(): CraftingRecipe {
        val recipe = ShapedRecipe(recipeKey, generate())
        recipe.shape("z", "a", "z")
        recipe.setIngredient('z', generate(CustomItemType.ZANITE_BLOCK))
        recipe.setIngredient('a', generate(CustomItemType.AMBROSIUM)) // TODO: Replace with Enchanted Ambrosium when compression items are in
        recipe.setCategory(CraftingBookCategory.MISC)
        return recipe
    }

    override fun unlockedBy(): MutableCollection<ItemStack?> {
        return mutableListOf(
            generate(CustomItemType.AMBROSIUM),
            generate(CustomItemType.ZANITE)
        )
    }

    override fun getWorth(itemStack: ItemStack): Int { return 5_220 * itemStack.amount }

    override fun getDisplayKey(): Key? { return IModelOverridden.ofItemTypeInDirectory(customItemType, "materials") }
}
