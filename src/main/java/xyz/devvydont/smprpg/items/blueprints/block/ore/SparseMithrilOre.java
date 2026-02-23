package xyz.devvydont.smprpg.items.blueprints.block.ore;

import xyz.devvydont.smprpg.block.CustomBlock;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.blueprints.block.BlockBlueprint;
import xyz.devvydont.smprpg.services.ItemService;

public class SparseMithrilOre extends BlockBlueprint {

    public SparseMithrilOre(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public CustomBlock getCustomBlock() {
        return CustomBlock.SPARSE_MITHRIL_ORE;
    }
}
