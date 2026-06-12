package xyz.devvydont.smprpg.items.blueprints.resources.mining

import net.kyori.adventure.key.Key
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint
import xyz.devvydont.smprpg.items.blueprints.craftengine.CraftEngineCompressibleBlueprint
import xyz.devvydont.smprpg.items.interfaces.ICompressible
import xyz.devvydont.smprpg.items.interfaces.ICompressible.CompressionStep
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden
import xyz.devvydont.smprpg.items.interfaces.ISellable
import xyz.devvydont.smprpg.services.ItemService

private const val WORTH_PER_ITEM = 300

class RawTitanium(itemService: ItemService, type: CustomItemType) :
    CraftEngineCompressibleBlueprint(itemService, type), ICompressible, ISellable, IModelOverridden {

    override val itemClassification: ItemClassification get() = ItemClassification.MATERIAL

    override val decompressor: CompressionStep? get() = null

    override val compressor: CompressionStep? get() = when (customItemType) {
        CustomItemType.RAW_TITANIUM ->
            CompressionStep(itemService.getBlueprint(CustomItemType.RAW_TITANIUM_BLOCK) as ICompressible, 9, 1)
        else -> null
    }

    override fun getWorth(itemStack: ItemStack): Int = WORTH_PER_ITEM * itemStack.amount

    override fun getDisplayKey(): Key =
        IModelOverridden.ofItemTypeInDirectory(CustomItemType.RAW_TITANIUM, "materials")
}

