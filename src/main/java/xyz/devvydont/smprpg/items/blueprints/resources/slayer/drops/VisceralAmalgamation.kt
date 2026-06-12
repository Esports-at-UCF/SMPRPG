package xyz.devvydont.smprpg.items.blueprints.resources.slayer.drops

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
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
import xyz.devvydont.smprpg.items.interfaces.IHeaderDescribable
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden
import xyz.devvydont.smprpg.items.interfaces.ISellable
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.services.ItemService.Companion.generate
import xyz.devvydont.smprpg.util.formatting.ComponentUtils

class VisceralAmalgamation(itemService: ItemService, type: CustomItemType) : CustomItemBlueprint(itemService, type),
    ICraftable, ISellable, IHeaderDescribable, IModelOverridden {

    override val itemClassification: ItemClassification get() = ItemClassification.ITEM

    override fun getWorth(item: ItemStack): Int { return 138172 * item.amount }

    override fun getHeader(itemStack: ItemStack?): MutableList<Component?> {
        return mutableListOf(
            ComponentUtils.merge(
                ComponentUtils.create("A horrifying concoction consisting of "),
                ComponentUtils.create("absurd amounts", NamedTextColor.DARK_RED)
            ),
            ComponentUtils.merge(
                ComponentUtils.create("of "),
                ComponentUtils.create("flesh at various stages of decomposition, ", NamedTextColor.RED)
            ),
            ComponentUtils.create("chunks of viscera and gored remains, ", NamedTextColor.GOLD),
            ComponentUtils.create("and of course, to bind it all together..."),
            ComponentUtils.merge(
                ComponentUtils.create("slime", NamedTextColor.GREEN, TextDecoration.BOLD),
                ComponentUtils.create(".")
            )
        )
    }

    override fun getRecipeKey(): NamespacedKey {
        return NamespacedKey(plugin, customItemType.key + "-recipe")
    }

    override fun getCustomRecipe(): CraftingRecipe {
        val recipe = ShapedRecipe(recipeKey, generate())
        recipe.shape("fvf", "vsv", "fvf")
        recipe.setCategory(CraftingBookCategory.MISC)
        recipe.setIngredient('s', generate(CustomItemType.PREMIUM_SLIME))
        recipe.setIngredient('v', generate(CustomItemType.REVILED_VISCERA))
        recipe.setIngredient('f', generate(CustomItemType.NECROTIC_FLESH_SINGULARITY))
        return recipe
    }

    override fun unlockedBy(): MutableCollection<ItemStack?> {
        return mutableListOf(
            itemService.getCustomItem(CustomItemType.REVILED_VISCERA)
        )
    }

    override fun getDisplayKey(): Key? { return IModelOverridden.ofItemTypeInDirectory(customItemType, "materials") }
}
