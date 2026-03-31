package xyz.devvydont.smprpg.blockbreaking

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.ProtocolManager
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import net.momirealms.craftengine.bukkit.api.event.CustomBlockInteractEvent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockDamageEvent
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.util.time.TickTime

class PacketManager : Listener {
    val plugin: SMPRPG = SMPRPG.plugin

    val manager: ProtocolManager = ProtocolLibrary.getProtocolManager()

    val blockPropertiesRegistry : BlockPropertiesRegistry = BlockPropertiesRegistry()

    val damage: BlockDamage

    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)
        damage = BlockDamage()
        receivedArmAnimation()
        checkArmAnimation()
    }

    private fun receivedArmAnimation() {
        manager.addPacketListener(object :
            PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.ARM_ANIMATION) {
            override fun onPacketReceiving(event: PacketEvent) {
                armSwinging.put(event.getPlayer().getName(), System.currentTimeMillis())
            }
        })
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private fun blockStartBreaking(event: BlockDamageEvent) {
        event.isCancelled = true // Cancel any vanilla behavior
        BlockDamage.cancelTaskWithBlockReset(event.player)
        damage.configureBreakingPacket(event.player, event.block)
    }

    /**
     * Checks that an arm swing packet was delivered in the last tick (0.15 seconds)
     */
    private fun checkArmAnimation() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, object : Runnable {
            override fun run() {
                val keySet: MutableSet<String?> = armSwinging.keys
                val currentTime = System.currentTimeMillis()
                for (string in keySet) {
                    if (armSwinging.get(string)!! + 150 < currentTime) {
                        armSwinging.remove(string)
                    }
                }
            }
        }, TickTime.INSTANTANEOUSLY, TickTime.TICK)
    }

    companion object {
        @JvmField
		var armSwinging: HashMap<String?, Long?> = HashMap<String?, Long?>()
    }
}
