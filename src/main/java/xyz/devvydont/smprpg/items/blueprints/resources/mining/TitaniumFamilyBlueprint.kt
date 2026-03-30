package xyz.devvydont.smprpg.items.blueprints.resources.mining

import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint
import xyz.devvydont.smprpg.items.interfaces.ICompressible
import xyz.devvydont.smprpg.items.interfaces.ICompressible.CompressionStep
import xyz.devvydont.smprpg.items.interfaces.ISellable
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.extensions.calculateCompressedWorth

class TitaniumFamilyBlueprint(itemService: ItemService, type: CustomItemType) :
    CustomItemBlueprint(itemService, type), ICompressible, ISellable {

    override val itemClassification get() = ItemClassification.MATERIAL

    override val decompressor: CompressionStep? get() = when (customItemType) {
        CustomItemType.TITANIUM_BLOCK          -> CompressionStep(itemService.getBlueprint(CustomItemType.TITANIUM_INGOT) as ICompressible, 1, 9)
        CustomItemType.ENCHANTED_TITANIUM      -> CompressionStep(itemService.getBlueprint(CustomItemType.TITANIUM_BLOCK) as ICompressible, 1, 9)
        CustomItemType.ENCHANTED_TITANIUM_BLOCK-> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_TITANIUM) as ICompressible, 1, 9)
        CustomItemType.TITANIUM_SINGULARITY    -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_TITANIUM_BLOCK) as ICompressible, 1, 9)
        else -> null
    }

    override val compressor: CompressionStep? get() = when (customItemType) {
        CustomItemType.TITANIUM_INGOT          -> CompressionStep(itemService.getBlueprint(CustomItemType.TITANIUM_BLOCK) as ICompressible, 9, 1)
        CustomItemType.TITANIUM_BLOCK          -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_TITANIUM) as ICompressible, 9, 1)
        CustomItemType.ENCHANTED_TITANIUM      -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_TITANIUM_BLOCK) as ICompressible, 9, 1)
        CustomItemType.ENCHANTED_TITANIUM_BLOCK-> CompressionStep(itemService.getBlueprint(CustomItemType.TITANIUM_SINGULARITY) as ICompressible, 9, 1)
        else -> null
    }

    override fun getWorth(itemStack: ItemStack) = calculateCompressedWorth(itemStack)
}
