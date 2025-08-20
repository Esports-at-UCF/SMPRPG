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

        register(Material.GRASS_BLOCK, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(60)
                .breakingPower(0)
                .build());

        register(Material.PODZOL, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(50)
                .breakingPower(0)
                .build());

        register(Material.MYCELIUM, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(50)
                .breakingPower(0)
                .build());

        register(Material.DIRT_PATH, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(65)
                .breakingPower(0)
                .build());

        register(Material.DIRT, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(50)
                .breakingPower(0)
                .build());

        register(Material.COARSE_DIRT, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(50)
                .breakingPower(0)
                .build());

        register(Material.ROOTED_DIRT, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(50)
                .breakingPower(0)
                .build());

        register(Material.MUD, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(50)
                .breakingPower(0)
                .build());

        register(Material.CLAY, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(60)
                .breakingPower(0)
                .build());

        register(Material.GRAVEL, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(60)
                .breakingPower(0)
                .build());

        register(Material.SAND, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(50)
                .breakingPower(0)
                .build());

        register(Material.SANDSTONE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(80)
                .breakingPower(0)
                .build());

        register(Material.SANDSTONE_SLAB, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(80)
                .breakingPower(0)
                .build());

        register(Material.SANDSTONE_STAIRS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(80)
                .breakingPower(0)
                .build());

        register(Material.CUT_SANDSTONE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(90)
                .breakingPower(0)
                .build());

        register(Material.CUT_SANDSTONE_SLAB, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(80)
                .breakingPower(0)
                .build());

        register(Material.CHISELED_SANDSTONE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(80)
                .breakingPower(0)
                .build());

        register(Material.RED_SAND, BlockPropertiesEntry.builder(ItemClassification.SHOVEL)
                .hardness(50)
                .breakingPower(0)
                .build());

        register(Material.RED_SANDSTONE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(80)
                .breakingPower(0)
                .build());

        register(Material.RED_SANDSTONE_SLAB, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(80)
                .breakingPower(0)
                .build());

        register(Material.RED_SANDSTONE_STAIRS, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(80)
                .breakingPower(0)
                .build());

        register(Material.CUT_RED_SANDSTONE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(90)
                .breakingPower(0)
                .build());

        register(Material.CUT_RED_SANDSTONE_SLAB, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(80)
                .breakingPower(0)
                .build());

        register(Material.CHISELED_RED_SANDSTONE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(80)
                .breakingPower(0)
                .build());

        register(Material.STONE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(150)
                .breakingPower(0)
                .build());

        register(Material.COBBLESTONE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(200)
                .breakingPower(0)
                .build());

        register(Material.DEEPSLATE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(300)
                .breakingPower(3)
                .build());

        register(Material.COBBLED_DEEPSLATE, BlockPropertiesEntry.builder(ItemClassification.PICKAXE)
                .hardness(350)
                .breakingPower(3)
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
