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

class RawSilverFamilyBlueprint(itemService: ItemService, type: CustomItemType) :
    CustomItemBlueprint(itemService, type), ICompressible, ISellable, IModelOverridden {

    override val itemClassification get() = ItemClassification.MATERIAL

    override val decompressor: CompressionStep? get() = when (customItemType) {
        CustomItemType.RAW_SILVER_BLOCK    -> CompressionStep(itemService.getBlueprint(CustomItemType.RAW_SILVER) as ICompressible, 1, 9)
        CustomItemType.ENCHANTED_RAW_SILVER-> CompressionStep(itemService.getBlueprint(CustomItemType.RAW_SILVER_BLOCK) as ICompressible, 1, 9)
        else -> null
    }

    override val compressor: CompressionStep? get() = when (customItemType) {
        CustomItemType.RAW_SILVER      -> CompressionStep(itemService.getBlueprint(CustomItemType.RAW_SILVER_BLOCK) as ICompressible, 9, 1)
        CustomItemType.RAW_SILVER_BLOCK-> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_RAW_SILVER) as ICompressible, 9, 1)
        else -> null
    }

    override fun getWorth(itemStack: ItemStack) = calculateCompressedWorth(itemStack)

    override fun getDisplayKey(): Key? = when (customItemType) {
        CustomItemType.RAW_SILVER, CustomItemType.ENCHANTED_RAW_SILVER ->
            IModelOverridden.ofItemTypeInDirectory(CustomItemType.RAW_SILVER, "materials")
        CustomItemType.RAW_SILVER_BLOCK ->
            IModelOverridden.ofItemTypeInDirectory(CustomItemType.SILVER_BLOCK, "blocks")
        else -> null
    }
}
