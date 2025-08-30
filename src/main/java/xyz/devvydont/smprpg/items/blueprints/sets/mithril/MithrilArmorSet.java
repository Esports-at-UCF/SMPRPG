package xyz.devvydont.smprpg.items.blueprints.sets.mithril;

import net.kyori.adventure.key.Key;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.CustomAttributeItem;
import xyz.devvydont.smprpg.items.interfaces.IEquippableAssetOverride;
import xyz.devvydont.smprpg.services.ItemService;

public abstract class MithrilArmorSet extends CustomAttributeItem implements IEquippableAssetOverride {

    private static final Key key = Key.key("mithril");

    public MithrilArmorSet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Key getAssetId() {
        return key;
    }
}
