package xyz.devvydont.smprpg.block.behaviors

import io.papermc.paper.entity.TeleportFlag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.level.ScheduledTickAccess
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.NetherPortalBlock
import net.momirealms.craftengine.bukkit.api.CraftEngineBlocks
import net.momirealms.craftengine.bukkit.block.behavior.BukkitBlockBehavior
import net.momirealms.craftengine.core.block.CustomBlock
import net.momirealms.craftengine.core.block.behavior.BlockBehavior
import net.momirealms.craftengine.core.block.behavior.BlockBehaviorFactory
import net.momirealms.craftengine.core.block.properties.StringProperty
import net.momirealms.craftengine.core.sound.SoundData
import net.momirealms.craftengine.core.util.Direction
import net.momirealms.craftengine.core.util.HorizontalDirection
import net.momirealms.craftengine.core.util.ResourceConfigUtils
import net.momirealms.craftengine.core.world.BlockPos
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.World
import org.bukkit.block.BlockFace
import org.bukkit.craftbukkit.entity.CraftLivingEntity
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import xyz.devvydont.smprpg.block.CraftEngineBlockEnums
import xyz.devvydont.smprpg.util.craftengine.CraftEngineHelpers
import xyz.devvydont.smprpg.util.persistence.KeyStore
import java.util.*
import java.util.concurrent.Callable
import kotlin.random.Random


class PortalBlockBehavior(customBlock: CustomBlock,
                          val dimensionKey : NamespacedKey,
                          val ambientSound : SoundData,
                          val transitionSound : SoundData,
                          val travelSound : SoundData,
                          val instantTravel : Boolean) : BukkitBlockBehavior(customBlock) {

    // BlockState state, Level level, BlockPos pos, Block neighborBlock, @Nullable Orientation orientation, boolean movedByPiston
    override fun neighborChanged(thisBlock: Any?, args: Array<out Any?>?, superMethod: Callable<in Any>?) {
        super.neighborChanged(thisBlock, args, superMethod)
        val world = (args!![1] as Level).world
        val blockPos = args[2] as net.minecraft.core.BlockPos
        val block = world.getBlockAt(Location(world, blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble()))
        var faces = mutableSetOf(BlockFace.UP, BlockFace.DOWN)
        var dir = CraftEngineBlocks.getCustomBlockState(block)!!.customBlockState().getProperty<HorizontalDirection>("facing").toDirection()
        if (dir != null) {
            if (dir == Direction.WEST || dir == Direction.EAST) {
                faces.add(BlockFace.EAST)
                faces.add(BlockFace.WEST)
            }
            else {
                faces.add(BlockFace.NORTH)
                faces.add(BlockFace.SOUTH)
            }
        }

        var gottaBreak = false
        for (face in faces) {
            val relBlock = block.getRelative(face)
            if (CraftEngineBlocks.isCustomBlock(relBlock)) {
                if (CraftEngineHelpers.getBlockKey(relBlock) != customBlock.id()) {
                    gottaBreak = true
                }
            }
            else when (relBlock.type) {
                Material.GLOWSTONE, Material.AIR -> {}
                else -> gottaBreak = true
            }
            if (gottaBreak)
                continue
        }
        if (gottaBreak) {
            CraftEngineBlocks.remove(block)
        }
    }

    override fun entityInside(thisBlock: Any?, args: Array<out Any?>?, superMethod: Callable<in Any>?) {
        super.entityInside(thisBlock, args, superMethod)
        if (!instantTravel) {
            val entity = args!![3] as Entity
            val craftEntity = entity.bukkitEntity

            // TODO: Very temporary, find a proper way to teleport them to a safe location.
            val overworld = Bukkit.getWorld(NamespacedKey("minecraft", "overworld"))
            val loc = craftEntity.location.clone()
            if (loc.world != overworld) {
                if (craftEntity is CraftPlayer)
                    craftEntity.teleport(overworld!!.spawnLocation)
                else {
                    val spawnLoc = (craftEntity as CraftPlayer).respawnLocation ?: overworld!!.spawnLocation
                    craftEntity.teleport(spawnLoc)
                }
            }
            else {
                val destWorld = Bukkit.getWorld(dimensionKey)
                loc.world = destWorld
                if (dimensionKey == KeyStore.DIM_AETHER) {
                    loc.y = destWorld!!.maxHeight + 10.0
                    craftEntity.teleport(loc)
                    if (craftEntity is CraftLivingEntity) {
                        craftEntity.addPotionEffect(
                            PotionEffect(
                                PotionEffectType.SLOW_FALLING,
                                20 * 20,
                                0,
                                true,
                                true
                            )
                        )
                    }
                }
            }
        }
    }

    override fun randomTick(thisBlock: Any?, args: Array<out Any?>?, superMethod: Callable<in Any>?) {
        super.tick(thisBlock, args, superMethod)

        val serverLevel = args!![1] as ServerLevel

        val blockPos = args[2] as net.minecraft.core.BlockPos
        val ceBlockPos = BlockPos.of(blockPos.asLong())

        val world = serverLevel.world

        val loc = Location(world, ceBlockPos.x.toDouble(), ceBlockPos.y.toDouble(), ceBlockPos.z.toDouble())

        world.playSound(loc, ambientSound.id.toString(), ambientSound.volume.get(), ambientSound.pitch.get())
    }

    companion object {
        val FACTORY = Factory()

        class Factory : BlockBehaviorFactory<BlockBehavior> {
            override fun create(block: CustomBlock, arguments: Map<String, Any>): PortalBlockBehavior {
                val dimensionComps = arguments.get("dimension").toString().split(":")
                val dimensionKey : NamespacedKey = NamespacedKey(dimensionComps[0], dimensionComps[1])
                val ambientSound = SoundData.create(
                    ResourceConfigUtils.requireNonNullOrThrow(
                        Optional.ofNullable(ResourceConfigUtils.getAsMap(arguments.get("sounds"), "sounds"))
                            .map({ sounds -> ResourceConfigUtils.get(sounds, "ambient-sound") })
                            .orElse(null),
                        "Missing required sounds for portal"
                    ), SoundData.SoundValue.FIXED_1, SoundData.SoundValue.fixed(1f)
                )
                val transitionSound = SoundData.create(
                    ResourceConfigUtils.requireNonNullOrThrow(
                        Optional.ofNullable(ResourceConfigUtils.getAsMap(arguments.get("sounds"), "sounds"))
                            .map({ sounds -> ResourceConfigUtils.get(sounds, "transition-sound") })
                            .orElse(null),
                        "Missing required sounds for portal"
                    ), SoundData.SoundValue.FIXED_1, SoundData.SoundValue.fixed(1f)
                )
                val travelSound = SoundData.create(
                    ResourceConfigUtils.requireNonNullOrThrow(
                        Optional.ofNullable(ResourceConfigUtils.getAsMap(arguments.get("sounds"), "sounds"))
                            .map({ sounds -> ResourceConfigUtils.get(sounds, "travel-sound") })
                            .orElse(null),
                        "Missing required sounds for portal"
                    ), SoundData.SoundValue.FIXED_1, SoundData.SoundValue.fixed(1f)
                )
                val instantTravel : Boolean = arguments.getOrDefault("instant", false) as Boolean
                return PortalBlockBehavior(block, dimensionKey, ambientSound, transitionSound, travelSound, instantTravel)
            }
        }
    }
}