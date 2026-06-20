package xyz.devvydont.smprpg.items.blueprints.resources.mining

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint
import xyz.devvydont.smprpg.items.interfaces.ICompressible
import xyz.devvydont.smprpg.items.interfaces.ICompressible.CompressionStep
import xyz.devvydont.smprpg.items.interfaces.ISellable
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.extensions.calculateCompressedWorth

class DirtFamilyBlueprint(itemService: ItemService, type: CustomItemType) : CustomItemBlueprint(itemService, type),
    ICompressible, ISellable {
    override val itemClassification: ItemClassification get() = ItemClassification.MATERIAL

    override val decompressor: CompressionStep? get() = when (customItemType) {
            CustomItemType.COMPRESSED_DIRT -> CompressionStep(itemService.getVanillaBlueprint(ItemStack.of(Material.DIRT)) as ICompressible, 1, 9)
            CustomItemType.DOUBLE_COMPRESSED_DIRT -> CompressionStep(itemService.getBlueprint(CustomItemType.COMPRESSED_DIRT) as ICompressible, 1, 9)
            CustomItemType.ENCHANTED_DIRT -> CompressionStep(itemService.getBlueprint(CustomItemType.DOUBLE_COMPRESSED_DIRT) as ICompressible, 1, 9)
            CustomItemType.DIRT_SINGULARITY -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_DIRT) as ICompressible, 1, 9)
            else -> null
        }

    override val compressor: CompressionStep? get() = when (customItemType) {
            CustomItemType.COMPRESSED_DIRT -> CompressionStep(itemService.getBlueprint(CustomItemType.DOUBLE_COMPRESSED_DIRT) as ICompressible, 9, 1)
            CustomItemType.DOUBLE_COMPRESSED_DIRT -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_DIRT) as ICompressible, 9, 1)
            CustomItemType.ENCHANTED_DIRT -> CompressionStep(itemService.getBlueprint(CustomItemType.DIRT_SINGULARITY) as ICompressible, 9, 1)
            else -> null
        }

    override fun getWorth(itemStack: ItemStack): Int {
        return this.calculateCompressedWorth(itemStack)
    }
}
