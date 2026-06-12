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

class SilverFamilyBlueprint(itemService: ItemService, type: CustomItemType) :
    CraftEngineCompressibleBlueprint(itemService, type), ICompressible, ISellable {

    override val itemClassification get() = ItemClassification.MATERIAL

    override val decompressor: CompressionStep? get() = when (customItemType) {
        CustomItemType.SILVER_BLOCK          -> CompressionStep(itemService.getBlueprint(CustomItemType.SILVER_INGOT) as ICompressible, 1, 9)
        CustomItemType.ENCHANTED_SILVER      -> CompressionStep(itemService.getBlueprint(CustomItemType.SILVER_BLOCK) as ICompressible, 1, 9)
        CustomItemType.ENCHANTED_SILVER_BLOCK-> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_SILVER) as ICompressible, 1, 9)
        CustomItemType.SILVER_SINGULARITY    -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_SILVER_BLOCK) as ICompressible, 1, 9)
        else -> null
    }

    override val compressor: CompressionStep? get() = when (customItemType) {
        CustomItemType.SILVER_INGOT          -> CompressionStep(itemService.getBlueprint(CustomItemType.SILVER_BLOCK) as ICompressible, 9, 1)
        CustomItemType.SILVER_BLOCK          -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_SILVER) as ICompressible, 9, 1)
        CustomItemType.ENCHANTED_SILVER      -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_SILVER_BLOCK) as ICompressible, 9, 1)
        CustomItemType.ENCHANTED_SILVER_BLOCK-> CompressionStep(itemService.getBlueprint(CustomItemType.SILVER_SINGULARITY) as ICompressible, 9, 1)
        else -> null
    }

    override fun getWorth(item: ItemStack) = calculateCompressedWorth(item)
}
