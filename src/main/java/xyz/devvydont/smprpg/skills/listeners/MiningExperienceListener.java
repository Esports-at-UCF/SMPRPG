package xyz.devvydont.smprpg.skills.listeners;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.block.CustomBlock;
import xyz.devvydont.smprpg.blockbreaking.BlockPropertiesRegistry;
import xyz.devvydont.smprpg.events.skills.SkillExperienceGainEvent;
import xyz.devvydont.smprpg.services.EntityService;
import xyz.devvydont.smprpg.skills.SkillInstance;
import xyz.devvydont.smprpg.util.world.ChunkUtil;

public class MiningExperienceListener implements Listener {


    public static int getBaseExperienceForDrop(Block block) {

        if (BlockPropertiesRegistry.isCustom(block)) {
            CustomBlock cb = CustomBlock.resolve(block);
            return switch (cb) {
                case RAW_SILVER_BLOCK -> 11;
                case RAW_TIN_BLOCK -> 8;
                case RAW_MITHRIL_BLOCK -> 20;
                case RAW_TITANIUM_BLOCK -> 125;
                case RAW_ADAMANTIUM_BLOCK -> 200;

                case SILVER_ORE -> 10;
                case DEEPSLATE_SILVER_ORE -> 30;
                case TIN_ORE -> 2;
                case DEEPSLATE_TIN_ORE -> 6;
                case SPARSE_MITHRIL_ORE -> 9;
                case MITHRIL_ORE -> 18;
                case DENSE_MITHRIL_ORE -> 36;
                case TITANIUM_ORE -> 75;
                case ADAMANTIUM_ORE -> 200;
                default -> 0;
            };
        }

        return switch (block.getType()) {

            case END_STONE, STONE, COBBLESTONE, COBBLED_DEEPSLATE, SAND, RED_SAND, SANDSTONE, RED_SANDSTONE, CLAY, MYCELIUM, GRASS_BLOCK, DIRT, GRAVEL, DEEPSLATE, TUFF, NETHERRACK, BLACKSTONE, BASALT, SMOOTH_BASALT, CRIMSON_NYLIUM, WARPED_NYLIUM, FLINT -> 1;
                    case ANDESITE, DIORITE, GRANITE, CALCITE, BONE_BLOCK, SOUL_SAND, SOUL_SOIL, ICE, PACKED_ICE -> 2;

                    case SEA_LANTERN -> 10;
                    case PRISMARINE -> 2;
                    case PRISMARINE_BRICKS, DARK_PRISMARINE -> 2;
                    case WET_SPONGE -> 25;

                    case BLUE_ICE -> 8;

                    case COAL_ORE, COAL -> 5;
                    case DEEPSLATE_COAL_ORE -> 15;

                    case COPPER_ORE, RAW_COPPER, COPPER_INGOT -> 2;
                    case DEEPSLATE_COPPER_ORE -> 6;
                    case RAW_COPPER_BLOCK -> 8;

                    case IRON_ORE, RAW_IRON, IRON_INGOT -> 7;
                    case RAW_IRON_BLOCK -> 8;
                    case IRON_BLOCK -> 9;
                    case DEEPSLATE_IRON_ORE -> 21;

                    case GOLD_ORE, RAW_GOLD, GOLD_INGOT -> 12;
                    case RAW_GOLD_BLOCK -> 14;
                    case DEEPSLATE_GOLD_ORE -> 36;

                    case OBSIDIAN -> 20;
                    case CRYING_OBSIDIAN -> 24;

                    case AMETHYST_BLOCK -> 4;
                    case AMETHYST_SHARD -> 10;
                    case AMETHYST_CLUSTER -> 15;
                    case BUDDING_AMETHYST -> 10;
                    case LARGE_AMETHYST_BUD -> 15;
                    case MEDIUM_AMETHYST_BUD -> 20;
                    case SMALL_AMETHYST_BUD -> 25;

                    case LAPIS_LAZULI -> 3;
                    case REDSTONE -> 4;

                    case REDSTONE_BLOCK -> 15;

                    case LAPIS_ORE, REDSTONE_ORE -> 18;
                    case DEEPSLATE_LAPIS_ORE, DEEPSLATE_REDSTONE_ORE -> 54;

                    case DIAMOND_ORE, DIAMOND -> 25;
                    case DEEPSLATE_DIAMOND_ORE -> 75;

                    case EMERALD_ORE, EMERALD -> 100;
                    case DEEPSLATE_EMERALD_ORE -> 300;

                    case GOLD_BLOCK -> 45;
                    case EMERALD_BLOCK -> 200;

                    case MAGMA_BLOCK -> 2;
                    case GOLD_NUGGET -> 5;
                    case NETHER_GOLD_ORE -> 22;
                    case GLOWSTONE_DUST -> 5;
                    case GLOWSTONE -> 15;
                    case NETHER_QUARTZ_ORE -> 25;
                    case QUARTZ -> 10;
                    case QUARTZ_BLOCK -> 15;
                    case ANCIENT_DEBRIS -> 200;


                    default -> 0;
                };
    }


    final SMPRPG plugin;

    public MiningExperienceListener(SMPRPG plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * When a player places a block, we need to mark that block as unable to be farmed for experience
     *
     * @param event
     */
    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent event) {

        if (event.isCancelled())
            return;

        // When any block is placed, it is no longer able to earn skill experience
        ChunkUtil.markBlockSkillInvalid(event.getBlock());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onGainGeneralMiningExperience(BlockBreakEvent event) {

        var block = event.getBlock();

        if (event.isCancelled())
            return;

        // If this block isn't allowed to retrieve experience
        if (ChunkUtil.isBlockSkillInvalid(block))
            return;

        SkillInstance skill = SMPRPG.getService(EntityService.class).getPlayerInstance(event.getPlayer()).getMiningSkill();

        int exp = 0;
        exp += getBaseExperienceForDrop(block);
        if (exp <= 0)
            return;

        event.setExpToDrop(Math.max(1, exp/10));
        skill.addExperience(exp, SkillExperienceGainEvent.ExperienceSource.ORE);
    }
}
