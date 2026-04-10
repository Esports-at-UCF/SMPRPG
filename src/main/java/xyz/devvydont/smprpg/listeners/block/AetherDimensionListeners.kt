package xyz.devvydont.smprpg.listeners.block

import io.papermc.paper.entity.TeleportFlag
import net.momirealms.craftengine.bukkit.api.CraftEngineBlocks
import net.momirealms.craftengine.core.util.Direction
import net.momirealms.craftengine.libraries.nbt.CompoundTag
import net.momirealms.craftengine.libraries.nbt.StringTag
import net.momirealms.craftengine.libraries.nbt.Tag
import org.bukkit.Bukkit
import org.bukkit.Color
import net.momirealms.craftengine.core.util.Key as CEKey
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.block.BlockFace
import org.bukkit.block.BlockState
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerBucketEmptyEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.BoundingBox
import xyz.devvydont.smprpg.SMPRPG.Companion.plugin
import xyz.devvydont.smprpg.block.CraftEngineBlockEnums
import xyz.devvydont.smprpg.util.craftengine.CraftEngineHelpers
import xyz.devvydont.smprpg.util.listeners.ToggleableListener
import java.util.UUID

class AetherDimensionListeners : ToggleableListener() {
    private val activeTransitions : MutableMap<UUID, NamespacedKey> = mutableMapOf()

    /**
     * Handle falling into overworld transition
     */
    @EventHandler
    private fun onEnterVoid(event: PlayerMoveEvent) {
        val player = event.player
        val location = player.location
        if (location.world.key != AETHER_DIM_KEY) return

        // If we are below world floor
        if (player.location.y <= (player.world.minHeight - 16)) {
            val newLoc = location.clone()
            newLoc.world = Bukkit.getWorld(OVERWORLD_DIM_KEY)
            newLoc.y = newLoc.world.maxHeight.toDouble()
            player.teleport(newLoc, TeleportFlag.Relative.VELOCITY_Y)
        }
    }

    /**
     * Lava converts directly to obsidian when placed in the Aether.
     */
    @EventHandler
    private fun onEmptyLavaBucket(event: PlayerBucketEmptyEvent) {
        if (event.block.world.key == AETHER_DIM_KEY) {
            if (event.bucket == Material.LAVA_BUCKET)
            // Set the block to obsidian one tick later.
                object : BukkitRunnable() {
                    override fun run() {
                        val world = event.block.world
                        world.playSound(event.block.location, Sound.BLOCK_LAVA_EXTINGUISH, 1f, 1f)
                        CraftEngineBlocks.place(event.block.location, CraftEngineBlockEnums.AEROGEL.key, false)
                        val particleLoc = event.block.location.clone()
                        particleLoc.x = particleLoc.x + 0.5
                        particleLoc.z = particleLoc.z + 0.5
                        world.spawnParticle(
                            Particle.DUST,
                            event.block.location,
                            50,
                            0.5,
                            0.5,
                            0.5,
                            0.1,
                            Particle.DustOptions(Color.BLACK, 2.0f)
                        )
                    }
                }.runTaskLater(plugin, 1L)
        }
    }

    /**
     * Fire extinguishes automatically in the Aether
     */
    @EventHandler
    private fun onFirePlace(event: BlockPlaceEvent) {
        val block = event.block
        if (block.world.key == AETHER_DIM_KEY) {
            if (event.block.type == Material.FIRE) {
                event.isCancelled = true
                val world = block.world
                world.playSound(event.block.location, Sound.BLOCK_FIRE_EXTINGUISH, 1f, 2f)
                val particleLoc = event.block.location.clone()
                particleLoc.x = particleLoc.x + 0.5
                particleLoc.z = particleLoc.z + 0.5
                world.spawnParticle(
                    Particle.DUST,
                    event.block.location,
                    25,
                    0.5,
                    0.5,
                    0.5,
                    0.1,
                    Particle.DustOptions(Color.BLACK, 1.0f)
                )
            }
        }
    }

