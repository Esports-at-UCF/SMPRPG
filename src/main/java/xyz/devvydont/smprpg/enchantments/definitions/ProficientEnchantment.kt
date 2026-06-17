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

class ProficientEnchantment(id: String) : CustomEnchantment(id), AttributeEnchantment {
    override val displayName: Component get() = ComponentUtils.create("Proficient")
    override val description: Component get() = ComponentUtils.merge(
            ComponentUtils.create("Increases "),
            ComponentUtils.create(AttributeWrapper.PROFICIENCY.DisplayName, NamedTextColor.GOLD),
            ComponentUtils.create(" by "),
            ComponentUtils.create(
                String.format("+%d", getProficiency(level)),
                NamedTextColor.GREEN
            )
        )
    override val scrollBindingColor: Color get() = Color.fromRGB(140, 121, 156)

    override val itemTypeTag: TagKey<ItemType> get() = ItemTypeTagKeys.ENCHANTABLE_VANISHING
    override val maxLevel: Int get() = 10
    override val weight: Int get() = EnchantmentRarity.RARE.weight
    override val equipmentSlotGroup: EquipmentSlotGroup? get() = EquipmentSlotGroup.ANY
    override val skillRequirement: Int get() = 22

    override val powerRating : Int get() = level / 3
    override val attributeModifierType : AttributeModifierType get() = AttributeModifierType.ENCHANTMENT
    override fun getHeldAttributes() : MutableCollection<AttributeEntry?>? {
        return mutableListOf(
            AttributeEntry.additive(AttributeWrapper.PROFICIENCY, getProficiency(level).toDouble())
        )
    }

    override val magicExperience: Int get() = level * 200 * (1 + (level * 4 / maxLevel))

    override fun getRecipe(level: Int): EnchantmentRecipe? {
        when (level) {
            1 -> {
                val ing1 = getIngredientStack(CustomItemType.MITHRIL_BLOCK, 4)
                val ing2 = getIngredientStack(Material.DIAMOND_BLOCK, 2)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 8)
                return EnchantmentRecipe(getRecipeKey(level), 0, ing1, ing2, lapis)
            }

            2 -> {
                val ing1 = getIngredientStack(CustomItemType.MITHRIL_BLOCK, 8)
                val ing2 = getIngredientStack(Material.DIAMOND_BLOCK, 4)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 16)
                return EnchantmentRecipe(getRecipeKey(level), 5, ing1, ing2, lapis)
            }

            3 -> {
                val ing1 = getIngredientStack(CustomItemType.MITHRIL_BLOCK, 16)
                val ing2 = getIngredientStack(Material.DIAMOND_BLOCK, 8)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 32)
                return EnchantmentRecipe(getRecipeKey(level), 15, ing1, ing2, lapis)
            }

            4 -> {
                val ing1 = getIngredientStack(CustomItemType.MITHRIL_BLOCK, 32)
                val ing2 = getIngredientStack(Material.DIAMOND_BLOCK, 16)
                val ing3 = getIngredientStack(Material.BOOK, 12)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 64)
                return EnchantmentRecipe(getRecipeKey(level), 20, ing1, ing2, ing3, lapis)
            }

            5 -> {
                val ing1 = getIngredientStack(CustomItemType.MITHRIL_BLOCK, 64)
                val ing2 = getIngredientStack(Material.DIAMOND_BLOCK, 32)
                val ing3 = getIngredientStack(Material.BOOK, 24)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 16)
                return EnchantmentRecipe(getRecipeKey(level), 30, ing1, ing2, ing3, lapis)
            }

            6 -> {
                val ing1 = getIngredientStack(CustomItemType.ENCHANTED_MITHRIL, 16)
                val ing2 = getIngredientStack(Material.DIAMOND_BLOCK, 64)
                val ing3 = getIngredientStack(Material.BOOK, 48)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 32)
                return EnchantmentRecipe(getRecipeKey(level), 40, ing1, ing2, ing3, lapis)
            }

            7 -> {
                val ing1 = getIngredientStack(CustomItemType.ENCHANTED_MITHRIL, 32)
                val ing2 = getIngredientStack(CustomItemType.ENCHANTED_DIAMOND, 16)
                val ing3 = getIngredientStack(Material.BOOKSHELF, 32)
                val ing4 = getIngredientStack(CustomItemType.COBALT_INGOT, 32)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 64)
                return EnchantmentRecipe(getRecipeKey(level), 60, ing1, ing2, ing3, ing4, lapis)
            }

            8 -> {
                val ing1 = getIngredientStack(CustomItemType.ENCHANTED_MITHRIL, 64)
                val ing2 = getIngredientStack(CustomItemType.ENCHANTED_DIAMOND, 32)
                val ing3 = getIngredientStack(Material.BOOKSHELF, 64)
                val ing4 = getIngredientStack(CustomItemType.COBALT_INGOT, 64)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 16)
                return EnchantmentRecipe(getRecipeKey(level), 70, ing1, ing2, ing3, ing4, lapis)
            }

            9 -> {
                val ing1 = getIngredientStack(CustomItemType.ENCHANTED_MITHRIL_BLOCK, 16)
                val ing2 = getIngredientStack(CustomItemType.ENCHANTED_DIAMOND, 64)
                val ing3 = getIngredientStack(Material.BOOKSHELF, 64)
                val ing4 = getIngredientStack(CustomItemType.COBALT_BLOCK, 16)
                val ing5 = getIngredientStack(Material.DRAGON_BREATH, 16)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 32)
                return EnchantmentRecipe(getRecipeKey(level), 80, ing1, ing2, ing3, ing4, ing5, lapis)
            }

            10 -> {
                val ing1 = getIngredientStack(CustomItemType.ENCHANTED_MITHRIL_BLOCK, 32)
                val ing2 = getIngredientStack(CustomItemType.ENCHANTED_DIAMOND_BLOCK, 16)
                val ing3 = getIngredientStack(Material.BOOKSHELF, 64)
                val ing4 = getIngredientStack(CustomItemType.COBALT_BLOCK, 32)
                val ing5 = getIngredientStack(Material.DRAGON_BREATH, 32)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 32)
                return EnchantmentRecipe(getRecipeKey(level), 90, ing1, ing2, ing3, ing4, ing5, lapis)
            }

            else -> {
                return null
            }
        }
    }

    companion object {
        fun getProficiency(level: Int): Int { return level * 5 }
    }
}
