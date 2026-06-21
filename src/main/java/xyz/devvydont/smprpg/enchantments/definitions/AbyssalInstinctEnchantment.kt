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
import xyz.devvydont.smprpg.entity.fishing.SeaCreature
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeModifierType
import xyz.devvydont.smprpg.util.formatting.ComponentUtils

class AbyssalInstinctEnchantment(id: String) : CustomEnchantment(id), AttributeEnchantment {
    override val displayName: Component get() = ComponentUtils.create("Abyssal Instinct")
    override val description: Component get() = ComponentUtils.merge(
            ComponentUtils.create("Increases "),
            ComponentUtils.create(AttributeWrapper.FISHING_CREATURE_CHANCE.DisplayName, SeaCreature.NAME_COLOR),
            ComponentUtils.create(" rating by "),
            ComponentUtils.create(
                String.format(
                    "+%d",
                    (TreasureHunterEnchantment.getTreasureChance(level) * 2).toInt()
                ), NamedTextColor.GREEN
            )
        )
    override val scrollBindingColor: Color get() = Color.fromRGB(0, 0, 61)

    override val itemTypeTag: TagKey<ItemType> get()           = ItemTypeTagKeys.ENCHANTABLE_FISHING
    override val maxLevel: Int get()                           = 5
    override val weight: Int get()                             = EnchantmentRarity.UNCOMMON.weight
    override val equipmentSlotGroup: EquipmentSlotGroup? get() = EquipmentSlotGroup.HAND
    override val skillRequirement: Int get()                   = 5

    override val powerRating: Int get() = level / 2
    override val attributeModifierType: AttributeModifierType = AttributeModifierType.ENCHANTMENT
    override fun getHeldAttributes() : MutableCollection<AttributeEntry?>? {
        return mutableListOf(
            AdditiveAttributeEntry(
                AttributeWrapper.FISHING_CREATURE_CHANCE, TreasureHunterEnchantment.getTreasureChance(level) * 2
            )
        )
    }

    override fun getRecipe(level: Int): EnchantmentRecipe? {
        when (level) {
            1 -> {
                val essence = getIngredientStack(CustomItemType.COMMON_FISH_ESSENCE, 16)
                val ink = getIngredientStack(Material.INK_SAC, 32)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 8)
                return EnchantmentRecipe(getRecipeKey(level), 5, essence, ink, lapis)
            }

            2 -> {
                val essence = getIngredientStack(CustomItemType.UNCOMMON_FISH_ESSENCE, 16)
                val ink = getIngredientStack(Material.INK_SAC, 64)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 16)
                return EnchantmentRecipe(getRecipeKey(level), 24, essence, ink, lapis)
            }

            3 -> {
                val essence = getIngredientStack(CustomItemType.RARE_FISH_ESSENCE, 16)
                val ink = getIngredientStack(CustomItemType.PREMIUM_INK_SAC, 16)
                val sharkFin = getIngredientStack(CustomItemType.SHARK_FIN, 4)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 8)
                return EnchantmentRecipe(getRecipeKey(level), 43, essence, ink, sharkFin, lapis)
            }

            4 -> {
                val essence = getIngredientStack(CustomItemType.EPIC_FISH_ESSENCE, 16)
                val ink = getIngredientStack(CustomItemType.ENCHANTED_INK_SAC, 8)
                val nautilus = getIngredientStack(Material.NAUTILUS_SHELL, 16)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 16)
                return EnchantmentRecipe(getRecipeKey(level), 61, essence, ink, nautilus, lapis)
            }

            5 -> {
                val essence = getIngredientStack(CustomItemType.LEGENDARY_FISH_ESSENCE, 8)
                val ink = getIngredientStack(CustomItemType.ENCHANTED_INK_SAC, 16)
                val heart = getIngredientStack(Material.HEART_OF_THE_SEA, 2)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 16)
                return EnchantmentRecipe(getRecipeKey(level), 80, essence, ink, heart, lapis)
            }
            else -> return null
        }
    }
}
