package xyz.devvydont.smprpg.items.blueprints.resources.mining

import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint
import xyz.devvydont.smprpg.items.blueprints.craftengine.CraftEngineCompressibleBlueprint
import xyz.devvydont.smprpg.items.interfaces.ICompressible
import xyz.devvydont.smprpg.items.interfaces.ICompressible.CompressionStep
import xyz.devvydont.smprpg.items.interfaces.ISellable
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.extensions.calculateCompressedWorth

class TinFamilyBlueprint(itemService: ItemService, type: CustomItemType) :
    CraftEngineCompressibleBlueprint(itemService, type), ICompressible, ISellable {

    override val itemClassification get() = ItemClassification.MATERIAL

    override val decompressor: CompressionStep? get() = when (customItemType) {
        CustomItemType.TIN_BLOCK          -> CompressionStep(itemService.getBlueprint(CustomItemType.TIN_INGOT) as ICompressible, 1, 9)
        CustomItemType.ENCHANTED_TIN      -> CompressionStep(itemService.getBlueprint(CustomItemType.TIN_BLOCK) as ICompressible, 1, 9)
        CustomItemType.ENCHANTED_TIN_BLOCK-> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_TIN) as ICompressible, 1, 9)
        CustomItemType.TIN_SINGULARITY    -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_TIN_BLOCK) as ICompressible, 1, 9)
        else -> null
    }

    override val compressor: CompressionStep? get() = when (customItemType) {
        CustomItemType.TIN_INGOT          -> CompressionStep(itemService.getBlueprint(CustomItemType.TIN_BLOCK) as ICompressible, 9, 1)
        CustomItemType.TIN_BLOCK          -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_TIN) as ICompressible, 9, 1)
        CustomItemType.ENCHANTED_TIN      -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_TIN_BLOCK) as ICompressible, 9, 1)
        CustomItemType.ENCHANTED_TIN_BLOCK-> CompressionStep(itemService.getBlueprint(CustomItemType.TIN_SINGULARITY) as ICompressible, 9, 1)
        else -> null
    }

    override fun getWorth(item: ItemStack) = calculateCompressedWorth(item)
}
