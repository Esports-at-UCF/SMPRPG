package xyz.devvydont.smprpg.listeners.block

import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.TileState
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.block.*
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.server.ServerLoadEvent
import xyz.devvydont.smprpg.SMPRPG.Companion.plugin
import xyz.devvydont.smprpg.block.CustomBlock
import xyz.devvydont.smprpg.blockbreaking.BlockPropertiesRegistry
import xyz.devvydont.smprpg.gui.items.MenuReforge
import xyz.devvydont.smprpg.items.interfaces.ICustomBlock
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.listeners.ToggleableListener
import xyz.devvydont.smprpg.util.time.TickTime

class NoteblockOverrideListener : ToggleableListener() {
    var placementDelays: HashMap<Player?, Int?> = HashMap()

    fun decrementPlacementDelays() {
        val copySet = ArrayList(placementDelays.keys)
        for (player in copySet) {
            placementDelays.put(player, (placementDelays.getOrDefault(player, 1)!! - 1))
            if (placementDelays.get(player) == 0) placementDelays.remove(player)
        }
    }

    @EventHandler
    fun onNotePlay(event: NotePlayEvent) {
        event.isCancelled = true // DIE
    }

    //@EventHandler(priority = EventPriority.LOW)
    //fun woodPlacementSoundHack(event: BlockPlaceEvent) {
    //    val block = event.block
    //    val entry = BlockPropertiesRegistry.get(block)
    //    if (entry != null && !BlockPropertiesRegistry.isCustom(block)) {
    //        val blockSound = BlockPropertiesRegistry.get(block)!!.getBlockSound()
    //        if (blockSound != null) {
    //            block.world.playSound(
    //                block.location,
    //                blockSound.PlaceSound,
    //                blockSound.PlaceVolume,
    //                blockSound.PlacePitch
    //            )
    //        }
    //    }
    //}

