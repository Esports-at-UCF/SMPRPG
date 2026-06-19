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
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.overrides.FortuneEnchantment.Companion.getFortune
import xyz.devvydont.smprpg.enchantments.recipe.EnchantmentRecipe
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.attribute.AttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeModifierType
import xyz.devvydont.smprpg.util.formatting.ComponentUtils

class HarvestingEnchantment(id: String) : CustomEnchantment(id), AttributeEnchantment {
    override val displayName: Component get() = ComponentUtils.create("Harvesting")
    override val description: Component get() = ComponentUtils.merge(
            ComponentUtils.create("Increases "),
            ComponentUtils.create(AttributeWrapper.FARMING_FORTUNE.DisplayName, NamedTextColor.GOLD),
            ComponentUtils.create(" by "),
            ComponentUtils.create(
                String.format("+%d", getFortune(level)),
                NamedTextColor.GREEN
            )
        )
    override val scrollBindingColor: Color get() = Color.fromRGB(3, 94, 0)

   override val itemTypeTag: TagKey<ItemType> get()           = ItemTypeTagKeys.HOES
   override val maxLevel: Int get()                           = 10
   override val weight: Int get()                             = EnchantmentRarity.UNCOMMON.weight
   override val equipmentSlotGroup: EquipmentSlotGroup? get() = EquipmentSlotGroup.MAINHAND
   override val skillRequirement: Int get()                   = 0

    override val powerRating : Int get() = level / 3
    override val attributeModifierType : AttributeModifierType get() = AttributeModifierType.ENCHANTMENT
    override fun getHeldAttributes() : MutableCollection<AttributeEntry?>? {
        return mutableListOf(
            AttributeEntry.additive(AttributeWrapper.FARMING_FORTUNE, getFortune(level).toDouble())
        )
    }

    override fun getRecipe(level: Int): EnchantmentRecipe? {
        when (level) {
            1 -> {
                val wheat = getIngredientStack(Material.HAY_BLOCK, 8)
                val pumpkin = getIngredientStack(CustomItemType.PREMIUM_PUMPKIN, 4)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 8)
                return EnchantmentRecipe(getRecipeKey(level), 0, wheat, pumpkin, lapis)
            }

            2 -> {
                val wheat = getIngredientStack(Material.HAY_BLOCK, 16)
                val pumpkin = getIngredientStack(CustomItemType.PREMIUM_PUMPKIN, 8)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 16)
                return EnchantmentRecipe(getRecipeKey(level), 5, wheat, pumpkin, lapis)
            }

            3 -> {
                val wheat = getIngredientStack(Material.HAY_BLOCK, 32)
                val pumpkin = getIngredientStack(CustomItemType.PREMIUM_PUMPKIN, 16)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 32)
                return EnchantmentRecipe(getRecipeKey(level), 10, wheat, pumpkin, lapis)
            }

            4 -> {
                val wheat = getIngredientStack(Material.HAY_BLOCK, 64)
                val pumpkin = getIngredientStack(CustomItemType.PREMIUM_PUMPKIN, 32)
                val carrot = getIngredientStack(CustomItemType.PREMIUM_CARROT, 16)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 64)
                return EnchantmentRecipe(getRecipeKey(level), 20, wheat, pumpkin, carrot, lapis)
            }

            5 -> {
                val wheat = getIngredientStack(CustomItemType.PREMIUM_WHEAT, 16)
                val pumpkin = getIngredientStack(CustomItemType.PREMIUM_PUMPKIN, 64)
                val carrot = getIngredientStack(CustomItemType.PREMIUM_CARROT, 32)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 16)
                return EnchantmentRecipe(getRecipeKey(level), 30, wheat, pumpkin, carrot, lapis)
            }

            6 -> {
                val wheat = getIngredientStack(CustomItemType.PREMIUM_WHEAT, 32)
                val pumpkin = getIngredientStack(CustomItemType.ENCHANTED_PUMPKIN, 16)
                val carrot = getIngredientStack(CustomItemType.PREMIUM_CARROT, 64)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 32)
                return EnchantmentRecipe(getRecipeKey(level), 40, wheat, pumpkin, carrot, lapis)
            }

            7 -> {
                val wheat = getIngredientStack(CustomItemType.PREMIUM_WHEAT, 64)
                val pumpkin = getIngredientStack(CustomItemType.ENCHANTED_PUMPKIN, 32)
                val carrot = getIngredientStack(CustomItemType.PREMIUM_GOLDEN_CARROT, 16)
                val squash = getIngredientStack(CustomItemType.SQUASH, 8)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 64)
                return EnchantmentRecipe(getRecipeKey(level), 50, wheat, pumpkin, carrot, squash, lapis)
            }

            8 -> {
                val wheat = getIngredientStack(CustomItemType.PREMIUM_HAY_BLOCK, 16)
                val pumpkin = getIngredientStack(CustomItemType.ENCHANTED_PUMPKIN, 64)
                val carrot = getIngredientStack(CustomItemType.PREMIUM_GOLDEN_CARROT, 32)
                val squash = getIngredientStack(CustomItemType.SQUASH, 16)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 16)
                return EnchantmentRecipe(getRecipeKey(level), 60, wheat, pumpkin, carrot, squash, lapis)
            }

            9 -> {
                val wheat = getIngredientStack(CustomItemType.PREMIUM_HAY_BLOCK, 32)
                val pumpkin = getIngredientStack(CustomItemType.PUMPKIN_SINGULARITY, 16)
                val carrot = getIngredientStack(CustomItemType.PREMIUM_GOLDEN_CARROT, 64)
                val squash = getIngredientStack(CustomItemType.SQUASH, 32)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 32)
                return EnchantmentRecipe(getRecipeKey(level), 70, wheat, pumpkin, carrot, squash, lapis)
            }

            10 -> {
                val wheat = getIngredientStack(CustomItemType.PREMIUM_HAY_BLOCK, 64)
                val pumpkin = getIngredientStack(CustomItemType.PUMPKIN_SINGULARITY, 32)
                val carrot = getIngredientStack(CustomItemType.ENCHANTED_CARROT, 16)
                val squash = getIngredientStack(CustomItemType.SQUASH, 64)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 64)
                return EnchantmentRecipe(getRecipeKey(level), 80, wheat, pumpkin, carrot, squash, lapis)
            }
            else -> return null
        }
    }
}
