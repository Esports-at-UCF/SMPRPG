package xyz.devvydont.smprpg.items.blueprints.resources.farming;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.ICompressible;
import xyz.devvydont.smprpg.items.interfaces.ISellable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.extensions.ItemExtensionsKt;

public class SugarCaneBlueprintFamily extends CustomItemBlueprint implements ICompressible, ISellable {

    public SugarCaneBlueprintFamily(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.MATERIAL;
    }

    @Override
    public CompressionStep getDecompressor() {
        return switch (getCustomItemType()) {
            case PREMIUM_SUGAR -> new CompressionStep((ICompressible) itemService.getVanillaBlueprint(ItemStack.of(Material.SUGAR_CANE)), 1, 9);
            case PREMIUM_SUGAR_CANE -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.PREMIUM_SUGAR), 1, 9);
            case ENCHANTED_SUGAR -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.PREMIUM_SUGAR_CANE), 1, 9);
            case ENCHANTED_SUGAR_CANE -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.ENCHANTED_SUGAR), 1, 9);
            case SUGAR_SINGULARITY -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.ENCHANTED_SUGAR_CANE), 1, 9);
            default -> null;
        };
    }

    @Override
    public CompressionStep getCompressor() {
        return switch (getCustomItemType()) {
            case PREMIUM_SUGAR -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.PREMIUM_SUGAR_CANE), 9, 1);
            case PREMIUM_SUGAR_CANE -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.ENCHANTED_SUGAR), 9, 1);
            case ENCHANTED_SUGAR -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.ENCHANTED_SUGAR_CANE), 9, 1);
            case ENCHANTED_SUGAR_CANE -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.SUGAR_SINGULARITY), 9, 1);
            default -> null;
        };
    }

    @Override
    public int getWorth(ItemStack itemStack) {
        return ItemExtensionsKt.calculateCompressedWorth(this, itemStack);
    }
}
