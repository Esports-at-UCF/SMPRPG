package xyz.devvydont.smprpg.items.blueprints.resources.mining

import net.kyori.adventure.key.Key
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.base.CustomCompressableBlueprint
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.crafting.CompressionRecipeMember
import xyz.devvydont.smprpg.util.crafting.MaterialWrapper

class SteelFamilyBlueprint(itemService: ItemService?, type: CustomItemType?) :
    CustomCompressableBlueprint(itemService, type), IModelOverridden {
    override fun getCompressionFlow(): MutableList<CompressionRecipeMember?> { return COMPRESSION_FLOW }

    override fun getDisplayKey(): Key? {
        return when (customItemType) {
            CustomItemType.STEEL_INGOT, CustomItemType.ENCHANTED_STEEL, CustomItemType.STEEL_SINGULARITY -> IModelOverridden.ofItemTypeInDirectory(CustomItemType.STEEL_INGOT, "materials")
            CustomItemType.STEEL_BLOCK, CustomItemType.ENCHANTED_STEEL_BLOCK -> IModelOverridden.ofItemTypeInDirectory(CustomItemType.STEEL_BLOCK, "blocks")
            else -> null
        }
    }

    companion object {
        val COMPRESSION_FLOW: MutableList<CompressionRecipeMember?> = mutableListOf(
            CompressionRecipeMember(MaterialWrapper(CustomItemType.STEEL_INGOT)),
            CompressionRecipeMember(MaterialWrapper(CustomItemType.STEEL_BLOCK)),
            CompressionRecipeMember(MaterialWrapper(CustomItemType.ENCHANTED_STEEL)),
            CompressionRecipeMember(MaterialWrapper(CustomItemType.ENCHANTED_STEEL_BLOCK)),
            CompressionRecipeMember(MaterialWrapper(CustomItemType.STEEL_SINGULARITY))
        )
    }
}
