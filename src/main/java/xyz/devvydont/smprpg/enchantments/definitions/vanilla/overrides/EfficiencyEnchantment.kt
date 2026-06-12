package xyz.devvydont.smprpg.enchantments.definitions.vanilla.overrides

import io.papermc.paper.registry.TypedKey
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys
import io.papermc.paper.registry.tag.TagKey
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemType
import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.enchantments.EnchantmentRarity
import xyz.devvydont.smprpg.enchantments.base.AttributeEnchantment
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.VanillaEnchantment
import xyz.devvydont.smprpg.enchantments.recipe.EnchantmentRecipe
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.attribute.AttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeModifierType
import xyz.devvydont.smprpg.util.formatting.ComponentUtils

class EfficiencyEnchantment(key: TypedKey<Enchantment>) : VanillaEnchantment(key), AttributeEnchantment {
    override val displayName: Component get() = ComponentUtils.create("Efficiency")
    override val description: Component get() = ComponentUtils.merge(
            ComponentUtils.create("Increases harvest speed by "),
            ComponentUtils.create(
                "+" + getMiningEfficiency(level),
                NamedTextColor.GREEN
            )
        )
    override val scrollBindingColor: Color get() = Color.fromRGB(255, 94, 94)

    override val itemTypeTag: TagKey<ItemType> get()           = ItemTypeTagKeys.ENCHANTABLE_MINING
    override val maxLevel: Int get()                           = 10
    override val weight: Int get()                             = EnchantmentRarity.COMMON.weight
    override val equipmentSlotGroup: EquipmentSlotGroup? get() = EquipmentSlotGroup.MAINHAND
    override val skillRequirement: Int get()                   = 0

    override val powerRating : Int get() = level / 2
    override val attributeModifierType : AttributeModifierType get() = AttributeModifierType.ENCHANTMENT
    override fun getHeldAttributes() : MutableCollection<AttributeEntry?>? {
        return mutableListOf(
            AttributeEntry.additive(AttributeWrapper.MINING_SPEED, getMiningEfficiency(level).toDouble())
        )
    }

    override fun getRecipe(level: Int): EnchantmentRecipe? {
        when (level) {
            1 -> {
                val sugar = getIngredientStack(Material.SUGAR, 20)
                val redstone = getIngredientStack(Material.REDSTONE, 10)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 8)
                return EnchantmentRecipe(getRecipeKey(level), 0, sugar, redstone, lapis)
            }

            2 -> {
                val sugar = getIngredientStack(Material.SUGAR, 40)
                val redstone = getIngredientStack(Material.REDSTONE, 20)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 16)
                return EnchantmentRecipe(getRecipeKey(level), 5, sugar, redstone, lapis)
            }

            3 -> {
                val sugar = getIngredientStack(Material.SUGAR, 80)
                val redstone = getIngredientStack(Material.REDSTONE, 40)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 32)
                return EnchantmentRecipe(getRecipeKey(level), 10, sugar, redstone, lapis)
            }

            4 -> {
                val sugar = getIngredientStack(CustomItemType.PREMIUM_SUGAR, 18)
                val redstone = getIngredientStack(Material.REDSTONE, 80)
                val glowstone = getIngredientStack(Material.GLOWSTONE_DUST, 10)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 64)
                return EnchantmentRecipe(getRecipeKey(level), 20, sugar, redstone, glowstone, lapis)
            }

            5 -> {
                val sugar = getIngredientStack(CustomItemType.PREMIUM_SUGAR, 36)
                val redstone = getIngredientStack(Material.REDSTONE_BLOCK, 18)
                val glowstone = getIngredientStack(Material.GLOWSTONE_DUST, 20)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 16)
                return EnchantmentRecipe(getRecipeKey(level), 30, sugar, redstone, glowstone, lapis)
            }

            6 -> {
                val sugar = getIngredientStack(CustomItemType.PREMIUM_SUGAR, 72)
                val redstone = getIngredientStack(Material.REDSTONE_BLOCK, 36)
                val glowstone = getIngredientStack(Material.GLOWSTONE_DUST, 40)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 32)
                return EnchantmentRecipe(getRecipeKey(level), 40, sugar, redstone, glowstone, lapis)
            }

            7 -> {
                val sugar = getIngredientStack(CustomItemType.ENCHANTED_SUGAR, 16)
                val redstone = getIngredientStack(Material.REDSTONE_BLOCK, 72)
                val glowstone = getIngredientStack(Material.GLOWSTONE_DUST, 80)
                val gravitite = getIngredientStack(CustomItemType.GRAVITITE_SHARDS, 4)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 64)
                return EnchantmentRecipe(getRecipeKey(level), 50, sugar, redstone, glowstone, gravitite, lapis)
            }

            8 -> {
                val sugar = getIngredientStack(CustomItemType.ENCHANTED_SUGAR, 32)
                val redstone = getIngredientStack(CustomItemType.ENCHANTED_REDSTONE, 16)
                val glowstone = getIngredientStack(Material.GLOWSTONE, 40)
                val gravitite = getIngredientStack(CustomItemType.GRAVITITE_SHARDS, 8)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 16)
                return EnchantmentRecipe(getRecipeKey(level), 60, sugar, redstone, glowstone, gravitite, lapis)
            }

            9 -> {
                val sugar = getIngredientStack(CustomItemType.ENCHANTED_SUGAR, 64)
                val redstone = getIngredientStack(CustomItemType.ENCHANTED_REDSTONE, 32)
                val glowstone = getIngredientStack(Material.GLOWSTONE, 80)
                val gravitite = getIngredientStack(CustomItemType.GRAVITITE_SHARDS, 16)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 32)
                return EnchantmentRecipe(getRecipeKey(level), 70, sugar, redstone, glowstone, gravitite, lapis)
            }

            10 -> {
                val sugar = getIngredientStack(CustomItemType.ENCHANTED_SUGAR_CANE, 15)
                val redstone = getIngredientStack(CustomItemType.ENCHANTED_REDSTONE, 32)
                val glowstone = getIngredientStack(CustomItemType.ENCHANTED_GLOWSTONE, 18)
                val gravitite = getIngredientStack(CustomItemType.GRAVITITE_SHARDS, 32)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 64)
                return EnchantmentRecipe(getRecipeKey(level), 80, sugar, redstone, glowstone, gravitite, lapis)
            }
            else -> return null
        }
    }

    companion object {
        fun getMiningEfficiency(level: Int): Int { return level * 150 }
    }
}
