package xyz.devvydont.smprpg.items.blueprints.block.rawmaterials

import xyz.devvydont.smprpg.block.CustomBlock
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.blueprints.block.BlockBlueprint
import xyz.devvydont.smprpg.items.interfaces.ICompressible
import xyz.devvydont.smprpg.items.interfaces.ICompressible.CompressionStep
import xyz.devvydont.smprpg.items.interfaces.ICustomBlock
import xyz.devvydont.smprpg.services.ItemService

class RawSilverBlock(itemService: ItemService, type: CustomItemType) : BlockBlueprint(itemService, type),
    ICustomBlock, ICompressible {
    override fun getCustomBlock(): CustomBlock {
        return CustomBlock.RAW_SILVER_BLOCK
    }

    override val decompressor: CompressionStep? get() = when (customItemType) {
        CustomItemType.RAW_SILVER_BLOCK ->
            CompressionStep(itemService.getBlueprint(CustomItemType.RAW_SILVER) as ICompressible, 1, 9)
        else -> null
    }

    override val compressor: CompressionStep? get() = when (customItemType) {
        CustomItemType.RAW_SILVER_BLOCK ->
            CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_RAW_SILVER) as ICompressible, 9, 1)
        else -> null
    }
}
