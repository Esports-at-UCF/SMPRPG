package xyz.devvydont.smprpg.enchantments.definitions.vanilla.unchanged

import io.papermc.paper.registry.TypedKey
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys
import io.papermc.paper.registry.tag.TagKey
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemType
import xyz.devvydont.smprpg.enchantments.EnchantmentRarity
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.UnchangedEnchantment
import xyz.devvydont.smprpg.enchantments.recipe.EnchantmentRecipe
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.util.formatting.ComponentUtils

class SilkTouchEnchantment(key: TypedKey<Enchantment>) : UnchangedEnchantment(key) {
    override val displayName: Component get() = ComponentUtils.create("Silk Touch")
    override val description: Component get() = ComponentUtils.merge(
            ComponentUtils.create("Mines blocks for their "),
            ComponentUtils.create("pure", NamedTextColor.LIGHT_PURPLE),
            ComponentUtils.create(" form")
        )
    override val scrollBindingColor: Color get() = Color.fromRGB(255, 255, 255)

    override val itemTypeTag: TagKey<ItemType> get() = ItemTypeTagKeys.ENCHANTABLE_MINING
    override val weight: Int get() = EnchantmentRarity.RARE.weight
    override val skillRequirement: Int get() = 20

    override fun getRecipe(level: Int): EnchantmentRecipe? {
        when (level) {
            1 -> {
                val ing1 = getIngredientStack(CustomItemType.ENCHANTED_STRING, 4)
                val ing2 = getIngredientStack(Material.HONEYCOMB, 32)
                val ing3 = getIngredientStack(CustomItemType.ONYX, 8)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 16)
                return EnchantmentRecipe(getRecipeKey(level), 25, ing1, ing2, ing3, lapis)
            }
            else -> { return null }
        }
    }
}
