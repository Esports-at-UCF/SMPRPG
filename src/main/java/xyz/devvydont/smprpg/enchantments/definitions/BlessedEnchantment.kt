package xyz.devvydont.smprpg.enchantments.definitions

import io.papermc.paper.registry.RegistryKey
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
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemType
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.enchantments.CustomEnchantment
import xyz.devvydont.smprpg.enchantments.EnchantmentRarity
import xyz.devvydont.smprpg.enchantments.EnchantmentUtil
import xyz.devvydont.smprpg.enchantments.recipe.EnchantmentRecipe
import xyz.devvydont.smprpg.entity.MobType
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.entity.base.LeveledEntity
import xyz.devvydont.smprpg.events.CustomEntityDamageByEntityEvent
import xyz.devvydont.smprpg.services.EnchantmentService
import xyz.devvydont.smprpg.services.EntityService
import xyz.devvydont.smprpg.util.formatting.ComponentUtils

class BlessedEnchantment(id: String) : CustomEnchantment(id), Listener {
    override val displayName: Component get() = ComponentUtils.create("Blessed")
    override val description: Component get() = ComponentUtils.merge(
            ComponentUtils.create("Increases damage dealt by "),
            ComponentUtils.create(
                "+" + getPercentageIncrease(level) + "%",
                NamedTextColor.GREEN
            ),
            ComponentUtils.create(" against "),
            ComponentUtils.create(MobType.NETHER.symbol, MobType.NETHER.symbolColor),
            ComponentUtils.create(" Nether", NamedTextColor.RED),
            ComponentUtils.create(" mobs.")
        )

    override val itemTypeTag: TagKey<ItemType> get()           = ItemTypeTagKeys.ENCHANTABLE_WEAPON
    override val maxLevel: Int get()                           = 5
    override val weight: Int get()                             = EnchantmentRarity.COMMON.weight
    override val equipmentSlotGroup: EquipmentSlotGroup? get() = EquipmentSlotGroup.HAND
    override val skillRequirement: Int get()                   = 19
    override val scrollBindingColor: Color get() = Color.fromRGB(161, 212, 212)

    override val conflictingEnchantments: RegistryKeySet<Enchantment>
        /**
         * A set of enchantments that this enchantment conflicts with.
         * If there are none, this enchantment has no conflicts
         *
         * @return
         */
        get() = RegistrySet.keySet(
            RegistryKey.ENCHANTMENT,
            EnchantmentKeys.SMITE,
            EnchantmentKeys.BANE_OF_ARTHROPODS,
            EnchantmentService.GENESIS.typedKey,
            EnchantmentService.VIGILANTE.typedKey,
            EnchantmentService.MUFFLE.typedKey
        )

    override fun getRecipe(level: Int): EnchantmentRecipe? {
        when (level) {
            1 -> {
                val glowstone = getIngredientStack(Material.GLOWSTONE_DUST, 16)
                val silver = getIngredientStack(CustomItemType.SILVER_INGOT, 2)
                val blaze = getIngredientStack(Material.BLAZE_POWDER, 16)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 8)
                return EnchantmentRecipe(getRecipeKey(level), 19, glowstone, silver, blaze, lapis)
            }

            2 -> {
                val glowstone = getIngredientStack(Material.GLOWSTONE_DUST, 32)
                val silver = getIngredientStack(CustomItemType.SILVER_INGOT, 8)
                val blaze = getIngredientStack(Material.BLAZE_ROD, 8)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 16)
                return EnchantmentRecipe(getRecipeKey(level), 34, glowstone, silver, blaze, lapis)
            }

            3 -> {
                val glowstone = getIngredientStack(CustomItemType.ENCHANTED_GLOWSTONE, 8)
                val silver = getIngredientStack(CustomItemType.SILVER_BLOCK, 4)
                val magma = getIngredientStack(Material.MAGMA_CREAM, 16)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 8)
                return EnchantmentRecipe(getRecipeKey(level), 49, glowstone, silver, magma, lapis)
            }

            4 -> {
                val glowstone = getIngredientStack(CustomItemType.ENCHANTED_GLOWSTONE, 16)
                val silver = getIngredientStack(CustomItemType.ENCHANTED_SILVER, 2)
                val blaze = getIngredientStack(CustomItemType.PREMIUM_BLAZE_ROD, 8)
                val tear = getIngredientStack(Material.GHAST_TEAR, 4)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 16)
                return EnchantmentRecipe(getRecipeKey(level), 65, glowstone, silver, blaze, tear, lapis)
            }

            5 -> {
                val glowstone = getIngredientStack(CustomItemType.ENCHANTED_GLOWSTONE_BLOCK, 8)
                val silver = getIngredientStack(CustomItemType.ENCHANTED_SILVER, 8)
                val blaze = getIngredientStack(CustomItemType.ENCHANTED_BLAZE_ROD, 8)
                val inferno = getIngredientStack(CustomItemType.INFERNO_RESIDUE, 2)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 16)
                return EnchantmentRecipe(getRecipeKey(level), 80, glowstone, silver, blaze, inferno, lapis)
            }

            else -> return null
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onDamageNetherMob(event: CustomEntityDamageByEntityEvent) {
        // Skip non undead

        if (!isNether(
                SMPRPG.getService(EntityService::class.java).getEntityInstance(event.damaged)
            )
        ) return

        // Skip entity if they aren't alive
        if (event.dealer !is LivingEntity) return
        val dealer = event.dealer

        if (event.dealer is Player) {
            val leveledPlayer = SMPRPG.getService(EntityService::class.java).getPlayerInstance(dealer)
            if (!isEnchantmentActive(dealer.equipment.itemInMainHand, leveledPlayer)) return
        }

        val level = EnchantmentUtil.getHoldingEnchantLevel(enchantment, EquipmentSlotGroup.HAND, dealer.equipment)
        if (level <= 0) return

        val multiplier: Double = 1.0 + (getPercentageIncrease(level) / 100.0)
        event.multiplyDamage(multiplier)
    }

    companion object {
        fun isNether(entity: LeveledEntity<*>): Boolean {
            return entity.mobTypes.contains(MobType.NETHER)
        }

        fun getPercentageIncrease(level: Int): Int {
            return level * 30
        }
    }
}
