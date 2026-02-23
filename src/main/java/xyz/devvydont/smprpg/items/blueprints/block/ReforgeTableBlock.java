package xyz.devvydont.smprpg.items.blueprints.block;

import org.bukkit.event.Listener;
import xyz.devvydont.smprpg.block.CustomBlock;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.ICustomBlock;
import xyz.devvydont.smprpg.services.ItemService;

public class ReforgeTableBlock extends BlockBlueprint {

    public ReforgeTableBlock(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public CustomBlock getCustomBlock() {
        return CustomBlock.REFORGE_TABLE;
    }
}
