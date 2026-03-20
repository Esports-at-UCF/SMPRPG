package xyz.devvydont.smprpg.items.blueprints.resources.slayer.drops

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint
import xyz.devvydont.smprpg.items.interfaces.IHeaderDescribable
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden
import xyz.devvydont.smprpg.items.interfaces.ISellable
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.formatting.ComponentUtils
import java.util.List

class HornOfWarlock(itemService: ItemService?, type: CustomItemType?) : CustomItemBlueprint(itemService, type),
    ISellable, IHeaderDescribable, IModelOverridden {
    /**
     * Determine what type of item this is.
     */
    override fun getItemClassification(): ItemClassification {
        return ItemClassification.ITEM
    }

    /**
     * Given this item stack, how much should it be able to sell for?
     * Keep in mind that the size of the stack needs to considered as well!
     *
     * @param item The item that can be sold.
     * @return The worth of the item.
     */
    override fun getWorth(item: ItemStack): Int {
        return 50000 * item.getAmount()
    }

    override fun getHeader(itemStack: ItemStack?): MutableList<Component?> {
        return List.of<Component?>(
            ComponentUtils.merge(
                ComponentUtils.create("A fractured horn from the "),
                ComponentUtils.create("Illager Warlock", NamedTextColor.DARK_PURPLE, TextDecoration.BOLD),
                ComponentUtils.create(".")
            ),
            ComponentUtils.EMPTY,
            ComponentUtils.create("This one does not make noise.")
        )
    }

    override fun getDisplayKey(): Key {
        return IModelOverridden.ofItemType(_type)
    }
}