    /**
     * Torches extinguish automatically in the Aether
     */
    @EventHandler
    private fun onTorchPlace(event: BlockPlaceEvent) {
        val block = event.block

        fun playExtinguishEffect() {
            val world = block.world
            world.playSound(event.block.location, Sound.BLOCK_FIRE_EXTINGUISH, 1f, 2f)
            val particleLoc = event.block.location.clone()
            particleLoc.x = particleLoc.x + 0.5
            particleLoc.z = particleLoc.z + 0.5
            particleLoc.y = particleLoc.y + 0.8
            world.spawnParticle(
                Particle.DUST,
                event.block.location,
                25,
                0.5,
                0.5,
                0.5,
                0.1,
                Particle.DustOptions(Color.BLACK, 1.0f)
            )
        }

        if (block.world.key == AETHER_DIM_KEY) {
            when (event.block.type) {
                Material.TORCH ->
                {
                    playExtinguishEffect()
                    CraftEngineBlocks.place(event.block.location, CraftEngineBlockEnums.EXTINGUISHED_TORCH.key, false)
                }
                Material.WALL_TORCH ->
                {
                    playExtinguishEffect()
                    val blockState = event.block.state
                    val stateData = blockState.blockData.asString.substringAfter('[').dropLast(1)
                    val stateComps = stateData.split("=")
                    val properties = CompoundTag(mutableMapOf(Pair(stateComps[0], StringTag(stateComps[1]) as Tag)))
                    CraftEngineBlocks.place(event.block.location, CraftEngineBlockEnums.EXTINGUISHED_WALL_TORCH.key, properties, false)
                }
                else -> {}
            }
        }
    }

    companion object {
        val AETHER_DIM_KEY = NamespacedKey(plugin, "the_aether")
        val OVERWORLD_DIM_KEY = NamespacedKey("minecraft", "overworld")
    }

}

class AetherPortalShape(
    private val axis : Direction,
    private val rightDir : Direction,
    private val numPortalBlocks : Int,
    private val bottomLeft : Location,
    private val topRight : Location,
    private val width : Int,
    private val height : Int
) {

    fun findEmptyPortalShape(world : World, location : Location, face : BlockFace) {

        //return findPortalShape()
    }

    //fun findPortalShape() : Optional<AetherPortalShape> {
    //    //val firstAxis = Optional.of(findAnyShape(level, pos, preferredAxis)).filter(isValid())
    //    return Optional.of()
    //}

    fun findAnyShape() {}

    fun calculateBottomLeft() {}

    fun calculateWidth() {}

    fun getDistanceUntilEdgeAboveFrame() {}

    fun calculateHeight() {}

    fun hasTopFrame() {}

    fun getDistanceUntilTop() {}

    fun isEmpty(state : BlockState) : Boolean {

        // Check for air blocks and water blocks, they are allowed to be replaced in frame.
        when (state.block.type) {
            Material.AIR, Material.WATER -> return true
            else -> {}
        }

        // Now check for portal blocks.
        if (CraftEngineBlocks.isCustomBlock(state.block)) {
            if (CraftEngineHelpers.getBlockKey(state.block) == PORTAL_STATE_KEY)
                return true
        }

        // Something is blocking here, abort.
        return false
    }

    fun isValid() : Boolean {
        return this.width >= MIN_WIDTH && this.width <= MAX_WIDTH && this.height >= MIN_HEIGHT && this.height <= MAX_HEIGHT
    }

    fun createPortalBlocks() {
        val portalBounds = BoundingBox(this.bottomLeft.x, this.bottomLeft.y, this.bottomLeft.z, this.topRight.x, this.topRight.y, this.topRight.z)
    }

    fun isComplete() : Boolean {
        return this.isValid() && this.numPortalBlocks == this.width * this.height
    }

    fun getRelativePosition() {}

    fun findCollisionFreePosition() {}

    companion object {
        val MIN_WIDTH = 2
        val MAX_WIDTH = 21
        val MIN_HEIGHT = 3
        val MAX_HEIGHT = 21
        val FRAME = Material.GLOWSTONE
        val SAFE_TRAVEL_MAX_ENTITY_XY = 4.0f
        val SAFE_TRAVEL_MAX_VERTICAL_DELTA = 1.0
        val PORTAL_STATE_KEY = CEKey.of("smprpg:aether_portal")
    }
}