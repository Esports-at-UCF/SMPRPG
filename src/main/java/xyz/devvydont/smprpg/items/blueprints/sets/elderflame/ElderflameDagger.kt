package xyz.devvydont.smprpg.items.blueprints.sets.elderflame

import org.bukkit.NamespacedKey
import org.bukkit.inventory.CraftingRecipe
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.recipe.CraftingBookCategory
import xyz.devvydont.smprpg.SMPRPG.Companion.plugin
import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeEntry
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry
import xyz.devvydont.smprpg.items.base.CustomAttributeItem
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment
import xyz.devvydont.smprpg.items.interfaces.ICraftable
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.services.ItemService.Companion.generate

class ElderflameDagger(itemService: ItemService, type: CustomItemType) : CustomAttributeItem(itemService, type),
    ICraftable, IBreakableEquipment {
    override val itemClassification: ItemClassification get() = ItemClassification.SWORD

    /**
     * What modifiers themselves will be contained on the item if there are no variables to affect them?
     *
     * @param item The item that is supposed to be holding the modifiers.
     * @return
     */
    override fun getAttributeModifiers(item: ItemStack?): MutableCollection<AttributeEntry?> {
        return mutableListOf(
            AdditiveAttributeEntry(AttributeWrapper.STRENGTH, 200.0),
            AdditiveAttributeEntry(AttributeWrapper.CRITICAL_DAMAGE, 50.0),
            MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, -.5)
        )
    }

    override fun getPowerRating(): Int { return 50 }

    override fun getActiveSlot(): EquipmentSlotGroup { return EquipmentSlotGroup.MAINHAND }

    override fun getMaxDurability(): Int { return ElderflameArmorSet.ARMOR_DURABILITY_UNIT * 8 }

    override fun getRecipeKey(): NamespacedKey { return NamespacedKey(plugin, this.customItemType.key + "_recipe") }

    override fun getCustomRecipe(): CraftingRecipe {
        val recipe = ShapedRecipe(recipeKey, generate())
        recipe.shape(" m ", " m ", " s ")
        recipe.setIngredient('m', generate(CustomItemType.DRACONIC_CRYSTAL))
        recipe.setIngredient('s', generate(CustomItemType.OBSIDIAN_TOOL_ROD))
        recipe.setCategory(CraftingBookCategory.EQUIPMENT)
        return recipe
    }

    override fun unlockedBy(): MutableCollection<ItemStack?> { return mutableListOf(generate(CustomItemType.DRACONIC_CRYSTAL)) }
}
