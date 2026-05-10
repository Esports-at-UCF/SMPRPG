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
    )
}