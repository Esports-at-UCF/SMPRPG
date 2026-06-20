package xyz.devvydont.smprpg.enchantments.definitions.vanilla.overrides

import io.papermc.paper.registry.RegistryKey
import io.papermc.paper.registry.TypedKey
import io.papermc.paper.registry.keys.EnchantmentKeys
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys
import io.papermc.paper.registry.set.RegistryKeySet
import io.papermc.paper.registry.set.RegistrySet
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

class ProtectionEnchantment(key: TypedKey<Enchantment>) : VanillaEnchantment(key), AttributeEnchantment {
    override val displayName: Component get() = ComponentUtils.create("Protection")
    override val description: Component get() = ComponentUtils.merge(
            ComponentUtils.create("Increases defense by "),
            ComponentUtils.create("+" + getProtection(level), NamedTextColor.GREEN)
        )
    override val scrollBindingColor: Color get() = Color.fromRGB(81, 101, 143)

    override val itemTypeTag: TagKey<ItemType> get()           = ItemTypeTagKeys.ENCHANTABLE_ARMOR
    override val maxLevel: Int get()                           = 10
    override val weight: Int get()                             = EnchantmentRarity.UNCOMMON.getWeight()
    override val equipmentSlotGroup: EquipmentSlotGroup? get() = EquipmentSlotGroup.ARMOR
    override val skillRequirement: Int get()                   = 0

    override val powerRating : Int get() = level / 2
    override val attributeModifierType : AttributeModifierType get() = AttributeModifierType.ENCHANTMENT
    override fun getHeldAttributes() : MutableCollection<AttributeEntry?>? {
        return mutableListOf(
            AdditiveAttributeEntry(AttributeWrapper.DEFENSE, getProtection(level).toDouble())
        )
    }

    override fun getRecipe(level: Int): EnchantmentRecipe? {
        when (level) {
            1 -> {
                val iron = getIngredientStack(Material.IRON_INGOT, 5)
                val titanium = getIngredientStack(CustomItemType.STEEL_INGOT, 2)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 8)
                return EnchantmentRecipe(getRecipeKey(level), 0, iron, titanium, lapis)
            }

            2 -> {
                val iron = getIngredientStack(Material.IRON_INGOT, 10)
                val steel = getIngredientStack(CustomItemType.STEEL_INGOT, 4)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 16)
                return EnchantmentRecipe(getRecipeKey(level), 5, iron, steel, lapis)
            }

            3 -> {
                val iron = getIngredientStack(Material.IRON_INGOT, 20)
                val steel = getIngredientStack(CustomItemType.STEEL_INGOT, 8)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 32)
                return EnchantmentRecipe(getRecipeKey(level), 15, iron, steel, lapis)
            }

            4 -> {
                val iron = getIngredientStack(Material.IRON_INGOT, 40)
                val steel = getIngredientStack(CustomItemType.STEEL_INGOT, 16)
                val titanium = getIngredientStack(CustomItemType.TITANIUM_INGOT, 4)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 64)
                return EnchantmentRecipe(getRecipeKey(level), 20, iron, steel, titanium, lapis)
            }

            5 -> {
                val iron = getIngredientStack(Material.IRON_INGOT, 80)
                val steel = getIngredientStack(CustomItemType.STEEL_INGOT, 32)
                val titanium = getIngredientStack(CustomItemType.TITANIUM_INGOT, 8)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 16)
                return EnchantmentRecipe(getRecipeKey(level), 30, iron, steel, titanium, lapis)
            }

            6 -> {
                val iron = getIngredientStack(Material.IRON_BLOCK, 18)
                val steel = getIngredientStack(CustomItemType.STEEL_INGOT, 64)
                val titanium = getIngredientStack(CustomItemType.TITANIUM_INGOT, 16)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 32)
                return EnchantmentRecipe(getRecipeKey(level), 40, iron, steel, titanium, lapis)
            }

            7 -> {
                val iron = getIngredientStack(Material.IRON_BLOCK, 36)
                val steel = getIngredientStack(CustomItemType.STEEL_BLOCK, 15)
                val titanium = getIngredientStack(CustomItemType.TITANIUM_INGOT, 32)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 64)
                val adamantium = getIngredientStack(CustomItemType.ADAMANTIUM_INGOT, 2)
                return EnchantmentRecipe(getRecipeKey(level), 60, iron, steel, titanium, adamantium, lapis)
            }

            8 -> {
                val iron = getIngredientStack(Material.IRON_BLOCK, 72)
                val steel = getIngredientStack(CustomItemType.STEEL_BLOCK, 30)
                val titanium = getIngredientStack(CustomItemType.TITANIUM_INGOT, 64)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 16)
                val adamantium = getIngredientStack(CustomItemType.ADAMANTIUM_INGOT, 4)
                return EnchantmentRecipe(getRecipeKey(level), 70, iron, steel, titanium, adamantium, lapis)
            }

            9 -> {
                val iron = getIngredientStack(CustomItemType.ENCHANTED_IRON, 16)
                val steel = getIngredientStack(CustomItemType.STEEL_BLOCK, 60)
                val titanium = getIngredientStack(CustomItemType.TITANIUM_BLOCK, 15)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 32)
                val adamantium = getIngredientStack(CustomItemType.ADAMANTIUM_INGOT, 8)
                return EnchantmentRecipe(getRecipeKey(level), 80, iron, steel, titanium, adamantium, lapis)
            }

            10 -> {
                val iron = getIngredientStack(CustomItemType.ENCHANTED_IRON, 32)
                val steel = getIngredientStack(CustomItemType.ENCHANTED_STEEL, 14)
                val titanium = getIngredientStack(CustomItemType.TITANIUM_BLOCK, 30)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 64)
                val adamantium = getIngredientStack(CustomItemType.ADAMANTIUM_INGOT, 16)
                return EnchantmentRecipe(getRecipeKey(level), 90, iron, steel, titanium, adamantium, lapis)
            }
            else -> return null
        }
    }

    companion object {
        fun getProtection(level: Int): Int {
            return when (level) {
                0 -> 0
                1 -> 5
                2 -> 10
                3 -> 15
                4 -> 20
                5 -> 30
                6 -> 40
                7 -> 50
                8 -> 65
                9 -> 80
                10 -> 100
                else -> getProtection(10) + 50 * (level - 10)
            }
        }
    }
}
