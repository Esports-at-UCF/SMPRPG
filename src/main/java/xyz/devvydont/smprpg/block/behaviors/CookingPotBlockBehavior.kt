package xyz.devvydont.smprpg.block.behaviors

import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.momirealms.craftengine.bukkit.api.BukkitAdaptors
import net.momirealms.craftengine.bukkit.block.behavior.SimpleStorageBlockBehavior
import net.momirealms.craftengine.core.block.CustomBlock
import net.momirealms.craftengine.core.block.ImmutableBlockState
import net.momirealms.craftengine.core.block.behavior.BlockBehaviorFactory
import net.momirealms.craftengine.core.block.behavior.EntityBlockBehavior
import net.momirealms.craftengine.core.block.entity.BlockEntity
import net.momirealms.craftengine.core.block.entity.BlockEntityType
import net.momirealms.craftengine.core.block.entity.tick.BlockEntityTicker
import net.momirealms.craftengine.core.util.ResourceConfigUtils
import net.momirealms.craftengine.core.world.BlockPos
import net.momirealms.craftengine.core.world.CEWorld
import org.bukkit.craftbukkit.block.CraftBlock
import xyz.devvydont.smprpg.block.entity.CookingPotBlockEntity
import xyz.devvydont.smprpg.block.entity.SMPRPGBlockEntityTypes
import xyz.devvydont.smprpg.util.formatting.Symbols
import java.util.concurrent.Callable

class CookingPotBlockBehavior(
    customBlock: CustomBlock,
    hasAnalogOutputSignal: Boolean
) : SimpleStorageBlockBehavior(
    customBlock,
    "<white>${Symbols.OFFSET_NEG_1 + Symbols.COOKING_POT_MENU}</white>${Symbols.OVERLAY_BG_OFFSET_STANDARD + Symbols.OFFSET_NEG_5 + Symbols.OFFSET_NEG_3}<#3f3f3f>               Cooking Pot</#3f3f3f>",
    4,
    null,
    null,
    hasAnalogOutputSignal,
    false,
    false,
    null
) {

    override fun <T : BlockEntity> blockEntityType(state: ImmutableBlockState): BlockEntityType<T?>? {
        return EntityBlockBehavior.blockEntityTypeHelper(SMPRPGBlockEntityTypes.COOKING_POT)
    }

    override fun createBlockEntity(pos: BlockPos, state: ImmutableBlockState): BlockEntity? {
        return CookingPotBlockEntity(pos, state)
    }

    override fun <T : BlockEntity?> createAsyncBlockEntityTicker(
        level: CEWorld?,
        state: ImmutableBlockState?,
        blockEntityType: BlockEntityType<T?>?
    ): BlockEntityTicker<T?>? {
        return EntityBlockBehavior.createTickerHelper<T?, CookingPotBlockEntity>(BlockEntityTicker { ceWorld: CEWorld, blockPos: BlockPos, state: ImmutableBlockState, cookingPot: CookingPotBlockEntity ->
            CookingPotBlockEntity.tick(
                ceWorld,
                blockPos,
                state,
                cookingPot
            )
        })
    }

    override fun neighborChanged(thisBlock: Any?, args: Array<out Any?>?, superMethod: Callable<in Any>?) {
        super.neighborChanged(thisBlock, args, superMethod)
        val level = args!![1] as Level
        val pos = args[2] as net.minecraft.core.BlockPos
        val belowPos = pos.below()
        val belowBlock = CraftBlock.at(level, belowPos)
        val ceBlock = BukkitAdaptors.adapt(belowBlock)
        var entityAt = ceBlock.world().storageWorld().getBlockEntityAtIfLoaded(BlockPos(pos.x, pos.y, pos.z))
        if (entityAt != null) {
            entityAt = entityAt as CookingPotBlockEntity
            entityAt.updateHeated()
        }
    }

    companion object {
        val FACTORY = Factory()

        class Factory : BlockBehaviorFactory<CookingPotBlockBehavior?> {
            override fun create(block: CustomBlock, arguments: MutableMap<String?, Any?>): CookingPotBlockBehavior {
                val hasAnalogOutputSignal =
                    ResourceConfigUtils.getAsBoolean(arguments.getOrDefault("has-signal", true), "has-signal")
                return CookingPotBlockBehavior(
                    block,
                    hasAnalogOutputSignal
                )
            }
        }
    }
}