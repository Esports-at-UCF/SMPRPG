package xyz.devvydont.smprpg.items.blueprints.sets.inferno

import org.bukkit.NamespacedKey
import org.bukkit.inventory.CraftingRecipe
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.recipe.CraftingBookCategory
import xyz.devvydont.smprpg.SMPRPG.Companion.plugin
import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeEntry.Companion.additive
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry
import xyz.devvydont.smprpg.items.base.CustomShortbow
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment
import xyz.devvydont.smprpg.items.interfaces.ICraftable
import xyz.devvydont.smprpg.items.interfaces.IRepairable
import xyz.devvydont.smprpg.items.interfaces.ISellable
import xyz.devvydont.smprpg.items.interfaces.ISkillRequirement
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.services.ItemService.Companion.generate
import xyz.devvydont.smprpg.skills.SkillType
import java.util.List

class InfernoShortbow(itemService: ItemService, type: CustomItemType) : CustomShortbow(itemService, type), ICraftable,
    ISellable, IBreakableEquipment, IRepairable, ISkillRequirement {

    override val repairMaterial: MutableCollection<ItemStack> get() = mutableListOf(itemService.getCustomItem(CustomItemType.INFERNO_REMNANT))
    override val skillRequirements: MutableMap<SkillType, Int> = mutableMapOf(Pair(SkillType.COMBAT, 35))

    override fun getAttributeModifiers(item: ItemStack): MutableCollection<AttributeEntry> {
        return mutableListOf(
            AdditiveAttributeEntry(AttributeWrapper.STRENGTH, 120.0),
            MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, -.4),
            AdditiveAttributeEntry(AttributeWrapper.CRITICAL_DAMAGE, 75.0),
            additive(AttributeWrapper.CRITICAL_CHANCE, 50.0)
        )
    }

    override fun getPowerRating(): Int {
        return InfernoArmorSet.POWER
    }

    override fun getRecipeKey(): NamespacedKey {
        return ICraftable.getDefaultRecipeKey(type)
    }

    override fun getCustomRecipe(): CraftingRecipe {
        val recipe = ShapedRecipe(getRecipeKey(), generate())
        recipe.shape(
            " rs",
            "r s",
            " rs"
        )
        recipe.setIngredient('r', generate(InfernoArmorSet.CRAFTING_COMPONENT))
        recipe.setIngredient('s', generate(CustomItemType.SCORCHING_STRING))
        recipe.setCategory(CraftingBookCategory.EQUIPMENT)
        return recipe
    }

    override fun unlockedBy(): MutableCollection<ItemStack> {
        return mutableListOf(
            generate(InfernoArmorSet.CRAFTING_COMPONENT)
        )
    }

    override fun getMaxDurability(): Int {
        return 40000
    }

}
