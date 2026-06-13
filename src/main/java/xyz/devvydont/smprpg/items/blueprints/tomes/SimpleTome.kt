package xyz.devvydont.smprpg.items.blueprints.tomes

import io.papermc.paper.datacomponent.DataComponentTypes
import net.kyori.adventure.key.Key
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.CraftingRecipe
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.recipe.CraftingBookCategory
import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeEntry
import xyz.devvydont.smprpg.items.interfaces.ICraftable
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden
import xyz.devvydont.smprpg.services.ItemService

class SimpleTome(itemService: ItemService, type: CustomItemType) : TomeBlueprint(itemService, type), ICraftable {

    override val maxSpellSlots: Int get() = 1
    override val cooldownMult: Double get() = 1.1

    override fun updateItemData(itemStack: ItemStack) {
        itemStack.setData(DataComponentTypes.MAX_STACK_SIZE, 1)
        super.updateItemData(itemStack)
    }

    override fun getAttributeModifiers(item: ItemStack): MutableCollection<AttributeEntry> {
        return mutableListOf(
            AdditiveAttributeEntry(AttributeWrapper.INTELLIGENCE, 100.0),
            AdditiveAttributeEntry(AttributeWrapper.STRENGTH, 5.0)
        )
    }

    override fun getPowerRating(): Int { return 10 }
    override fun getActiveSlot(): EquipmentSlotGroup { return EquipmentSlotGroup.HAND }

    override fun getRecipeKey(): NamespacedKey { return ICraftable.getDefaultRecipeKey(type) }
    override fun getCustomRecipe(): CraftingRecipe {
        val recipe = ShapedRecipe(recipeKey, generate())
        recipe.shape(
            "lll",
            "lbl",
            "lll")
        recipe.setIngredient('l', itemService.getCustomItem(Material.LAPIS_LAZULI))
        recipe.setIngredient('b', itemService.getCustomItem(Material.BOOK))
        recipe.setCategory(CraftingBookCategory.EQUIPMENT)
        return recipe
    }
    override fun unlockedBy(): Collection<ItemStack> { return listOf(itemService.getCustomItem(Material.LAPIS_LAZULI)) }
}
