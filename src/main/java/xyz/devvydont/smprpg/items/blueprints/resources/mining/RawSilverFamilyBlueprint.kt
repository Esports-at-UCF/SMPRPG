package xyz.devvydont.smprpg.items.blueprints.resources.mining

import net.kyori.adventure.key.Key
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.base.CustomCompressableBlueprint
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.crafting.CompressionRecipeMember
import xyz.devvydont.smprpg.util.crafting.MaterialWrapper

class RawSilverFamilyBlueprint(itemService: ItemService?, type: CustomItemType?) :
    CustomCompressableBlueprint(itemService, type), IModelOverridden {
    override fun getCompressionFlow(): MutableList<CompressionRecipeMember?> { return COMPRESSION_FLOW }

    override fun getDisplayKey(): Key? {
        return when (customItemType) {
            CustomItemType.RAW_SILVER, CustomItemType.ENCHANTED_RAW_SILVER -> IModelOverridden.ofItemTypeInDirectory(CustomItemType.RAW_SILVER, "materials")
            CustomItemType.RAW_SILVER_BLOCK -> IModelOverridden.ofItemTypeInDirectory(CustomItemType.SILVER_BLOCK, "blocks")
            else -> null
        }
    }

    companion object {
        val COMPRESSION_FLOW: MutableList<CompressionRecipeMember?> = mutableListOf(
            CompressionRecipeMember(MaterialWrapper(CustomItemType.RAW_SILVER)),
            CompressionRecipeMember(MaterialWrapper(CustomItemType.RAW_SILVER_BLOCK)),
            CompressionRecipeMember(MaterialWrapper(CustomItemType.ENCHANTED_RAW_SILVER))
        )
    }
}
