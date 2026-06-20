package xyz.devvydont.smprpg.enchantments.definitions

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
import xyz.devvydont.smprpg.util.persistence.KeyStore

class AptitudeEnchantment(id: String) : CustomEnchantment(id), AttributeEnchantment {
    override val displayName: Component get() = ComponentUtils.create("Aptitude")
    override val description: Component get() = ComponentUtils.merge(
            ComponentUtils.create("Increases "),
            ComponentUtils.create(AttributeWrapper.INTELLIGENCE.DisplayName, NamedTextColor.GOLD),
            ComponentUtils.create(" by "),
            ComponentUtils.create(
                "+" + getIntelligenceIncrease(level),
                NamedTextColor.AQUA
            )
        )
    override val scrollBindingColor: Color get() = Color.fromRGB(0, 255, 255)

    override val itemTypeTag: TagKey<ItemType> get()           = KeyStore.ENCHANTABLE_APTITUDE
    override val maxLevel: Int get()                           = 5
    override val weight: Int get()                             = EnchantmentRarity.COMMON.weight
    override val equipmentSlotGroup: EquipmentSlotGroup? get() = EquipmentSlotGroup.ANY
    override val skillRequirement: Int get()                   = 10

    override val powerRating : Int get() = level / 2 + 1
    override val attributeModifierType : AttributeModifierType get() = AttributeModifierType.ENCHANTMENT
    override fun getHeldAttributes() : MutableCollection<AttributeEntry?>? {
        return mutableListOf(
            AdditiveAttributeEntry(AttributeWrapper.INTELLIGENCE, getIntelligenceIncrease(level).toDouble())
        )
    }

    override fun getRecipe(level: Int): EnchantmentRecipe? {
        when (level) {
            1 -> {
                val powder = getIngredientStack(CustomItemType.SPELL_POWDER, 16)
                val carrot = getIngredientStack(CustomItemType.PREMIUM_CARROT, 32)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 8)
                return EnchantmentRecipe(getRecipeKey(level), 4, powder, carrot, lapis)
            }

            2 -> {
                val powder = getIngredientStack(CustomItemType.SPELL_POWDER, 32)
                val carrot = getIngredientStack(CustomItemType.PREMIUM_CARROT, 64)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 16)
                return EnchantmentRecipe(getRecipeKey(level), 23, powder, carrot, lapis)
            }

            3 -> {
                val powder = getIngredientStack(CustomItemType.PREMIUM_SPELL_POWDER, 16)
                val carrot = getIngredientStack(CustomItemType.PREMIUM_GOLDEN_CARROT, 16)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 8)
                return EnchantmentRecipe(getRecipeKey(level), 42, powder, carrot, lapis)
            }

            4 -> {
                val powder = getIngredientStack(CustomItemType.PREMIUM_SPELL_POWDER, 32)
                val carrot = getIngredientStack(CustomItemType.PREMIUM_GOLDEN_CARROT, 32)
                val pearl = getIngredientStack(CustomItemType.ENCHANTED_ENDER_PEARL, 8)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 16)
                return EnchantmentRecipe(getRecipeKey(level), 62, powder, carrot, pearl, lapis)
            }

            5 -> {
                val powder = getIngredientStack(CustomItemType.ENCHANTED_SPELL_POWDER, 16)
                val carrot = getIngredientStack(CustomItemType.PREMIUM_GOLDEN_CARROT, 64)
                val pearl = getIngredientStack(CustomItemType.ENCHANTED_ENDER_PEARL, 16)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 16)
                return EnchantmentRecipe(getRecipeKey(level), 80, powder, carrot, pearl, lapis)
            }
            else -> return null
        }
    }

    companion object {
        fun getIntelligenceIncrease(level: Int): Int {
            return when (level) {
                0 -> 0
                1 -> 40
                2 -> 80
                3 -> 120
                4 -> 160
                5 -> 200
                else -> getIntelligenceIncrease(5) + 40 * (level - 5)
            }
        }
    }
}
