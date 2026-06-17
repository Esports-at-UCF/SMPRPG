package xyz.devvydont.smprpg.items.blueprints.resources.farming;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.ICompressible;
import xyz.devvydont.smprpg.items.interfaces.ISellable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.extensions.ItemExtensionsKt;

public class WheatBlueprintFamily extends CustomItemBlueprint implements ICompressible, ISellable, Listener {

    public WheatBlueprintFamily(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.MATERIAL;
    }

    @Override
    public CompressionStep getDecompressor() {
        return switch (getCustomItemType()) {
            case PREMIUM_WHEAT -> new CompressionStep((ICompressible) itemService.getVanillaBlueprint(ItemStack.of(Material.HAY_BLOCK)), 1, 9);
            case PREMIUM_HAY_BLOCK -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.PREMIUM_WHEAT), 1, 9);
            case ENCHANTED_WHEAT -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.PREMIUM_MELON), 1, 9);
            case ENCHANTED_HAY_BLOCK -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.ENCHANTED_MELON_SLICE), 1, 9);
            case WHEAT_SINGULARITY -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.ENCHANTED_MELON), 1, 9);
            default -> null;
        };
    }

    @Override
    public CompressionStep getCompressor() {
        return switch (getCustomItemType()) {
            case PREMIUM_WHEAT -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.PREMIUM_HAY_BLOCK), 9, 1);
            case PREMIUM_HAY_BLOCK -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.ENCHANTED_WHEAT), 9, 1);
            case ENCHANTED_WHEAT -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.ENCHANTED_HAY_BLOCK), 9, 1);
            case ENCHANTED_HAY_BLOCK -> new CompressionStep((ICompressible) itemService.getBlueprint(CustomItemType.WHEAT_SINGULARITY), 9, 1);
            default -> null;
        };
    }

    @Override
    public int getWorth(ItemStack itemStack) {
        return ItemExtensionsKt.calculateCompressedWorth(this, itemStack);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        if (event.getItem() == null)
            return;

        if (!isItemOfType(event.getItem()))
            return;

        event.setCancelled(true);
    }
}
