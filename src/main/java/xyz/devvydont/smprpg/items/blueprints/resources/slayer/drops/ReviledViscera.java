package xyz.devvydont.smprpg.items.blueprints.resources.slayer.drops;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.IHeaderDescribable;
import xyz.devvydont.smprpg.items.interfaces.ISellable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.List;

public class ReviledViscera extends CustomItemBlueprint implements ISellable, IHeaderDescribable {

    public ReviledViscera(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    /**
     * Determine what type of item this is.
     */
    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.ITEM;
    }

    /**
     * Given this item stack, how much should it be able to sell for?
     * Keep in mind that the size of the stack needs to considered as well!
     *
     * @param item The item that can be sold.
     * @return The worth of the item.
     */
    @Override
    public int getWorth(ItemStack item) {
        return 10_000 * item.getAmount();
    }

    @Override
    public List<Component> getHeader(ItemStack itemStack) {
        return List.of(
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
        );
    }
}
