package xyz.devvydont.smprpg.items.blueprints.resources.mining

import net.kyori.adventure.key.Key
import org.bukkit.Material
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.base.CustomCompressableBlueprint
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.crafting.CompressionRecipeMember
import xyz.devvydont.smprpg.util.crafting.MaterialWrapper
import java.util.List

class SulfurFamilyBlueprint(itemService: ItemService?, type: CustomItemType?) :
    CustomCompressableBlueprint(itemService, type), IModelOverridden {
    override fun getCompressionFlow(): MutableList<CompressionRecipeMember?> {
        return COMPRESSION_FLOW
    }

    override fun getDisplayKey(): Key? {
        return when (customItemType) {
            CustomItemType.SULFUR, CustomItemType.ENCHANTED_SULFUR, CustomItemType.SULFUR_SINGULARITY -> IModelOverridden.ofItemTypeInDirectory(CustomItemType.SULFUR, "materials")
            CustomItemType.SULFUR_BLOCK, CustomItemType.ENCHANTED_SULFUR_BLOCK -> IModelOverridden.ofMaterial(Material.POISONOUS_POTATO)
            else -> null
        }
    }

    companion object {
        val COMPRESSION_FLOW: MutableList<CompressionRecipeMember?> = mutableListOf(
            CompressionRecipeMember(MaterialWrapper(CustomItemType.SULFUR)),
            CompressionRecipeMember(MaterialWrapper(CustomItemType.SULFUR_BLOCK)),
            CompressionRecipeMember(MaterialWrapper(CustomItemType.ENCHANTED_SULFUR)),
            CompressionRecipeMember(MaterialWrapper(CustomItemType.ENCHANTED_SULFUR_BLOCK)),
            CompressionRecipeMember(MaterialWrapper(CustomItemType.SULFUR_SINGULARITY))
        )
    }
}
