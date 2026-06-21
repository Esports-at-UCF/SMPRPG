package xyz.devvydont.smprpg.items.blueprints.resources.farming

import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.blueprints.craftengine.CraftEngineCompressibleBlueprint
import xyz.devvydont.smprpg.items.interfaces.ICompressible
import xyz.devvydont.smprpg.items.interfaces.ISellable
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.extensions.calculateCompressedWorth

class OnionBlueprintFamily(itemService: ItemService, type: CustomItemType) :
    CraftEngineCompressibleBlueprint(itemService, type), ICompressible, ISellable {

    override val itemClassification get() = ItemClassification.MATERIAL

    override val decompressor: ICompressible.CompressionStep? get() = when (customItemType) {
        CustomItemType.PREMIUM_ONION          -> ICompressible.CompressionStep(itemService.getBlueprint(CustomItemType.ONION) as ICompressible, 1, 9)
        CustomItemType.ENCHANTED_ONION      -> ICompressible.CompressionStep(itemService.getBlueprint(CustomItemType.PREMIUM_ONION) as ICompressible, 1, 9)
        CustomItemType.ONION_SINGULARITY -> ICompressible.CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_ONION) as ICompressible, 1, 9)
        else -> null
    }

    override val compressor: ICompressible.CompressionStep? get() = when (customItemType) {
        CustomItemType.ONION          -> ICompressible.CompressionStep(itemService.getBlueprint(CustomItemType.PREMIUM_ONION) as ICompressible, 9, 1)
        CustomItemType.PREMIUM_ONION          -> ICompressible.CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_ONION) as ICompressible, 9, 1)
        CustomItemType.ENCHANTED_ONION      -> ICompressible.CompressionStep(itemService.getBlueprint(CustomItemType.ONION_SINGULARITY) as ICompressible, 9, 1)
        else -> null
    }

    override fun wantFakeEnchantGlow(): Boolean {
        return type != CustomItemType.ONION
    }

    override fun getWorth(item: ItemStack) = calculateCompressedWorth(item)
}