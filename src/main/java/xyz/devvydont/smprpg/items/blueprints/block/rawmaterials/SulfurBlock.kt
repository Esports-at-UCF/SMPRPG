package xyz.devvydont.smprpg.items.blueprints.block.rawmaterials

import org.bukkit.NamespacedKey
import org.bukkit.inventory.CraftingRecipe
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.recipe.CraftingBookCategory
import xyz.devvydont.smprpg.SMPRPG.Companion.plugin
import xyz.devvydont.smprpg.block.CustomBlock
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.blueprints.block.BlockBlueprint
import xyz.devvydont.smprpg.items.interfaces.ICraftable
import xyz.devvydont.smprpg.items.interfaces.ISellable
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.services.ItemService.Companion.blueprint
import xyz.devvydont.smprpg.services.ItemService.Companion.generate

class SulfurBlock(itemService: ItemService, type: CustomItemType) : BlockBlueprint(itemService, type), ICraftable,
    ISellable {
    override fun getCustomBlock(): CustomBlock {
        return CustomBlock.SULFUR_BLOCK
    }

    override fun getRecipeKey(): NamespacedKey {
        return NamespacedKey(plugin, this.customItemType.key + "_recipe")
    }

    override fun getCustomRecipe(): CraftingRecipe {
        val recipe = ShapedRecipe(this.recipeKey, generate())
        recipe.shape(
            "sss",
            "sss",
            "sss"
        )
        recipe.setIngredient('s', generate(CustomItemType.SULFUR))
        recipe.setCategory(CraftingBookCategory.MISC)
        return recipe
    }

    override fun unlockedBy(): MutableCollection<ItemStack?> {
        return mutableListOf(
            itemService.getCustomItem(CustomItemType.SULFUR)
        )
    }

    override fun getWorth(item: ItemStack): Int {
        val dust = itemService.getCustomItem(CustomItemType.SULFUR)
        val bp = blueprint(dust)
        if (bp is ISellable) {
            return ((bp as ISellable).getWorth(dust) * 9) * item.amount
        }
        return 0
    }
}
