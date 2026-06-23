package xyz.devvydont.smprpg.items.blueprints.sets.fishing.ruination;

import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.services.ItemService;

public class RuinationBoots extends RuinationSet {

    public RuinationBoots(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public int getHealth() {
        return RuinationChestplate.HEALTH / 2;
    }

    @Override
    public int getDefense() {
        return RuinationChestplate.DEFENSE / 2;
    }

    /**
     * Determine what type of item this is.
     */
    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.BOOTS;
    }
}
