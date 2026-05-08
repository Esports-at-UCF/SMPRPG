package xyz.devvydont.smprpg.recipe.freezer

import org.bukkit.Material
import org.bukkit.NamespacedKey
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.services.ItemService

enum class FreezerRecipes(val recipe: FreezerRecipe) {
    WATER_BUCKET_TO_ICE(FreezerRecipe(
        NamespacedKey(SMPRPG.plugin, "water_bucket_to_ice_freezing"),
        ItemService.generate(Material.WATER_BUCKET),
        200,
        ItemService.generate(Material.ICE)
    )),
    LAVA_BUCKET_TO_OBSIDIAN(FreezerRecipe(
        NamespacedKey(SMPRPG.plugin, "water_bucket_to_ice_freezing"),
        ItemService.generate(Material.LAVA_BUCKET),
        200,
        ItemService.generate(Material.OBSIDIAN)
    )),
    ICE_TO_PACKED_ICE(FreezerRecipe(
        NamespacedKey(SMPRPG.plugin, "ice_to_packed_ice_freezing"),
        ItemService.generate(Material.ICE),
        300,
        ItemService.generate(Material.PACKED_ICE)
    )),
    PACKED_ICE_TO_BLUE_ICE(FreezerRecipe(
        NamespacedKey(SMPRPG.plugin, "packed_ice_to_blue_ice_freezing"),
        ItemService.generate(Material.PACKED_ICE),
        500,
        ItemService.generate(Material.BLUE_ICE)
    )),
    COLD_AERCLOUD_TO_BLUE_AERCLOUD(FreezerRecipe(
        NamespacedKey(SMPRPG.plugin, "cold_aercloud_to_blue_aercloud_freezing"),
        ItemService.generate(CustomItemType.COLD_AERCLOUD),
        400,
        ItemService.generate(CustomItemType.BLUE_AERCLOUD)
    )),
    MAGMA_CREAM_TO_SLIMEBALL(FreezerRecipe(
        NamespacedKey(SMPRPG.plugin, "magma_cream_to_slimeball_freezing"),
        ItemService.generate(Material.MAGMA_CREAM),
        100,
        ItemService.generate(Material.SLIME_BALL)
    )),
    SCORCHING_STRING_TO_STRING(FreezerRecipe(
        NamespacedKey(SMPRPG.plugin, "scorching_string_to_string_freezing"),
        ItemService.generate(CustomItemType.SCORCHING_STRING),
        1600,
        ItemService.generate(CustomItemType.ENCHANTED_STRING)
    )),
    BLAZE_ROD_TO_BREEZE_ROD(FreezerRecipe(
        NamespacedKey(SMPRPG.plugin, "blaze_rod_to_breeze_rod_freezing"),
        ItemService.generate(Material.BLAZE_ROD),
        800,
        ItemService.generate(Material.BREEZE_ROD)
    )),
    BOILING_INGOT_TO_ENCHANTED_IRON(FreezerRecipe(
        NamespacedKey(SMPRPG.plugin, "boiling_ingot_to_enchanted_iron_freezing"),
        ItemService.generate(CustomItemType.BOILING_INGOT),
        1600,
        ItemService.generate(CustomItemType.ENCHANTED_IRON)
    ))
}