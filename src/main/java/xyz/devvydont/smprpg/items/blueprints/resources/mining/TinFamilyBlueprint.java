package xyz.devvydont.smprpg.items.blueprints.resources.mining;

import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.ICompressible;
import xyz.devvydont.smprpg.items.interfaces.ISellable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.extensions.ItemExtensionsKt;

public class TinFamilyBlueprint extends CustomItemBlueprint implements ICompressible, ISellable {

    public TinFamilyBlueprint(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.MATERIAL;
    }

    @Override
    public CompressionStep getDecompressor() {
        return switch (getCustomItemType()) {
            case TIN_BLOCK -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.TIN_INGOT), 1, 9);
            case ENCHANTED_TIN -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.TIN_BLOCK), 1, 9);
            case ENCHANTED_TIN_BLOCK -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.ENCHANTED_TIN), 1, 9);
            case TIN_SINGULARITY -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.ENCHANTED_TIN_BLOCK), 1, 9);
            default -> null;
        };
    }

    @Override
    public CompressionStep getCompressor() {
        return switch (getCustomItemType()) {
            case TIN_INGOT -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.TIN_BLOCK), 9, 1);
            case TIN_BLOCK -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.ENCHANTED_TIN), 9, 1);
            case ENCHANTED_TIN -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.ENCHANTED_TIN_BLOCK), 9, 1);
            case ENCHANTED_TIN_BLOCK -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.TIN_SINGULARITY), 9, 1);
            default -> null;
        };
    }

    @Override
    public int getWorth(ItemStack itemStack) {
        return ItemExtensionsKt.calculateCompressedWorth(this, itemStack);
    }
}
