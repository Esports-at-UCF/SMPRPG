package xyz.devvydont.smprpg.items.blueprints.resources.slayer

import net.kyori.adventure.key.Key
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint
import xyz.devvydont.smprpg.items.interfaces.ICompressible
import xyz.devvydont.smprpg.items.interfaces.ICompressible.CompressionStep
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden
import xyz.devvydont.smprpg.items.interfaces.ISellable
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.extensions.calculateCompressedWorth

class SpellPowderFamilyBlueprint(itemService: ItemService, type: CustomItemType) :
    CustomItemBlueprint(itemService, type), ICompressible, ISellable, IModelOverridden {

    override val itemClassification: ItemClassification get() = ItemClassification.ITEM

    override val decompressor: CompressionStep? get() = when (customItemType) {
        CustomItemType.PREMIUM_SPELL_POWDER -> CompressionStep(itemService.getBlueprint(CustomItemType.SPELL_POWDER) as ICompressible, 1, 9)
        CustomItemType.ENCHANTED_SPELL_POWDER -> CompressionStep(itemService.getBlueprint(CustomItemType.PREMIUM_SPELL_POWDER) as ICompressible, 1, 9)
        CustomItemType.SPELL_POWDER_SINGULARITY -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_SPELL_POWDER) as ICompressible, 1, 9)
        else -> null
    }

    override val compressor: CompressionStep? get() = when (customItemType) {
        CustomItemType.SPELL_POWDER -> CompressionStep(itemService.getBlueprint(CustomItemType.PREMIUM_SPELL_POWDER) as ICompressible, 9, 1)
        CustomItemType.PREMIUM_SPELL_POWDER -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_SPELL_POWDER) as ICompressible, 9, 1)
        CustomItemType.ENCHANTED_SPELL_POWDER -> CompressionStep(itemService.getBlueprint(CustomItemType.SPELL_POWDER_SINGULARITY) as ICompressible, 9, 1)
        else -> null
    }

    override fun getWorth(itemStack: ItemStack): Int = calculateCompressedWorth(itemStack)

    override fun getDisplayKey(): Key {
        return IModelOverridden.ofItemTypeInDirectory(CustomItemType.SPELL_POWDER, "materials")
    }
}
