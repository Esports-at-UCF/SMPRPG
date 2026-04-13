package xyz.devvydont.smprpg.items.blueprints.block.interactable

import org.bukkit.NamespacedKey
import org.bukkit.inventory.CraftingRecipe
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.recipe.CraftingBookCategory
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.blueprints.block.CraftEngineBlueprint
import xyz.devvydont.smprpg.items.interfaces.ICraftable
import xyz.devvydont.smprpg.services.ItemService

class ReforgeTable(itemService: ItemService, type: CustomItemType) : CraftEngineBlueprint(itemService, type),
    ICraftable {

    override fun getRecipeKey(): NamespacedKey {
        return ICraftable.getDefaultRecipeKey(customItemType)
    }

    override fun getCustomRecipe(): CraftingRecipe {
        val recipe = ShapedRecipe(recipeKey, generate())
        recipe.shape(
            "zz",
            "ss",
            "ss"
        )
        recipe.setIngredient('s', itemService.getCustomItem(CustomItemType.STEEL_INGOT))
        recipe.setIngredient('z', itemService.getCustomItem(CustomItemType.ZANITE))
        recipe.setCategory(CraftingBookCategory.MISC)
        return recipe
    }

    override fun unlockedBy(): Collection<ItemStack?> {
        return mutableListOf(
            itemService.getCustomItem(CustomItemType.STEEL_INGOT),
            itemService.getCustomItem(CustomItemType.ZANITE)
        )
    }
}