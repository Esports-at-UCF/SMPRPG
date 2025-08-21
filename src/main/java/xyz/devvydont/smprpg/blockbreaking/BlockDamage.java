package xyz.devvydont.smprpg.blockbreaking;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.destroystokyo.paper.ParticleBuilder;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.block.BlockLootRegistry;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.services.AttributeService;
import xyz.devvydont.smprpg.services.BlockBreakingService;
import xyz.devvydont.smprpg.services.EconomyService;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.formatting.Symbols;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class BlockDamage {

	protected final SMPRPG plugin;
	protected final SpeedConfigFileHandler filehandler = SMPRPG.getService(BlockBreakingService.class).filehandler;
    
    private static HashMap<String, ScheduleTask> scheduleId = new HashMap<String, ScheduleTask>();
	
	private static ProtocolManager manager;
	
	public BlockDamage() {
		this.plugin = SMPRPG.getInstance();
		manager = ProtocolLibrary.getProtocolManager();
	}
	

    protected void configureBreakingPacket(Player player, Block block) {
		PacketContainer breakingAnimation = manager.createPacket(PacketType.Play.Server.BLOCK_BREAK_ANIMATION);
		
		// this enusres that the player wont conflict with another player's breaking animation
		int entityId = player.getEntityId() + 1;
		entityId = entityId * 1000;
		
		
		breakingAnimation.getIntegers().write(0, entityId);
        breakingAnimation.getBlockPositionModifier().write(0, new BlockPosition(block.getX(), block.getY(), block.getZ()));
        
        breakingTimeCheck(player, block, breakingAnimation);

	}

    private void breakingTimeCheck(Player player, Block block, PacketContainer breakingAnimation) {
		double breakingTimeTicks = getBreakingTime(player, block);


        // Check if the breakingTime is instant/unbreakable
        if (breakingTimeTicks <= 0) {
			if (breakingTimeTicks == -1)
				return;
        	playerBreakBlock(player, block);
        	return;
        }

        startBreaking(player, breakingAnimation, breakingTimeTicks, block);
    
    }
    
    private void startBreaking(Player player, PacketContainer breakingAnimation, double breakingTimeTicks, Block originalBlock) {

    	int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
        	double currentTicks = 0d;
        	
        	@Override
            public void run() {
                
        		// stops breaking if player isn't actively breaking the block
        		if (!(PacketManager.armSwinging.containsKey(player.getName()))) {
                	Bukkit.getScheduler().cancelTask(scheduleId.get(player.getName()).taskId);
                	scheduleId.remove(player.getName());      	
                    
                    // returns the breaking animation back to none
                	breakingAnimation.getIntegers().write(1, -1);
                    manager.sendServerPacket(player, breakingAnimation);
                    return;
        		}
        		
        		Block currentTarget = player.getTargetBlockExact(5);
        		
        		// removes any progress if mining from block onto air and cancels this task
                if (currentTarget == null) {
                	Bukkit.getScheduler().cancelTask(scheduleId.get(player.getName()).taskId);
                	scheduleId.remove(player.getName());      	
                    
                    // returns the breaking animation back to none
                	breakingAnimation.getIntegers().write(1, -1);
                    manager.sendServerPacket(player, breakingAnimation);
                    return;
                }
                
                // breaks the block if it has been mined for a sufficient amount of time
                if(currentTicks >= breakingTimeTicks) {
                	// sets the final breaking animation
                	breakingAnimation.getIntegers().write(1, 10);  // Set to 10 to remove break stage. Anything not within 0-9 unsigned byte range uses no texture.
                    manager.sendServerPacket(player, breakingAnimation);

                    playerBreakBlock(player, originalBlock);
                    Bukkit.getScheduler().cancelTask(scheduleId.get(player.getName()).taskId);
                    scheduleId.remove(player.getName());
                    return;
                } else {
                	double multiplier = 0.1;
                	for (int x=0; x <= 9; x++) {
                		if (currentTicks <= (breakingTimeTicks * multiplier)) {
                        	breakingAnimation.getIntegers().write(1, x-1);
                            manager.sendServerPacket(player, breakingAnimation);
                            break;
                		}
                		multiplier += 0.1;
                	}
                }
                
                currentTicks = currentTicks + 1;
            }
    	},0L, 1L);
    	
    	scheduleId.put(player.getName(), new ScheduleTask(taskId, originalBlock));
    }

    public static void cancelTaskWithBlockReset(Player player) {
    	if (scheduleId.containsKey(player.getName())) {
        	Block block = scheduleId.get(player.getName()).block;
    		
    		Bukkit.getScheduler().cancelTask(scheduleId.get(player.getName()).taskId);
        	scheduleId.remove(player.getName());
        	
    		
        	// this enusres that the player wont conflict with another player's breaking animation
    		int entityId = player.getEntityId() + 1;
    		entityId = entityId * 1000;
    		PacketContainer breakingAnimation = manager.createPacket(PacketType.Play.Server.BLOCK_BREAK_ANIMATION);
    		
    		breakingAnimation.getIntegers().write(0, entityId);
            breakingAnimation.getBlockPositionModifier().write(0, new BlockPosition(block.getX(), block.getY(), block.getZ()));
        	
            // returns the breaking animation back to none
        	breakingAnimation.getIntegers().write(1, -1);
            manager.sendServerPacket(player, breakingAnimation);
        }
    }
    
    @SuppressWarnings("deprecation")
	private double getBreakingTime(Player player, Block block) {
		double speedMultiplier = 100d;

		// Check if held item has a proper tool component. If it doesn't, assume unarmed
		var item = player.getEquipment().getItemInMainHand();
		var entry = BlockPropertiesRegistry.get(block.getType());
		Set<ItemClassification> preferredTools;

		// Failfast if entry is null.
		if (entry == null) {
			SMPRPG.getInstance().getLogger().warning("Unknown block entry " + block.toString() + ". Please add it to BlockPropertiesRegistry!");
			player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 0.5F, 0.75F);
			return -1d;
		}
		else
			preferredTools = entry.getPreferredTools();
		boolean isPreferred = false;
		if (preferredTools != null)
			isPreferred = preferredTools.contains(SMPRPG.getService(ItemService.class).getBlueprint(item).getItemClassification()) || entry.getSoftRequirement();

		if (isPreferred)  // Only add extra mining speed once we know this is a preferred break option
		{
			speedMultiplier -= 100;  // Subtract our implicit 100 speed given for unarmed/non-tool options
			speedMultiplier += AttributeService.getInstance().getOrCreateAttribute(player, AttributeWrapper.MINING_SPEED).getValue();
		}

		// Haste gives +100 mining speed per level, Mining Fatigue gives -100 mining speed per level.
		if (player.hasPotionEffect(PotionEffectType.HASTE))
			speedMultiplier += 100 * player.getPotionEffect(PotionEffectType.HASTE).getAmplifier();
		if (player.hasPotionEffect(PotionEffectType.MINING_FATIGUE))
			speedMultiplier -= 100 * player.getPotionEffect(PotionEffectType.MINING_FATIGUE).getAmplifier();

		if (player.isInWater())
		  speedMultiplier *= AttributeService.getInstance().getOrCreateAttribute(player, AttributeWrapper.UNDERWATER_MINING).getValue();

		if (!player.isOnGround())
			speedMultiplier *= AttributeService.getInstance().getOrCreateAttribute(player, AttributeWrapper.AIRBORNE_MINING).getValue();

		double damage;
		
		// Checks for a custom hardness.
		float hardness;

		// Failsafe, block is unbreakable if not properly defined.
		double playerBp = AttributeService.getInstance().getOrCreateAttribute(player, AttributeWrapper.MINING_POWER).getValue();
		if (playerBp >= entry.getBreakingPower())
			hardness = entry.getHardness();
		else {
			player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 0.5F, 0.5F);
			player.sendMessage(ComponentUtils.success(ComponentUtils.merge(
					ComponentUtils.create("You cannot break this block, as you only have a breaking power of ", NamedTextColor.RED),
					ComponentUtils.create(Symbols.PICKAXE + String.valueOf((int) playerBp), NamedTextColor.DARK_PURPLE),
					ComponentUtils.create(". In order to break this block, you need ", NamedTextColor.RED),
					ComponentUtils.create(Symbols.PICKAXE + String.valueOf((int) entry.getBreakingPower()), NamedTextColor.LIGHT_PURPLE),
					ComponentUtils.create(" breaking power.", NamedTextColor.RED)
			)));
			return -1d;
		}
		damage = speedMultiplier / hardness;

		if (isPreferred)
			damage /= 30;
		else
			damage /= 100;

		damage = Math.max(damage, 0);  // Prevents negative mining speed.

		// Instant breaking
		if (damage > 1) {
		  return 0d;
		}

		return Math.round(1 / damage);
    }
    
    private void playerBreakBlock(Player player, Block block) {
//    	Collection<ItemStack> blockDrops = block.getDrops(player.getEquipment().getItemInMainHand());
//    	
//    	block.getLocation().getBlock().setType(Material.AIR);
//    	block.getWorld().playSound(block.getLocation(), Sound.BLOCK_STONE_BREAK, 1.0f, 1.0f);    	
//    	for (ItemStack drop : blockDrops) {
//    		block.getWorld().dropItem(block.getLocation(), drop);	
//    	}

		var blockState = block.getState();

		block.getWorld().playSound(block.getLocation(), block.getBlockData().getSoundGroup().getBreakSound(), 1.0f, 0.8f);
		var particle = new ParticleBuilder(Particle.BLOCK)
				.location(block.getLocation().toCenterLocation())
				.data(block.getBlockData())
				.count(40)
				.offset(0.25, 0.25, 0.25)
				.spawn();
    	player.breakBlock(block);
    }
}