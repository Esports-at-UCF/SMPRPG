package xyz.devvydont.smprpg.blockbreaking;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import xyz.devvydont.smprpg.SMPRPG;

import java.util.HashMap;
import java.util.Set;

public class PacketManager implements Listener{

	protected final SMPRPG plugin;
	
	public static HashMap<String, Long> armSwinging = new HashMap<String, Long>();
	
	private ProtocolManager manager;
	
	private BlockDamage damage;
	
	public PacketManager() {
		this.plugin = SMPRPG.getPlugin();
		manager = ProtocolLibrary.getProtocolManager();
		Bukkit.getPluginManager().registerEvents(this, plugin);
		damage = new BlockDamage();
		receivedArmAnimation();
		checkArmAnimation();
		
	}
	
	private void receivedArmAnimation() {
		manager.addPacketListener(new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.ARM_ANIMATION) {
		    @Override
		    public void onPacketReceiving(PacketEvent event) {
		        armSwinging.put(event.getPlayer().getName(), System.currentTimeMillis());
		    }
		});
	}
	
	@EventHandler
	private void blockStartBreaking(BlockDamageEvent event) {
		event.setCancelled(true);  // Cancel any vanilla behavior
		BlockDamage.cancelTaskWithBlockReset(event.getPlayer());
		damage.configureBreakingPacket(event.getPlayer(), event.getBlock());
        
	}
	
	// checks that an arm swing packet was delivered in the last tick (0.15 seconds)
	private void checkArmAnimation() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

			@Override
			public void run() {
				Set<String> keySet = armSwinging.keySet();
				long currentTime = System.currentTimeMillis();
				for (String string : keySet) {
					if (armSwinging.get(string) + 150 < currentTime) {
						armSwinging.remove(string);
					}
				}
			}
			
		}, 1L, 1L);
	}
	
	

}
