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

class LureEnchantment(key: TypedKey<Enchantment>) : VanillaEnchantment(key), AttributeEnchantment {
    override val displayName: Component get() = ComponentUtils.create("Lure")
    override val description: Component get() = ComponentUtils.merge(
            ComponentUtils.create("Increases "),
            ComponentUtils.create(AttributeWrapper.FISHING_SPEED.DisplayName, NamedTextColor.AQUA),
            ComponentUtils.create(" by "),
            ComponentUtils.create(
                String.format("+%d", getSpeedIncrease(level)),
                NamedTextColor.GREEN
            )
        )
    override val scrollBindingColor: Color get() = Color.fromRGB(40, 252, 231)

    override val itemTypeTag: TagKey<ItemType> get()           = ItemTypeTagKeys.ENCHANTABLE_FISHING
    override val skillRequirement: Int get()                   = 13
    override val maxLevel: Int get()                           = 5
    override val weight: Int get()                             = EnchantmentRarity.COMMON.weight
    override val equipmentSlotGroup: EquipmentSlotGroup? get() = EquipmentSlotGroup.HAND

    override val powerRating : Int get() = level / 2
    override val attributeModifierType : AttributeModifierType get() = AttributeModifierType.ENCHANTMENT
    override fun getHeldAttributes() : MutableCollection<AttributeEntry?>? {
        return mutableListOf(
            AdditiveAttributeEntry(AttributeWrapper.FISHING_SPEED, getSpeedIncrease(level).toDouble())
        )
    }


    override fun getRecipe(level: Int): EnchantmentRecipe? {
        when (level) {
            1 -> {
                val essence = getIngredientStack(CustomItemType.COMMON_FISH_ESSENCE, 16)
                val kelp = getIngredientStack(Material.KELP, 32)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 8)
                return EnchantmentRecipe(getRecipeKey(level), 13, essence, kelp, lapis)
            }

            2 -> {
                val essence = getIngredientStack(CustomItemType.COMMON_FISH_ESSENCE, 32)
                val prismarine = getIngredientStack(Material.PRISMARINE_SHARD, 16)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 16)
                return EnchantmentRecipe(getRecipeKey(level), 30, essence, prismarine, lapis)
            }

            3 -> {
                val essence = getIngredientStack(CustomItemType.UNCOMMON_FISH_ESSENCE, 16)
                val prismarine = getIngredientStack(Material.PRISMARINE_SHARD, 32)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 8)
                return EnchantmentRecipe(getRecipeKey(level), 48, essence, prismarine, lapis)
            }

            4 -> {
                val essence = getIngredientStack(CustomItemType.RARE_FISH_ESSENCE, 16)
                val crystals = getIngredientStack(Material.PRISMARINE_CRYSTALS, 16)
                val nautilus = getIngredientStack(Material.NAUTILUS_SHELL, 8)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 16)
                return EnchantmentRecipe(getRecipeKey(level), 65, essence, crystals, nautilus, lapis)
            }

            5 -> {
                val essence = getIngredientStack(CustomItemType.EPIC_FISH_ESSENCE, 8)
                val crystals = getIngredientStack(Material.PRISMARINE_CRYSTALS, 32)
                val heart = getIngredientStack(Material.HEART_OF_THE_SEA, 1)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 16)
                return EnchantmentRecipe(getRecipeKey(level), 82, essence, crystals, heart, lapis)
            }
            else -> return null
        }
    }

    companion object {
        fun getSpeedIncrease(level: Int): Int {
            return when (level) {
                1 -> 30
                2 -> 60
                3 -> 90
                4 -> 120
                5 -> 150
                else -> 0
            }
        }
    }
}
