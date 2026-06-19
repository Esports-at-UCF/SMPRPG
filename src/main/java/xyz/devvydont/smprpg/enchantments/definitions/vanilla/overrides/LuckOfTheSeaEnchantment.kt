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
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeModifierType
import xyz.devvydont.smprpg.util.formatting.ComponentUtils

class LuckOfTheSeaEnchantment(key: TypedKey<Enchantment>) : VanillaEnchantment(key), AttributeEnchantment {
    override val displayName: Component get() = ComponentUtils.create("Luck of the Sea")
    override val description: Component get() = ComponentUtils.merge(
            ComponentUtils.create("Increases "),
            ComponentUtils.create(AttributeWrapper.FISHING_RATING.DisplayName, NamedTextColor.GOLD),
            ComponentUtils.create(" rating by "),
            ComponentUtils.create(
                String.format(
                    "+%d",
                    getRatingIncrease(level)
                ), NamedTextColor.GREEN
            )
        )
    override val scrollBindingColor: Color get() = Color.fromRGB(86, 199, 188)

    override val itemTypeTag: TagKey<ItemType> get()           = ItemTypeTagKeys.ENCHANTABLE_FISHING
    override val maxLevel: Int get()                           = 5
    override val weight: Int get()                             = EnchantmentRarity.UNCOMMON.weight
    override val equipmentSlotGroup: EquipmentSlotGroup? get() = EquipmentSlotGroup.HAND
    override val skillRequirement: Int get()                   = 0

    override val powerRating : Int get() = level / 2
    override val attributeModifierType : AttributeModifierType get() = AttributeModifierType.ENCHANTMENT
    override fun getHeldAttributes() : MutableCollection<AttributeEntry?>? {
        return mutableListOf(
            AdditiveAttributeEntry(AttributeWrapper.FISHING_RATING, getRatingIncrease(level).toDouble())
        )
    }

    override fun getRecipe(level: Int): EnchantmentRecipe? {
        when (level) {
            1 -> {
                val essence = getIngredientStack(CustomItemType.COMMON_FISH_ESSENCE, 16)
                val nautilus = getIngredientStack(Material.NAUTILUS_SHELL, 2)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 8)
                return EnchantmentRecipe(getRecipeKey(level), 0, essence, nautilus, lapis)
            }

            2 -> {
                val essence = getIngredientStack(CustomItemType.UNCOMMON_FISH_ESSENCE, 16)
                val nautilus = getIngredientStack(Material.NAUTILUS_SHELL, 4)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 16)
                return EnchantmentRecipe(getRecipeKey(level), 20, essence, nautilus, lapis)
            }

            3 -> {
                val essence = getIngredientStack(CustomItemType.RARE_FISH_ESSENCE, 16)
                val crystals = getIngredientStack(Material.PRISMARINE_CRYSTALS, 16)
                val heart = getIngredientStack(Material.HEART_OF_THE_SEA, 1)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 8)
                return EnchantmentRecipe(getRecipeKey(level), 40, essence, crystals, heart, lapis)
            }

            4 -> {
                val essence = getIngredientStack(CustomItemType.EPIC_FISH_ESSENCE, 16)
                val nautilus = getIngredientStack(Material.NAUTILUS_SHELL, 8)
                val heart = getIngredientStack(Material.HEART_OF_THE_SEA, 2)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 16)
                return EnchantmentRecipe(getRecipeKey(level), 60, essence, nautilus, heart, lapis)
            }

            5 -> {
                val essence = getIngredientStack(CustomItemType.LEGENDARY_FISH_ESSENCE, 16)
                val caviar = getIngredientStack(CustomItemType.CAVIAR, 4)
                val heart = getIngredientStack(Material.HEART_OF_THE_SEA, 4)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 16)
                return EnchantmentRecipe(getRecipeKey(level), 80, essence, caviar, heart, lapis)
            }
            else -> return null
        }
    }

    companion object {
        fun getRatingIncrease(level: Int): Int {
            return when (level) {
                1 -> 10
                2 -> 25
                3 -> 45
                4 -> 70
                5 -> 100
                else -> 0
            }
        }
    }
}
