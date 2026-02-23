package xyz.devvydont.smprpg.items.blueprints.block.decoration;

import xyz.devvydont.smprpg.block.CustomBlock;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.blueprints.block.BlockBlueprint;
import xyz.devvydont.smprpg.services.ItemService;

public class CobbledGrimstoneBlock extends BlockBlueprint {

    public CobbledGrimstoneBlock(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public CustomBlock getCustomBlock() {
        return CustomBlock.COBBLED_GRIMSTONE;
    }
}
