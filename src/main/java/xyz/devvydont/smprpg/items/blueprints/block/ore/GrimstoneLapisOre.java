package xyz.devvydont.smprpg.items.blueprints.block.ore;

import xyz.devvydont.smprpg.block.CustomBlock;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.blueprints.block.BlockBlueprint;
import xyz.devvydont.smprpg.services.ItemService;

public class GrimstoneLapisOre extends BlockBlueprint {

    public GrimstoneLapisOre(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public CustomBlock getCustomBlock() {
        return CustomBlock.GRIMSTONE_LAPIS_ORE;
    }
}
