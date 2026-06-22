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

class SerratedEnchantment(id: String) : CustomEnchantment(id), AttributeEnchantment {
    override val displayName: Component get() = ComponentUtils.create("Serrated")
    override val description: Component get() = ComponentUtils.merge(
            ComponentUtils.create("Increases "),
            ComponentUtils.create("Critical Rating", NamedTextColor.BLUE),
            ComponentUtils.create(" by "),
            ComponentUtils.create(
                "+" + getAdditionalPercentageIncrease(level),
                NamedTextColor.GREEN
            )
        )
    override val scrollBindingColor: Color get() = Color.fromRGB(255, 176, 115)

    override val itemTypeTag: TagKey<ItemType> get()           = ItemTypeTagKeys.ENCHANTABLE_WEAPON
    override val maxLevel: Int get()                           = 7
    override val weight: Int get()                             = EnchantmentRarity.COMMON.weight
    override val equipmentSlotGroup: EquipmentSlotGroup? get() = EquipmentSlotGroup.MAINHAND
    override val skillRequirement: Int get()                   = 11

    override val powerRating : Int get() = this.level / 3
    override val attributeModifierType : AttributeModifierType get() = AttributeModifierType.ENCHANTMENT
    override fun getHeldAttributes() : MutableCollection<AttributeEntry?>? {
        return mutableListOf(
            AdditiveAttributeEntry(
                AttributeWrapper.CRITICAL_DAMAGE,
                getAdditionalPercentageIncrease(level).toDouble()
            )
        )
    }

    override fun getRecipe(level: Int): EnchantmentRecipe? {
        when (level) {
            1 -> {
                val flint = getIngredientStack(Material.FLINT, 16)
                val iron = getIngredientStack(Material.IRON_INGOT, 8)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 8)
                return EnchantmentRecipe(getRecipeKey(level), 11, flint, iron, lapis)
            }

            2 -> {
                val flint = getIngredientStack(Material.FLINT, 32)
                val iron = getIngredientStack(Material.IRON_INGOT, 16)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 16)
                return EnchantmentRecipe(getRecipeKey(level), 23, flint, iron, lapis)
            }

            3 -> {
                val flint = getIngredientStack(CustomItemType.COMPRESSED_FLINT, 16)
                val cactus = getIngredientStack(Material.CACTUS, 32)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 32)
                return EnchantmentRecipe(getRecipeKey(level), 34, flint, cactus, lapis)
            }

            4 -> {
                val flint = getIngredientStack(CustomItemType.COMPRESSED_FLINT, 32)
                val iron = getIngredientStack(CustomItemType.ENCHANTED_IRON, 4)
                val cactus = getIngredientStack(Material.CACTUS, 64)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 8)
                return EnchantmentRecipe(getRecipeKey(level), 46, flint, iron, cactus, lapis)
            }

            5 -> {
                val flint = getIngredientStack(CustomItemType.ENCHANTED_FLINT, 8)
                val iron = getIngredientStack(CustomItemType.ENCHANTED_IRON, 8)
                val steel = getIngredientStack(CustomItemType.STEEL_INGOT, 32)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 16)
                return EnchantmentRecipe(getRecipeKey(level), 57, flint, iron, steel, lapis)
            }

            6 -> {
                val flint = getIngredientStack(CustomItemType.ENCHANTED_FLINT, 16)
                val iron = getIngredientStack(CustomItemType.ENCHANTED_IRON, 16)
                val steel = getIngredientStack(CustomItemType.STEEL_BLOCK, 16)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 8)
                return EnchantmentRecipe(getRecipeKey(level), 69, flint, iron, steel, lapis)
            }

            7 -> {
                val flint = getIngredientStack(CustomItemType.ENCHANTED_FLINT, 32)
                val iron = getIngredientStack(CustomItemType.ENCHANTED_IRON, 32)
                val steel = getIngredientStack(CustomItemType.ENCHANTED_STEEL, 8)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 16)
                return EnchantmentRecipe(getRecipeKey(level), 80, flint, iron, steel, lapis)
            }

            else -> return null
        }
    }

    companion object {
        fun getAdditionalPercentageIncrease(level: Int): Int {
            return when (level) {
                0 -> 0
                1 -> 10
                2 -> 20
                3 -> 30
                4 -> 45
                5 -> 60
                6 -> 75
                7 -> 100
                else -> getAdditionalPercentageIncrease(5) + 25 * (level - 7)
            }
        }
    }
}
