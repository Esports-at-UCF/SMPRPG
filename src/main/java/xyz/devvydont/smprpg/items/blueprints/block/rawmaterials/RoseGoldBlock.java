package xyz.devvydont.smprpg.items.blueprints.block.rawmaterials;

import xyz.devvydont.smprpg.block.CustomBlock;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.blueprints.block.BlockBlueprint;
import xyz.devvydont.smprpg.services.ItemService;

public class RoseGoldBlock extends BlockBlueprint {

    public RoseGoldBlock(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public CustomBlock getCustomBlock() {
        return CustomBlock.ROSE_GOLD_BLOCK;
    }
}
