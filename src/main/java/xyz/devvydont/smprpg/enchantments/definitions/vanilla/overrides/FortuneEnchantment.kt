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

class FortuneEnchantment(key: TypedKey<Enchantment>) : VanillaEnchantment(key), AttributeEnchantment {
    override val displayName: Component get() = ComponentUtils.create("Fortune")
    override val description: Component get() = ComponentUtils.merge(
            ComponentUtils.create("Increases "),
            ComponentUtils.create(AttributeWrapper.MINING_FORTUNE.DisplayName, NamedTextColor.GOLD),
            ComponentUtils.create(" by "),
            ComponentUtils.create(
                String.format("+%d", getFortune(level)),
                NamedTextColor.GREEN
            )
        )
    override val scrollBindingColor: Color get() = Color.fromRGB(131, 255, 120)

    override val itemTypeTag: TagKey<ItemType> get()           = ItemTypeTagKeys.PICKAXES
    override val maxLevel: Int get()                           = 10
    override val weight: Int get()                             = EnchantmentRarity.UNCOMMON.weight
    override val equipmentSlotGroup: EquipmentSlotGroup? get() = EquipmentSlotGroup.MAINHAND
    override val skillRequirement: Int get()                   = 0

    override val powerRating : Int get() = level / 3
    override val attributeModifierType : AttributeModifierType get() = AttributeModifierType.ENCHANTMENT
    override fun getHeldAttributes(): MutableCollection<AttributeEntry?>{
        return mutableListOf(
            AttributeEntry.additive(AttributeWrapper.MINING_FORTUNE, getFortune(level).toDouble())
        )
    }

    override fun getRecipe(level: Int): EnchantmentRecipe? {
        when (level) {
            1 -> {
                val gold = getIngredientStack(Material.GOLD_INGOT, 8)
                val diamond = getIngredientStack(Material.DIAMOND, 4)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 8)
                return EnchantmentRecipe(getRecipeKey(level), 0, gold, diamond, lapis)
            }

            2 -> {
                val gold = getIngredientStack(Material.GOLD_INGOT, 16)
                val diamond = getIngredientStack(Material.DIAMOND, 8)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 16)
                return EnchantmentRecipe(getRecipeKey(level), 5, gold, diamond, lapis)
            }

            3 -> {
                val gold = getIngredientStack(Material.GOLD_INGOT, 32)
                val diamond = getIngredientStack(Material.DIAMOND, 16)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 32)
                return EnchantmentRecipe(getRecipeKey(level), 10, gold, diamond, lapis)
            }

            4 -> {
                val gold = getIngredientStack(Material.GOLD_INGOT, 64)
                val diamond = getIngredientStack(Material.DIAMOND, 32)
                val mithril = getIngredientStack(CustomItemType.MITHRIL_INGOT, 64)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 64)
                return EnchantmentRecipe(getRecipeKey(level), 20, gold, diamond, mithril, lapis)
            }

            5 -> {
                val gold = getIngredientStack(Material.GOLD_BLOCK, 15)
                val diamond = getIngredientStack(Material.DIAMOND, 64)
                val mithril = getIngredientStack(CustomItemType.MITHRIL_BLOCK, 15)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 16)
                return EnchantmentRecipe(getRecipeKey(level), 30, gold, diamond, mithril, lapis)
            }

            6 -> {
                val gold = getIngredientStack(Material.GOLD_BLOCK, 30)
                val diamond = getIngredientStack(Material.DIAMOND_BLOCK, 15)
                val mithril = getIngredientStack(CustomItemType.MITHRIL_BLOCK, 30)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 32)
                return EnchantmentRecipe(getRecipeKey(level), 40, gold, diamond, mithril, lapis)
            }

            7 -> {
                val gold = getIngredientStack(Material.GOLD_BLOCK, 60)
                val diamond = getIngredientStack(Material.DIAMOND_BLOCK, 30)
                val mithril = getIngredientStack(CustomItemType.MITHRIL_BLOCK, 60)
                val platinum = getIngredientStack(CustomItemType.PLATINUM_BLOCK, 4)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 64)
                return EnchantmentRecipe(getRecipeKey(level), 50, gold, diamond, mithril, platinum, lapis)
            }

            8 -> {
                val gold = getIngredientStack(CustomItemType.ENCHANTED_GOLD, 14)
                val diamond = getIngredientStack(Material.DIAMOND_BLOCK, 60)
                val mithril = getIngredientStack(CustomItemType.ENCHANTED_MITHRIL, 14)
                val platinum = getIngredientStack(CustomItemType.PLATINUM_BLOCK, 8)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 16)
                return EnchantmentRecipe(getRecipeKey(level), 60, gold, diamond, mithril, platinum, lapis)
            }

            9 -> {
                val gold = getIngredientStack(CustomItemType.ENCHANTED_GOLD, 28)
                val diamond = getIngredientStack(CustomItemType.ENCHANTED_DIAMOND, 14)
                val mithril = getIngredientStack(CustomItemType.ENCHANTED_MITHRIL, 28)
                val platinum = getIngredientStack(CustomItemType.PLATINUM_BLOCK, 16)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 32)
                return EnchantmentRecipe(getRecipeKey(level), 70, gold, diamond, mithril, platinum, lapis)
            }

            10 -> {
                val gold = getIngredientStack(CustomItemType.ENCHANTED_GOLD, 56)
                val diamond = getIngredientStack(CustomItemType.ENCHANTED_DIAMOND, 28)
                val mithril = getIngredientStack(CustomItemType.ENCHANTED_MITHRIL, 56)
                val platinum = getIngredientStack(CustomItemType.PLATINUM_BLOCK, 32)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 64)
                return EnchantmentRecipe(getRecipeKey(level), 80, gold, diamond, mithril, platinum, lapis)
            }
            else -> return null
        }
    }

    companion object {
        @JvmStatic
        fun getFortune(level: Int): Int {
            return when (level) {
                0 -> 0
                1 -> 10
                2 -> 25
                3 -> 40
                4 -> 55
                5 -> 70
                6 -> 85
                7 -> 100
                8 -> 115
                9 -> 130
                10 -> 150
                else -> level * 25 + getFortune(10)
            }
        }
    }
}
