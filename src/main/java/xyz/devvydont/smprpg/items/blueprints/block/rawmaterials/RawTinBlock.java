package xyz.devvydont.smprpg.items.blueprints.block.rawmaterials;

import xyz.devvydont.smprpg.block.CustomBlock;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.blueprints.block.BlockBlueprint;
import xyz.devvydont.smprpg.services.ItemService;

public class RawTinBlock extends BlockBlueprint {

    public RawTinBlock(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public CustomBlock getCustomBlock() {
        return CustomBlock.RAW_TIN_BLOCK;
    }
}
