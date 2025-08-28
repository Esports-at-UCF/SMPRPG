package xyz.devvydont.smprpg.items.blueprints.block.rawmaterials;

import xyz.devvydont.smprpg.block.CustomBlock;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.blueprints.block.BlockBlueprint;
import xyz.devvydont.smprpg.services.ItemService;

public class BronzeBlock extends BlockBlueprint {

    public BronzeBlock(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public CustomBlock getCustomBlock() {
        return CustomBlock.BRONZE_BLOCK;
    }
}
