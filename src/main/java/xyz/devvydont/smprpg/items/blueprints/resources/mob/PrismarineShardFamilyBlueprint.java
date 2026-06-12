package xyz.devvydont.smprpg.items.blueprints.resources.mob;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.ICompressible;
import xyz.devvydont.smprpg.items.interfaces.ISellable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.extensions.ItemExtensionsKt;

public class PrismarineShardFamilyBlueprint extends CustomItemBlueprint implements ICompressible, ISellable {

    public PrismarineShardFamilyBlueprint(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.MATERIAL;
    }

    @Override
    public CompressionStep getDecompressor() {
        return switch (getCustomItemType()) {
            case PREMIUM_PRISMARINE_SHARD -> new CompressionStep((ICompressible) itemService.getVanillaBlueprint(ItemStack.of(Material.PRISMARINE_SHARD)), 1, 9);
            case ENCHANTED_PRISMARINE_SHARD -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.PREMIUM_PRISMARINE_SHARD), 1, 9);
            default -> null;
        };
    }

    @Override
    public CompressionStep getCompressor() {
        return switch (getCustomItemType()) {
            case PREMIUM_PRISMARINE_SHARD -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.ENCHANTED_PRISMARINE_SHARD), 9, 1);
            default -> null;
        };
    }

    @Override
    public int getWorth(ItemStack itemStack) {
        return ItemExtensionsKt.calculateCompressedWorth(this, itemStack);
    }
}
