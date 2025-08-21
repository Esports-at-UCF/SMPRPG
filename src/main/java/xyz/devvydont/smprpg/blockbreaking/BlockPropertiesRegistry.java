package xyz.devvydont.smprpg.blockbreaking;

import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.services.ItemService;

import java.util.EnumMap;
import java.util.Map;

/**
 * The loot dropped from blocks is a very complex system. When blocks are dropped, there's conditions
 * that can be met that completely shift the result of what is dropped.
 * The idea with our plugin, is that we hook into specific events to override the drops of such events, and assume
 * vanilla (unchanged) behavior when our plugin doesn't have a case defined. Think of events as follows:
 * - Breaking a block with a preferred tool. (pickaxe breaking stone)
 * - Breaking a block with silk touch.
 * - Breaking a block with auto smelt.
 * - Breaking a block with incorrect tool. (fist and stone, or axe and stone)
 */
public class BlockPropertiesRegistry {

    private static final Map<Material, BlockPropertiesEntry> entries = new EnumMap<>(Material.class);

    // Inputs necessary block drop overrides. Keep in mind, we only need to add OVERRIDES. If the vanilla behavior
    // is fine, then you can omit it :)
    // Just keep in mind, vanilla drops will NOT be affected by any fortune stats, so you do need to add all ores etc.
    static {
        // Natural / Ores (Overworld)

        //<editor-fold desc="Shovelable blocks">
        register(Material.CLAY, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(60)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.COARSE_DIRT, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(50)
                .breakingPower(0)
                .softRequirement(true)
                .build());
        //<editor-fold desc="Concrete powders">
        register(Material.WHITE_CONCRETE_POWDER, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(50)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.ORANGE_CONCRETE_POWDER, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(50)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.MAGENTA_CONCRETE_POWDER, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(50)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.LIGHT_BLUE_CONCRETE_POWDER, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(50)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.YELLOW_CONCRETE_POWDER, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(50)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.LIME_CONCRETE_POWDER, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(50)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.PINK_CONCRETE_POWDER, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(50)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.GRAY_CONCRETE_POWDER, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(50)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.LIGHT_GRAY_CONCRETE_POWDER, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(50)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.CYAN_CONCRETE_POWDER, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(50)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.PURPLE_CONCRETE_POWDER, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(50)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.BLUE_CONCRETE_POWDER, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(50)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.BROWN_CONCRETE_POWDER, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(50)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.GREEN_CONCRETE_POWDER, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(50)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.RED_CONCRETE_POWDER, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(50)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.BLACK_CONCRETE_POWDER, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(50)
                .breakingPower(0)
                .softRequirement(true)
                .build());
        //</editor-fold>

        register(Material.DIRT, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(50)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.DIRT_PATH, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(65)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.FARMLAND, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(50)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.GRAVEL, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(60)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.GRASS_BLOCK, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(60)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.MUD, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(50)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.MUDDY_MANGROVE_ROOTS, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)  // TODO: Reassess with mangrove roots maybe?
                .hardness(70)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.MYCELIUM, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(50)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.PODZOL, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(50)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.RED_SAND, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(50)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.ROOTED_DIRT, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(50)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.SAND, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(50)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.SNOW, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(10)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.SNOW_BLOCK, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(20)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.SOUL_SAND, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(50)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.SOUL_SOIL, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(50)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.SUSPICIOUS_GRAVEL, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(25)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.SUSPICIOUS_SAND, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(25)
                .breakingPower(0)
                .softRequirement(true)
                .build());
        //</editor-fold>

        //<editor-fold desc="Pickaxe blocks">

        //<editor-fold desc="Ores">
        register(Material.AMETHYST_BLOCK, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.AMETHYST_CLUSTER, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.SMALL_AMETHYST_BUD, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.MEDIUM_AMETHYST_BUD, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.LARGE_AMETHYST_BUD, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.ANCIENT_DEBRIS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(3000)
                .breakingPower(5)
                .softRequirement(false)
                .build());

