package xyz.devvydont.smprpg.recipe.cookingpot

import org.bukkit.Material
import org.bukkit.NamespacedKey
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.skills.SkillType
import xyz.devvydont.smprpg.skills.utils.SkillExperienceReward

enum class CookingPotRecipes(val recipe: CookingPotRecipe) {

    // Stews and soups
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
    MUSHROOM_STEW(
        CookingPotRecipe(
            NamespacedKey(SMPRPG.plugin, "mushroom_stew_cooking_pot"),
            listOf(
                ItemService.generate(Material.BROWN_MUSHROOM),
                ItemService.generate(Material.RED_MUSHROOM)
            ),
            200,
            ItemService.generate(Material.MUSHROOM_STEW),
            SkillExperienceReward().add(SkillType.FARMING, 50),
            ItemService.generate(Material.BOWL)
        )
    ),
    BEETROOT_SOUP(
        CookingPotRecipe(
            NamespacedKey(SMPRPG.plugin, "beetroot_soup_cooking_pot"),
            listOf(
                ItemService.generate(Material.BEETROOT),
                ItemService.generate(Material.BEETROOT),
                ItemService.generate(Material.BEETROOT)
            ),
            200,
            ItemService.generate(Material.BEETROOT_SOUP),
            SkillExperienceReward().add(SkillType.FARMING, 50),
            ItemService.generate(Material.BOWL)
        )
    ),
    RABBIT_STEW(
        CookingPotRecipe(
            NamespacedKey(SMPRPG.plugin, "rabbit_stew_cooking_pot"),
            listOf(
                ItemService.generate(Material.POTATO),
                ItemService.generate(Material.RABBIT),
                ItemService.generate(Material.CARROT),
                ItemService.generate(Material.BROWN_MUSHROOM)
            ),
            200,
            ItemService.generate(Material.RABBIT_STEW),
            SkillExperienceReward().add(SkillType.FARMING, 50),
            ItemService.generate(Material.BOWL)
        )
    ),
    CHICKEN_SOUP(
        CookingPotRecipe(
            NamespacedKey(SMPRPG.plugin, "chicken_soup_cooking_pot"),
            listOf(
                ItemService.generate(Material.CHICKEN),
                ItemService.generate(Material.CARROT),
                ItemService.generate(CustomItemType.CABBAGE_LEAF),
                ItemService.generate(CustomItemType.TOMATO)
            ),
            200,
            ItemService.generate(CustomItemType.CHICKEN_SOUP),
            SkillExperienceReward().add(SkillType.FARMING, 50),
            ItemService.generate(Material.BOWL)
        )
    ),
    ONION_SOUP(
        CookingPotRecipe(
            NamespacedKey(SMPRPG.plugin, "onion_soup_cooking_pot"),
            listOf(
                ItemService.generate(CustomItemType.ONION),
                ItemService.generate(CustomItemType.ONION),
                ItemService.generate(Material.BREAD),
                ItemService.generate(Material.MILK_BUCKET)
            ),
            200,
            ItemService.generate(CustomItemType.ONION_SOUP),
            SkillExperienceReward().add(SkillType.FARMING, 50),
            ItemService.generate(Material.BOWL)
        )
    ),
    PUMPKIN_SOUP(
        CookingPotRecipe(
            NamespacedKey(SMPRPG.plugin, "pumpkin_soup_cooking_pot"),
            listOf(
                ItemService.generate(CustomItemType.PUMPKIN_SLICE),
                ItemService.generate(CustomItemType.CABBAGE_LEAF),
                ItemService.generate(Material.PORKCHOP),
                ItemService.generate(Material.MILK_BUCKET)
            ),
            200,
            ItemService.generate(CustomItemType.PUMPKIN_SOUP),
            SkillExperienceReward().add(SkillType.FARMING, 50),
            ItemService.generate(Material.BOWL)
        )
    ),
    FISH_STEW(
        CookingPotRecipe(
            NamespacedKey(SMPRPG.plugin, "fish_stew_cooking_pot"),
            listOf(
                ItemService.generate(Material.SALMON),
                ItemService.generate(CustomItemType.TOMATO_SAUCE),
                ItemService.generate(CustomItemType.ONION)
            ),
            200,
            ItemService.generate(CustomItemType.FISH_STEW),
            SkillExperienceReward().add(SkillType.FARMING, 50),
            ItemService.generate(Material.BOWL)
        )
    ),
    BAKED_COD_STEW(
        CookingPotRecipe(
            NamespacedKey(SMPRPG.plugin, "baked_cod_stew_cooking_pot"),
            listOf(
                ItemService.generate(Material.COD),
                ItemService.generate(Material.POTATO),
                ItemService.generate(Material.EGG),
                ItemService.generate(CustomItemType.TOMATO)
            ),
            200,
            ItemService.generate(CustomItemType.BAKED_COD_STEW),
            SkillExperienceReward().add(SkillType.FARMING, 50),
            ItemService.generate(Material.BOWL)
        )
    ),
    VEGETABLE_SOUP(
        CookingPotRecipe(
            NamespacedKey(SMPRPG.plugin, "vegetable_soup_cooking_pot"),
            listOf(
                ItemService.generate(Material.CARROT),
                ItemService.generate(Material.POTATO),
                ItemService.generate(Material.BEETROOT),
                ItemService.generate(CustomItemType.CABBAGE_LEAF)
            ),
            200,
            ItemService.generate(CustomItemType.VEGETABLE_SOUP),
            SkillExperienceReward().add(SkillType.FARMING, 50),
            ItemService.generate(Material.BOWL)
        )
    ),
    BONE_BROTH(
        CookingPotRecipe(
            NamespacedKey(SMPRPG.plugin, "bone_broth_cooking_pot"),
            listOf(
                ItemService.generate(Material.BONE),
                ItemService.generate(Material.BROWN_MUSHROOM)
            ),
            200,
            ItemService.generate(CustomItemType.BONE_BROTH),
            SkillExperienceReward().add(SkillType.FARMING, 20),
            ItemService.generate(Material.BOWL)
        )
    ),

