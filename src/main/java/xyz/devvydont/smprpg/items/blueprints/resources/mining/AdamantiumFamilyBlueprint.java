package xyz.devvydont.smprpg.items.blueprints.resources.mining;

import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.base.CustomCompressableBlueprint;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.CompressionRecipeMember;
import xyz.devvydont.smprpg.util.crafting.MaterialWrapper;

import java.util.List;

public class AdamantiumFamilyBlueprint extends CustomCompressableBlueprint {

    public static final List<CompressionRecipeMember> COMPRESSION_FLOW = List.of(
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ADAMANTIUM_INGOT)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ADAMANTIUM_BLOCK)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_ADAMANTIUM)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_ADAMANTIUM_BLOCK)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ADAMANTIUM_SINGULARITY))
    );

    public AdamantiumFamilyBlueprint(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public List<CompressionRecipeMember> getCompressionFlow() {
        return COMPRESSION_FLOW;
    }

}
