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

class RawTitaniumFamilyBlueprint(itemService: ItemService, type: CustomItemType) :
    CustomItemBlueprint(itemService, type), ICompressible, ISellable, IModelOverridden {

    override val itemClassification get() = ItemClassification.MATERIAL

    override val decompressor: CompressionStep? get() = when (customItemType) {
        CustomItemType.RAW_TITANIUM_BLOCK -> CompressionStep(itemService.getBlueprint(CustomItemType.RAW_TITANIUM) as ICompressible, 1, 9)
        CustomItemType.ENCHANTED_RAW_TITANIUM -> CompressionStep(itemService.getBlueprint(CustomItemType.RAW_TITANIUM_BLOCK) as ICompressible, 1, 9)
        else -> null
    }

    override val compressor: CompressionStep? get() = when (customItemType) {
        CustomItemType.RAW_TITANIUM -> CompressionStep(itemService.getBlueprint(CustomItemType.RAW_TITANIUM_BLOCK) as ICompressible, 9, 1)
        CustomItemType.RAW_TITANIUM_BLOCK -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_RAW_TITANIUM) as ICompressible, 9, 1)
        else -> null
    }

    override fun getWorth(itemStack: ItemStack) = calculateCompressedWorth(itemStack)

    override fun getDisplayKey(): Key? = when (customItemType) {
        CustomItemType.RAW_TITANIUM, CustomItemType.ENCHANTED_RAW_TITANIUM ->
            IModelOverridden.ofItemTypeInDirectory(CustomItemType.RAW_TITANIUM, "materials")
        CustomItemType.RAW_TITANIUM_BLOCK ->
            IModelOverridden.ofItemTypeInDirectory(CustomItemType.RAW_TITANIUM_BLOCK, "blocks")
        else -> null
    }
}
