package xyz.devvydont.smprpg.items.blueprints.resources.mining

import net.kyori.adventure.key.Key
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.base.CustomCompressableBlueprint
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.crafting.CompressionRecipeMember
import xyz.devvydont.smprpg.util.crafting.MaterialWrapper

class SilverFamilyBlueprint(itemService: ItemService?, type: CustomItemType?) :
    CustomCompressableBlueprint(itemService, type), IModelOverridden {
    override fun getCompressionFlow(): MutableList<CompressionRecipeMember?> { return COMPRESSION_FLOW }

    override fun getDisplayKey(): Key? {
        return when (customItemType) {
            CustomItemType.SILVER_INGOT, CustomItemType.ENCHANTED_SILVER, CustomItemType.SILVER_SINGULARITY -> IModelOverridden.ofItemTypeInDirectory(CustomItemType.SILVER_INGOT, "materials")
            CustomItemType.SILVER_BLOCK, CustomItemType.ENCHANTED_SILVER_BLOCK -> IModelOverridden.ofItemTypeInDirectory(CustomItemType.SILVER_BLOCK, "blocks")
            else -> null
        }
    }

    companion object {
        val COMPRESSION_FLOW: MutableList<CompressionRecipeMember?> = mutableListOf(
            CompressionRecipeMember(MaterialWrapper(CustomItemType.SILVER_INGOT)),
            CompressionRecipeMember(MaterialWrapper(CustomItemType.SILVER_BLOCK)),
            CompressionRecipeMember(MaterialWrapper(CustomItemType.ENCHANTED_SILVER)),
            CompressionRecipeMember(MaterialWrapper(CustomItemType.ENCHANTED_SILVER_BLOCK)),
            CompressionRecipeMember(MaterialWrapper(CustomItemType.SILVER_SINGULARITY))
        )
    }
}
