package xyz.devvydont.smprpg.items.blueprints.sets.adamantium;

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

public class AdamantiumDrillHead extends CustomItemBlueprint implements IModularToolComponent {

    public static final String attrKey = "adamantium_drill_head";

    public AdamantiumDrillHead(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributes() {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.MINING_SPEED, 1650, getAttrKey()),
                new AdditiveAttributeEntry(AttributeWrapper.MINING_POWER, 5, getAttrKey())
        );
    }

    @Override
    public String getAttrKey() {
        return attrKey;
    }

    @Override
    public ItemClassification getItemClassification() { return ItemClassification.ITEM; }

    @Override
    public String getComponentPrefix() { return "Adamantium"; }
}
