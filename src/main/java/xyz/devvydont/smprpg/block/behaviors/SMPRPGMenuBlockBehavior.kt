package xyz.devvydont.smprpg.block.behaviors

import net.momirealms.craftengine.bukkit.block.behavior.BukkitBlockBehavior
import net.momirealms.craftengine.core.block.CustomBlock
import net.momirealms.craftengine.core.block.ImmutableBlockState
import net.momirealms.craftengine.core.block.behavior.BlockBehavior
import net.momirealms.craftengine.core.block.behavior.BlockBehaviorFactory
import net.momirealms.craftengine.core.entity.player.InteractionResult
import net.momirealms.craftengine.core.world.context.UseOnContext
import org.bukkit.Bukkit
import xyz.devvydont.smprpg.gui.items.MenuReforge

class SMPRPGMenuBlockBehavior(customBlock: CustomBlock,
                              val menuName: String): BukkitBlockBehavior(customBlock) {

    override fun useWithoutItem(context: UseOnContext, state: ImmutableBlockState): InteractionResult {
        if (context.player != null) {
            val player = Bukkit.getServer().getPlayer(context.player!!.uuid())
            if (player == null) return super.useWithoutItem(context, state)
            when (menuName) {
                "reforge" -> {
                    player.swingMainHand()
                    MenuReforge(player).openMenu()
                    return InteractionResult.SUCCESS
                }
            }
        }
        return super.useWithoutItem(context, state)
    }

    companion object {
        val FACTORY = Factory()

        class Factory : BlockBehaviorFactory<BlockBehavior> {
            override fun create(block: CustomBlock, arguments: Map<String, Any>): SMPRPGMenuBlockBehavior {
                val menuName : String = arguments["menu"] as String
                return SMPRPGMenuBlockBehavior(block, menuName)
            }
        }
    }
}