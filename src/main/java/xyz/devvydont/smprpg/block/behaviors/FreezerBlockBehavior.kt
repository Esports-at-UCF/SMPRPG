package xyz.devvydont.smprpg.block.behaviors

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
import xyz.devvydont.smprpg.block.entity.FreezerBlockEntity
import xyz.devvydont.smprpg.block.entity.SMPRPGBlockEntityTypes
import xyz.devvydont.smprpg.util.formatting.Symbols

class FreezerBlockBehavior(
    customBlock: CustomBlock,
    hasAnalogOutputSignal: Boolean
) : SimpleStorageBlockBehavior(
    customBlock,
    "<white>${Symbols.OFFSET_NEG_1 + Symbols.FREEZER_MENU}</white>${Symbols.OVERLAY_BG_OFFSET_STANDARD + Symbols.OFFSET_NEG_8 + Symbols.OFFSET_NEG_3}<#3f3f3f>                   Freezer</#3f3f3f>",
    3,
    null,
    null,
    hasAnalogOutputSignal,
    true,
    true,
    null
) {

    override fun <T : BlockEntity> blockEntityType(state: ImmutableBlockState): BlockEntityType<T?>? {
        return EntityBlockBehavior.blockEntityTypeHelper(SMPRPGBlockEntityTypes.FREEZER)
    }

    override fun createBlockEntity(pos: BlockPos, state: ImmutableBlockState): BlockEntity? {
        return FreezerBlockEntity(pos, state)
    }

    override fun <T : BlockEntity?> createAsyncBlockEntityTicker(
        level: CEWorld?,
        state: ImmutableBlockState?,
        blockEntityType: BlockEntityType<T?>?
    ): BlockEntityTicker<T?>? {
        return EntityBlockBehavior.createTickerHelper<T?, FreezerBlockEntity>(BlockEntityTicker { ceWorld: CEWorld, blockPos: BlockPos, state: ImmutableBlockState, freezer: FreezerBlockEntity ->
            FreezerBlockEntity.tick(
                ceWorld,
                blockPos,
                state,
                freezer
            )
        })
    }

    companion object {
        val FACTORY = Factory()

        class Factory : BlockBehaviorFactory<FreezerBlockBehavior?> {
            override fun create(block: CustomBlock, arguments: MutableMap<String?, Any?>): FreezerBlockBehavior {
                val hasAnalogOutputSignal =
                    ResourceConfigUtils.getAsBoolean(arguments.getOrDefault("has-signal", true), "has-signal")
                return FreezerBlockBehavior(
                    block,
                    hasAnalogOutputSignal
                )
            }
        }
    }
}