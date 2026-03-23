package xyz.devvydont.smprpg.items.blueprints.resources.slayer.drops

import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint
import xyz.devvydont.smprpg.items.interfaces.ISellable
import xyz.devvydont.smprpg.services.ItemService
import java.util.function.Consumer

class NecronomiconExcerpts(itemService: ItemService?, type: CustomItemType?) : CustomItemBlueprint(itemService, type),
    ISellable {
    /**
     * Determine what type of item this is.
     */
    override val itemClassification: ItemClassification get() = ItemClassification.ITEM

    /**
     * Given this item stack, how much should it be able to sell for?
     * Keep in mind that the size of the stack needs to considered as well!
     *
     * @param item The item that can be sold.
     * @return The worth of the item.
     */
    override fun getWorth(item: ItemStack): Int {
        return 500000 * item.getAmount()
    }

    override fun updateItemData(itemStack: ItemStack) {
        super.updateItemData(itemStack)
        itemStack.editMeta(Consumer { meta: ItemMeta? -> meta!!.setMaxStackSize(4) })
    }
}
