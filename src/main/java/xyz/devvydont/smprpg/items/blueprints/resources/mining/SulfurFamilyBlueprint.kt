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

class SulfurFamilyBlueprint(itemService: ItemService, type: CustomItemType) :
    CustomItemBlueprint(itemService, type), ICompressible, ISellable, IModelOverridden {

    override val itemClassification get() = ItemClassification.MATERIAL

    override val decompressor: CompressionStep? get() = when (customItemType) {
        CustomItemType.SULFUR_BLOCK           -> CompressionStep(itemService.getBlueprint(CustomItemType.SULFUR) as ICompressible, 1, 9)
        CustomItemType.ENCHANTED_SULFUR       -> CompressionStep(itemService.getBlueprint(CustomItemType.SULFUR_BLOCK) as ICompressible, 1, 9)
        CustomItemType.ENCHANTED_SULFUR_BLOCK -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_SULFUR) as ICompressible, 1, 9)
        CustomItemType.SULFUR_SINGULARITY     -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_SULFUR_BLOCK) as ICompressible, 1, 9)
        else -> null
    }

    override val compressor: CompressionStep? get() = when (customItemType) {
        CustomItemType.SULFUR                 -> CompressionStep(itemService.getBlueprint(CustomItemType.SULFUR_BLOCK) as ICompressible, 9, 1)
        CustomItemType.SULFUR_BLOCK           -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_SULFUR) as ICompressible, 9, 1)
        CustomItemType.ENCHANTED_SULFUR       -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_SULFUR_BLOCK) as ICompressible, 9, 1)
        CustomItemType.ENCHANTED_SULFUR_BLOCK -> CompressionStep(itemService.getBlueprint(CustomItemType.SULFUR_SINGULARITY) as ICompressible, 9, 1)
        else -> null
    }

    override fun getWorth(itemStack: ItemStack) = calculateCompressedWorth(itemStack)

    override fun getDisplayKey(): Key? = when (customItemType) {
        CustomItemType.SULFUR, CustomItemType.ENCHANTED_SULFUR, CustomItemType.SULFUR_SINGULARITY ->
            IModelOverridden.ofItemTypeInDirectory(CustomItemType.SULFUR, "materials")
        CustomItemType.SULFUR_BLOCK, CustomItemType.ENCHANTED_SULFUR_BLOCK ->
            IModelOverridden.ofMaterial(org.bukkit.Material.POISONOUS_POTATO)
        else -> null
    }
}
