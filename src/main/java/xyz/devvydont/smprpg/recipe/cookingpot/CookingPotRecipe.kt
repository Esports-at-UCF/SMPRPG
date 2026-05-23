package xyz.devvydont.smprpg.recipe.cookingpot

import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import xyz.devvydont.smprpg.block.entity.CookingPotBlockEntityController
import xyz.devvydont.smprpg.block.entity.CookingPotBlockEntityController.Companion.INGREDIENT_SLOTS
import xyz.devvydont.smprpg.skills.utils.SkillExperienceReward
import xyz.devvydont.smprpg.util.extensions.takeIfPresent

class CookingPotRecipe(val key: NamespacedKey,
                       val inputs: List<ItemStack>,
                       val cookTime: Int,
                       val recipeResult: ItemStack,
                       val skillXpReward: SkillExperienceReward?,
                       val platingItem: ItemStack?) : Recipe {

    constructor(key: NamespacedKey, inputs: List<ItemStack>, cookTime: Int, recipeResult: ItemStack, skillXpReward: SkillExperienceReward?) : this(key, inputs, cookTime, recipeResult, skillXpReward, null)
    constructor(key: NamespacedKey, inputs: List<ItemStack>, cookTime: Int, recipeResult: ItemStack) : this(key, inputs, cookTime, recipeResult, null, null)

    override fun getResult(): ItemStack {
        return recipeResult
    }

    companion object {
        fun getFirstRecipeMatch(pot: CookingPotBlockEntityController) : CookingPotRecipe? {
            var isValidRecipe: Boolean
            val potIngs = mutableListOf<ItemStack>()
            for (slot in INGREDIENT_SLOTS) {
                val ing = pot.inventory()?.getItem(slot) ?: continue
                if (!ing.isEmpty) {
                    potIngs.add(ing)
                }
            }
            for (entry in CookingPotRecipes.entries) {
                val currRecipe = entry.recipe
                val workingIngs = entry.recipe.inputs.map { it.clone() }
                var i = 0
                for (recipeIng in currRecipe.inputs) {
                    for (potIng in potIngs) {
                        if (potIng.isSimilar(recipeIng)) {
                            workingIngs[i].amount -= potIng.amount
                        }
                    }
                    i++
                }
                isValidRecipe = true
                if (currRecipe.platingItem != null) {
                    val platingItem = pot.inventory()?.getItem(CookingPotBlockEntityController.PLATING_SLOT) ?: continue
                    if (!platingItem.isSimilar(currRecipe.platingItem))
                        continue
                }
                for (workingIng in workingIngs) {
                    if (workingIng.amount > 0) {
                        isValidRecipe = false
                    }
                }
                if (isValidRecipe)
                    return currRecipe
            }
            return null
        }

        fun takeIngredients(pot: CookingPotBlockEntityController, recipe: CookingPotRecipe) {
            val inv = pot.inventory()!!
            for (ing in recipe.inputs) {
                inv.takeIfPresent(ing)
            }

            // We can assume the plating item exists if this method is being called.
            if (recipe.platingItem != null)
                inv.getItem(CookingPotBlockEntityController.PLATING_SLOT)!!.amount--
        }
    }
}