    @EventHandler(priority = EventPriority.LOW)
    fun onRightClickNoteblock(event: PlayerInteractEvent) {
        // Checks for VANILLA blocks right clicking onto custom blocks.
        if (event.action == Action.RIGHT_CLICK_BLOCK && event.clickedBlock!!.type == Material.NOTE_BLOCK)
        {
            event.isCancelled = true
            var item = event.item
            val player = event.getPlayer()
            if (item != null) {
                // Check for vanilla like blocks, and allow placements still.
                if (item.type.isBlock) {
                    val blockData = item.type.createBlockData()
                    if (placementDelays.getOrDefault(player, 0)!! <= 0) {
                        val blockDest = event.clickedBlock!!.getRelative(event.blockFace)
                        if (blockDest.isReplaceable && blockDest.canPlace(blockData)) {
                            val placeEvent = BlockPlaceEvent(
                                blockDest,
                                blockDest.state,
                                event.clickedBlock!!,
                                item,
                                player,
                                true,
                                event.hand!!
                            )
                            placeEvent.callEvent()
                            if (placeEvent.isCancelled)
                                return

                            // We passed our placement checks, set the destination block.
                            blockDest.type = item.type
                            blockDest.blockData = blockData
                            placementDelays.put(player, 4)
                            val soundGroup = blockData.soundGroup
                            blockDest.world.playSound(
                                blockDest.location,
                                soundGroup.placeSound,
                                1f,
                                0.8f
                            )
                            if (player.gameMode != GameMode.CREATIVE)
                                item.setAmount(item.getAmount() - 1)
                        }
                    }
                }
            }

            if (event.clickedBlock!!.blockData == CustomBlock.REFORGE_TABLE.BlockData)
                MenuReforge(player).openMenu()
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlaceCustomBlock(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK)
            return

        if (event.clickedBlock == null)
            return

        var item = event.item;
        if (item == null)
            return

        val bp = ItemService.blueprint(item)
        if (bp is ICustomBlock) {
            val player = event.getPlayer()
            if (placementDelays.getOrDefault(player, 0)!! > 0)
                return


            val item = event.item
            if (item == null) return
            event.setCancelled(true)
            val blockEnum = bp.getCustomBlock()
            val blockDest = event.getClickedBlock()!!.getRelative(event.getBlockFace())

            // Cancel placements on tile entities if holding custom block and not sneaking
            if (event.clickedBlock!!.state is TileState && !player.isSneaking)
                return

            if (blockDest.isReplaceable && blockDest.canPlace(blockEnum.BlockData)) {
                val placeEvent = BlockPlaceEvent(
                    blockDest,
                    blockDest.getState(),
                    event.getClickedBlock()!!,
                    event.getItem()!!,
                    event.getPlayer(),
                    true,
                    event.getHand()!!
                )
                placeEvent.callEvent()
                if (placeEvent.isCancelled())
                    return
                blockDest.setType(blockEnum.BlockMaterial)
                blockDest.setBlockData(blockEnum.BlockData)
                placementDelays.put(player, 4)
                val blockSound = BlockPropertiesRegistry.get(blockDest)!!.getBlockSound()
                if (blockSound != null) {
                    blockDest.getWorld().playSound(
                        blockDest.getLocation(),
                        blockSound.PlaceSound,
                        blockSound.PlaceVolume,
                        blockSound.PlacePitch
                    )
                }
                player.swingHand(event.hand!!)
                if (player.getGameMode() != GameMode.CREATIVE)
                    item.setAmount(item.getAmount() - 1)
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onBlockPhysics(event: BlockPhysicsEvent) {
        // Noteblocks will just not receive updates, period.
        if (event.getBlock().getType() == Material.NOTE_BLOCK) event.setCancelled(true)

        val aboveBlock = event.getBlock().getRelative(BlockFace.UP)
        val belowBlock = event.getBlock().getRelative(BlockFace.DOWN)

        // Check chain going up. This is going to be more common than down.
        if (aboveBlock.getType() == Material.NOTE_BLOCK) {
            updateAndCheck(event.getBlock().getLocation(), BlockFace.UP)
            event.setCancelled(true)
        }

        // Check chain going down. Skulls implemented this functionality so we need to check for it.
        if (belowBlock.getType() == Material.NOTE_BLOCK) {
            updateAndCheck(event.getBlock().getLocation(), BlockFace.DOWN)
            event.setCancelled(true)
        }
        event.getBlock().getState().update(true, false)
    }

    fun updateAndCheck(loc: Location, face: BlockFace) {
        val b = loc.getBlock().getRelative(face)
        if (b.getType() == Material.NOTE_BLOCK) b.getState().update(true, true)
        val nextBlock = b.getRelative(BlockFace.DOWN).getLocation()
        if (nextBlock.getBlock().getType() == Material.NOTE_BLOCK) updateAndCheck(b.getLocation(), BlockFace.DOWN)
    }

    // TODO :These next two handlers are temporary. We need to roll proper loot from explosions off of these blocks.
    @EventHandler
    fun removeNoteblocksFromEntityExplosions(event: EntityExplodeEvent) {
        val blockList = event.blockList()
        val copyList = ArrayList<Block>(blockList)
        for (block in copyList) {
            if (block.type == Material.NOTE_BLOCK) blockList.remove(block)
        }
    }

    @EventHandler
    fun removeNoteblocksFromBlockExplosions(event: BlockExplodeEvent) {
        // We need to duplicate this event for beds, respawn anchors, etc.
        val blockList = event.blockList()
        val copyList = ArrayList<Block>(blockList)
        for (block in copyList) {
            if (block.type == Material.NOTE_BLOCK) blockList.remove(block)
        }
    }

    @EventHandler
    fun startPlacementDelayHandler(event: ServerLoadEvent?) {
        Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
            decrementPlacementDelays()
        }, TickTime.INSTANTANEOUSLY, TickTime.TICK)
    }

    @EventHandler
    fun onNoteblockCatchFire(event: BlockIgniteEvent) {
        // TODO: Very rudimentary hacky way to do this, should be done more gracefully with block properties later
        if (event.block.type == Material.NOTE_BLOCK)
            event.isCancelled = true
    }

    @EventHandler
    fun onNoteblockDestroyedByFire(event: BlockBurnEvent) {
        // TODO: Very rudimentary hacky way to do this, should be done more gracefully with block properties later
        if (event.block.type == Material.NOTE_BLOCK)
            event.isCancelled = true
    }

    @EventHandler
    fun onPistonExtend(event: BlockPistonExtendEvent) {
        for (block in event.blocks) {
            if (block.type == Material.NOTE_BLOCK)
                event.isCancelled = true
        }
    }

    @EventHandler
    fun onPistonRetract(event: BlockPistonRetractEvent) {
        for (block in event.blocks) {
            if (block.type == Material.NOTE_BLOCK)
                event.isCancelled = true
        }
    }
}