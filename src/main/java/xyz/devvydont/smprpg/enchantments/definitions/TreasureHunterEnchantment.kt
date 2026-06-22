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

class TreasureHunterEnchantment(id: String) : CustomEnchantment(id), AttributeEnchantment {
    override val displayName: Component get() = ComponentUtils.create("Treasure Hunter")
    override val description: Component get() = ComponentUtils.merge(
            ComponentUtils.create("Increases "),
            ComponentUtils.create(AttributeWrapper.FISHING_TREASURE_CHANCE.DisplayName, NamedTextColor.GOLD),
            ComponentUtils.create(" rating by "),
            ComponentUtils.create(
                String.format(
                    "+%.2f",
                    getTreasureChance(level)
                ), NamedTextColor.GREEN
            )
        )
    override val scrollBindingColor: Color get() = Color.fromRGB(255, 212, 23)

    override val itemTypeTag: TagKey<ItemType> get()           = ItemTypeTagKeys.ENCHANTABLE_FISHING
    override val maxLevel: Int get()                           = 5
    override val weight: Int get()                             = EnchantmentRarity.UNCOMMON.weight
    override val equipmentSlotGroup: EquipmentSlotGroup? get() = EquipmentSlotGroup.HAND
    override val skillRequirement: Int get()                   = 10

    override val powerRating : Int get() = level / 2
    override val attributeModifierType : AttributeModifierType get() = AttributeModifierType.ENCHANTMENT
    override fun getHeldAttributes() : MutableCollection<AttributeEntry?>? {
        return mutableListOf(
            AdditiveAttributeEntry(AttributeWrapper.FISHING_TREASURE_CHANCE, getTreasureChance(level))
        )
    }

    override fun getRecipe(level: Int): EnchantmentRecipe? {
        when (level) {
            1 -> {
                val essence = getIngredientStack(CustomItemType.COMMON_FISH_ESSENCE, 16)
                val gold = getIngredientStack(Material.GOLD_INGOT, 16)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 8)
                return EnchantmentRecipe(getRecipeKey(level), 10, essence, gold, lapis)
            }

            2 -> {
                val essence = getIngredientStack(CustomItemType.UNCOMMON_FISH_ESSENCE, 16)
                val gold = getIngredientStack(Material.GOLD_INGOT, 32)
                val nautilus = getIngredientStack(Material.NAUTILUS_SHELL, 8)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 16)
                return EnchantmentRecipe(getRecipeKey(level), 27, essence, gold, nautilus, lapis)
            }

            3 -> {
                val essence = getIngredientStack(CustomItemType.RARE_FISH_ESSENCE, 16)
                val gold = getIngredientStack(CustomItemType.ENCHANTED_GOLD, 4)
                val caviar = getIngredientStack(CustomItemType.CAVIAR, 4)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 8)
                return EnchantmentRecipe(getRecipeKey(level), 45, essence, gold, caviar, lapis)
            }

            4 -> {
                val essence = getIngredientStack(CustomItemType.EPIC_FISH_ESSENCE, 16)
                val gold = getIngredientStack(CustomItemType.ENCHANTED_GOLD, 8)
                val nautilus = getIngredientStack(CustomItemType.PREMIUM_NAUTILUS_SHELL, 4)
                val caviar = getIngredientStack(CustomItemType.CAVIAR, 8)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 16)
                return EnchantmentRecipe(getRecipeKey(level), 62, essence, gold, nautilus, caviar, lapis)
            }

            5 -> {
                val essence = getIngredientStack(CustomItemType.LEGENDARY_FISH_ESSENCE, 8)
                val gold = getIngredientStack(CustomItemType.ENCHANTED_GOLD_BLOCK, 4)
                val seaHeart = getIngredientStack(Material.HEART_OF_THE_SEA, 2)
                val voidHeart = getIngredientStack(CustomItemType.HEART_OF_THE_VOID, 2)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 16)
                return EnchantmentRecipe(getRecipeKey(level), 80, essence, gold, seaHeart, voidHeart, lapis)
            }

            else -> return null
        }
    }

    companion object {
        fun getTreasureChance(level: Int): Double {
            return when (level) {
                0 -> 0.0
                1 -> 1.0
                2 -> 2.0
                3 -> 3.0
                4 -> 4.0
                5 -> 5.0
                else -> 6.0
            }
        }
    }
}
