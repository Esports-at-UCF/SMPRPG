package xyz.devvydont.smprpg.enchantments.definitions.vanilla.overrides

import io.papermc.paper.registry.TypedKey
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys
import io.papermc.paper.registry.tag.TagKey
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.Listener
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemType
import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.enchantments.EnchantmentRarity
import xyz.devvydont.smprpg.enchantments.base.AttributeEnchantment
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.VanillaEnchantment
import xyz.devvydont.smprpg.enchantments.recipe.EnchantmentRecipe
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeModifierType
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry
import xyz.devvydont.smprpg.util.formatting.ComponentUtils
import kotlin.math.max
import kotlin.math.min

class FeatherFallingEnchantment(key: TypedKey<Enchantment>) : VanillaEnchantment(key), AttributeEnchantment, Listener {
    override val displayName: Component get() = ComponentUtils.create("Feather Falling")
    override val description: Component get() = ComponentUtils.merge(
            ComponentUtils.create("Resists "),
            ComponentUtils.create(
                getFallResistPercent(level).toString() + "%",
                NamedTextColor.GREEN
            ),
            ComponentUtils.create(" of fall damage")
        )
    override val scrollBindingColor: Color get() = Color.fromRGB(255, 255, 255)

    override val itemTypeTag: TagKey<ItemType> get()           = ItemTypeTagKeys.ENCHANTABLE_FOOT_ARMOR
    override val maxLevel: Int get()                           = 10
    override val weight: Int get()                             = EnchantmentRarity.COMMON.weight
    override val equipmentSlotGroup: EquipmentSlotGroup? get() = EquipmentSlotGroup.FEET
    override val skillRequirement: Int get()                   = 9

    override val powerRating : Int get() = level / 5
    override val attributeModifierType: AttributeModifierType = AttributeModifierType.ENCHANTMENT
    override fun getHeldAttributes() : MutableCollection<AttributeEntry?>? {
        return mutableListOf(
            MultiplicativeAttributeEntry(AttributeWrapper.FALL_DAMAGE_MULTIPLIER, -getFallResistPercent(level) / 100.0),
            AdditiveAttributeEntry(AttributeWrapper.SAFE_FALL, (level * 2).toDouble())
        )
    }

    override fun getRecipe(level: Int): EnchantmentRecipe? {
        when (level) {
            1 -> {
                val feather = getIngredientStack(Material.FEATHER, 4)
                val wool = getIngredientStack(Material.WHITE_WOOL, 2)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 4)
                return EnchantmentRecipe(getRecipeKey(level), 0, feather, wool, lapis)
            }

            2 -> {
                val feather = getIngredientStack(Material.FEATHER, 8)
                val wool = getIngredientStack(Material.WHITE_WOOL, 4)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 8)
                return EnchantmentRecipe(getRecipeKey(level), 5, feather, wool, lapis)
            }

            3 -> {
                val feather = getIngredientStack(Material.FEATHER, 16)
                val wool = getIngredientStack(Material.WHITE_WOOL, 8)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 16)
                return EnchantmentRecipe(getRecipeKey(level), 10, feather, wool, lapis)
            }

            4 -> {
                val feather = getIngredientStack(Material.FEATHER, 32)
                val wool = getIngredientStack(Material.WHITE_WOOL, 16)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 32)
                return EnchantmentRecipe(getRecipeKey(level), 15, feather, wool, lapis)
            }

            5 -> {
                val feather = getIngredientStack(Material.FEATHER, 64)
                val wool = getIngredientStack(Material.WHITE_WOOL, 32)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 64)
                return EnchantmentRecipe(getRecipeKey(level), 20, feather, wool, lapis)
            }

            6 -> {
                val feather = getIngredientStack(CustomItemType.PREMIUM_FEATHER, 15)
                val wool = getIngredientStack(Material.WHITE_WOOL, 64)
                val aercloud = getIngredientStack(CustomItemType.COLD_AERCLOUD, 4)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 16)
                return EnchantmentRecipe(getRecipeKey(level), 25, feather, wool, aercloud, lapis)
            }

            7 -> {
                val feather = getIngredientStack(CustomItemType.PREMIUM_FEATHER, 15)
                val membrane = getIngredientStack(Material.PHANTOM_MEMBRANE, 5)
                val aercloud = getIngredientStack(CustomItemType.COLD_AERCLOUD, 8)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 32)
                return EnchantmentRecipe(getRecipeKey(level), 30, feather, membrane, aercloud, lapis)
            }

            8 -> {
                val feather = getIngredientStack(CustomItemType.PREMIUM_FEATHER, 30)
                val membrane = getIngredientStack(Material.PHANTOM_MEMBRANE, 10)
                val aercloud = getIngredientStack(CustomItemType.COLD_AERCLOUD, 16)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 64)
                return EnchantmentRecipe(getRecipeKey(level), 35, feather, membrane, aercloud, lapis)
            }

            9 -> {
                val feather = getIngredientStack(CustomItemType.PREMIUM_FEATHER, 30)
                val membrane = getIngredientStack(Material.PHANTOM_MEMBRANE, 20)
                val aercloud = getIngredientStack(CustomItemType.COLD_AERCLOUD, 16)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 16)
                return EnchantmentRecipe(getRecipeKey(level), 40, feather, membrane, aercloud, lapis)
            }

            10 -> {
                val feather = getIngredientStack(CustomItemType.PREMIUM_FEATHER, 60)
                val membrane = getIngredientStack(Material.PHANTOM_MEMBRANE, 40)
                val aercloud = getIngredientStack(CustomItemType.COLD_AERCLOUD, 32)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 32)
                return EnchantmentRecipe(getRecipeKey(level), 50, feather, membrane, aercloud, lapis)
            }
            else -> return null
        }
    }

    companion object {
        fun getFallResistPercent(level: Int): Int { return min(max(0, level * 9), 99) }
    }
}
