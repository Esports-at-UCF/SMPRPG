package xyz.devvydont.smprpg.items.blueprints.equipment

import io.papermc.paper.datacomponent.DataComponentTypes
import net.kyori.adventure.key.Key
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.CraftingRecipe
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.recipe.CraftingBookCategory
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint
import xyz.devvydont.smprpg.items.interfaces.ICosmeticDurability
import xyz.devvydont.smprpg.items.interfaces.ICraftable
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.services.ItemService.Companion.generate

class WalletBlueprint(itemService: ItemService, type: CustomItemType) : CustomItemBlueprint(itemService, type), IModelOverridden,
    ICosmeticDurability, ICraftable {

    override val itemClassification: ItemClassification get() = ItemClassification.EQUIPMENT
    val maxCoins: Int get() = when (type) {
        CustomItemType.SMALL_COIN_PURSE    -> 10_000
        CustomItemType.MEDIUM_COIN_PURSE   -> 50_000
        CustomItemType.LARGE_COIN_PURSE    -> 100_000
        CustomItemType.GIGANTIC_COIN_PURSE -> 250_000
        CustomItemType.COLOSSAL_COIN_PURSE -> 500_000
        else -> 1
    }

    override fun updateItemData(itemStack: ItemStack) {
        if (itemStack.getDataOrDefault(DataComponentTypes.DAMAGE, -1) == -1) itemStack.setData(DataComponentTypes.DAMAGE, maxCoins)
        itemStack.setData(DataComponentTypes.MAX_DAMAGE, maxCoins + 1)
        itemStack.setData(DataComponentTypes.MAX_STACK_SIZE, 1)
        super.updateItemData(itemStack)
    }

    override fun getDisplayKey(): Key? {
        return IModelOverridden.ofItemTypeInDirectory(type, "equipment")
    }

    override fun getRecipeKey(): NamespacedKey {
        return ICraftable.getDefaultRecipeKey(type)
    }

    override fun getCustomRecipe(): CraftingRecipe {
        val baseItem = when (type) {
            CustomItemType.SMALL_COIN_PURSE    -> generate(CustomItemType.ENCHANTED_COPPER)
            CustomItemType.MEDIUM_COIN_PURSE   -> generate(CustomItemType.ENCHANTED_SILVER)
            CustomItemType.LARGE_COIN_PURSE    -> generate(CustomItemType.ENCHANTED_GOLD)
            CustomItemType.GIGANTIC_COIN_PURSE -> generate(CustomItemType.ENCHANTED_PLATINUM)
            CustomItemType.COLOSSAL_COIN_PURSE -> generate(CustomItemType.DRAGONSTEEL_BLOCK)
            else -> generate(CustomItemType.MILK_BOTTLE)
        }
        val bindingItem = when (type) {
            CustomItemType.SMALL_COIN_PURSE    -> generate(Material.LEATHER)
            CustomItemType.MEDIUM_COIN_PURSE   -> generate(CustomItemType.PREMIUM_LEATHER)
            CustomItemType.LARGE_COIN_PURSE    -> generate(CustomItemType.ENCHANTED_LEATHER)
            CustomItemType.GIGANTIC_COIN_PURSE -> generate(CustomItemType.PREMIUM_MEMBRANE)
            CustomItemType.COLOSSAL_COIN_PURSE -> generate(CustomItemType.ENCHANTED_MEMBRANE)
            else -> generate(CustomItemType.MILK_BOTTLE)
        }
        val claspItem = when (type) {
            CustomItemType.SMALL_COIN_PURSE    -> generate(Material.STRING)
            CustomItemType.MEDIUM_COIN_PURSE   -> generate(CustomItemType.PREMIUM_STRING)
            CustomItemType.LARGE_COIN_PURSE    -> generate(CustomItemType.ENCHANTED_STRING)
            CustomItemType.GIGANTIC_COIN_PURSE -> generate(CustomItemType.ASTRAL_FILAMENT)
            CustomItemType.COLOSSAL_COIN_PURSE -> generate(CustomItemType.STRANGE_FIBER)
            else -> generate(CustomItemType.MILK_BOTTLE)
        }

        val recipe = ShapedRecipe(recipeKey, generate())
        recipe.setCategory(CraftingBookCategory.EQUIPMENT)
        recipe.shape(" c ", "b b", "lbl")
        recipe.setIngredient('b', baseItem)
        recipe.setIngredient('l', bindingItem)
        recipe.setIngredient('c', claspItem)
        return recipe
    }

    override fun unlockedBy(): Collection<ItemStack> {
        return listOf(
            generate(Material.LEATHER)
        )
    }
}