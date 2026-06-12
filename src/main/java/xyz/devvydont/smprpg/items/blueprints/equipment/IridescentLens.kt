package xyz.devvydont.smprpg.items.blueprints.equipment

import net.kyori.adventure.key.Key
import org.bukkit.NamespacedKey
import org.bukkit.inventory.CraftingRecipe
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.recipe.CraftingBookCategory
import xyz.devvydont.smprpg.SMPRPG.Companion.plugin
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.interfaces.ICraftable
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden
import xyz.devvydont.smprpg.items.interfaces.ISellable
import xyz.devvydont.smprpg.reforge.ReforgeType
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.services.ItemService.Companion.generate
import java.util.List

class IridescentLens(itemService: ItemService?, type: CustomItemType?) : ReforgeStone(itemService, type), ICraftable,
    ISellable, IModelOverridden {
    override fun getRecipeKey(): NamespacedKey { return ICraftable.getDefaultRecipeKey(customItemType) }

    override fun getCustomRecipe(): CraftingRecipe {
        val recipe = ShapedRecipe(recipeKey, generate())
        recipe.shape(
            "cdc",
            "djd",
            "cdc"
        )
        recipe.setIngredient('c', generate(CustomItemType.ENCHANTED_PRISMARINE_CRYSTAL))
        recipe.setIngredient('d', generate(CustomItemType.ENCHANTED_DIAMOND))
        recipe.setIngredient('j', generate(CustomItemType.JUPITERS_ARTIFACT))
        recipe.setCategory(CraftingBookCategory.MISC)
        return recipe
    }

    override fun unlockedBy(): MutableCollection<ItemStack?> { return mutableListOf(itemService.getCustomItem(CustomItemType.JUPITER_CRYSTAL)) }

    override fun getWorth(item: ItemStack): Int { return 75000 * item.amount }

    override fun getReforgeType(): ReforgeType { return ReforgeType.PRISMATIC }

    override fun getExperienceCost(): Int { return 50 }

    override fun getDisplayKey(): Key { return IModelOverridden.ofItemTypeInDirectory(customItemType, "reforge_stones") }
}
