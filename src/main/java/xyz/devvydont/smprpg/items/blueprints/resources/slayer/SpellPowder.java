package xyz.devvydont.smprpg.items.blueprints.resources.slayer;

import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.consumable.ConsumeEffect;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jspecify.annotations.NonNull;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.IEdible;
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden;
import xyz.devvydont.smprpg.items.interfaces.ISellable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.time.TickTime;

import java.util.ArrayList;
import java.util.List;

public class SpellPowder extends CustomItemBlueprint implements ISellable, IModelOverridden {

    public SpellPowder(ItemService itemService, CustomItemType type) {
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
        return 75 * item.getAmount();
    }

    @Override
    public Key getDisplayKey() {
        return IModelOverridden.ofItemType(_type);
    }
}
