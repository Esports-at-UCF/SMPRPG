package xyz.devvydont.smprpg.items.blueprints.resources.mining

import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.blueprints.craftengine.CraftEngineCompressibleBlueprint
import xyz.devvydont.smprpg.items.interfaces.ICompressible
import xyz.devvydont.smprpg.items.interfaces.ICompressible.CompressionStep
import xyz.devvydont.smprpg.items.interfaces.ISellable
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.extensions.calculateCompressedWorth

class PlatinumFamilyBlueprint(itemService: ItemService, type: CustomItemType) :
    CraftEngineCompressibleBlueprint(itemService, type), ICompressible, ISellable {

    override val itemClassification get() = ItemClassification.MATERIAL

    override val decompressor: CompressionStep? get() = when (customItemType) {
        CustomItemType.PLATINUM_BLOCK          -> CompressionStep(itemService.getBlueprint(CustomItemType.PLATINUM_INGOT) as ICompressible, 1, 9)
        CustomItemType.ENCHANTED_PLATINUM      -> CompressionStep(itemService.getBlueprint(CustomItemType.PLATINUM_BLOCK) as ICompressible, 1, 9)
        CustomItemType.ENCHANTED_PLATINUM_BLOCK-> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_PLATINUM) as ICompressible, 1, 9)
        CustomItemType.PLATINUM_SINGULARITY    -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_PLATINUM_BLOCK) as ICompressible, 1, 9)
        else -> null
    }

    override val compressor: CompressionStep? get() = when (customItemType) {
        CustomItemType.PLATINUM_INGOT          -> CompressionStep(itemService.getBlueprint(CustomItemType.PLATINUM_BLOCK) as ICompressible, 9, 1)
        CustomItemType.PLATINUM_BLOCK          -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_PLATINUM) as ICompressible, 9, 1)
        CustomItemType.ENCHANTED_PLATINUM      -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_PLATINUM_BLOCK) as ICompressible, 9, 1)
        CustomItemType.ENCHANTED_PLATINUM_BLOCK-> CompressionStep(itemService.getBlueprint(CustomItemType.PLATINUM_SINGULARITY) as ICompressible, 9, 1)
        else -> null
    }

    override fun getWorth(item: ItemStack) = calculateCompressedWorth(item)
}
