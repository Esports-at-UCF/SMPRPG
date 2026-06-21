package xyz.devvydont.smprpg.enchantments.definitions

import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys
import io.papermc.paper.registry.tag.TagKey
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemType
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.enchantments.CustomEnchantment
import xyz.devvydont.smprpg.enchantments.EnchantmentRarity
import xyz.devvydont.smprpg.enchantments.recipe.EnchantmentRecipe
import xyz.devvydont.smprpg.events.CustomEntityDamageByEntityEvent
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.services.EntityService
import xyz.devvydont.smprpg.util.formatting.ComponentUtils
import kotlin.math.max

class ExecuteEnchantment(id: String) : CustomEnchantment(id), Listener {
    override val displayName: Component get() = ComponentUtils.create("Execute")
    override val description: Component get() = ComponentUtils.merge(
            ComponentUtils.create("Increases damage by "),
            ComponentUtils.create(
                "+" + getPercentDamageIncreaseForLowEnemy(level) + "%",
                NamedTextColor.GREEN
            ),
            ComponentUtils.create(" for enemies under "),
            ComponentUtils.create("$HEALTH_THRESHOLD%", NamedTextColor.GREEN),
            ComponentUtils.create(" of their maximum health")
        )
    override val scrollBindingColor: Color get() = Color.fromRGB(189, 71, 21)

    override val itemTypeTag: TagKey<ItemType> get()           = ItemTypeTagKeys.ENCHANTABLE_WEAPON
    override val maxLevel: Int get()                           = 5
    override val weight: Int get()                             = EnchantmentRarity.UNCOMMON.weight
    override val equipmentSlotGroup: EquipmentSlotGroup? get() = EquipmentSlotGroup.HAND
    override val skillRequirement: Int get()                   = 8

    override fun getRecipe(level: Int): EnchantmentRecipe? {
        when (level) {
            1 -> {
                val flesh = getIngredientStack(CustomItemType.NECROTIC_FLESH, 16)
                val iron = getIngredientStack(Material.IRON_INGOT, 16)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 8)
                return EnchantmentRecipe(getRecipeKey(level), 8, flesh, iron, lapis)
            }

            2 -> {
                val flesh = getIngredientStack(CustomItemType.NECROTIC_FLESH, 32)
                val iron = getIngredientStack(CustomItemType.ENCHANTED_IRON, 4)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 16)
                return EnchantmentRecipe(getRecipeKey(level), 26, flesh, iron, lapis)
            }

            3 -> {
                val flesh = getIngredientStack(CustomItemType.PREMIUM_NECROTIC_FLESH, 16)
                val obsidian = getIngredientStack(CustomItemType.COMPRESSED_OBSIDIAN, 16)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 8)
                return EnchantmentRecipe(getRecipeKey(level), 44, flesh, obsidian, lapis)
            }

            4 -> {
                val flesh = getIngredientStack(CustomItemType.ENCHANTED_NECROTIC_FLESH, 8)
                val obsidian = getIngredientStack(CustomItemType.ENCHANTED_OBSIDIAN, 8)
                val adamantium = getIngredientStack(CustomItemType.ADAMANTIUM_BLOCK, 2)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 8)
                return EnchantmentRecipe(getRecipeKey(level), 62, flesh, obsidian, adamantium, lapis)
            }

            5 -> {
                val flesh = getIngredientStack(CustomItemType.NECROTIC_FLESH_SINGULARITY, 2)
                val obsidian = getIngredientStack(CustomItemType.ENCHANTED_OBSIDIAN, 16)
                val adamantium = getIngredientStack(CustomItemType.ENCHANTED_ADAMANTIUM, 1)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 16)
                return EnchantmentRecipe(getRecipeKey(level), 80, flesh, obsidian, adamantium, lapis)
            }
            else -> return null
        }
    }

    @EventHandler
    private fun onDealDamageWithExecute(event: CustomEntityDamageByEntityEvent) {
        if (event.dealer !is LivingEntity) return

        val dealer = event.dealer
        if (dealer is Player) {
            val leveledPlayer = SMPRPG.getService(EntityService::class.java).getPlayerInstance(dealer)
            if (!isEnchantmentActive(dealer.equipment.itemInMainHand, leveledPlayer)) return
        }

        if(dealer.equipment == null) return

        if (event.damaged !is LivingEntity) return
        val damaged = event.damaged
        if (damaged.getAttribute(Attribute.MAX_HEALTH) == null) return

        // Are they over the threshold?
        val hp: Double = damaged.health
        val maxHP: Double = damaged.getAttribute(Attribute.MAX_HEALTH)!!.value
        if (hp / maxHP * 100 > HEALTH_THRESHOLD) return

        // Retrieve the higher first strike level of the two hands to determine which one to use
        val firstStrikeLevels: Int
        val mainHandFSLevels: Int = dealer.equipment!!.itemInMainHand.getEnchantmentLevel(enchantment)
        val offHandFSLevels: Int = dealer.equipment!!.itemInOffHand.getEnchantmentLevel(enchantment)
        firstStrikeLevels = max(mainHandFSLevels, offHandFSLevels)

        if (firstStrikeLevels <= 0) return

        val multiplier: Double = 1.0 + getPercentDamageIncreaseForLowEnemy(firstStrikeLevels) / 100.0
        event.multiplyDamage(multiplier)
    }

    companion object {
        fun getPercentDamageIncreaseForLowEnemy(level: Int): Int { return level * 15 }
        var HEALTH_THRESHOLD: Int = 50
    }
}
