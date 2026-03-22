package xyz.devvydont.smprpg.items.blueprints.resources.slayer.drops

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
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
import xyz.devvydont.smprpg.items.interfaces.IHeaderDescribable
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden
import xyz.devvydont.smprpg.items.interfaces.ISellable
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.formatting.ComponentUtils

class SpellboundCloth(itemService: ItemService?, type: CustomItemType?) : CustomItemBlueprint(itemService, type),
    ISellable, IHeaderDescribable, IModelOverridden, ICraftable {
    /**
     * Determine what type of item this is.
     */
    override val itemClassification: ItemClassification get() = ItemClassification.ITEM

    /**
     * Given this item stack, how much should it be able to sell for?
     * Keep in mind that the size of the stack needs to considered as well!
     *
     * @param item The item that can be sold.
     * @return The worth of the item.
     */
    override fun getWorth(item: ItemStack): Int {
        return 56600 * item.amount
    }

    override fun getHeader(itemStack: ItemStack?): MutableList<Component?> {
        return mutableListOf(
            ComponentUtils.create("Hexed Cloth, refined with Spell Powder"),
            ComponentUtils.create("to remove the curses, and leave behind"),
            ComponentUtils.create("a potent, magical cloth.")
        )
    }

    override fun getDisplayKey(): Key {
        return IModelOverridden.ofItemType(_type)
    }

    override fun getRecipeKey(): NamespacedKey? {
        return NamespacedKey(plugin, customItemType.key + "-recipe")
    }

    override fun getCustomRecipe(): CraftingRecipe? {
        val recipe = ShapedRecipe(recipeKey!!, generate())
        recipe.setCategory(CraftingBookCategory.MISC)
        recipe.shape(
            "sss",
            "shs",
            "sss"
        )
        recipe.setIngredient('s', itemService.getCustomItem(CustomItemType.ENCHANTED_SPELL_POWDER))
        recipe.setIngredient('h', itemService.getCustomItem(CustomItemType.HEXED_CLOTH))
        return recipe
    }

    override fun unlockedBy(): Collection<ItemStack?>? {
        return listOf(
            ItemService.generate(CustomItemType.HEXED_CLOTH),
            ItemService.generate(CustomItemType.SPELL_POWDER)
        )
    }
}
