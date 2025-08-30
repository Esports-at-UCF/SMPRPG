package xyz.devvydont.smprpg.items.blueprints.block;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Consumable;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.ICustomBlock;
import xyz.devvydont.smprpg.services.ItemService;

import java.util.Set;

public abstract class BlockBlueprint extends CustomItemBlueprint implements ICustomBlock, Listener {

    public BlockBlueprint(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.BLOCK;
    }

    @Override
    public void updateItemData(ItemStack itemStack) {
        itemStack.unsetData(DataComponentTypes.CONSUMABLE);
        super.updateItemData(itemStack);
    }
}
