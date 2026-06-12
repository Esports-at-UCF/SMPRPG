package xyz.devvydont.smprpg.items.blueprints.resources.mining

import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint
import xyz.devvydont.smprpg.items.interfaces.ICompressible
import xyz.devvydont.smprpg.items.interfaces.ICompressible.CompressionStep
import xyz.devvydont.smprpg.items.interfaces.ISellable
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.extensions.calculateCompressedWorth
import org.bukkit.inventory.ItemStack

class MithrilFamilyBlueprint(itemService: ItemService, type: CustomItemType) :
    CustomItemBlueprint(itemService, type), ICompressible, ISellable {

    override val itemClassification get() = ItemClassification.MATERIAL

    override val decompressor: CompressionStep? get() = when (customItemType) {
        CustomItemType.MITHRIL_BLOCK          -> CompressionStep(itemService.getBlueprint(CustomItemType.MITHRIL_INGOT) as ICompressible, 1, 9)
        CustomItemType.ENCHANTED_MITHRIL      -> CompressionStep(itemService.getBlueprint(CustomItemType.MITHRIL_BLOCK) as ICompressible, 1, 9)
        CustomItemType.ENCHANTED_MITHRIL_BLOCK-> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_MITHRIL) as ICompressible, 1, 9)
        CustomItemType.MITHRIL_SINGULARITY    -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_MITHRIL_BLOCK) as ICompressible, 1, 9)
        else -> null
    }

    override val compressor: CompressionStep? get() = when (customItemType) {
        CustomItemType.MITHRIL_INGOT          -> CompressionStep(itemService.getBlueprint(CustomItemType.MITHRIL_BLOCK) as ICompressible, 9, 1)
        CustomItemType.MITHRIL_BLOCK          -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_MITHRIL) as ICompressible, 9, 1)
        CustomItemType.ENCHANTED_MITHRIL      -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_MITHRIL_BLOCK) as ICompressible, 9, 1)
        CustomItemType.ENCHANTED_MITHRIL_BLOCK-> CompressionStep(itemService.getBlueprint(CustomItemType.MITHRIL_SINGULARITY) as ICompressible, 9, 1)
        else -> null
    }

    override fun getWorth(itemStack: ItemStack) = calculateCompressedWorth(itemStack)
}