        register(Material.BUDDING_AMETHYST, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.COAL_ORE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(300)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.COPPER_ORE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(300)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.DIAMOND_ORE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(300)
                .breakingPower(4)
                .softRequirement(false)
                .build());

        register(Material.EMERALD_ORE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(300)
                .breakingPower(4)
                .softRequirement(false)
                .build());

        register(Material.GOLD_ORE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(300)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.IRON_ORE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(300)
                .breakingPower(2)
                .softRequirement(false)
                .build());

        register(Material.LAPIS_ORE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(300)
                .breakingPower(2)
                .softRequirement(false)
                .build());

        register(Material.NETHER_GOLD_ORE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(300)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.NETHER_QUARTZ_ORE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(300)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.REDSTONE_ORE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(300)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.DEEPSLATE_COAL_ORE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(450)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.DEEPSLATE_COPPER_ORE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(450)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.DEEPSLATE_DIAMOND_ORE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(450)
                .breakingPower(6)
                .softRequirement(false)
                .build());

        register(Material.DEEPSLATE_EMERALD_ORE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(450)
                .breakingPower(6)
                .softRequirement(false)
                .build());

        register(Material.DEEPSLATE_GOLD_ORE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(450)
                .breakingPower(5)
                .softRequirement(false)
                .build());

        register(Material.DEEPSLATE_IRON_ORE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(450)
                .breakingPower(4)
                .softRequirement(false)
                .build());

        register(Material.DEEPSLATE_LAPIS_ORE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(450)
                .breakingPower(4)
                .softRequirement(false)
                .build());

        register(Material.DEEPSLATE_REDSTONE_ORE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(450)
                .breakingPower(5)
                .softRequirement(false)
                .build());
        //</editor-fold>
        //<editor-fold desc="Stones">
        register(Material.BEDROCK, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(-1)
                .breakingPower(-1)
                .softRequirement(false)
                .build());

        register(Material.STONE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(0)
                .softRequirement(false)
                .build());

        register(Material.STONE_SLAB, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(0)
                .softRequirement(false)
                .build());

        register(Material.STONE_STAIRS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(0)
                .softRequirement(false)
                .build());

        register(Material.STONE_PRESSURE_PLATE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(50)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.STONE_BUTTON, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(50)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.COBBLESTONE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(200)
                .breakingPower(0)
                .softRequirement(false)
                .build());

        register(Material.COBBLESTONE_SLAB, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(200)
                .breakingPower(0)
                .softRequirement(false)
                .build());

        register(Material.COBBLESTONE_STAIRS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(200)
                .breakingPower(0)
                .softRequirement(false)
                .build());

        register(Material.COBBLESTONE_WALL, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(200)
                .breakingPower(0)
                .softRequirement(false)
                .build());

        register(Material.MOSSY_COBBLESTONE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(200)
                .breakingPower(0)
                .softRequirement(false)
                .build());

        register(Material.MOSSY_COBBLESTONE_SLAB, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(200)
                .breakingPower(0)
                .softRequirement(false)
                .build());

        register(Material.MOSSY_COBBLESTONE_STAIRS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(200)
                .breakingPower(0)
                .softRequirement(false)
                .build());

        register(Material.MOSSY_COBBLESTONE_WALL, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(200)
                .breakingPower(0)
                .softRequirement(false)
                .build());

        register(Material.SMOOTH_STONE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(200)
                .breakingPower(0)
                .softRequirement(false)
                .build());

        register(Material.SMOOTH_STONE_SLAB, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(200)
                .breakingPower(0)
                .softRequirement(false)
                .build());

        register(Material.STONE_BRICKS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(0)
                .softRequirement(false)
                .build());

        register(Material.STONE_BRICK_SLAB, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(0)
                .softRequirement(false)
                .build());

        register(Material.STONE_BRICK_STAIRS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(0)
                .softRequirement(false)
                .build());

        register(Material.STONE_BRICK_WALL, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(0)
                .softRequirement(false)
                .build());

        register(Material.CRACKED_STONE_BRICKS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(0)
                .softRequirement(false)
                .build());

        register(Material.MOSSY_STONE_BRICKS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(0)
                .softRequirement(false)
                .build());

