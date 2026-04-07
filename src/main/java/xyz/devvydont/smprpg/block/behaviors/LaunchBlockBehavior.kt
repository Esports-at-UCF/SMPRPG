package xyz.devvydont.smprpg.block.behaviors

import net.momirealms.craftengine.bukkit.block.behavior.BukkitBlockBehavior
import net.momirealms.craftengine.bukkit.plugin.reflection.minecraft.CoreReflections
import net.momirealms.craftengine.core.block.CustomBlock
import net.momirealms.craftengine.core.block.behavior.BlockBehavior
import net.momirealms.craftengine.core.block.behavior.BlockBehaviorFactory
import net.momirealms.craftengine.core.world.BlockPos
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.World
import org.bukkit.block.BlockState
import org.bukkit.block.data.type.BubbleColumn
import org.bukkit.entity.Entity
import xyz.devvydont.smprpg.util.persistence.KeyStore
import java.util.concurrent.Callable
import kotlin.random.Random

class LaunchBlockBehavior(customBlock: CustomBlock,
                          val launchPower : Float,
                          val bubbleBoosted : Boolean,
                          val wantParticles : Boolean) : BukkitBlockBehavior(customBlock) {

    override fun stepOn(thisBlock: Any?, args: Array<out Any?>?, superMethod: Callable<in Any>?) {
        super.stepOn(thisBlock, args, superMethod)

        val nmsBlockPos = args?.get(1)!! as net.minecraft.core.BlockPos
        val nmsEntity = args[3]!! as net.minecraft.world.entity.Entity

        val blockPos = BlockPos.of(nmsBlockPos.asLong())
        val entity = nmsEntity.bukkitEntity

        val world = entity.world

        val blockLoc = Location(entity.world,
            blockPos.x.toDouble(),
            blockPos.y.toDouble(),
            blockPos.z.toDouble())

        val blockBelowLoc = blockLoc.clone()
        blockBelowLoc.y = blockBelowLoc.y - 1

        val newVel = entity.velocity.clone()
        var upwardsVel = launchPower
        newVel.y = upwardsVel.toDouble()

        var blockBelowCloud = world.getBlockAt(blockBelowLoc).state
        val blockData = blockBelowCloud.blockData
        if (bubbleBoosted) {
            if (blockBelowCloud.type == Material.BUBBLE_COLUMN) {
                val column = blockData as BubbleColumn
                if (column.isDrag)
                    upwardsVel = launchPower / 2
                else
                    upwardsVel = launchPower * 1.5f
                newVel.y = upwardsVel.toDouble()
                if (wantParticles) {
                    for (count in 0..50) {
                        val pos = blockLoc
                        val xOffset = pos.x + Random.nextDouble()
                        val yOffset = pos.y + Random.nextDouble()
                        val zOffset = pos.z + Random.nextDouble()
                        world.spawnParticle(Particle.SPLASH, xOffset, yOffset, zOffset, 50)
                    }
                }
            }
        }
        world.playSound(
            blockLoc,
            KeyStore.AUDIO_BLUE_AERCOULD_BOUNCE.toString(),
            1f,
            (upwardsVel / 2.0).toFloat()
        )
        entity.velocity = newVel
    }

    companion object {
        val FACTORY = Factory()

        class Factory : BlockBehaviorFactory<BlockBehavior> {
            override fun create(block: CustomBlock, arguments: Map<String, Any>): LaunchBlockBehavior {
                val launchPower : Float = arguments["launch-power"] as Float
                val bubbleBoosted : Boolean = arguments.getOrDefault("bubble-boosted", false) as Boolean
                val wantParticles : Boolean = arguments.getOrDefault("want-particles", false) as Boolean
                return LaunchBlockBehavior(block, launchPower, bubbleBoosted, wantParticles)
            }
        }
    }
}