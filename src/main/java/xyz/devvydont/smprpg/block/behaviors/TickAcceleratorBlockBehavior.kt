package xyz.devvydont.smprpg.block.behaviors

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.*
import net.momirealms.craftengine.bukkit.api.CraftEngineBlocks
import net.momirealms.craftengine.bukkit.block.behavior.BukkitBlockBehavior
import net.momirealms.craftengine.bukkit.util.LocationUtils
import net.momirealms.craftengine.core.block.CustomBlock
import net.momirealms.craftengine.core.block.behavior.BlockBehavior
import net.momirealms.craftengine.core.block.behavior.BlockBehaviorFactory
import java.util.concurrent.Callable
import kotlin.random.Random

class TickAcceleratorBlockBehavior(customBlock: CustomBlock,
                                   val chance : Float,
                                   val mode : String): BukkitBlockBehavior(customBlock) {

    override fun randomTick(thisBlock: Any?, args: Array<out Any?>?, superMethod: Callable<in Any>?) {
        super.randomTick(thisBlock, args, superMethod)
        val blockAbovePos = LocationUtils.above(args!![2]) as BlockPos
        val level = args[1] as ServerLevel
        val blockState = level.getBlockState(blockAbovePos)
        val bukkitBlock = level.world.getBlockAt(blockAbovePos.x, blockAbovePos.y, blockAbovePos.z)
        val block = blockState.block

        var numTicks = 0
        var chanceDupe = chance
        while (chanceDupe >= 1.0) {
            numTicks += 1
            chanceDupe -= 1.0f
        }
        if (Random.nextFloat() <= chanceDupe)
            numTicks += 1

        when (mode) {
            "any", "all" -> { blockState.randomTick(level, blockAbovePos, RandomSource.create()) }
            "crop" -> {
                if (CraftEngineBlocks.isCustomBlock(bukkitBlock)) {
                    val ceBlockState = CraftEngineBlocks.getCustomBlockState(bukkitBlock) ?: return
                    val properties = ceBlockState.propertyEntries().keys
                    val propertyNames = mutableListOf<String>()
                    for (property in properties)
                        propertyNames.add(property.name())
                    // Little hacky, but we can't check composite behaviors yet.
                    if (propertyNames.contains("age") || propertyNames.contains("stage")) {
                        for (i in 1..numTicks) {
                            blockState.randomTick(level, blockAbovePos, RandomSource.create())
                            return
                        }
                    }
                }
                if (block is CropBlock || block is SugarCaneBlock || block is VegetationBlock ||
                    block is GrowingPlantBodyBlock || block is ChorusFlowerBlock || block is BambooStalkBlock) {
                    for (i in 1..numTicks) {
                        blockState.randomTick(level, blockAbovePos, RandomSource.create())
                        return
                    }
                    return
                }
            }
        }
    }

    companion object {
        val FACTORY = Factory()

        class Factory : BlockBehaviorFactory<BlockBehavior> {
            override fun create(block: CustomBlock, arguments: Map<String, Any>): TickAcceleratorBlockBehavior {
                val chance : Float = arguments["chance"] as Float
                val mode : String = arguments["mode"] as String  // Any, Crop
                return TickAcceleratorBlockBehavior(block, chance, mode)
            }
        }
    }
}