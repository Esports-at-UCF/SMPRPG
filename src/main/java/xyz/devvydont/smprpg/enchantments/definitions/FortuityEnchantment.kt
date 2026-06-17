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
import xyz.devvydont.smprpg.items.attribute.AttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeModifierType
import xyz.devvydont.smprpg.util.formatting.ComponentUtils

class FortuityEnchantment(id: String) : CustomEnchantment(id), AttributeEnchantment {
    override val displayName: Component get() = ComponentUtils.create("Fortuity")
    override val description: Component get() = ComponentUtils.merge(
            ComponentUtils.create("Increases "),
            ComponentUtils.create(AttributeWrapper.LUCK.DisplayName, NamedTextColor.GOLD),
            ComponentUtils.create(" by "),
            ComponentUtils.create("+" + getChanceIncrease(level), NamedTextColor.GREEN)
        )
    override val scrollBindingColor: Color get() = Color.fromRGB(112, 255, 119)

    override val itemTypeTag: TagKey<ItemType> get()           = ItemTypeTagKeys.ENCHANTABLE_VANISHING
    override val maxLevel: Int get()                           = 10
    override val weight: Int get()                             = EnchantmentRarity.RARE.weight
    override val equipmentSlotGroup: EquipmentSlotGroup? get() = EquipmentSlotGroup.ANY
    override val skillRequirement: Int get()                   = 25

    override val powerRating : Int get() = 0
    override val attributeModifierType : AttributeModifierType get() = AttributeModifierType.ENCHANTMENT
    override fun getHeldAttributes() : MutableCollection<AttributeEntry?>? {
        return mutableListOf(
            AttributeEntry.additive(AttributeWrapper.LUCK, getChanceIncrease(level).toDouble())
        )
    }

    override val magicExperience: Int get() = level * 200 * (1 + (level * 4 / maxLevel))

    override fun getRecipe(level: Int): EnchantmentRecipe? {
        when (level) {
            1 -> {
                val ing1 = getIngredientStack(Material.EMERALD_BLOCK, 4)
                val ing2 = getIngredientStack(Material.GOLD_BLOCK, 2)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 8)
                return EnchantmentRecipe(getRecipeKey(level), 0, ing1, ing2, lapis)
            }

            2 -> {
                val ing1 = getIngredientStack(Material.EMERALD_BLOCK, 8)
                val ing2 = getIngredientStack(Material.GOLD_BLOCK, 4)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 16)
                return EnchantmentRecipe(getRecipeKey(level), 5, ing1, ing2, lapis)
            }

            3 -> {
                val ing1 = getIngredientStack(Material.EMERALD_BLOCK, 16)
                val ing2 = getIngredientStack(Material.GOLD_BLOCK, 8)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 32)
                return EnchantmentRecipe(getRecipeKey(level), 15, ing1, ing2, lapis)
            }

            4 -> {
                val ing1 = getIngredientStack(Material.EMERALD_BLOCK, 32)
                val ing2 = getIngredientStack(Material.GOLD_BLOCK, 16)
                val ing3 = getIngredientStack(Material.RABBIT_FOOT, 2)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 64)
                return EnchantmentRecipe(getRecipeKey(level), 20, ing1, ing2, ing3, lapis)
            }

            5 -> {
                val ing1 = getIngredientStack(Material.EMERALD_BLOCK, 64)
                val ing2 = getIngredientStack(Material.GOLD_BLOCK, 32)
                val ing3 = getIngredientStack(Material.RABBIT_FOOT, 4)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 16)
                return EnchantmentRecipe(getRecipeKey(level), 30, ing1, ing2, ing3, lapis)
            }

            6 -> {
                val ing1 = getIngredientStack(CustomItemType.ENCHANTED_EMERALD, 16)
                val ing2 = getIngredientStack(Material.GOLD_BLOCK, 64)
                val ing3 = getIngredientStack(Material.RABBIT_FOOT, 8)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 32)
                return EnchantmentRecipe(getRecipeKey(level), 40, ing1, ing2, ing3, lapis)
            }

            7 -> {
                val ing1 = getIngredientStack(CustomItemType.ENCHANTED_EMERALD, 32)
                val ing2 = getIngredientStack(CustomItemType.ENCHANTED_GOLD, 16)
                val ing3 = getIngredientStack(Material.RABBIT_FOOT, 16)
                val ing4 = getIngredientStack(CustomItemType.ORICHALCUM_INGOT, 32)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 64)
                return EnchantmentRecipe(getRecipeKey(level), 60, ing1, ing2, ing3, ing4, lapis)
            }

            8 -> {
                val ing1 = getIngredientStack(CustomItemType.ENCHANTED_EMERALD, 64)
                val ing2 = getIngredientStack(CustomItemType.ENCHANTED_GOLD, 32)
                val ing3 = getIngredientStack(Material.RABBIT_FOOT, 32)
                val ing4 = getIngredientStack(CustomItemType.ORICHALCUM_INGOT, 64)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 16)
                return EnchantmentRecipe(getRecipeKey(level), 70, ing1, ing2, ing3, ing4, lapis)
            }

            9 -> {
                val ing1 = getIngredientStack(CustomItemType.ENCHANTED_EMERALD_BLOCK, 16)
                val ing2 = getIngredientStack(CustomItemType.ENCHANTED_GOLD, 64)
                val ing3 = getIngredientStack(Material.RABBIT_FOOT, 64)
                val ing4 = getIngredientStack(CustomItemType.ORICHALCUM_BLOCK, 16)
                val ing5 = getIngredientStack(CustomItemType.ENCHANTED_ENDER_PEARL, 8)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 32)
                return EnchantmentRecipe(getRecipeKey(level), 80, ing1, ing2, ing3, ing4, ing5, lapis)
            }

            10 -> {
                val ing1 = getIngredientStack(CustomItemType.ENCHANTED_EMERALD_BLOCK, 32)
                val ing2 = getIngredientStack(CustomItemType.ENCHANTED_GOLD_BLOCK, 16)
                val ing3 = getIngredientStack(Material.RABBIT_FOOT, 64)
                val ing4 = getIngredientStack(CustomItemType.ORICHALCUM_BLOCK, 32)
                val ing5 = getIngredientStack(CustomItemType.ENCHANTED_ENDER_PEARL, 16)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 32)
                return EnchantmentRecipe(getRecipeKey(level), 90, ing1, ing2, ing3, ing4, ing5, lapis)
            }

            else -> {
                return null
            }
        }
    }

    companion object {
        fun getChanceIncrease(level: Int): Int {
            return when (level) {
                0 -> 0
                1 -> 2
                2 -> 4
                3 -> 6
                4 -> 8
                5 -> 10
                else -> getChanceIncrease(5) + 2 * (level - 5)
            }
        }
    }
}
