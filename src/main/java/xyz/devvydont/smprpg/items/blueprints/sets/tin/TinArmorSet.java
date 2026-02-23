package xyz.devvydont.smprpg.items.blueprints.sets.tin;

import net.kyori.adventure.key.Key;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.CustomAttributeItem;
import xyz.devvydont.smprpg.items.interfaces.IEquippableAssetOverride;
import xyz.devvydont.smprpg.services.ItemService;

public abstract class TinArmorSet extends CustomAttributeItem implements IEquippableAssetOverride {

    private static final Key key = Key.key("tin");

    public TinArmorSet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Key getAssetId() {
        return key;
    }
}
