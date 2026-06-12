package xyz.devvydont.smprpg.items.blueprints.resources.mining;

import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.ICompressible;
import xyz.devvydont.smprpg.items.interfaces.ISellable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.extensions.ItemExtensionsKt;

public class CobaltFamilyBlueprint extends CustomItemBlueprint implements ICompressible, ISellable {

    public CobaltFamilyBlueprint(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.MATERIAL;
    }

    @Override
    public CompressionStep getDecompressor() {
        return switch (getCustomItemType()) {
            case COBALT_BLOCK -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.COBALT_INGOT), 1, 9);
            case ENCHANTED_COBALT -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.COBALT_BLOCK), 1, 9);
            case ENCHANTED_COBALT_BLOCK -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.ENCHANTED_COBALT), 1, 9);
            case COBALT_SINGULARITY -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.ENCHANTED_COBALT_BLOCK), 1, 9);
            default -> null;
        };
    }

    @Override
    public CompressionStep getCompressor() {
        return switch (getCustomItemType()) {
            case COBALT_INGOT -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.COBALT_BLOCK), 9, 1);
            case COBALT_BLOCK -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.ENCHANTED_COBALT), 9, 1);
            case ENCHANTED_COBALT -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.ENCHANTED_COBALT_BLOCK), 9, 1);
            case ENCHANTED_COBALT_BLOCK -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.COBALT_SINGULARITY), 9, 1);
            default -> null;
        };
    }

    @Override
    public int getWorth(ItemStack itemStack) {
        return ItemExtensionsKt.calculateCompressedWorth(this, itemStack);
    }
}
