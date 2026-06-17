package xyz.devvydont.smprpg.enchantments.definitions

import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys
import io.papermc.paper.registry.tag.TagKey
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemType
import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.enchantments.CustomEnchantment
import xyz.devvydont.smprpg.enchantments.EnchantmentRarity
import xyz.devvydont.smprpg.enchantments.ScrollColor
import xyz.devvydont.smprpg.enchantments.base.AttributeEnchantment
import xyz.devvydont.smprpg.enchantments.recipe.EnchantmentRecipe
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.attribute.AttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeModifierType
import xyz.devvydont.smprpg.util.formatting.ComponentUtils

class AmplificationBlessing(id: String) : CustomEnchantment(id), AttributeEnchantment {

    override val displayName: Component get() = ComponentUtils.create("Blessing of Amplification", NamedTextColor.YELLOW)
    override val description: Component
        get() = ComponentUtils.merge(
        ComponentUtils.create("Increases damage by "),
        ComponentUtils.create(
            "+" + getDamageIncrease(level) + "%",
            NamedTextColor.GREEN
        )
    )
    override val enchantColor: TextColor get()   = NamedTextColor.YELLOW
    override val scrollColor: Color get()        = ScrollColor.BLESSING.color
    override val scrollBindingColor: Color get() = Color.fromRGB(255, 95, 184)

    override val itemTypeTag: TagKey<ItemType> get()           = ItemTypeTagKeys.ENCHANTABLE_WEAPON
    override val maxLevel: Int get()                           = 5
    override val weight: Int get()                             = EnchantmentRarity.BLESSING.weight
    override val isBlessing: Boolean get()                     = true
    override val equipmentSlotGroup: EquipmentSlotGroup? get() = EquipmentSlotGroup.MAINHAND
    override val skillRequirement: Int get()                   = 0

    override val powerRating : Int get() = level
    override val attributeModifierType : AttributeModifierType get() = AttributeModifierType.ENCHANTMENT
    override fun getHeldAttributes() : MutableCollection<AttributeEntry?>? {
        return mutableListOf(
            AttributeEntry.multiplicative(AttributeWrapper.STRENGTH, getDamageIncrease(level) / 100.0)
        )
    }

    override fun getRecipe(level: Int): EnchantmentRecipe? {
        when (level) {
            1 -> {
                val blaze = getIngredientStack(Material.BLAZE_POWDER, 64)
                val wart = getIngredientStack(Material.NETHER_WART, 64)
                val amethyst = getIngredientStack(CustomItemType.ENCHANTED_AMETHYST_BLOCK, 1)
                val lapis = getIngredientStack(CustomItemType.MITHRIL_BLOCK, 8)
                return EnchantmentRecipe(getRecipeKey(level), 20, blaze, wart, amethyst, lapis)
            }

            2 -> {
                val blaze = getIngredientStack(CustomItemType.PREMIUM_BLAZE_ROD, 16)
                val wart = getIngredientStack(Material.NETHER_WART_BLOCK, 64)
                val amethyst = getIngredientStack(CustomItemType.ENCHANTED_AMETHYST_BLOCK, 2)
                val spellPowder = getIngredientStack(CustomItemType.PREMIUM_SPELL_POWDER, 16)
                val lapis = getIngredientStack(CustomItemType.MITHRIL_BLOCK, 32)
                return EnchantmentRecipe(getRecipeKey(level), 35, blaze, wart, amethyst, spellPowder, lapis)
            }

            3 -> {
                val blaze = getIngredientStack(CustomItemType.PREMIUM_BLAZE_ROD, 64)
                val wart = getIngredientStack(CustomItemType.PREMIUM_MEMBRANE, 8)
                val amethyst = getIngredientStack(CustomItemType.ENCHANTED_AMETHYST_BLOCK, 8)
                val spellPowder = getIngredientStack(CustomItemType.PREMIUM_SPELL_POWDER, 64)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_MITHRIL, 15)
                return EnchantmentRecipe(getRecipeKey(level), 50, blaze, wart, amethyst, spellPowder, lapis)
            }

            4 -> {
                val blaze = getIngredientStack(Material.ECHO_SHARD, 1)
                val wart = getIngredientStack(CustomItemType.PREMIUM_MEMBRANE, 32)
                val amethyst = getIngredientStack(CustomItemType.ENCHANTED_AMETHYST_BLOCK, 32)
                val spellPowder = getIngredientStack(CustomItemType.ENCHANTED_SPELL_POWDER, 16)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_MITHRIL, 60)
                return EnchantmentRecipe(getRecipeKey(level), 65, blaze, wart, amethyst, spellPowder, lapis)
            }

            5 -> {
                val blaze = getIngredientStack(CustomItemType.PREMIUM_ECHO_SHARD, 8)
                val wart = getIngredientStack(CustomItemType.ENCHANTED_MEMBRANE, 16)
                val amethyst = getIngredientStack(CustomItemType.DRAGONSTEEL_BLOCK, 4)
                val spellPowder = getIngredientStack(CustomItemType.ENCHANTED_SPELL_POWDER, 64)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_MITHRIL_BLOCK, 15)
                return EnchantmentRecipe(getRecipeKey(level), 80, blaze, wart, amethyst, spellPowder, lapis)
            }

            else -> {
                return null
            }
        }
    }

    companion object {
        fun getDamageIncrease(level: Int): Int { return level * 20 }
    }
}
