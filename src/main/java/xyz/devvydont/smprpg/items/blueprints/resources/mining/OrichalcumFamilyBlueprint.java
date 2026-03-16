package xyz.devvydont.smprpg.items.blueprints.resources.mining;

import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.CustomCompressableBlueprint;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.CompressionRecipeMember;
import xyz.devvydont.smprpg.util.crafting.MaterialWrapper;

import java.util.List;

public class OrichalcumFamilyBlueprint extends CustomCompressableBlueprint {

    public static final List<CompressionRecipeMember> COMPRESSION_FLOW = List.of(
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ORICHALCUM_INGOT)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ORICHALCUM_BLOCK)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_ORICHALCUM)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_ORICHALCUM_BLOCK)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ORICHALCUM_SINGULARITY))
    );

    public OrichalcumFamilyBlueprint(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public List<CompressionRecipeMember> getCompressionFlow() {
        return COMPRESSION_FLOW;
    }

}
