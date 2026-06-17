package xyz.devvydont.smprpg.enchantments.definitions

import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys
import io.papermc.paper.registry.tag.TagKey
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemType
import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.enchantments.CustomEnchantment
import xyz.devvydont.smprpg.enchantments.EnchantmentRarity
import xyz.devvydont.smprpg.enchantments.base.AttributeEnchantment
import xyz.devvydont.smprpg.enchantments.recipe.EnchantmentRecipe
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeModifierType
import xyz.devvydont.smprpg.util.formatting.ComponentUtils
import xyz.devvydont.smprpg.util.formatting.Symbols
import java.util.List

class HeartyEnchantment(id: String) : CustomEnchantment(id), AttributeEnchantment {
    override val displayName: Component get() = ComponentUtils.create("Hearty")
    override val description: Component get() = ComponentUtils.merge(
            ComponentUtils.create("Increases max HP by "),
            ComponentUtils.create("+" + getHealthIncrease(level), NamedTextColor.GREEN),
            ComponentUtils.create(Symbols.HEART, NamedTextColor.RED)
        )
    override val scrollBindingColor: Color get() = Color.fromRGB(255, 25, 68)

   override val itemTypeTag: TagKey<ItemType> get()           = ItemTypeTagKeys.ENCHANTABLE_ARMOR
   override val maxLevel: Int get()                           = 10
   override val weight: Int get()                             = EnchantmentRarity.COMMON.getWeight()
   override val equipmentSlotGroup: EquipmentSlotGroup? get() = EquipmentSlotGroup.ARMOR
   override val skillRequirement: Int get()                   = 1

    override val powerRating : Int get() = level / 2 + 1
    override val attributeModifierType : AttributeModifierType get() = AttributeModifierType.ENCHANTMENT
    override fun getHeldAttributes() : MutableCollection<AttributeEntry?>? {
        return mutableListOf(
            AdditiveAttributeEntry(AttributeWrapper.HEALTH, getHealthIncrease(level).toDouble())
        )
    }

    override val magicExperience: Int get() = level * 200 * (1 + (level * 4 / maxLevel))

    override fun getRecipe(level: Int): EnchantmentRecipe? {
        when (level) {
            1 -> {
                val ing1 = getIngredientStack(Material.MELON, 16)
                val ing2 = getIngredientStack(CustomItemType.HEARTBEET, 1)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 8)
                return EnchantmentRecipe(getRecipeKey(level), 0, ing1, ing2, lapis)
            }

            2 -> {
                val ing1 = getIngredientStack(Material.MELON, 32)
                val ing2 = getIngredientStack(CustomItemType.HEARTBEET, 2)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 16)
                return EnchantmentRecipe(getRecipeKey(level), 5, ing1, ing2, lapis)
            }

            3 -> {
                val ing1 = getIngredientStack(Material.MELON, 64)
                val ing2 = getIngredientStack(CustomItemType.HEARTBEET, 4)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 32)
                return EnchantmentRecipe(getRecipeKey(level), 15, ing1, ing2, lapis)
            }

            4 -> {
                val ing1 = getIngredientStack(CustomItemType.PREMIUM_MELON_SLICE, 16)
                val ing2 = getIngredientStack(CustomItemType.HEARTBEET, 8)
                val ing3 = getIngredientStack(CustomItemType.PREMIUM_POTATO, 8)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 64)
                return EnchantmentRecipe(getRecipeKey(level), 20, ing1, ing2, ing3, lapis)
            }

            5 -> {
                val ing1 = getIngredientStack(CustomItemType.PREMIUM_MELON_SLICE, 32)
                val ing2 = getIngredientStack(CustomItemType.HEARTBEET, 16)
                val ing3 = getIngredientStack(CustomItemType.PREMIUM_POTATO, 16)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 16)
                return EnchantmentRecipe(getRecipeKey(level), 30, ing1, ing2, ing3, lapis)
            }

            6 -> {
                val ing1 = getIngredientStack(CustomItemType.PREMIUM_MELON_SLICE, 64)
                val ing2 = getIngredientStack(CustomItemType.HEARTBEET, 32)
                val ing3 = getIngredientStack(CustomItemType.PREMIUM_POTATO, 32)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 32)
                return EnchantmentRecipe(getRecipeKey(level), 40, ing1, ing2, ing3, lapis)
            }

            7 -> {
                val ing1 = getIngredientStack(CustomItemType.PREMIUM_MELON, 16)
                val ing2 = getIngredientStack(CustomItemType.HEARTBEET, 64)
                val ing3 = getIngredientStack(CustomItemType.PREMIUM_POTATO, 64)
                val ing4 = getIngredientStack(Material.GOLDEN_APPLE, 8)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 64)
                return EnchantmentRecipe(getRecipeKey(level), 60, ing1, ing2, ing3, ing4, lapis)
            }

            8 -> {
                val ing1 = getIngredientStack(CustomItemType.PREMIUM_MELON, 32)
                val ing2 = getIngredientStack(CustomItemType.HEARTBEET, 64)
                val ing3 = getIngredientStack(CustomItemType.PREMIUM_BAKED_POTATO, 16)
                val ing4 = getIngredientStack(Material.GOLDEN_APPLE, 16)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 16)
                return EnchantmentRecipe(getRecipeKey(level)!!, 70, ing1, ing2, ing3, ing4, lapis)
            }

            9 -> {
                val ing1 = getIngredientStack(CustomItemType.PREMIUM_MELON, 32)
                val ing2 = getIngredientStack(CustomItemType.HEARTBEET, 64)
                val ing3 = getIngredientStack(CustomItemType.PREMIUM_BAKED_POTATO, 32)
                val ing4 = getIngredientStack(Material.GOLDEN_APPLE, 32)
                val ing5 = getIngredientStack(Material.ENCHANTED_GOLDEN_APPLE, 1)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 32)
                return EnchantmentRecipe(getRecipeKey(level), 80, ing1, ing2, ing3, ing4, ing5, lapis)
            }

            10 -> {
                val ing1 = getIngredientStack(CustomItemType.PREMIUM_MELON, 64)
                val ing2 = getIngredientStack(CustomItemType.HEARTBEET, 64)
                val ing3 = getIngredientStack(CustomItemType.PREMIUM_BAKED_POTATO, 64)
                val ing4 = getIngredientStack(Material.GOLDEN_APPLE, 64)
                val ing5 = getIngredientStack(Material.ENCHANTED_GOLDEN_APPLE, 2)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 32)
                return EnchantmentRecipe(getRecipeKey(level), 90, ing1, ing2, ing3, ing4, ing5, lapis)
            }

            else -> {
                return null
            }
        }
    }

    companion object {
        fun getHealthIncrease(level: Int): Int {
            return when (level) {
                0 -> 0
                1 -> 10
                2 -> 20
                3 -> 30
                4 -> 45
                5 -> 60
                6 -> 75
                7 -> 90
                8 -> 105
                9 -> 125
                10 -> 150
                else -> getHealthIncrease(10) + 25 * (level - 10)
            }
        }
    }
}
