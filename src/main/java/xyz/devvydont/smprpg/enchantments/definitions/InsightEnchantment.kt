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

class InsightEnchantment(id: String) : CustomEnchantment(id), AttributeEnchantment {
    override val displayName: Component get() = ComponentUtils.create("Insight")
    override val description: Component get() = ComponentUtils.merge(
            ComponentUtils.create("Increases "),
            ComponentUtils.create(AttributeWrapper.ARCANE_RATING.DisplayName, NamedTextColor.GOLD),
            ComponentUtils.create(" by "),
            ComponentUtils.create(
                "+" + getArcaneRatingIncrease(level),
                NamedTextColor.AQUA
            )
        )
    override val scrollBindingColor: Color get() = Color.fromRGB(11, 128, 108)

    override val itemTypeTag: TagKey<ItemType> get()           = ItemTypeTagKeys.ENCHANTABLE_WEAPON
    override val maxLevel: Int get()                           = 5
    override val weight: Int get()                             = EnchantmentRarity.UNCOMMON.weight
    override val equipmentSlotGroup: EquipmentSlotGroup? get() = EquipmentSlotGroup.ARMOR
    override val skillRequirement: Int get()                   = 4

    override val powerRating : Int get() = level / 2 + 1
    override val attributeModifierType : AttributeModifierType get() = AttributeModifierType.ENCHANTMENT
    override fun getHeldAttributes() : MutableCollection<AttributeEntry?>? {
        return mutableListOf(
            AdditiveAttributeEntry(AttributeWrapper.ARCANE_RATING, getArcaneRatingIncrease(level).toDouble())
        )
    }

    override fun getRecipe(level: Int): EnchantmentRecipe? {
        when (level) {
            1 -> {
                val powder = getIngredientStack(CustomItemType.SPELL_POWDER, 16)
                val amethyst = getIngredientStack(Material.AMETHYST_SHARD, 32)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 8)
                return EnchantmentRecipe(getRecipeKey(level), 4, powder, amethyst, lapis)
            }

            2 -> {
                val powder = getIngredientStack(CustomItemType.SPELL_POWDER, 32)
                val amethyst = getIngredientStack(Material.AMETHYST_SHARD, 64)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 16)
                return EnchantmentRecipe(getRecipeKey(level), 23, powder, amethyst, lapis)
            }

            3 -> {
                val powder = getIngredientStack(CustomItemType.PREMIUM_SPELL_POWDER, 16)
                val amethyst = getIngredientStack(CustomItemType.ENCHANTED_AMETHYST, 16)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 8)
                return EnchantmentRecipe(getRecipeKey(level), 42, powder, amethyst, lapis)
            }

            4 -> {
                val powder = getIngredientStack(CustomItemType.PREMIUM_SPELL_POWDER, 32)
                val amethyst = getIngredientStack(CustomItemType.ENCHANTED_AMETHYST_BLOCK, 4)
                val quartz = getIngredientStack(CustomItemType.ENCHANTED_QUARTZ, 16)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 16)
                return EnchantmentRecipe(getRecipeKey(level), 62, powder, amethyst, quartz, lapis)
            }

            5 -> {
                val powder = getIngredientStack(CustomItemType.ENCHANTED_SPELL_POWDER, 16)
                val amethyst = getIngredientStack(CustomItemType.ENCHANTED_AMETHYST_BLOCK, 16)
                val quartz = getIngredientStack(CustomItemType.ENCHANTED_QUARTZ, 32)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 16)
                return EnchantmentRecipe(getRecipeKey(level), 80, powder, amethyst, quartz, lapis)
            }
            else -> return null
        }
    }

    companion object {
        fun getArcaneRatingIncrease(level: Int): Int { return 5 * level }
    }
}
