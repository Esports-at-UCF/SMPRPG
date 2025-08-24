package xyz.devvydont.smprpg.listeners.block;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.NotePlayEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.devvydont.smprpg.block.CustomBlock;
import xyz.devvydont.smprpg.blockbreaking.BlockPropertiesRegistry;
import xyz.devvydont.smprpg.gui.items.MenuReforge;
import xyz.devvydont.smprpg.items.interfaces.ICustomBlock;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.listeners.ToggleableListener;

public class NoteblockOverrideListener extends ToggleableListener {

    @EventHandler
    public void onNotePlay(NotePlayEvent event) {
        event.setCancelled(true);  // DIE
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onRightClickNoteblock(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.NOTE_BLOCK) {
            event.setCancelled(true);  // Cancel vanilla event, so that we aren't changing blockstates.

            System.out.println(event.getClickedBlock().getBlockData());
            System.out.println(CustomBlock.REFORGE_TABLE.BlockData);
            if (event.getClickedBlock().getBlockData().equals(CustomBlock.REFORGE_TABLE.BlockData))
                new MenuReforge(event.getPlayer()).openMenu();
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlaceCustomBlock(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if (event.getClickedBlock() == null)
            return;


        if (event.getItem() == null)
            return;

        var bp = ItemService.blueprint(event.getItem());
        if (bp instanceof ICustomBlock custom)
        {
            var item = event.getItem();
            if (item == null)
                return;
            event.setCancelled(true);
            CustomBlock blockEnum = custom.getCustomBlock();
            var blockDest = event.getClickedBlock().getRelative(event.getBlockFace());
            if (blockDest.canPlace(blockEnum.BlockData)) {
                var placeEvent = new BlockPlaceEvent(blockDest, blockDest.getState(), event.getClickedBlock(), event.getItem(), event.getPlayer(), true, event.getHand());
                placeEvent.callEvent();
                if (placeEvent.isCancelled())
                    return;
                blockDest.setType(blockEnum.BlockMaterial);
                blockDest.setBlockData(blockEnum.BlockData);
                String placeSound  = BlockPropertiesRegistry.get(blockDest).getPlaceSound();
                if (placeSound != null)
                    blockDest.getWorld().playSound(blockDest.getLocation(), placeSound, 1.0f, 1.0f);
                if (event.getPlayer().getGameMode() != GameMode.CREATIVE)
                    item.setAmount(item.getAmount() - 1);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPhysics(BlockPhysicsEvent event) {
        // Noteblocks will just not receive updates, period.
        if (event.getBlock().getType() == Material.NOTE_BLOCK)
            event.setCancelled(true);

        Block aboveBlock = event.getBlock().getRelative(BlockFace.UP);
        Block belowBlock = event.getBlock().getRelative(BlockFace.DOWN);

        // Check chain going up. This is going to be more common than down.
        if (aboveBlock.getType() == Material.NOTE_BLOCK) {
            updateAndCheck(event.getBlock().getLocation(), BlockFace.UP);
            event.setCancelled(true);
        }

        // Check chain going down. Skulls implemented this functionality so we need to check for it.
        if (belowBlock.getType() == Material.NOTE_BLOCK) {
            updateAndCheck(event.getBlock().getLocation(), BlockFace.DOWN);
            event.setCancelled(true);
        }
        event.getBlock().getState().update(true, false);
    }

    public void updateAndCheck(Location loc, BlockFace face) {
        Block b = loc.getBlock().getRelative(face);
        if (b.getType() == Material.NOTE_BLOCK)
            b.getState().update(true, true);
        Location nextBlock = b.getRelative(BlockFace.DOWN).getLocation();
        if (nextBlock.getBlock().getType() == Material.NOTE_BLOCK)
            updateAndCheck(b.getLocation(), BlockFace.DOWN);
    }

}