    // Rice dishes
    COOKED_RICE(
        CookingPotRecipe(
            NamespacedKey(SMPRPG.plugin, "cooked_rice_cooking_pot"),
            listOf(
                ItemService.generate(CustomItemType.RICE)
            ),
            100,
            ItemService.generate(CustomItemType.COOKED_RICE),
            SkillExperienceReward().add(SkillType.FARMING, 20),
            ItemService.generate(Material.BOWL)
        )
    ),
    FRIED_RICE(
        CookingPotRecipe(
            NamespacedKey(SMPRPG.plugin, "fried_rice_cooking_pot"),
            listOf(
                ItemService.generate(CustomItemType.RICE),
                ItemService.generate(Material.EGG),
                ItemService.generate(Material.CARROT),
                ItemService.generate(CustomItemType.ONION)
            ),
            200,
            ItemService.generate(CustomItemType.FRIED_RICE),
            SkillExperienceReward().add(SkillType.FARMING, 50),
            ItemService.generate(Material.BOWL)
        )
    ),
    MUSHROOM_RICE(
        CookingPotRecipe(
            NamespacedKey(SMPRPG.plugin, "mushroom_rice_cooking_pot"),
            listOf(
                ItemService.generate(Material.BROWN_MUSHROOM),
                ItemService.generate(Material.RED_MUSHROOM),
                ItemService.generate(CustomItemType.RICE),
                ItemService.generate(Material.CARROT)
            ),
            200,
            ItemService.generate(CustomItemType.MUSHROOM_RICE),
            SkillExperienceReward().add(SkillType.FARMING, 50),
            ItemService.generate(Material.BOWL)
        )
    ),

