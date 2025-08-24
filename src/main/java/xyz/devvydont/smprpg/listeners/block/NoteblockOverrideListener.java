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
import xyz.devvydont.smprpg.items.interfaces.ICustomBlock;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.listeners.ToggleableListener;

public class NoteblockOverrideListener extends ToggleableListener {

    @EventHandler
    public void onNotePlay(NotePlayEvent event) {
        event.setCancelled(true);  // DIE
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
                if (event.getPlayer().getGameMode() != GameMode.CREATIVE)
                    item.setAmount(item.getAmount() - 1);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPhysics(BlockPhysicsEvent event) {
        Block aboveBlock = event.getBlock().getRelative(BlockFace.UP);
        if (aboveBlock.getType() == Material.NOTE_BLOCK) {
            updateAndCheck(event.getBlock().getLocation());
            event.setCancelled(true);
        }
        if (event.getBlock().getType() == Material.NOTE_BLOCK)
            event.setCancelled(true);
        event.getBlock().getState().update(true, false);

    }

    public void updateAndCheck(Location loc) {
        Block b = loc.getBlock().getRelative(BlockFace.UP);
        if (b.getType() == Material.NOTE_BLOCK)
            b.getState().update(true, true);
        Location nextBlock = b.getRelative(BlockFace.UP).getLocation();
        if (nextBlock.getBlock().getType() == Material.NOTE_BLOCK)
            updateAndCheck(b.getLocation());
    }

}
