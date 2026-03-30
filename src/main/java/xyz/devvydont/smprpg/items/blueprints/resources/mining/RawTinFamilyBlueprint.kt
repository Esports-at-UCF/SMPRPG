package xyz.devvydont.smprpg.items.blueprints.resources.mining

import net.kyori.adventure.key.Key
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.base.CustomCompressableBlueprint
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.crafting.CompressionRecipeMember
import xyz.devvydont.smprpg.util.crafting.MaterialWrapper
import java.util.List

class RawTinFamilyBlueprint(itemService: ItemService?, type: CustomItemType?) :
    CustomCompressableBlueprint(itemService, type), IModelOverridden {

    override fun getDisplayKey(): Key? {
        return when (customItemType) {
            CustomItemType.RAW_TIN, CustomItemType.ENCHANTED_RAW_TIN -> IModelOverridden.ofItemTypeInDirectory(CustomItemType.RAW_TIN, "materials")
            CustomItemType.RAW_TIN_BLOCK -> IModelOverridden.ofItemTypeInDirectory(CustomItemType.RAW_TIN_BLOCK, "blocks")
            else -> null
        }
    }

    override fun getCompressionFlow(): MutableList<CompressionRecipeMember?> {
        return COMPRESSION_FLOW
    }

    companion object {
        val COMPRESSION_FLOW: MutableList<CompressionRecipeMember?> = mutableListOf<CompressionRecipeMember?>(
            CompressionRecipeMember(MaterialWrapper(CustomItemType.RAW_TIN)),
            CompressionRecipeMember(MaterialWrapper(CustomItemType.RAW_TIN_BLOCK)),
            CompressionRecipeMember(MaterialWrapper(CustomItemType.ENCHANTED_RAW_TIN))
        )
    }
}
