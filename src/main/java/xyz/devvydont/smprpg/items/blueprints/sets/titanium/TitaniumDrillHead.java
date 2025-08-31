package xyz.devvydont.smprpg.items.blueprints.sets.titanium;

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

public class TitaniumDrillHead extends CustomItemBlueprint implements IModularToolComponent {

    public static final String attrKey = "titanium_drill_head";

    public TitaniumDrillHead(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributes() {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.MINING_SPEED, 1200, attrKey),
                new AdditiveAttributeEntry(AttributeWrapper.MINING_POWER, 5, attrKey)
        );
    }

    @Override
    public ItemClassification getItemClassification() { return ItemClassification.ITEM; }

    @Override
    public String getComponentPrefix() { return "Titanium"; }
}