    // Pasta dishes
    TOMATO_SAUCE(
        CookingPotRecipe(
            NamespacedKey(SMPRPG.plugin, "tomato_sauce_cooking_pot"),
            listOf(
                ItemService.generate(CustomItemType.TOMATO),
                ItemService.generate(CustomItemType.TOMATO)
            ),
            100,
            ItemService.generate(CustomItemType.TOMATO_SAUCE),
            SkillExperienceReward().add(SkillType.FARMING, 20)
        )
    ),
    PASTA_WITH_MEATBALLS(
        CookingPotRecipe(
            NamespacedKey(SMPRPG.plugin, "pasta_with_meatballs_cooking_pot"),
            listOf(
                ItemService.generate(CustomItemType.GROUND_BEEF),
                ItemService.generate(CustomItemType.RAW_PASTA),
                ItemService.generate(CustomItemType.TOMATO_SAUCE)
            ),
            200,
            ItemService.generate(CustomItemType.PASTA_WITH_MEATBALLS),
            SkillExperienceReward().add(SkillType.FARMING, 50)
        )
    ),
    PASTA_WITH_MUTTON_CHOP(
        CookingPotRecipe(
            NamespacedKey(SMPRPG.plugin, "pasta_with_mutton_chop_cooking_pot"),
            listOf(
                ItemService.generate(Material.MUTTON),
                ItemService.generate(CustomItemType.RAW_PASTA),
                ItemService.generate(CustomItemType.TOMATO_SAUCE)
            ),
            200,
            ItemService.generate(CustomItemType.PASTA_WITH_MUTTON_CHOP),
            SkillExperienceReward().add(SkillType.FARMING, 50)
        )
    ),
    SQUID_INK_PASTA(
        CookingPotRecipe(
            NamespacedKey(SMPRPG.plugin, "squid_ink_pasta_cooking_pot"),
            listOf(
                ItemService.generate(Material.SALMON),
                ItemService.generate(CustomItemType.RAW_PASTA),
                ItemService.generate(CustomItemType.TOMATO),
                ItemService.generate(Material.INK_SAC)
            ),
            200,
            ItemService.generate(CustomItemType.SQUID_INK_PASTA),
            SkillExperienceReward().add(SkillType.FARMING, 50)
        )
    ),
    NOODLE_SOUP(
        CookingPotRecipe(
            NamespacedKey(SMPRPG.plugin, "noodle_soup_cooking_pot"),
            listOf(
                ItemService.generate(CustomItemType.RAW_PASTA),
                ItemService.generate(Material.EGG),
                ItemService.generate(Material.DRIED_KELP),
                ItemService.generate(Material.PORKCHOP)
            ),
            200,
            ItemService.generate(CustomItemType.NOODLE_SOUP),
            SkillExperienceReward().add(SkillType.FARMING, 50),
            ItemService.generate(Material.BOWL)
        )
    ),
    VEGETABLE_NOODLES(
        CookingPotRecipe(
            NamespacedKey(SMPRPG.plugin, "vegetable_noodles_cooking_pot"),
            listOf(
                ItemService.generate(Material.CARROT),
                ItemService.generate(Material.BROWN_MUSHROOM),
                ItemService.generate(CustomItemType.RAW_PASTA),
                ItemService.generate(CustomItemType.CABBAGE_LEAF),
                ItemService.generate(CustomItemType.TOMATO)
            ),
            200,
            ItemService.generate(CustomItemType.VEGETABLE_NOODLES),
            SkillExperienceReward().add(SkillType.FARMING, 50)
        )
    ),

