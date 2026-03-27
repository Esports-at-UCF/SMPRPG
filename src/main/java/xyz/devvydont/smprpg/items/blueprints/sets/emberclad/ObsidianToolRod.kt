package xyz.devvydont.smprpg.items.blueprints.sets.emberclad

import net.kyori.adventure.key.Key
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.CraftingRecipe
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.recipe.CraftingBookCategory
import xyz.devvydont.smprpg.SMPRPG.Companion.plugin
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint
import xyz.devvydont.smprpg.items.interfaces.ICraftable
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden
import xyz.devvydont.smprpg.items.interfaces.ISellable
import xyz.devvydont.smprpg.services.ItemService
import java.util.List

class ObsidianToolRod(itemService: ItemService, type: CustomItemType) : CustomItemBlueprint(itemService, type),
    ICraftable, ISellable, IModelOverridden {

    override  val itemClassification: ItemClassification get() = ItemClassification.MATERIAL

    override fun getRecipeKey(): NamespacedKey { return ICraftable.getDefaultRecipeKey(customItemType) }

    override fun getCustomRecipe(): CraftingRecipe {
        val recipe = ShapedRecipe(recipeKey, generate())
        recipe.shape("o", "o")
        recipe.setIngredient('o', itemService.getCustomItem(CustomItemType.ENCHANTED_OBSIDIAN))
        recipe.setCategory(CraftingBookCategory.EQUIPMENT)
        return recipe
    }

    override fun unlockedBy(): MutableCollection<ItemStack?> { return mutableListOf(itemService.getCustomItem(Material.OBSIDIAN)) }

    override fun getWorth(itemStack: ItemStack): Int { return 5000 * itemStack.amount }

    override fun getDisplayKey(): Key? { return IModelOverridden.ofItemTypeInDirectory(customItemType, "materials") }
}