        register(Material.MOSSY_STONE_BRICK_SLAB, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(0)
                .softRequirement(false)
                .build());

        register(Material.MOSSY_STONE_BRICK_STAIRS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(0)
                .softRequirement(false)
                .build());

        register(Material.MOSSY_STONE_BRICK_WALL, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(0)
                .softRequirement(false)
                .build());

        register(Material.INFESTED_COBBLESTONE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(100)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.INFESTED_STONE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(75)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.INFESTED_MOSSY_STONE_BRICKS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(75)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.INFESTED_CRACKED_STONE_BRICKS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(75)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.INFESTED_CHISELED_STONE_BRICKS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(75)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.INFESTED_DEEPSLATE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.INFESTED_COBBLESTONE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(100)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.DEEPSLATE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(300)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.COBBLED_DEEPSLATE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(350)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.COBBLED_DEEPSLATE_SLAB, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(350)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.COBBLED_DEEPSLATE_STAIRS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(350)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.COBBLED_DEEPSLATE_WALL, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(350)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.CHISELED_DEEPSLATE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(350)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.POLISHED_DEEPSLATE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(350)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.POLISHED_DEEPSLATE_SLAB, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(350)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.POLISHED_DEEPSLATE_STAIRS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(350)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.POLISHED_DEEPSLATE_WALL, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(350)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.DEEPSLATE_BRICKS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(350)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.DEEPSLATE_BRICK_SLAB, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(350)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.DEEPSLATE_BRICK_STAIRS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(350)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.DEEPSLATE_BRICK_WALL, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(350)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.CRACKED_DEEPSLATE_BRICKS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(350)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.DEEPSLATE_TILES, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(350)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.DEEPSLATE_TILES, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(350)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.DEEPSLATE_TILE_SLAB, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(350)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.DEEPSLATE_TILE_STAIRS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(350)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.CRACKED_DEEPSLATE_TILES, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(350)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.REINFORCED_DEEPSLATE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(-1)
                .breakingPower(-1)
                .softRequirement(false)
                .build());

        register(Material.CALCITE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(75)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.GRANITE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.GRANITE_SLAB, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.GRANITE_STAIRS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.GRANITE_WALL, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.POLISHED_GRANITE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.POLISHED_GRANITE_SLAB, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.POLISHED_GRANITE_STAIRS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.ANDESITE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.ANDESITE_SLAB, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.ANDESITE_STAIRS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.ANDESITE_WALL, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.POLISHED_ANDESITE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.POLISHED_ANDESITE_SLAB, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.POLISHED_ANDESITE_STAIRS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.DIORITE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.DIORITE_SLAB, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.DIORITE_STAIRS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.DIORITE_WALL, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.POLISHED_DIORITE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.POLISHED_DIORITE_SLAB, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.POLISHED_DIORITE_STAIRS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.TUFF, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.TUFF_SLAB, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.TUFF_STAIRS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.TUFF_WALL, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.TUFF_BRICKS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.TUFF_BRICK_SLAB, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.TUFF_BRICK_STAIRS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.TUFF_BRICK_WALL, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.CHISELED_TUFF, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.POLISHED_TUFF, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.POLISHED_TUFF_SLAB, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.POLISHED_TUFF_STAIRS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.POLISHED_TUFF_WALL, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.TUFF_BRICKS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.TUFF_BRICK_SLAB, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.TUFF_BRICK_STAIRS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.TUFF_BRICK_WALL, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.CHISELED_TUFF_BRICKS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.DRIPSTONE_BLOCK, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.POINTED_DRIPSTONE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.PRISMARINE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.PRISMARINE_SLAB, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.PRISMARINE_STAIRS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.PRISMARINE_WALL, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.PRISMARINE_BRICKS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.PRISMARINE_BRICK_SLAB, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.PRISMARINE_BRICK_STAIRS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.DARK_PRISMARINE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.DARK_PRISMARINE_SLAB, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.DARK_PRISMARINE_STAIRS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.SEA_LANTERN, BlockPropertiesEntry.builder()
                .hardness(30)
                .breakingPower(0)
                .softRequirement(false)
                .build());

