package xyz.devvydont.smprpg.items.blueprints.sets.radiant;

import org.bukkit.Material;
import org.bukkit.inventory.meta.trim.TrimPattern;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemArmor;
import xyz.devvydont.smprpg.services.ItemService;

public class RadiantLeggings extends RadiantArmorSet {

    public RadiantLeggings(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public double getHealth() {
        return 20;
    }

    @Override
    public double getDefense() {
        return ItemArmor.getDefenseFromMaterial(Material.NETHERITE_LEGGINGS)-10;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.LEGGINGS;
    }

    @Override
    public TrimPattern getTrimPattern() {
        return TrimPattern.RAISER;
    }
}
