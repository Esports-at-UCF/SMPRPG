package xyz.devvydont.smprpg.items.blueprints.resources.crafting

import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.blueprints.craftengine.CraftEngineBlueprint
import xyz.devvydont.smprpg.items.interfaces.ICompressible
import xyz.devvydont.smprpg.items.interfaces.ICompressible.CompressionStep
import xyz.devvydont.smprpg.items.interfaces.ISellable
import xyz.devvydont.smprpg.services.ItemService

class PlatinumIngot(itemService: ItemService, type: CustomItemType) :
    CraftEngineBlueprint(itemService, type), ICompressible, ISellable {
    override val itemClassification: ItemClassification
        /**
         * Determine what type of item this is.
         */
        get() = ItemClassification.MATERIAL

    override fun getWorth(item: ItemStack): Int {
        return 450 * item.amount
    }

    override val decompressor: CompressionStep? get() = null

    override val compressor: CompressionStep? get() = when (customItemType) {
        CustomItemType.PLATINUM_INGOT ->
            CompressionStep(itemService.getBlueprint(CustomItemType.PLATINUM_BLOCK) as ICompressible, 9, 1)
        else -> null
    }
}
