package xyz.devvydont.smprpg.items.interfaces

import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe

/**
 * Unifies the notion that "this blueprint contributes one or more crafting recipes, and each of those recipes is
 * revealed in the recipe book once the player acquires certain items".
 *
 * Both directly craftable items ([ICraftable]) and compression chain members ([ICompressible]) expose their recipes
 * through this single contract so that registration and unlock-discovery have exactly one code path. This avoids the
 * class of bug where two parallel registration flows drift apart and one silently stops registering unlock links.
 */
interface IRecipeProvider {

    /**
     * A recipe paired with the items whose acquisition should reveal it in the recipe book.
     */
    class UnlockableRecipe(val recipe: Recipe, val unlockedBy: Collection<ItemStack>)

    /**
     * Every recipe this blueprint contributes to the server, along with the items that unlock each one.
     * May be empty if the blueprint contributes no recipes in its current state (e.g. the end of a compression chain).
     */
    fun getProvidedRecipes(): Collection<UnlockableRecipe>
}
