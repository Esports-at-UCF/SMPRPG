package xyz.devvydont.smprpg.recipe.freezer

import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe

class FreezerRecipe(val key: NamespacedKey, val input: ItemStack, val freezeTime: Int, val recipeResult: ItemStack) : Recipe {

    override fun getResult(): ItemStack {
        return recipeResult
    }
}