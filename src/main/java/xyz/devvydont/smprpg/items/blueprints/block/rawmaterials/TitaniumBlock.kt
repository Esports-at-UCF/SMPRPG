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
import xyz.devvydont.smprpg.items.interfaces.ICompressible
import xyz.devvydont.smprpg.items.interfaces.ICompressible.CompressionStep
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.services.ItemService.Companion.blueprint
import xyz.devvydont.smprpg.services.ItemService.Companion.generate

class TitaniumBlock(itemService: ItemService, type: CustomItemType) : BlockBlueprint(itemService, type),
    ISellable, ICompressible {
    override fun getCustomBlock(): CustomBlock {
        return CustomBlock.TITANIUM_BLOCK
    }

    override fun getWorth(item: ItemStack): Int {
        val ingot = itemService.getCustomItem(CustomItemType.TITANIUM_INGOT)
        val bp = blueprint(ingot)
        if (bp is ISellable) {
            return ((bp as ISellable).getWorth(ingot) * 9) * item.amount
        }
        return 0
    }

    override val decompressor: CompressionStep? get() = when (customItemType) {
        CustomItemType.TITANIUM_BLOCK ->
            CompressionStep(itemService.getBlueprint(CustomItemType.TITANIUM_INGOT) as ICompressible, 1, 9)
        else -> null
    }

    override val compressor: CompressionStep? get() = when (customItemType) {
        CustomItemType.TITANIUM_BLOCK ->
            CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_TITANIUM) as ICompressible, 9, 1)
        else -> null
    }
}