        register(Material.MAGMA_BLOCK, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(50)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.CRYING_OBSIDIAN, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(5000)
                .breakingPower(5)
                .softRequirement(false)
                .build());

        register(Material.OBSIDIAN, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(5000)
                .breakingPower(5)
                .softRequirement(false)
                .build());

        register(Material.NETHERRACK, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(40)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.CRIMSON_NYLIUM, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(40)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.WARPED_NYLIUM, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(40)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.BONE_BLOCK, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(200)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.BLACKSTONE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.BLACKSTONE_SLAB, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.BLACKSTONE_STAIRS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.BLACKSTONE_WALL, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.GILDED_BLACKSTONE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.CHISELED_POLISHED_BLACKSTONE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.POLISHED_BLACKSTONE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.POLISHED_BLACKSTONE_SLAB, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.POLISHED_BLACKSTONE_STAIRS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.POLISHED_BLACKSTONE_WALL, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.POLISHED_BLACKSTONE_BUTTON, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(50)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.POLISHED_BLACKSTONE_PRESSURE_PLATE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(50)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.POLISHED_BLACKSTONE_BRICKS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.POLISHED_BLACKSTONE_BRICK_SLAB, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.POLISHED_BLACKSTONE_BRICK_STAIRS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.POLISHED_BLACKSTONE_BRICK_WALL, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.SANDSTONE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(80)
                .breakingPower(0)
                .softRequirement(false)
                .build());

        register(Material.SANDSTONE_SLAB, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(80)
                .breakingPower(0)
                .softRequirement(false)
                .build());

        register(Material.SANDSTONE_STAIRS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(80)
                .breakingPower(0)
                .softRequirement(false)
                .build());

        register(Material.CUT_SANDSTONE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(90)
                .breakingPower(0)
                .softRequirement(false)
                .build());

        register(Material.CUT_SANDSTONE_SLAB, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(80)
                .breakingPower(0)
                .softRequirement(false)
                .build());

        register(Material.CHISELED_SANDSTONE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(80)
                .breakingPower(0)
                .softRequirement(false)
                .build());

        register(Material.RED_SANDSTONE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(80)
                .breakingPower(0)
                .softRequirement(false)
                .build());

        register(Material.RED_SANDSTONE_SLAB, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(80)
                .breakingPower(0)
                .softRequirement(false)
                .build());

        register(Material.RED_SANDSTONE_STAIRS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(80)
                .breakingPower(0)
                .softRequirement(false)
                .build());

        register(Material.CUT_RED_SANDSTONE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(90)
                .breakingPower(0)
                .softRequirement(false)
                .build());

        register(Material.CUT_RED_SANDSTONE_SLAB, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(80)
                .breakingPower(0)
                .softRequirement(false)
                .build());

        register(Material.CHISELED_RED_SANDSTONE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(80)
                .breakingPower(0)
                .softRequirement(false)
                .build());

        register(Material.END_STONE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(300)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.END_STONE_BRICKS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(300)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.END_STONE_BRICK_SLAB, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(300)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.END_STONE_BRICK_STAIRS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(300)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.END_STONE_BRICK_WALL, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(300)
                .breakingPower(1)
                .softRequirement(false)
                .build());
        //</editor-fold>
        register(Material.COAL_BLOCK, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(500)
                .breakingPower(1)
                .softRequirement(false)
                .build());
        register(Material.DIAMOND_BLOCK, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(500)
                .breakingPower(4)
                .softRequirement(false)
                .build());
        register(Material.EMERALD_BLOCK, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(500)
                .breakingPower(4)
                .softRequirement(false)
                .build());
        register(Material.IRON_BLOCK, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(500)
                .breakingPower(2)
                .softRequirement(false)
                .build());
        register(Material.NETHERITE_BLOCK, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(5000)
                .breakingPower(5)
                .softRequirement(false)
                .build());
        //</editor-fold>

