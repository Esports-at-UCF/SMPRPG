package xyz.devvydont.smprpg.util.craftengine

import net.momirealms.craftengine.bukkit.api.CraftEngineBlocks
import net.momirealms.craftengine.core.util.Key as CEKey
import org.bukkit.block.Block
import org.bukkit.block.BlockState

class CraftEngineHelpers {
    companion object {
        fun getBlockKey(block: Block): CEKey? {
            if (CraftEngineBlocks.isCustomBlock(block))
                return CEKey.of(CraftEngineBlocks.getCustomBlockState(block)!!.customBlockState().asString)
            else
                return null
        }

        fun getBlockKey(state: BlockState): CEKey? {
            val block = state.block
            return getBlockKey(block)
        }
    }
}