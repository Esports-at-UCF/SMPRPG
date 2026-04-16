package xyz.devvydont.smprpg.block.behaviors

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockState
import net.momirealms.craftengine.bukkit.block.behavior.FallingBlockBehavior
import net.momirealms.craftengine.core.block.CustomBlock
import net.momirealms.craftengine.core.block.behavior.BlockBehavior
import net.momirealms.craftengine.core.block.behavior.BlockBehaviorFactory
import net.momirealms.craftengine.core.sound.SoundData
import net.momirealms.craftengine.core.util.ResourceConfigUtils
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.craftbukkit.block.data.CraftBlockData
import org.bukkit.craftbukkit.entity.CraftFallingBlock
import xyz.devvydont.smprpg.events.block.AscendingBlockRegisterEvent
import java.util.*
import java.util.concurrent.Callable


class AscendingBlockBehavior(customBlock: CustomBlock,
                             val hurtAmount: Float,
                             val maxHurt: Int,
                             landSound: SoundData?,
                             destroySound: SoundData?): FallingBlockBehavior(customBlock, hurtAmount, maxHurt, landSound, destroySound) {

    override fun tick(thisBlock: Any?, args: Array<out Any?>?, superMethod: Callable<in Any>?) {
        val net_serverLevel = args!![1] as ServerLevel
        val net_blockPos = args[2] as BlockPos

        val world = net_serverLevel.world
        val loc = Location(world, net_blockPos.x.toDouble(), net_blockPos.y.toDouble(), net_blockPos.z.toDouble())

        val aboveLoc = BlockPos(loc.x.toInt(), loc.y.toInt() + 1, loc.z.toInt())
        val aboveState = net_serverLevel.getBlockState(aboveLoc)
        val isFree = net.minecraft.world.level.block.FallingBlock.isFree(aboveState)
        if (!isFree) return

        val net_blockState = args[0] as BlockState
        val startLoc = loc.clone()
        startLoc.x += 0.5
        startLoc.y += 0.0784
        startLoc.z += 0.5
        val fallingBlock = world.spawnFallingBlock(startLoc, CraftBlockData.fromData(net_blockState))
        world.setBlockData(loc, Material.AIR.createBlockData())
        if (hurtAmount > 0 && maxHurt > 0) (fallingBlock as CraftFallingBlock).handle.setHurtsEntities(hurtAmount, maxHurt)

        AscendingBlockRegisterEvent(fallingBlock).callEvent()
    }

    companion object {
        val FACTORY = Factory()

        class Factory : BlockBehaviorFactory<BlockBehavior> {
            override fun create(block: CustomBlock, arguments: Map<String, Any>): AscendingBlockBehavior {
                val hurtAmount =
                    ResourceConfigUtils.getAsFloat(arguments.getOrDefault("hurt-amount", -1f), "hurt-amount")
                val hurtMax = ResourceConfigUtils.getAsInt(arguments.getOrDefault("max-hurt", -1), "max-hurt")
                val sounds = arguments["sounds"] as MutableMap<*, *>?
                var fallSound: SoundData? = null
                var destroySound: SoundData? = null
                if (sounds != null) {
                    fallSound = Optional.ofNullable(sounds["land"]).map { obj ->
                        SoundData.create(
                            obj,
                            SoundData.SoundValue.FIXED_1,
                            SoundData.SoundValue.ranged(0.9f, 1f)
                        )
                    }.orElse(null)
                    destroySound = Optional.ofNullable(sounds["destroy"]).map { obj ->
                        SoundData.create(
                            obj,
                            SoundData.SoundValue.FIXED_1,
                            SoundData.SoundValue.ranged(0.9f, 1f)
                        )
                    }.orElse(null)
                }
                return AscendingBlockBehavior(block, hurtAmount, hurtMax, fallSound, destroySound)
            }
        }
    }
}