        // LOGS/WOOD
        register(Material.OAK_LOG, BlockPropertiesEntry.builder(ItemClassification.AXE)
                .hardness(200)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.OAK_WOOD, BlockPropertiesEntry.builder(ItemClassification.AXE)
                .hardness(200)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.STRIPPED_OAK_LOG, BlockPropertiesEntry.builder(ItemClassification.AXE)
                .hardness(200)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.STRIPPED_OAK_WOOD, BlockPropertiesEntry.builder(ItemClassification.AXE)
                .hardness(200)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.OAK_PLANKS, BlockPropertiesEntry.builder(ItemClassification.AXE)
                .hardness(200)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.OAK_SLAB, BlockPropertiesEntry.builder(ItemClassification.AXE)
                .hardness(200)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.OAK_STAIRS, BlockPropertiesEntry.builder(ItemClassification.AXE)
                .hardness(200)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.OAK_DOOR, BlockPropertiesEntry.builder(ItemClassification.AXE)
                .hardness(200)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.OAK_FENCE, BlockPropertiesEntry.builder(ItemClassification.AXE)
                .hardness(200)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.OAK_FENCE_GATE, BlockPropertiesEntry.builder(ItemClassification.AXE)
                .hardness(200)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.OAK_TRAPDOOR, BlockPropertiesEntry.builder(ItemClassification.AXE)
                .hardness(200)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.OAK_PRESSURE_PLATE, BlockPropertiesEntry.builder(ItemClassification.AXE)
                .hardness(50)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.OAK_BUTTON, BlockPropertiesEntry.builder(ItemClassification.AXE)
                .hardness(50)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.OAK_SIGN, BlockPropertiesEntry.builder(ItemClassification.AXE)
                .hardness(50)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.OAK_HANGING_SIGN, BlockPropertiesEntry.builder(ItemClassification.AXE)
                .hardness(50)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.OAK_LEAVES, BlockPropertiesEntry.builder(ItemClassification.AXE, ItemClassification.HOE)  // TODO: Add classification for shears
                .hardness(20)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.SPRUCE_LOG, BlockPropertiesEntry.builder(ItemClassification.AXE)
                .hardness(200)
                .breakingPower(0)
                .softRequirement(true)
                .build());

        register(Material.BIRCH_LOG, BlockPropertiesEntry.builder(ItemClassification.AXE)
                .hardness(250)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.JUNGLE_LOG, BlockPropertiesEntry.builder(ItemClassification.AXE)
                .hardness(250)
                .breakingPower(1)
                .softRequirement(false)
                .build());

        register(Material.ACACIA_LOG, BlockPropertiesEntry.builder(ItemClassification.AXE)
                .hardness(350)
                .breakingPower(2)
                .softRequirement(false)
                .build());

        register(Material.DARK_OAK_LOG, BlockPropertiesEntry.builder(ItemClassification.AXE)
                .hardness(350)
                .breakingPower(2)
                .softRequirement(false)
                .build());

        register(Material.CHERRY_LOG, BlockPropertiesEntry.builder(ItemClassification.AXE)
                .hardness(500)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.MANGROVE_LOG, BlockPropertiesEntry.builder(ItemClassification.AXE)
                .hardness(500)
                .breakingPower(3)
                .softRequirement(false)
                .build());

        register(Material.PALE_OAK_LOG, BlockPropertiesEntry.builder(ItemClassification.AXE)
                .hardness(750)
                .breakingPower(4)
                .softRequirement(false)
                .build());

        register(Material.CRIMSON_STEM, BlockPropertiesEntry.builder(ItemClassification.AXE)
                .hardness(1000)
                .breakingPower(5)
                .softRequirement(false)
                .build());

        register(Material.WARPED_STEM, BlockPropertiesEntry.builder(ItemClassification.AXE)
                .hardness(1000)
                .breakingPower(5)
                .softRequirement(false)
                .build());
    }

    public static void register(Material material, BlockPropertiesEntry entry) {
        entries.put(material, entry);
    }

    public static @Nullable BlockPropertiesEntry get(Material material) {
        return entries.get(material);
    }

    public static boolean contains(Material material) {
        return entries.containsKey(material);
    }

}
