package xyz.devvydont.smprpg.listeners.block;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.server.ServerLoadEvent;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.block.BlockSound;
import xyz.devvydont.smprpg.block.CustomBlock;
import xyz.devvydont.smprpg.blockbreaking.BlockPropertiesRegistry;
import xyz.devvydont.smprpg.gui.items.MenuModularToolModify;
import xyz.devvydont.smprpg.gui.items.MenuReforge;
import xyz.devvydont.smprpg.items.interfaces.ICustomBlock;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.listeners.ToggleableListener;
import xyz.devvydont.smprpg.util.time.TickTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class NoteblockOverrideListener extends ToggleableListener {

    public HashMap<Player, Integer> placementDelays = new HashMap<Player, Integer>();

    public void decrementPlacementDelays() {
        ArrayList<Player> copySet = new ArrayList<Player>(placementDelays.keySet());
        for (var player : copySet) {
            placementDelays.put(player, (placementDelays.getOrDefault(player, 1) - 1));
            if (placementDelays.get(player) == 0)
                placementDelays.remove(player);
        }
    }

    @EventHandler
    public void onNotePlay(NotePlayEvent event) {
        event.setCancelled(true);  // DIE
    }

    @EventHandler(priority = EventPriority.LOW)
    public void woodPlacementSoundHack(BlockPlaceEvent event) {
        var block = event.getBlock();
        var entry = BlockPropertiesRegistry.get(block);
        if (entry != null && !BlockPropertiesRegistry.isCustom(block)) {
            BlockSound blockSound  = BlockPropertiesRegistry.get(block).getBlockSound();
            if (blockSound != null) {
                block.getWorld().playSound(block.getLocation(), blockSound.PlaceSound, blockSound.PlaceVolume, blockSound.PlacePitch);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onRightClickNoteblock(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.NOTE_BLOCK) {
            event.setCancelled(true);
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
            var player = event.getPlayer();
            if (placementDelays.getOrDefault(player, 0) > 0)
                return;

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
                placementDelays.put(player, 4);
                BlockSound blockSound  = BlockPropertiesRegistry.get(blockDest).getBlockSound();
                if (blockSound != null) {
                    blockDest.getWorld().playSound(blockDest.getLocation(), blockSound.PlaceSound, blockSound.PlaceVolume, blockSound.PlacePitch);
                }
                if (player.getGameMode() != GameMode.CREATIVE)
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

    @EventHandler
    public void removeNoteblocksFromEntityExplosions(EntityExplodeEvent event) {
        var blockList = event.blockList();
        var copyList = new ArrayList<>(blockList);
        for (Block block : copyList) {
            if (block.getType() == Material.NOTE_BLOCK)
                blockList.remove(block);
        }
    }

    @EventHandler
    public void removeNoteblocksFromBlockExplosions(BlockExplodeEvent event) {
        // We need to duplicate this event for beds, respawn anchors, etc.
        var blockList = event.blockList();
        var copyList = new ArrayList<>(blockList);
        for (Block block : copyList) {
            if (block.getType() == Material.NOTE_BLOCK)
                blockList.remove(block);
        }
    }

    @EventHandler
    public void startPlacementDelayHandler(ServerLoadEvent event) {
        Bukkit.getScheduler().runTaskTimer(SMPRPG.getInstance(), () -> {
            decrementPlacementDelays();
        }, TickTime.INSTANTANEOUSLY, TickTime.TICK);
    }

}
