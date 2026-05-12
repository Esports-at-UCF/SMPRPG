package xyz.devvydont.smprpg.recipe.cuttingboard

import net.momirealms.craftengine.core.util.Key
import org.bukkit.Material
import org.bukkit.NamespacedKey
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.services.ItemService

class CuttingBoardToolTags() {

    companion object {
        val KNIVES = Key.of("smprpg:knives")
    }
}

enum class CuttingBoardRecipes(val recipe: CuttingBoardRecipe) {
    BEEF_TO_GROUND_BEEF(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.Companion.plugin, "beef_to_ground_beef_cutting"),
            ItemService.generate(Material.BEEF),
            listOf(
                Pair(ItemService.generate(CustomItemType.GROUND_BEEF, 2), 1.0),
            ),
            CuttingBoardToolTags.KNIVES
        )
    ),
    STEAK_TO_STEAK_STRIPS(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.Companion.plugin, "steak_to_steak_strips_cutting"),
            ItemService.generate(Material.COOKED_BEEF),
            listOf(
                Pair(ItemService.generate(CustomItemType.STEAK_STRIPS, 2), 1.0),
            ),
            CuttingBoardToolTags.KNIVES
        )
    ),
    CABBAGE_TO_CABBAGE_LEAVES(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.Companion.plugin, "cabbage_to_cabbage_leaves_cutting"),
            ItemService.generate(CustomItemType.CABBAGE),
            listOf(
                Pair(ItemService.generate(CustomItemType.CABBAGE_LEAF, 2), 1.0),
            ),
            CuttingBoardToolTags.KNIVES
        )
    ),
    RICE_PANICLE_TO_RICE(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.Companion.plugin, "cabbage_to_cabbage_leaves_cutting"),
            ItemService.generate(CustomItemType.RICE_PANICLE),
            listOf(
                Pair(ItemService.generate(CustomItemType.RICE), 1.0),
                Pair(ItemService.generate(CustomItemType.STRAW), 0.5),
            ),
            CuttingBoardToolTags.KNIVES
        )
    )
}