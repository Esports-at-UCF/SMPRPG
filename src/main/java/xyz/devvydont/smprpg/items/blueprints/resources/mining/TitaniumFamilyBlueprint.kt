package xyz.devvydont.smprpg.items.blueprints.resources.mining

import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.base.CustomCompressableBlueprint
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.crafting.CompressionRecipeMember
import xyz.devvydont.smprpg.util.crafting.MaterialWrapper
import java.util.List

class TitaniumFamilyBlueprint(itemService: ItemService?, type: CustomItemType?) :
    CustomCompressableBlueprint(itemService, type) {
    override fun getCompressionFlow(): MutableList<CompressionRecipeMember?> {
        return COMPRESSION_FLOW
    }

    companion object {
        val COMPRESSION_FLOW: MutableList<CompressionRecipeMember?> = mutableListOf(
            CompressionRecipeMember(MaterialWrapper(CustomItemType.TITANIUM_INGOT)),
            CompressionRecipeMember(MaterialWrapper(CustomItemType.TITANIUM_BLOCK)),
            CompressionRecipeMember(MaterialWrapper(CustomItemType.ENCHANTED_TITANIUM)),
            CompressionRecipeMember(MaterialWrapper(CustomItemType.ENCHANTED_TITANIUM_BLOCK)),
            CompressionRecipeMember(MaterialWrapper(CustomItemType.TITANIUM_SINGULARITY))
        )
    }
}
