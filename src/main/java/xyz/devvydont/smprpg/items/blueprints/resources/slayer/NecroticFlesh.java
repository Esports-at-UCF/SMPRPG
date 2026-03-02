package xyz.devvydont.smprpg.items.blueprints.resources.slayer;

import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.consumable.ConsumeEffect;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jspecify.annotations.NonNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.ICraftable;
import xyz.devvydont.smprpg.items.interfaces.IEdible;
import xyz.devvydont.smprpg.items.interfaces.IHeaderDescribable;
import xyz.devvydont.smprpg.items.interfaces.ISellable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.time.TickTime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NecroticFlesh extends CustomItemBlueprint implements ISellable, IEdible {

    public NecroticFlesh(ItemService itemService, CustomItemType type) {
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
        return 300 * item.getAmount();
    }

    @Override
    public @NonNull Consumable getConsumableComponent(ItemStack item) {

        var effects = new ArrayList<ConsumeEffect>();

        effects.add(ConsumeEffect.applyStatusEffects(List.of(
                new PotionEffect(PotionEffectType.WITHER, (int) TickTime.minutes(1), 0)
        ), 1f));

        return Consumable.consumable()
                .consumeSeconds(10)
                .addEffects(effects)
                .build();
    }

    @Override
    public int getNutrition(ItemStack item) {
        return 1;
    }

    @Override
    public float getSaturation(ItemStack item) {
        return 1;
    }

    @Override
    public boolean canAlwaysEat(ItemStack item) {
        return true;
    }
}
