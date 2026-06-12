package xyz.devvydont.smprpg.items.blueprints.resources.mining;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.ICompressible;
import xyz.devvydont.smprpg.items.interfaces.ISellable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.extensions.ItemExtensionsKt;

public class AmethystFamilyBlueprint extends CustomItemBlueprint implements ICompressible, ISellable {

    public AmethystFamilyBlueprint(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.MATERIAL;
    }

    @Override
    public CompressionStep getDecompressor() {
        return switch (getCustomItemType()) {
            case ENCHANTED_AMETHYST -> new CompressionStep((ICompressible) itemService.getVanillaBlueprint(ItemStack.of(Material.AMETHYST_BLOCK)), 1, 9);
            case ENCHANTED_AMETHYST_BLOCK -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.ENCHANTED_AMETHYST), 1, 9);
            case AMETHYST_SINGULARITY -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.ENCHANTED_AMETHYST_BLOCK), 1, 9);
            default -> null;
        };
    }

    @Override
    public CompressionStep getCompressor() {
        return switch (getCustomItemType()) {
            case ENCHANTED_AMETHYST -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.ENCHANTED_AMETHYST_BLOCK), 9, 1);
            case ENCHANTED_AMETHYST_BLOCK -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.AMETHYST_SINGULARITY), 9, 1);
            default -> null;
        };
    }

    @Override
    public int getWorth(ItemStack itemStack) {
        return ItemExtensionsKt.calculateCompressedWorth(this, itemStack);
    }
}
