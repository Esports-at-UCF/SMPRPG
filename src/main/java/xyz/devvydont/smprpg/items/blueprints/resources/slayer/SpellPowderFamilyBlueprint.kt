package xyz.devvydont.smprpg.items.blueprints.resources.slayer

import net.kyori.adventure.key.Key
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.base.CustomCompressableBlueprint
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.crafting.CompressionRecipeMember
import xyz.devvydont.smprpg.util.crafting.MaterialWrapper
import java.util.List

class SpellPowderFamilyBlueprint(itemService: ItemService?, type: CustomItemType?) :
    CustomCompressableBlueprint(itemService, type), IModelOverridden {
    override fun getCompressionFlow(): MutableList<CompressionRecipeMember?> {
        return COMPRESSION_FLOW
    }

    override fun getItemClassification(): ItemClassification {
        return ItemClassification.ITEM
    }

    override fun getDisplayKey(): Key {
        return IModelOverridden.ofItemType(_type)
    }

    companion object {
        val COMPRESSION_FLOW: MutableList<CompressionRecipeMember?> = List.of<CompressionRecipeMember?>(
            CompressionRecipeMember(MaterialWrapper(CustomItemType.SPELL_POWDER)),
            CompressionRecipeMember(MaterialWrapper(CustomItemType.PREMIUM_SPELL_POWDER)),
            CompressionRecipeMember(MaterialWrapper(CustomItemType.ENCHANTED_SPELL_POWDER)),
            CompressionRecipeMember(MaterialWrapper(CustomItemType.SPELL_POWDER_SINGULARITY))
        )
    }
}
