package xyz.devvydont.smprpg.recipe.cookingpot

import org.bukkit.Material
import org.bukkit.NamespacedKey
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.recipe.freezer.FreezerRecipe
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.skills.SkillType
import xyz.devvydont.smprpg.skills.utils.SkillExperienceReward

enum class CookingPotRecipes(val recipe: CookingPotRecipe) {
    BEEF_STEW(
        CookingPotRecipe(
            NamespacedKey(SMPRPG.plugin, "beef_stew_cooking_pot"),
            listOf(
                ItemService.generate(Material.CARROT),
                ItemService.generate(Material.POTATO),
                ItemService.generate(CustomItemType.ONION),
                ItemService.generate(Material.BEEF)
            ),
            200,
            ItemService.generate(CustomItemType.BEEF_STEW),
            SkillExperienceReward().add(SkillType.FARMING, 50),
            ItemService.generate(Material.BOWL)
        )
    ),
    COOKED_RICE(
        CookingPotRecipe(
            NamespacedKey(SMPRPG.plugin, "cooked_rice_cooking_pot"),
            listOf(
                ItemService.generate(CustomItemType.RICE)
            ),
            200,
            ItemService.generate(CustomItemType.COOKED_RICE),
            SkillExperienceReward().add(SkillType.FARMING, 20),
            ItemService.generate(Material.BOWL)
        )
    )
}