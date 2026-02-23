package xyz.devvydont.smprpg.items.blueprints.sets.steel;

import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.IModularToolComponent;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.items.ToolGlobals;

import java.util.Collection;
import java.util.List;

public class SteelDrillBase extends CustomItemBlueprint implements IModularToolComponent {

    public static final String attrKey = "steel_drill_base";

    public SteelDrillBase(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributes() {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.MINING_FORTUNE, 50, getAttrKey())
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
    public String getComponentPrefix() { return "Steel"; }
}
