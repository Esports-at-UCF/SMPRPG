package xyz.devvydont.smprpg.items.blueprints.resources.mining

import net.kyori.adventure.key.Key
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint
import xyz.devvydont.smprpg.items.interfaces.ICompressible
import xyz.devvydont.smprpg.items.interfaces.ICompressible.CompressionStep
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden
import xyz.devvydont.smprpg.items.interfaces.ISellable
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.extensions.calculateCompressedWorth

class SilverFamilyBlueprint(itemService: ItemService, type: CustomItemType) :
    CustomItemBlueprint(itemService, type), ICompressible, ISellable, IModelOverridden {

    override val itemClassification get() = ItemClassification.MATERIAL

    override val decompressor: CompressionStep? get() = when (customItemType) {
        CustomItemType.SILVER_BLOCK           -> CompressionStep(itemService.getBlueprint(CustomItemType.SILVER_INGOT) as ICompressible, 1, 9)
        CustomItemType.ENCHANTED_SILVER       -> CompressionStep(itemService.getBlueprint(CustomItemType.SILVER_BLOCK) as ICompressible, 1, 9)
        CustomItemType.ENCHANTED_SILVER_BLOCK -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_SILVER) as ICompressible, 1, 9)
        CustomItemType.SILVER_SINGULARITY     -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_SILVER_BLOCK) as ICompressible, 1, 9)
        else -> null
    }

    override val compressor: CompressionStep? get() = when (customItemType) {
        CustomItemType.SILVER_INGOT           -> CompressionStep(itemService.getBlueprint(CustomItemType.SILVER_BLOCK) as ICompressible, 9, 1)
        CustomItemType.SILVER_BLOCK           -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_SILVER) as ICompressible, 9, 1)
        CustomItemType.ENCHANTED_SILVER       -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_SILVER_BLOCK) as ICompressible, 9, 1)
        CustomItemType.ENCHANTED_SILVER_BLOCK -> CompressionStep(itemService.getBlueprint(CustomItemType.SILVER_SINGULARITY) as ICompressible, 9, 1)
        else -> null
    }

    override fun getWorth(itemStack: ItemStack) = calculateCompressedWorth(itemStack)

    override fun getDisplayKey(): Key? = when (customItemType) {
        CustomItemType.SILVER_INGOT, CustomItemType.ENCHANTED_SILVER, CustomItemType.SILVER_SINGULARITY ->
            IModelOverridden.ofItemTypeInDirectory(CustomItemType.SILVER_INGOT, "materials")
        CustomItemType.SILVER_BLOCK, CustomItemType.ENCHANTED_SILVER_BLOCK ->
            IModelOverridden.ofItemTypeInDirectory(CustomItemType.SILVER_BLOCK, "blocks")
        else -> null
    }
}
