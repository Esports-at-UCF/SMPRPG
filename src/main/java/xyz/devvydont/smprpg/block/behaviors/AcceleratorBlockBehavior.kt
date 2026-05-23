package xyz.devvydont.smprpg.block.behaviors

import net.momirealms.craftengine.bukkit.block.behavior.BukkitBlockBehavior
import net.momirealms.craftengine.core.block.BlockDefinition
import net.momirealms.craftengine.core.block.behavior.BlockBehavior
import net.momirealms.craftengine.core.block.behavior.BlockBehaviorFactory
import net.momirealms.craftengine.core.plugin.config.ConfigSection
import org.bukkit.Bukkit
import org.bukkit.util.Vector
import xyz.devvydont.smprpg.SMPRPG
import java.util.concurrent.Callable

class AcceleratorBlockBehavior(blockDefinition: BlockDefinition,
                               val acceleration : Float): BukkitBlockBehavior(blockDefinition) {

    override fun stepOn(thisBlock: Any?, args: Array<out Any?>?) {
        super.stepOn(thisBlock, args)
        val nmsEntity = args!!.get(3) as net.minecraft.world.entity.Entity
        val craftEntity = nmsEntity.bukkitEntity

        val prevLoc = craftEntity.location.clone()

        Bukkit.getScheduler().runTaskLater(SMPRPG.plugin, Runnable {
            val vecDif = craftEntity.location.clone().subtract(prevLoc).toVector()
            if (!vecDif.isZero) {
                vecDif.y = 0.002
                vecDif.add(vecDif.normalize().multiply(acceleration).multiply(0.5))
                println(vecDif.x)
                println(vecDif.y)
                println(vecDif.z)
                if (vecDif.y > 0.9)
                    vecDif.y = 0.0
                craftEntity.velocity = vecDif
            }
        }, 1L)
    }

    companion object {
        val FACTORY = Factory()

        class Factory : BlockBehaviorFactory<BlockBehavior> {
            override fun create(block: BlockDefinition, section: ConfigSection): AcceleratorBlockBehavior {
                val acceleration : Float = section.getFloat("acceleration")
                return AcceleratorBlockBehavior(block, acceleration)
            }
        }
    }
}