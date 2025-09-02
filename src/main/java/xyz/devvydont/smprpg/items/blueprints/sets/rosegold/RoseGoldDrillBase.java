package xyz.devvydont.smprpg.items.blueprints.sets.rosegold;

import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.IModularToolComponent;
import xyz.devvydont.smprpg.services.ItemService;

import java.util.Collection;
import java.util.List;

public class RoseGoldDrillBase extends CustomItemBlueprint implements IModularToolComponent {

    public static final String attrKey = "rose_gold_drill_base";

    public RoseGoldDrillBase(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributes() {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.MINING_FORTUNE, 105, getAttrKey()),
                new AdditiveAttributeEntry(AttributeWrapper.MINING_POWER, -1, getAttrKey())
        );
    }

    @Override
    public String getAttrKey() {
        return attrKey;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.ITEM;
    }

    @Override
    public String getComponentPrefix() { return "Rose Gold"; }
}
