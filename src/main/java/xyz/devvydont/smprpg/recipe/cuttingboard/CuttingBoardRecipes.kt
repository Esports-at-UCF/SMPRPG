package xyz.devvydont.smprpg.recipe.cuttingboard

import net.momirealms.craftengine.core.util.Key
import org.bukkit.Material
import org.bukkit.NamespacedKey
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.services.ItemService

class CuttingBoardToolTags() {

    companion object {
        val KNIVES: Key = Key.of("smprpg:knives")
        val AXES: Key = Key.of("minecraft:axes")
        val SHOVELS: Key = Key.of("minecraft:shovels")
    }
}

enum class CuttingBoardRecipes(val recipe: CuttingBoardRecipe) {

    // Raw meat processing
    BEEF_TO_GROUND_BEEF(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "beef_to_ground_beef_cutting"),
            ItemService.generate(Material.BEEF),
            listOf(
                Pair(ItemService.generate(CustomItemType.GROUND_BEEF, 2), 1.0),
            ),
            CuttingBoardToolTags.KNIVES
        )
    ),
    CHICKEN_TO_CHICKEN_CUTS(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "chicken_to_chicken_cuts_cutting"),
            ItemService.generate(Material.CHICKEN),
            listOf(
                Pair(ItemService.generate(CustomItemType.CHICKEN_CUTS, 2), 1.0),
                Pair(ItemService.generate(Material.BONE_MEAL), 1.0),
            ),
            CuttingBoardToolTags.KNIVES
        )
    ),
    PORKCHOP_TO_BACON(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "porkchop_to_bacon_cutting"),
            ItemService.generate(Material.PORKCHOP),
            listOf(
                Pair(ItemService.generate(CustomItemType.BACON, 2), 1.0),
            ),
            CuttingBoardToolTags.KNIVES
        )
    ),
    MUTTON_TO_MUTTON_CHOPS(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "mutton_to_mutton_chops_cutting"),
            ItemService.generate(Material.MUTTON),
            listOf(
                Pair(ItemService.generate(CustomItemType.MUTTON_CHOPS, 2), 1.0),
            ),
            CuttingBoardToolTags.KNIVES
        )
    ),
    SALMON_TO_SALMON_SLICE(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "salmon_to_salmon_slice_cutting"),
            ItemService.generate(Material.SALMON),
            listOf(
                Pair(ItemService.generate(CustomItemType.SALMON_FILET, 2), 1.0),
                Pair(ItemService.generate(Material.BONE_MEAL), 1.0),
            ),
            CuttingBoardToolTags.KNIVES
        )
    ),
    COD_TO_COD_SLICE(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "cod_to_cod_slice_cutting"),
            ItemService.generate(Material.COD),
            listOf(
                Pair(ItemService.generate(CustomItemType.COD_FILET, 2), 1.0),
                Pair(ItemService.generate(Material.BONE_MEAL), 1.0),
            ),
            CuttingBoardToolTags.KNIVES
        )
    ),

    // Cooked meat processing
    STEAK_TO_STEAK_STRIPS(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "steak_to_steak_strips_cutting"),
            ItemService.generate(Material.COOKED_BEEF),
            listOf(
                Pair(ItemService.generate(CustomItemType.STEAK_STRIPS, 2), 1.0),
            ),
            CuttingBoardToolTags.KNIVES
        )
    ),
    COOKED_CHICKEN_TO_COOKED_CHICKEN_CUTS(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "cooked_chicken_to_cooked_chicken_cuts_cutting"),
            ItemService.generate(Material.COOKED_CHICKEN),
            listOf(
                Pair(ItemService.generate(CustomItemType.COOKED_CHICKEN_CUTS, 2), 1.0),
                Pair(ItemService.generate(Material.BONE_MEAL), 1.0),
            ),
            CuttingBoardToolTags.KNIVES
        )
    ),
    COOKED_MUTTON_TO_COOKED_MUTTON_CHOPS(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "cooked_mutton_to_cooked_mutton_chops_cutting"),
            ItemService.generate(Material.COOKED_MUTTON),
            listOf(
                Pair(ItemService.generate(CustomItemType.COOKED_MUTTON_CHOPS, 2), 1.0),
            ),
            CuttingBoardToolTags.KNIVES
        )
    ),
    COOKED_SALMON_TO_COOKED_SALMON_SLICE(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "cooked_salmon_to_cooked_salmon_slice_cutting"),
            ItemService.generate(Material.COOKED_SALMON),
            listOf(
                Pair(ItemService.generate(CustomItemType.COOKED_SALMON_FILET, 2), 1.0),
                Pair(ItemService.generate(Material.BONE_MEAL), 1.0),
            ),
            CuttingBoardToolTags.KNIVES
        )
    ),
    COOKED_COD_TO_COOKED_COD_SLICE(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "cooked_cod_to_cooked_cod_slice_cutting"),
            ItemService.generate(Material.COOKED_COD),
            listOf(
                Pair(ItemService.generate(CustomItemType.COOKED_COD_FILET, 2), 1.0),
                Pair(ItemService.generate(Material.BONE_MEAL), 1.0),
            ),
            CuttingBoardToolTags.KNIVES
        )
    ),

    // Cured meat processing
    HAM_TO_PORKCHOPS(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "ham_to_porkchops_cutting"),
            ItemService.generate(CustomItemType.HAM),
            listOf(
                Pair(ItemService.generate(Material.PORKCHOP, 2), 1.0),
                Pair(ItemService.generate(Material.BONE), 1.0),
            ),
            CuttingBoardToolTags.KNIVES
        )
    ),
    SMOKED_HAM_TO_COOKED_PORKCHOPS(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "smoked_ham_to_cooked_porkchops_cutting"),
            ItemService.generate(CustomItemType.SMOKED_HAM),
            listOf(
                Pair(ItemService.generate(Material.COOKED_PORKCHOP, 2), 1.0),
                Pair(ItemService.generate(Material.BONE), 1.0),
            ),
            CuttingBoardToolTags.KNIVES
        )
    ),

    // Crops and plants
    CABBAGE_TO_CABBAGE_LEAVES(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "cabbage_to_cabbage_leaves_cutting"),
            ItemService.generate(CustomItemType.CABBAGE),
            listOf(
                Pair(ItemService.generate(CustomItemType.CABBAGE_LEAF, 2), 1.0),
            ),
            CuttingBoardToolTags.KNIVES
        )
    ),
    RICE_PANICLE_TO_RICE(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "rice_panicle_to_rice_cutting"),
            ItemService.generate(CustomItemType.RICE_PANICLE),
            listOf(
                Pair(ItemService.generate(CustomItemType.RICE), 1.0),
                Pair(ItemService.generate(CustomItemType.STRAW), 1.0),
            ),
            CuttingBoardToolTags.KNIVES
        )
    ),
    MELON_TO_MELON_SLICES(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "melon_to_melon_slices_cutting"),
            ItemService.generate(Material.MELON),
            listOf(
                Pair(ItemService.generate(Material.MELON_SLICE, 9), 1.0),
            ),
            CuttingBoardToolTags.KNIVES
        )
    ),
    PUMPKIN_TO_PUMPKIN_SLICES(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "pumpkin_to_pumpkin_slices_cutting"),
            ItemService.generate(Material.PUMPKIN),
            listOf(
                Pair(ItemService.generate(CustomItemType.PUMPKIN_SLICE, 4), 1.0),
            ),
            CuttingBoardToolTags.KNIVES
        )
    ),

    // Food slicing
    CAKE_TO_CAKE_SLICES(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "cake_to_cake_slices_cutting"),
            ItemService.generate(Material.CAKE),
            listOf(
                Pair(ItemService.generate(CustomItemType.CAKE_SLICE, 7), 1.0),
            ),
            CuttingBoardToolTags.KNIVES
        )
    ),
    PUMPKIN_PIE_TO_PUMPKIN_PIE_SLICES(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "pumpkin_pie_to_pumpkin_pie_slices_cutting"),
            ItemService.generate(Material.PUMPKIN_PIE),
            listOf(
                Pair(ItemService.generate(CustomItemType.PUMPKIN_PIE_SLICE, 4), 1.0),
            ),
            CuttingBoardToolTags.KNIVES
        )
    ),
    APPLE_PIE_TO_APPLE_PIE_SLICES(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "apple_pie_to_apple_pie_slices_cutting"),
            ItemService.generate(CustomItemType.APPLE_PIE),
            listOf(
                Pair(ItemService.generate(CustomItemType.APPLE_PIE_SLICE, 4), 1.0),
            ),
            CuttingBoardToolTags.KNIVES
        )
    ),
    CHOCOLATE_PIE_TO_CHOCOLATE_PIE_SLICES(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "chocolate_pie_to_chocolate_pie_slices_cutting"),
            ItemService.generate(CustomItemType.CHOCOLATE_PIE),
            listOf(
                Pair(ItemService.generate(CustomItemType.CHOCOLATE_PIE_SLICE, 4), 1.0),
            ),
            CuttingBoardToolTags.KNIVES
        )
    ),
    SWEET_BERRY_CHEESECAKE_TO_SLICES(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "sweet_berry_cheesecake_to_slices_cutting"),
            ItemService.generate(CustomItemType.SWEET_BERRY_CHEESECAKE),
            listOf(
                Pair(ItemService.generate(CustomItemType.SWEET_BERRY_CHEESECAKE_SLICE, 4), 1.0),
            ),
            CuttingBoardToolTags.KNIVES
        )
    ),
    KELP_ROLL_TO_KELP_ROLL_SLICES(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "kelp_roll_to_kelp_roll_slices_cutting"),
            ItemService.generate(CustomItemType.KELP_ROLL),
            listOf(
                Pair(ItemService.generate(CustomItemType.KELP_ROLL_SLICE, 3), 1.0),
            ),
            CuttingBoardToolTags.KNIVES
        )
    ),

    // Dough processing
    WHEAT_DOUGH_TO_RAW_PASTA(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "wheat_dough_to_raw_pasta_cutting"),
            ItemService.generate(CustomItemType.WHEAT_DOUGH),
            listOf(
                Pair(ItemService.generate(CustomItemType.RAW_PASTA), 1.0),
            ),
            CuttingBoardToolTags.KNIVES
        )
    ),

    // Flowers to dyes
    ALLIUM_TO_MAGENTA_DYE(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "allium_to_magenta_dye_cutting"),
            ItemService.generate(Material.ALLIUM),
            listOf(Pair(ItemService.generate(Material.MAGENTA_DYE, 2), 1.0)),
            CuttingBoardToolTags.KNIVES
        )
    ),
    AZURE_BLUET_TO_LIGHT_GRAY_DYE(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "azure_bluet_to_light_gray_dye_cutting"),
            ItemService.generate(Material.AZURE_BLUET),
            listOf(Pair(ItemService.generate(Material.LIGHT_GRAY_DYE, 2), 1.0)),
            CuttingBoardToolTags.KNIVES
        )
    ),
    BLUE_ORCHID_TO_LIGHT_BLUE_DYE(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "blue_orchid_to_light_blue_dye_cutting"),
            ItemService.generate(Material.BLUE_ORCHID),
            listOf(Pair(ItemService.generate(Material.LIGHT_BLUE_DYE, 2), 1.0)),
            CuttingBoardToolTags.KNIVES
        )
    ),
    CORNFLOWER_TO_BLUE_DYE(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "cornflower_to_blue_dye_cutting"),
            ItemService.generate(Material.CORNFLOWER),
            listOf(Pair(ItemService.generate(Material.BLUE_DYE, 2), 1.0)),
            CuttingBoardToolTags.KNIVES
        )
    ),
    DANDELION_TO_YELLOW_DYE(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "dandelion_to_yellow_dye_cutting"),
            ItemService.generate(Material.DANDELION),
            listOf(Pair(ItemService.generate(Material.YELLOW_DYE, 2), 1.0)),
            CuttingBoardToolTags.KNIVES
        )
    ),
    LILY_OF_THE_VALLEY_TO_WHITE_DYE(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "lily_of_the_valley_to_white_dye_cutting"),
            ItemService.generate(Material.LILY_OF_THE_VALLEY),
            listOf(Pair(ItemService.generate(Material.WHITE_DYE, 2), 1.0)),
            CuttingBoardToolTags.KNIVES
        )
    ),
    ORANGE_TULIP_TO_ORANGE_DYE(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "orange_tulip_to_orange_dye_cutting"),
            ItemService.generate(Material.ORANGE_TULIP),
            listOf(Pair(ItemService.generate(Material.ORANGE_DYE, 2), 1.0)),
            CuttingBoardToolTags.KNIVES
        )
    ),
    OXEYE_DAISY_TO_LIGHT_GRAY_DYE(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "oxeye_daisy_to_light_gray_dye_cutting"),
            ItemService.generate(Material.OXEYE_DAISY),
            listOf(Pair(ItemService.generate(Material.LIGHT_GRAY_DYE, 2), 1.0)),
            CuttingBoardToolTags.KNIVES
        )
    ),
    PINK_TULIP_TO_PINK_DYE(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "pink_tulip_to_pink_dye_cutting"),
            ItemService.generate(Material.PINK_TULIP),
            listOf(Pair(ItemService.generate(Material.PINK_DYE, 2), 1.0)),
            CuttingBoardToolTags.KNIVES
        )
    ),
    POPPY_TO_RED_DYE(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "poppy_to_red_dye_cutting"),
            ItemService.generate(Material.POPPY),
            listOf(Pair(ItemService.generate(Material.RED_DYE, 2), 1.0)),
            CuttingBoardToolTags.KNIVES
        )
    ),
    RED_TULIP_TO_RED_DYE(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "red_tulip_to_red_dye_cutting"),
            ItemService.generate(Material.RED_TULIP),
            listOf(Pair(ItemService.generate(Material.RED_DYE, 2), 1.0)),
            CuttingBoardToolTags.KNIVES
        )
    ),
    TORCHFLOWER_TO_ORANGE_DYE(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "torchflower_to_orange_dye_cutting"),
            ItemService.generate(Material.TORCHFLOWER),
            listOf(Pair(ItemService.generate(Material.ORANGE_DYE, 2), 1.0)),
            CuttingBoardToolTags.KNIVES
        )
    ),
    WHITE_TULIP_TO_LIGHT_GRAY_DYE(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "white_tulip_to_light_gray_dye_cutting"),
            ItemService.generate(Material.WHITE_TULIP),
            listOf(Pair(ItemService.generate(Material.LIGHT_GRAY_DYE, 2), 1.0)),
            CuttingBoardToolTags.KNIVES
        )
    ),
    WITHER_ROSE_TO_BLACK_DYE(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "wither_rose_to_black_dye_cutting"),
            ItemService.generate(Material.WITHER_ROSE),
            listOf(Pair(ItemService.generate(Material.BLACK_DYE, 2), 1.0)),
            CuttingBoardToolTags.KNIVES
        )
    ),
    INK_SAC_TO_BLACK_DYE(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "ink_sac_to_black_dye_cutting"),
            ItemService.generate(Material.INK_SAC),
            listOf(Pair(ItemService.generate(Material.BLACK_DYE, 2), 1.0)),
            CuttingBoardToolTags.KNIVES
        )
    ),
    VIOLET_TO_PURPLE_DYE(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "violet_to_purple_dye_cutting"),
            ItemService.generate(CustomItemType.VIOLET),
            listOf(Pair(ItemService.generate(Material.PURPLE_DYE, 2), 1.0)),
            CuttingBoardToolTags.KNIVES
        )
    ),
    PUFFBLOOM_TO_WHITE_DYE(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "puffbloom_to_white_dye_cutting"),
            ItemService.generate(CustomItemType.PUFFBLOOM),
            listOf(Pair(ItemService.generate(Material.WHITE_DYE, 2), 1.0)),
            CuttingBoardToolTags.KNIVES
        )
    ),

    // Terrain processing (shovels)
    CLAY_TO_CLAY_BALLS(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "clay_to_clay_balls_cutting"),
            ItemService.generate(Material.CLAY),
            listOf(
                Pair(ItemService.generate(Material.CLAY_BALL, 4), 1.0),
            ),
            CuttingBoardToolTags.SHOVELS
        )
    ),
    GRAVEL_SIFTING(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "gravel_sifting_cutting"),
            ItemService.generate(Material.GRAVEL),
            listOf(
                Pair(ItemService.generate(Material.GRAVEL), 1.0),
                Pair(ItemService.generate(Material.FLINT), 0.1),
            ),
            CuttingBoardToolTags.SHOVELS
        )
    ),

    OAK_LOG_STRIPPING(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "oak_log_stripping_cutting"),
            ItemService.generate(Material.OAK_LOG),
            listOf(
                Pair(ItemService.generate(Material.STRIPPED_OAK_LOG), 1.0)
            ),
            CuttingBoardToolTags.AXES
        )
    ),
    OAK_WOOD_STRIPPING(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "oak_wood_stripping_cutting"),
            ItemService.generate(Material.OAK_WOOD),
            listOf(
                Pair(ItemService.generate(Material.STRIPPED_OAK_WOOD), 1.0)
            ),
            CuttingBoardToolTags.AXES
        )
    ),
    BIRCH_LOG_STRIPPING(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "birch_log_stripping_cutting"),
            ItemService.generate(Material.BIRCH_LOG),
            listOf(
                Pair(ItemService.generate(Material.STRIPPED_BIRCH_LOG), 1.0)
            ),
            CuttingBoardToolTags.AXES
        )
    ),
    BIRCH_WOOD_STRIPPING(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "birch_wood_stripping_cutting"),
            ItemService.generate(Material.BIRCH_WOOD),
            listOf(
                Pair(ItemService.generate(Material.STRIPPED_BIRCH_WOOD), 1.0)
            ),
            CuttingBoardToolTags.AXES
        )
    ),
    SPRUCE_LOG_STRIPPING(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "spruce_log_stripping_cutting"),
            ItemService.generate(Material.SPRUCE_LOG),
            listOf(
                Pair(ItemService.generate(Material.STRIPPED_SPRUCE_LOG), 1.0)
            ),
            CuttingBoardToolTags.AXES
        )
    ),
    SPRUCE_WOOD_STRIPPING(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "spruce_wood_stripping_cutting"),
            ItemService.generate(Material.SPRUCE_WOOD),
            listOf(
                Pair(ItemService.generate(Material.STRIPPED_SPRUCE_WOOD), 1.0)
            ),
            CuttingBoardToolTags.AXES
        )
    ),
    JUNGLE_LOG_STRIPPING(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "jungle_log_stripping_cutting"),
            ItemService.generate(Material.JUNGLE_LOG),
            listOf(
                Pair(ItemService.generate(Material.STRIPPED_JUNGLE_LOG), 1.0)
            ),
            CuttingBoardToolTags.AXES
        )
    ),
    JUNGLE_WOOD_STRIPPING(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "jungle_wood_stripping_cutting"),
            ItemService.generate(Material.JUNGLE_WOOD),
            listOf(
                Pair(ItemService.generate(Material.STRIPPED_JUNGLE_WOOD), 1.0)
            ),
            CuttingBoardToolTags.AXES
        )
    ),
    DARK_OAK_LOG_STRIPPING(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "dark_oak_log_stripping_cutting"),
            ItemService.generate(Material.DARK_OAK_LOG),
            listOf(
                Pair(ItemService.generate(Material.STRIPPED_DARK_OAK_LOG), 1.0)
            ),
            CuttingBoardToolTags.AXES
        )
    ),
    DARK_OAK_WOOD_STRIPPING(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "dark_oak_wood_stripping_cutting"),
            ItemService.generate(Material.DARK_OAK_WOOD),
            listOf(
                Pair(ItemService.generate(Material.STRIPPED_DARK_OAK_WOOD), 1.0)
            ),
            CuttingBoardToolTags.AXES
        )
    ),
    ACACIA_LOG_STRIPPING(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "acacia_log_stripping_cutting"),
            ItemService.generate(Material.ACACIA_LOG),
            listOf(
                Pair(ItemService.generate(Material.STRIPPED_ACACIA_LOG), 1.0)
            ),
            CuttingBoardToolTags.AXES
        )
    ),
    ACACIA_WOOD_STRIPPING(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "acacia_wood_stripping_cutting"),
            ItemService.generate(Material.ACACIA_WOOD),
            listOf(
                Pair(ItemService.generate(Material.STRIPPED_ACACIA_WOOD), 1.0)
            ),
            CuttingBoardToolTags.AXES
        )
    ),
    CHERRY_LOG_STRIPPING(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "cherry_log_stripping_cutting"),
            ItemService.generate(Material.CHERRY_LOG),
            listOf(
                Pair(ItemService.generate(Material.STRIPPED_CHERRY_LOG), 1.0)
            ),
            CuttingBoardToolTags.AXES
        )
    ),
    CHERRY_WOOD_STRIPPING(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "cherry_wood_stripping_cutting"),
            ItemService.generate(Material.CHERRY_WOOD),
            listOf(
                Pair(ItemService.generate(Material.STRIPPED_CHERRY_WOOD), 1.0)
            ),
            CuttingBoardToolTags.AXES
        )
    ),
    MANGROVE_LOG_STRIPPING(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "mangrove_log_stripping_cutting"),
            ItemService.generate(Material.MANGROVE_LOG),
            listOf(
                Pair(ItemService.generate(Material.STRIPPED_MANGROVE_LOG), 1.0)
            ),
            CuttingBoardToolTags.AXES
        )
    ),
    MANGROVE_WOOD_STRIPPING(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "mangrove_wood_stripping_cutting"),
            ItemService.generate(Material.MANGROVE_WOOD),
            listOf(
                Pair(ItemService.generate(Material.STRIPPED_MANGROVE_WOOD), 1.0)
            ),
            CuttingBoardToolTags.AXES
        )
    ),
    CRIMSON_STEM_STRIPPING(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "crimson_stem_stripping_cutting"),
            ItemService.generate(Material.CRIMSON_STEM),
            listOf(
                Pair(ItemService.generate(Material.STRIPPED_CRIMSON_STEM), 1.0)
            ),
            CuttingBoardToolTags.AXES
        )
    ),
    CRIMSON_HYPHAE_STRIPPING(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "crimson_hyphae_stripping_cutting"),
            ItemService.generate(Material.CRIMSON_HYPHAE),
            listOf(
                Pair(ItemService.generate(Material.STRIPPED_CRIMSON_HYPHAE), 1.0)
            ),
            CuttingBoardToolTags.AXES
        )
    ),
    WARPED_STEM_STRIPPING(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "warped_stem_stripping_cutting"),
            ItemService.generate(Material.WARPED_STEM),
            listOf(
                Pair(ItemService.generate(Material.STRIPPED_WARPED_STEM), 1.0)
            ),
            CuttingBoardToolTags.AXES
        )
    ),
    WARPED_HYPHAE_STRIPPING(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "warped_hyphae_stripping_cutting"),
            ItemService.generate(Material.WARPED_HYPHAE),
            listOf(
                Pair(ItemService.generate(Material.STRIPPED_WARPED_HYPHAE), 1.0)
            ),
            CuttingBoardToolTags.AXES
        )
    ),
    BAMBOO_BLOCK_STRIPPING(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "bamboo_block_stripping_cutting"),
            ItemService.generate(Material.BAMBOO_BLOCK),
            listOf(
                Pair(ItemService.generate(Material.STRIPPED_BAMBOO_BLOCK), 1.0),
                Pair(ItemService.generate(CustomItemType.STRAW), 1.0),
            ),
            CuttingBoardToolTags.AXES
        )
    ),
    SKYROOT_LOG_STRIPPING(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "skyroot_log_stripping_cutting"),
            ItemService.generate(CustomItemType.SKYROOT_LOG),
            listOf(
                Pair(ItemService.generate(CustomItemType.STRIPPED_SKYROOT_LOG), 1.0)
            ),
            CuttingBoardToolTags.AXES
        )
    ),
    SKYROOT_WOOD_STRIPPING(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "skyroot_wood_stripping_cutting"),
            ItemService.generate(CustomItemType.SKYROOT_LOG),
            listOf(
                Pair(ItemService.generate(CustomItemType.STRIPPED_SKYROOT_WOOD), 1.0)
            ),
            CuttingBoardToolTags.AXES
        )
    ),
    GOLDEN_OAK_LOG_STRIPPING(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "golden_oak_log_stripping_cutting"),
            ItemService.generate(CustomItemType.SKYROOT_LOG),
            listOf(
                Pair(ItemService.generate(CustomItemType.STRIPPED_GOLDEN_OAK_LOG), 1.0)
            ),
            CuttingBoardToolTags.AXES
        )
    ),
    GOLDEN_OAK_WOOD_STRIPPING(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "golden_oak_wood_stripping_cutting"),
            ItemService.generate(CustomItemType.SKYROOT_LOG),
            listOf(
                Pair(ItemService.generate(CustomItemType.STRIPPED_GOLDEN_OAK_WOOD), 1.0)
            ),
            CuttingBoardToolTags.AXES
        )
    ),
    BINARY_LOG_STRIPPING(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "binary_log_stripping_cutting"),
            ItemService.generate(CustomItemType.BINARY_LOG),
            listOf(
                Pair(ItemService.generate(CustomItemType.STRIPPED_BINARY_LOG), 1.0)
            ),
            CuttingBoardToolTags.AXES
        )
    ),
    BINARY_WOOD_STRIPPING(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.plugin, "binary_wood_stripping_cutting"),
            ItemService.generate(CustomItemType.BINARY_LOG),
            listOf(
                Pair(ItemService.generate(CustomItemType.STRIPPED_BINARY_WOOD), 1.0)
            ),
            CuttingBoardToolTags.AXES
        )
    ),
}
