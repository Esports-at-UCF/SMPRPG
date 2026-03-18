package xyz.devvydont.smprpg.items.blueprints.resources.slayer.drops;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.inventory.ItemStack;
import org.w3c.dom.Text;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.IHeaderDescribable;
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden;
import xyz.devvydont.smprpg.items.interfaces.ISellable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.List;

public class HornOfWarlock extends CustomItemBlueprint implements ISellable, IHeaderDescribable, IModelOverridden {

    public HornOfWarlock(ItemService itemService, CustomItemType type) {
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
        return 50_000 * item.getAmount();
    }

    @Override
    public List<Component> getHeader(ItemStack itemStack) {
        return List.of(
                ComponentUtils.merge(
                        ComponentUtils.create("A fractured horn from the "),
                    ComponentUtils.create("Illager Warlock", NamedTextColor.DARK_PURPLE, TextDecoration.BOLD),
                    ComponentUtils.create(".")
                ),
                ComponentUtils.EMPTY,
                ComponentUtils.create("This one does not make noise.")
        );
    }

    @Override
    public Key getDisplayKey() {
        return IModelOverridden.ofItemType(_type);
    }
}
