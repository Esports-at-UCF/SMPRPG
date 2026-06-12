package xyz.devvydont.smprpg.items.blueprints.resources.crafting

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

class MithrilIngot(itemService: ItemService, type: CustomItemType) : CustomItemBlueprint(itemService, type),
    ISellable, IModelOverridden, ICompressible {
    override val itemClassification: ItemClassification get() = ItemClassification.MATERIAL

    /**
     * Given this item stack, how much should it be able to sell for?
     * Keep in mind that the size of the stack needs to considered as well!
     *
     * @param item The item that can be sold.
     * @return The worth of the item.
     */
    override fun getWorth(item: ItemStack): Int { return 150 * item.amount }

    override fun getDisplayKey(): Key { return IModelOverridden.ofItemTypeInDirectory(customItemType, "materials") }

    override val decompressor: CompressionStep? get() = null

    override val compressor: CompressionStep? get() = when (customItemType) {
        CustomItemType.MITHRIL_INGOT ->
            CompressionStep(itemService.getBlueprint(CustomItemType.MITHRIL_BLOCK) as ICompressible, 9, 1)
        else -> null
    }
}
