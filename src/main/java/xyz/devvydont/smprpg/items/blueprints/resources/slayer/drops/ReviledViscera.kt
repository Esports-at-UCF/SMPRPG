package xyz.devvydont.smprpg.items.blueprints.resources.slayer.drops

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint
import xyz.devvydont.smprpg.items.interfaces.IHeaderDescribable
import xyz.devvydont.smprpg.items.interfaces.ISellable
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.formatting.ComponentUtils
import java.util.List

class ReviledViscera(itemService: ItemService, type: CustomItemType) : CustomItemBlueprint(itemService, type),
    ISellable, IHeaderDescribable {
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
        return 10000 * item.getAmount()
    }

    override fun getHeader(itemStack: ItemStack?): MutableList<Component?> {
        return List.of<Component?>(
            ComponentUtils.merge(
                ComponentUtils.create("A large chunk of "),
                ComponentUtils.create("organ tissue", NamedTextColor.RED)
            ),
            ComponentUtils.merge(
                ComponentUtils.create("from a "),
                ComponentUtils.create("Shambling Abomination", NamedTextColor.DARK_PURPLE, TextDecoration.BOLD),
                ComponentUtils.create(".")
            ),

            ComponentUtils.EMPTY,
            ComponentUtils.create("...it might make a really good"),
            ComponentUtils.create("binding agent, actually.")
        )
    }
}
