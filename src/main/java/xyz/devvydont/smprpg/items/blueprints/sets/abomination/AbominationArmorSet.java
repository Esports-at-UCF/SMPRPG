package xyz.devvydont.smprpg.items.blueprints.sets.abomination;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.entity.slayer.shambling.ShamblingAbominationParent;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.CustomAttributeItem;
import xyz.devvydont.smprpg.items.interfaces.IEquippableAssetOverride;
import xyz.devvydont.smprpg.items.interfaces.IFooterDescribable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.items.ToolGlobals;
import xyz.devvydont.smprpg.util.persistence.KeyStore;

import java.util.List;

public abstract class AbominationArmorSet extends CustomAttributeItem implements IEquippableAssetOverride, Listener, IFooterDescribable {

    private static final Key key = Key.key("abomination");
    public static final NamespacedKey killstoreKey = new NamespacedKey(SMPRPG.getPlugin(), "abomination_kill_count");
    public static final double BOSS_DAMAGE_REDUCTION = 0.95;

    public AbominationArmorSet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Key getAssetId() {
        return key;
    }

    public double getKillStrengthMultBoost(ItemStack item) {
        int numKills = item.getPersistentDataContainer().getOrDefault(killstoreKey, PersistentDataType.INTEGER, 0);
        if (numKills >= 25_000) { return 0.2; }
        else if (numKills >= 15_000) { return 0.18; }
        else if (numKills >= 10_000) { return 0.16; }
        else if (numKills >= 5_000) { return 0.14; }
        else if (numKills >= 2_500) { return 0.12; }
        else if (numKills >= 1_500) { return 0.1; }
        else if (numKills >= 1_000) { return 0.08; }
        else if (numKills >= 500) { return 0.06; }
        else if (numKills >= 250) { return  0.04; }
        else if (numKills >= 100) { return 0.02; }
        else { return 0.0; }
    }

    public int getNextKillMilestone(ItemStack item) {
        int numKills = item.getPersistentDataContainer().getOrDefault(killstoreKey, PersistentDataType.INTEGER, 0);
        if (numKills < 100) { return 100; }
        else if (numKills < 250) { return 250; }
        else if (numKills < 500) { return 500; }
        else if (numKills < 1_000) { return 1_000; }
        else if (numKills < 2_500) { return 2_500; }
        else if (numKills < 5_000) { return 5_000; }
        else if (numKills < 10_000) { return 10_000; }
        else if (numKills < 15_000) { return 15_000; }
        else { return 25_000; }
    }

    @Override
    public int getPowerRating() { return 30; }

    @Override
    public List<Component> getFooter(ItemStack itemStack) {
        int kills = itemStack.getPersistentDataContainer().getOrDefault(killstoreKey, PersistentDataType.INTEGER, 0);
        Component killComp;
        if (kills >= 25_000) {
            killComp = ComponentUtils.merge(
                    ComponentUtils.create("Kills: "),
                    ComponentUtils.create("MAXED!", NamedTextColor.AQUA, TextDecoration.BOLD)
            );
        }
        else {
            killComp = ComponentUtils.merge(
                    ComponentUtils.create("Kills: "),
                    ComponentUtils.create(kills, NamedTextColor.DARK_AQUA),
                    ComponentUtils.create("/" + getNextKillMilestone(itemStack), NamedTextColor.DARK_GRAY)
            );
        }
        return List.of(
                ComponentUtils.merge(
                        ComponentUtils.create("All incoming damage from "),
                        ComponentUtils.create("Shambling Abominations", NamedTextColor.DARK_PURPLE, TextDecoration.BOLD),
                        ComponentUtils.create(" is multiplied by 0.95x per piece.")
                ),
                ComponentUtils.merge(
                    ComponentUtils.create("Gains "),
                    ComponentUtils.create("+2% Strength", NamedTextColor.RED),
                    ComponentUtils.create(" for every kill milestone reached.")
                ),
                killComp
        );
    }

}
