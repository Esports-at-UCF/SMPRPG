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
import xyz.devvydont.smprpg.items.interfaces.ICompressible
import xyz.devvydont.smprpg.items.interfaces.ISellable
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.services.ItemService.Companion.blueprint
import xyz.devvydont.smprpg.services.ItemService.Companion.generate

class RawTitaniumBlock(itemService: ItemService, type: CustomItemType) : BlockBlueprint(itemService, type),
    ICraftable, ISellable, ICompressible {
    override fun getCustomBlock(): CustomBlock {
        return CustomBlock.RAW_TITANIUM_BLOCK
    }

    override fun getRecipeKey(): NamespacedKey {
        return NamespacedKey(plugin, this.customItemType.key + "_recipe")
    }

    override fun getCustomRecipe(): CraftingRecipe {
        val recipe = ShapedRecipe(this.recipeKey, generate())
        recipe.shape(
            "ttt",
            "ttt",
            "ttt"
        )
        recipe.setIngredient('t', generate(CustomItemType.RAW_TITANIUM))
        recipe.setCategory(CraftingBookCategory.MISC)
        return recipe
    }

    override fun unlockedBy(): MutableCollection<ItemStack?> {
        return mutableListOf(
            itemService.getCustomItem(CustomItemType.RAW_TITANIUM)
        )
    }

    override fun getWorth(item: ItemStack): Int {
        val ingot = itemService.getCustomItem(CustomItemType.RAW_TITANIUM)
        val bp = blueprint(ingot)
        if (bp is ISellable) {
            return ((bp as ISellable).getWorth(ingot) * 9) * item.amount
        }
        return 0
    }

    override val decompressor: ICompressible.CompressionStep? get() = when (customItemType) {
        CustomItemType.RAW_TITANIUM_BLOCK ->
            ICompressible.CompressionStep(itemService.getBlueprint(CustomItemType.RAW_TITANIUM) as ICompressible, 1, 9)
        else -> null
    }

    override val compressor: ICompressible.CompressionStep? get() = when (customItemType) {
        CustomItemType.RAW_TITANIUM_BLOCK ->
            ICompressible.CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_RAW_TITANIUM) as ICompressible, 9, 1)
        else -> null
    }
}
