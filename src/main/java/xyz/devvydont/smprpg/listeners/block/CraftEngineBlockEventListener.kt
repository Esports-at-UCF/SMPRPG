package xyz.devvydont.smprpg.listeners.block

import io.papermc.paper.event.entity.EntityMoveEvent
import net.momirealms.craftengine.bukkit.api.CraftEngineBlocks
import net.momirealms.craftengine.bukkit.api.event.CustomBlockInteractEvent
import net.momirealms.craftengine.core.entity.player.InteractionHand
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.BlockState
import org.bukkit.block.data.type.BubbleColumn
import org.bukkit.entity.Entity
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockBurnEvent
import org.bukkit.event.player.PlayerMoveEvent
import xyz.devvydont.smprpg.block.CraftEngineBlockEnums
import xyz.devvydont.smprpg.gui.items.MenuReforge
import xyz.devvydont.smprpg.util.craftengine.CraftEngineHelpers
import xyz.devvydont.smprpg.util.listeners.ToggleableListener
import xyz.devvydont.smprpg.util.persistence.KeyStore
import kotlin.random.Random

class CraftEngineBlockEventListener : ToggleableListener() {
    /**
     * Listens to interactions from CraftEngine blocks, handles their logic here.
     */
    @EventHandler
    @Suppress("unused")
    private fun onCraftEngineInteract(event: CustomBlockInteractEvent) {
        if (event.action() != CustomBlockInteractEvent.Action.RIGHT_CLICK) return

        if (event.hand() != InteractionHand.MAIN_HAND) return

        val clickedBlock = event.customBlock()
        val blockKey = clickedBlock.id()
        when (blockKey) {
            CraftEngineBlockEnums.REFORGE_TABLE.key -> MenuReforge(event.player).openMenu()
            else -> return
        }
        event.isCancelled = true
    }

    @EventHandler
    private fun onBlockBurn(event: BlockBurnEvent) {
        val block = event.block
        if (CraftEngineBlocks.isCustomBlock(block)) {
            val blockKey = CraftEngineHelpers.getBlockKey(block)
            when (blockKey) {
                CraftEngineBlockEnums.GUNPOWDER_BLOCK.key -> { block.world.createExplosion(block.location, 5.0f) }  // Not really realistic, but game logic is game logic
                else -> return
            }
        }
    }
}
