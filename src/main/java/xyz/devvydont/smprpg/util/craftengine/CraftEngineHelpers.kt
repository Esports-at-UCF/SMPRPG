package xyz.devvydont.smprpg.util.craftengine

import net.momirealms.craftengine.bukkit.api.CraftEngineBlocks
import net.momirealms.craftengine.core.util.Key as CEKey
import org.bukkit.block.Block

class CraftEngineHelpers {
    companion object {
        fun getBlockKey(block: Block): CEKey? {
            if (CraftEngineBlocks.isCustomBlock(block))
                return CEKey.of(CraftEngineBlocks.getCustomBlockState(block)!!.customBlockState().asString)
            else
                return null
        }
    }
}