    // Other dishes
    RATATOUILLE(
        CookingPotRecipe(
            NamespacedKey(SMPRPG.plugin, "ratatouille_cooking_pot"),
            listOf(
                ItemService.generate(CustomItemType.TOMATO),
                ItemService.generate(CustomItemType.ONION),
                ItemService.generate(Material.BEETROOT),
                ItemService.generate(CustomItemType.CABBAGE_LEAF)
            ),
            200,
            ItemService.generate(CustomItemType.RATATOUILLE),
            SkillExperienceReward().add(SkillType.FARMING, 50)
        )
    ),
    CABBAGE_ROLLS(
        CookingPotRecipe(
            NamespacedKey(SMPRPG.plugin, "cabbage_rolls_cooking_pot"),
            listOf(
                ItemService.generate(CustomItemType.CABBAGE),
                ItemService.generate(Material.BEEF)
            ),
            100,
            ItemService.generate(CustomItemType.CABBAGE_ROLLS),
            SkillExperienceReward().add(SkillType.FARMING, 20)
        )
    ),
    DUMPLINGS(
        CookingPotRecipe(
            NamespacedKey(SMPRPG.plugin, "dumplings_cooking_pot"),
            listOf(
                ItemService.generate(CustomItemType.WHEAT_DOUGH),
                ItemService.generate(CustomItemType.CABBAGE),
                ItemService.generate(CustomItemType.ONION),
                ItemService.generate(Material.BEEF)
            ),
            200,
            ItemService.generate(CustomItemType.DUMPLINGS, 2),
            SkillExperienceReward().add(SkillType.FARMING, 50)
        )
    ),
    // Pumpkin goes in the plating slot and becomes the stuffed pumpkin's vessel
    STUFFED_PUMPKIN(
        CookingPotRecipe(
            NamespacedKey(SMPRPG.plugin, "stuffed_pumpkin_cooking_pot"),
            listOf(
                ItemService.generate(CustomItemType.RICE),
                ItemService.generate(CustomItemType.ONION),
                ItemService.generate(Material.BROWN_MUSHROOM),
                ItemService.generate(Material.POTATO),
                ItemService.generate(Material.SWEET_BERRIES),
                ItemService.generate(CustomItemType.TOMATO)
            ),
            400,
            ItemService.generate(CustomItemType.STUFFED_PUMPKIN),
            SkillExperienceReward().add(SkillType.FARMING, 100),
            ItemService.generate(Material.PUMPKIN)
        )
    ),
    DOG_FOOD(
        CookingPotRecipe(
            NamespacedKey(SMPRPG.plugin, "dog_food_cooking_pot"),
            listOf(
                ItemService.generate(Material.ROTTEN_FLESH),
                ItemService.generate(Material.BONE_MEAL),
                ItemService.generate(Material.BEEF),
                ItemService.generate(CustomItemType.RICE)
            ),
            200,
            ItemService.generate(CustomItemType.DOG_FOOD),
            SkillExperienceReward().add(SkillType.FARMING, 50)
        )
    ),

    // Drinks and desserts
    APPLE_CIDER(
        CookingPotRecipe(
            NamespacedKey(SMPRPG.plugin, "apple_cider_cooking_pot"),
            listOf(
                ItemService.generate(Material.APPLE),
                ItemService.generate(Material.APPLE),
                ItemService.generate(Material.SUGAR)
            ),
            200,
            ItemService.generate(CustomItemType.APPLE_CIDER),
            SkillExperienceReward().add(SkillType.FARMING, 50)
        )
    ),
    HOT_COCOA(
        CookingPotRecipe(
            NamespacedKey(SMPRPG.plugin, "hot_cocoa_cooking_pot"),
            listOf(
                ItemService.generate(Material.MILK_BUCKET),
                ItemService.generate(Material.SUGAR),
                ItemService.generate(Material.COCOA_BEANS),
                ItemService.generate(Material.COCOA_BEANS)
            ),
            200,
            ItemService.generate(CustomItemType.HOT_COCOA),
            SkillExperienceReward().add(SkillType.FARMING, 50)
        )
    ),
    GLOW_BERRY_CUSTARD(
        CookingPotRecipe(
            NamespacedKey(SMPRPG.plugin, "glow_berry_custard_cooking_pot"),
            listOf(
                ItemService.generate(Material.GLOW_BERRIES),
                ItemService.generate(Material.MILK_BUCKET),
                ItemService.generate(Material.EGG),
                ItemService.generate(Material.SUGAR)
            ),
            200,
            ItemService.generate(CustomItemType.GLOW_BERRY_CUSTARD),
            SkillExperienceReward().add(SkillType.FARMING, 50)
        )
    ),
}
