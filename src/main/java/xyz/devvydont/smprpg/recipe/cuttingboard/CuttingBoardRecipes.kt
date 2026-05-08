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
    TEST_WHEAT_TO_POTATO(
        CuttingBoardRecipe(
            NamespacedKey(SMPRPG.Companion.plugin, "test_wheat_to_potato_cutting"),
            ItemService.generate(Material.WHEAT),
            ItemService.generate(Material.POTATO, 2),
            CuttingBoardToolTags.KNIVES
        )
    )
}