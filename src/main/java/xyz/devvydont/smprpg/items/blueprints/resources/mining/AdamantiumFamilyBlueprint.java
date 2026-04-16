package xyz.devvydont.smprpg.items.blueprints.resources.mining;

import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.ICompressible;
import xyz.devvydont.smprpg.items.interfaces.ISellable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.extensions.ItemExtensionsKt;

public class AdamantiumFamilyBlueprint extends CustomItemBlueprint implements ICompressible, ISellable {

    public AdamantiumFamilyBlueprint(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.MATERIAL;
    }

    @Override
    public CompressionStep getDecompressor() {
        return switch (getCustomItemType()) {
            case ADAMANTIUM_BLOCK -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.ADAMANTIUM_INGOT), 1, 9);
            case ENCHANTED_ADAMANTIUM -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.ADAMANTIUM_BLOCK), 1, 9);
            case ENCHANTED_ADAMANTIUM_BLOCK -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.ENCHANTED_ADAMANTIUM), 1, 9);
            case ADAMANTIUM_SINGULARITY -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.ENCHANTED_ADAMANTIUM_BLOCK), 1, 9);
            default -> null;
        };
    }

    @Override
    public CompressionStep getCompressor() {
        return switch (getCustomItemType()) {
            case ADAMANTIUM_INGOT -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.ADAMANTIUM_BLOCK), 9, 1);
            case ADAMANTIUM_BLOCK -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.ENCHANTED_ADAMANTIUM), 9, 1);
            case ENCHANTED_ADAMANTIUM -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.ENCHANTED_ADAMANTIUM_BLOCK), 9, 1);
            case ENCHANTED_ADAMANTIUM_BLOCK -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.ADAMANTIUM_SINGULARITY), 9, 1);
            default -> null;
        };
    }

    @Override
    public int getWorth(ItemStack itemStack) {
        return ItemExtensionsKt.calculateCompressedWorth(this, itemStack);
    }
}
