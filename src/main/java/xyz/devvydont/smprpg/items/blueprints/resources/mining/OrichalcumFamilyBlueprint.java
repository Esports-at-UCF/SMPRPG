package xyz.devvydont.smprpg.items.blueprints.resources.mining;

import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.ICompressible;
import xyz.devvydont.smprpg.items.interfaces.ISellable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.extensions.ItemExtensionsKt;

public class OrichalcumFamilyBlueprint extends CustomItemBlueprint implements ICompressible, ISellable {

    public OrichalcumFamilyBlueprint(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.MATERIAL;
    }

    @Override
    public CompressionStep getDecompressor() {
        return switch (getCustomItemType()) {
            case ORICHALCUM_BLOCK -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.ORICHALCUM_INGOT), 1, 9);
            case ENCHANTED_ORICHALCUM -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.ORICHALCUM_BLOCK), 1, 9);
            case ENCHANTED_ORICHALCUM_BLOCK -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.ENCHANTED_ORICHALCUM), 1, 9);
            case ORICHALCUM_SINGULARITY -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.ENCHANTED_ORICHALCUM_BLOCK), 1, 9);
            default -> null;
        };
    }

    @Override
    public CompressionStep getCompressor() {
        return switch (getCustomItemType()) {
            case ORICHALCUM_INGOT -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.ORICHALCUM_BLOCK), 9, 1);
            case ORICHALCUM_BLOCK -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.ENCHANTED_ORICHALCUM), 9, 1);
            case ENCHANTED_ORICHALCUM -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.ENCHANTED_ORICHALCUM_BLOCK), 9, 1);
            case ENCHANTED_ORICHALCUM_BLOCK -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.ORICHALCUM_SINGULARITY), 9, 1);
            default -> null;
        };
    }

    @Override
    public int getWorth(ItemStack itemStack) {
        return ItemExtensionsKt.calculateCompressedWorth(this, itemStack);
    }
}
