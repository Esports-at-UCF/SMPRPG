package xyz.devvydont.smprpg.recipe.cuttingboard

import net.momirealms.craftengine.core.util.Key
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe

class CuttingBoardRecipe(val key: NamespacedKey, val input: ItemStack, val recipeResult: List<Pair<ItemStack, Double>>, val processToolTag: Key) :
    Recipe {

    override fun getResult(): ItemStack {
        throw IllegalStateException("getResult should not be used on CuttingBoardRecipes. Use recipeResult instead.")
    }
}