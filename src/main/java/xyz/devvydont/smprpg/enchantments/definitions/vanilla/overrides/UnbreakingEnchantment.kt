package xyz.devvydont.smprpg.enchantments.definitions.vanilla.overrides

import io.papermc.paper.registry.TypedKey
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys
import io.papermc.paper.registry.tag.TagKey
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemDamageEvent
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ItemType
import xyz.devvydont.smprpg.enchantments.EnchantmentRarity
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.VanillaEnchantment
import xyz.devvydont.smprpg.enchantments.recipe.EnchantmentRecipe
import xyz.devvydont.smprpg.entity.player.LeveledPlayer
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.util.formatting.ComponentUtils
import java.util.*

class UnbreakingEnchantment(key: TypedKey<Enchantment>) : VanillaEnchantment(key), Listener {
    private val random = Random()

    override val displayName: Component get() = ComponentUtils.create("Unbreaking")
    override val description: Component get() = ComponentUtils.merge(
            ComponentUtils.create("Increases max durability of this item by "),
            ComponentUtils.create(
                "${getDurabilityIncrease(level)}%",
                NamedTextColor.GREEN
            )
        )
    override val scrollBindingColor: Color get() = Color.fromRGB(89, 89, 89)

    override val itemTypeTag: TagKey<ItemType> get()           = ItemTypeTagKeys.ENCHANTABLE_DURABILITY
    override val maxLevel: Int get()                           = 10
    override val weight: Int get()                             = EnchantmentRarity.COMMON.weight
    override val equipmentSlotGroup: EquipmentSlotGroup? get() = EquipmentSlotGroup.ANY
    override val skillRequirement: Int get()                   = 0

    override fun getRecipe(level: Int): EnchantmentRecipe? {
        when (level) {
            1 -> {
                val obsidian = getIngredientStack(Material.OBSIDIAN, 5)
                val iron = getIngredientStack(Material.IRON_INGOT, 2)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 8)
                return EnchantmentRecipe(getRecipeKey(level), 0, obsidian, iron, lapis)
            }

            2 -> {
                val obsidian = getIngredientStack(Material.OBSIDIAN, 10)
                val iron = getIngredientStack(Material.IRON_INGOT, 4)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 16)
                return EnchantmentRecipe(getRecipeKey(level), 5, obsidian, iron, lapis)
            }

            3 -> {
                val obsidian = getIngredientStack(Material.OBSIDIAN, 20)
                val iron = getIngredientStack(Material.IRON_INGOT, 8)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 32)
                return EnchantmentRecipe(getRecipeKey(level), 15, obsidian, iron, lapis)
            }

            4 -> {
                val obsidian = getIngredientStack(Material.OBSIDIAN, 40)
                val iron = getIngredientStack(Material.IRON_INGOT, 16)
                val diamond = getIngredientStack(Material.DIAMOND, 8)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 64)
                return EnchantmentRecipe(getRecipeKey(level), 20, obsidian, iron, diamond, lapis)
            }

            5 -> {
                val obsidian = getIngredientStack(Material.OBSIDIAN, 80)
                val iron = getIngredientStack(Material.IRON_INGOT, 32)
                val diamond = getIngredientStack(Material.DIAMOND, 16)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 16)
                return EnchantmentRecipe(getRecipeKey(level), 30, obsidian, iron, diamond, lapis)
            }

            6 -> {
                val obsidian = getIngredientStack(CustomItemType.COMPRESSED_OBSIDIAN, 18)
                val iron = getIngredientStack(Material.IRON_INGOT, 64)
                val diamond = getIngredientStack(Material.DIAMOND, 32)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 32)
                return EnchantmentRecipe(getRecipeKey(level), 40, obsidian, iron, diamond, lapis)
            }

            7 -> {
                val obsidian = getIngredientStack(CustomItemType.COMPRESSED_OBSIDIAN, 18)
                val iron = getIngredientStack(Material.IRON_BLOCK, 15)
                val diamond = getIngredientStack(Material.DIAMOND, 64)
                val tungsten = getIngredientStack(CustomItemType.TUNGSTEN_INGOT, 32)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 64)
                return EnchantmentRecipe(getRecipeKey(level), 60, obsidian, iron, diamond, tungsten, lapis)
            }

            8 -> {
                val obsidian = getIngredientStack(CustomItemType.COMPRESSED_OBSIDIAN, 18)
                val iron = getIngredientStack(Material.IRON_BLOCK, 30)
                val diamond = getIngredientStack(Material.DIAMOND_BLOCK, 15)
                val tungsten = getIngredientStack(CustomItemType.TUNGSTEN_INGOT, 64)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 16)
                return EnchantmentRecipe(getRecipeKey(level), 70, obsidian, iron, diamond, tungsten, lapis)
            }

            9 -> {
                val obsidian = getIngredientStack(CustomItemType.COMPRESSED_OBSIDIAN, 36)
                val iron = getIngredientStack(Material.IRON_BLOCK, 60)
                val diamond = getIngredientStack(Material.DIAMOND_BLOCK, 30)
                val tungsten = getIngredientStack(CustomItemType.TUNGSTEN_BLOCK, 15)
                val adamantium = getIngredientStack(CustomItemType.ADAMANTIUM_BLOCK, 1)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 32)
                return EnchantmentRecipe(getRecipeKey(level), 80, obsidian, iron, diamond, tungsten, adamantium, lapis)
            }

            10 -> {
                val obsidian = getIngredientStack(CustomItemType.COMPRESSED_OBSIDIAN, 72)
                val iron = getIngredientStack(CustomItemType.ENCHANTED_IRON, 15)
                val diamond = getIngredientStack(Material.DIAMOND_BLOCK, 60)
                val tungsten = getIngredientStack(CustomItemType.TUNGSTEN_BLOCK, 30)
                val adamantium = getIngredientStack(CustomItemType.ADAMANTIUM_BLOCK, 2)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 64)
                return EnchantmentRecipe(getRecipeKey(level), 90, obsidian, iron, diamond, tungsten, adamantium, lapis)
            }

            else -> {
                return null
            }
        }
    }

    override fun isEnchantmentActive(itemStack: ItemStack, player: LeveledPlayer): Boolean {return true }

    companion object {
        fun getDurabilityIncrease(level: Int): Int { return level * 20 }
    }
}
