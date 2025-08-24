package xyz.devvydont.smprpg.items.blueprints.block;

import xyz.devvydont.smprpg.block.CustomBlock;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.services.ItemService;

public class SteelBlock extends BlockBlueprint {

    public SteelBlock(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public CustomBlock getCustomBlock() {
        return CustomBlock.STEEL_BLOCK;
    }
}
