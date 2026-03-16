package xyz.devvydont.smprpg.items.blueprints.resources.mining;

import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.CustomCompressableBlueprint;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.CompressionRecipeMember;
import xyz.devvydont.smprpg.util.crafting.MaterialWrapper;

import java.util.List;

public class CobaltFamilyBlueprint extends CustomCompressableBlueprint {

    public static final List<CompressionRecipeMember> COMPRESSION_FLOW = List.of(
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.COBALT_INGOT)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.COBALT_BLOCK)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_COBALT)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_COBALT_BLOCK)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.COBALT_SINGULARITY))
    );

    public CobaltFamilyBlueprint(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public List<CompressionRecipeMember> getCompressionFlow() {
        return COMPRESSION_FLOW;
    }

}
