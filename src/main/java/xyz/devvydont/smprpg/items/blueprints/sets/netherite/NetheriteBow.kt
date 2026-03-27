package xyz.devvydont.smprpg.items.blueprints.sets.netherite

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.CraftingRecipe
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.recipe.CraftingBookCategory
import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeEntry
import xyz.devvydont.smprpg.items.base.CustomAttributeItem
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemSword.Companion.getSwordDamage
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemSword.Companion.getSwordRating
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment
import xyz.devvydont.smprpg.items.interfaces.ICraftable
import xyz.devvydont.smprpg.items.interfaces.IRepairable
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.items.ToolStats

class NetheriteBow(itemService: ItemService, type: CustomItemType) : CustomAttributeItem(itemService, type), ICraftable,
    IBreakableEquipment, IRepairable {

    override val itemClassification: ItemClassification get() = ItemClassification.BOW
    override val repairMaterial: MutableCollection<ItemStack> get() = mutableListOf(itemService.getCustomItem(Material.NETHERITE_INGOT))

    override fun getAttributeModifiers(item: ItemStack?): MutableCollection<AttributeEntry?> {
        return mutableListOf(
            AdditiveAttributeEntry(AttributeWrapper.STRENGTH, getSwordDamage(Material.NETHERITE_SWORD))
        )
    }

    override fun getPowerRating(): Int { return getSwordRating(Material.NETHERITE_SWORD) }

    override fun getActiveSlot(): EquipmentSlotGroup { return EquipmentSlotGroup.HAND }

    override fun getRecipeKey(): NamespacedKey { return ICraftable.getDefaultRecipeKey(customItemType) }

    override fun getCustomRecipe(): CraftingRecipe {
        val recipe = ShapedRecipe(recipeKey, generate())
        recipe.shape(
            " ns",
            "n s",
            " ns"
        )
        recipe.setIngredient('n', itemService.getCustomItem(Material.NETHERITE_INGOT))
        recipe.setIngredient('s', itemService.getCustomItem(Material.STRING))
        recipe.setCategory(CraftingBookCategory.EQUIPMENT)
        return recipe
    }

    override fun unlockedBy(): MutableCollection<ItemStack?> { return mutableListOf(itemService.getCustomItem(Material.NETHERITE_INGOT)) }

    override fun getMaxDurability(): Int { return ToolStats.NETHERITE.durability }

    companion object {
        const val DAMAGE: Double = 80.0
    }
}
