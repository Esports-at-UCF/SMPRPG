package xyz.devvydont.smprpg.items.blueprints.resources.woodcutting

import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.blueprints.craftengine.CraftEngineCompressibleBlueprint
import xyz.devvydont.smprpg.items.interfaces.ICompressible
import xyz.devvydont.smprpg.items.interfaces.ICompressible.CompressionStep
import xyz.devvydont.smprpg.items.interfaces.ISellable
import xyz.devvydont.smprpg.services.ItemService

private const val WORTH_PER_ITEM = 8

class SkyrootLog(itemService: ItemService, type: CustomItemType) :
    CraftEngineCompressibleBlueprint(itemService, type), ICompressible, ISellable {

    override val itemClassification: ItemClassification get() = ItemClassification.MATERIAL

    override val decompressor: CompressionStep? get() = null

    override val compressor: CompressionStep? get() = when (customItemType) {
        CustomItemType.SULFUR ->
            CompressionStep(itemService.getBlueprint(CustomItemType.PREMIUM_SKYROOT_LOG) as ICompressible, 9, 1)
        else -> null
    }

    override fun getWorth(item: ItemStack): Int = WORTH_PER_ITEM * item.amount
}

