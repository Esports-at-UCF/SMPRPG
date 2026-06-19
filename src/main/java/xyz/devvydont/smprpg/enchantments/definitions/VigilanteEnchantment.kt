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
import xyz.devvydont.smprpg.entity.base.LeveledEntity
import xyz.devvydont.smprpg.events.CustomEntityDamageByEntityEvent
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.services.EnchantmentService
import xyz.devvydont.smprpg.services.EntityService
import xyz.devvydont.smprpg.util.formatting.ComponentUtils

class VigilanteEnchantment(id: String) : CustomEnchantment(id), Listener {
    override val displayName: Component get() = ComponentUtils.create("Vigilante")
    override val description: Component get() = ComponentUtils.merge(
            ComponentUtils.create("Increases damage dealt by "),
            ComponentUtils.create(
                "+" + getPercentageIncrease(level) + "%",
                NamedTextColor.GREEN
            ),
            ComponentUtils.create(" against "),
            ComponentUtils.create("Illagers", NamedTextColor.RED)
        )
    override val scrollBindingColor: Color get() = Color.fromRGB(142, 147, 147)

    override val itemTypeTag: TagKey<ItemType> get()           = ItemTypeTagKeys.ENCHANTABLE_WEAPON
    override val maxLevel: Int get()                           = 10
    override val weight: Int get()                             = EnchantmentRarity.COMMON.weight
    override val equipmentSlotGroup: EquipmentSlotGroup? get() = EquipmentSlotGroup.MAINHAND
    override val skillRequirement: Int get()                   = 0

    override fun getRecipe(level: Int): EnchantmentRecipe? {
        when (level) {
            1 -> {
                val silver = getIngredientStack(Material.EMERALD, 5)
                val flesh = getIngredientStack(Material.DARK_OAK_LOG, 2)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 8)
                return EnchantmentRecipe(getRecipeKey(level), 0, silver, flesh, lapis)
            }

            2 -> {
                val silver = getIngredientStack(Material.EMERALD, 10)
                val flesh = getIngredientStack(Material.DARK_OAK_LOG, 4)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 16)
                return EnchantmentRecipe(getRecipeKey(level), 5, silver, flesh, lapis)
            }

            3 -> {
                val silver = getIngredientStack(Material.EMERALD, 20)
                val flesh = getIngredientStack(Material.DARK_OAK_LOG, 8)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 32)
                return EnchantmentRecipe(getRecipeKey(level), 15, silver, flesh, lapis)
            }

            4 -> {
                val silver = getIngredientStack(Material.EMERALD, 40)
                val flesh = getIngredientStack(Material.DARK_OAK_LOG, 16)
                val necrotic = getIngredientStack(CustomItemType.PREMIUM_SPELL_POWDER, 16)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 64)
                return EnchantmentRecipe(getRecipeKey(level), 20, silver, flesh, necrotic, lapis)
            }

            5 -> {
                val silver = getIngredientStack(Material.EMERALD, 80)
                val flesh = getIngredientStack(Material.DARK_OAK_LOG, 32)
                val necrotic = getIngredientStack(CustomItemType.PREMIUM_SPELL_POWDER, 32)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 16)
                return EnchantmentRecipe(getRecipeKey(level), 30, silver, flesh, necrotic, lapis)
            }

            6 -> {
                val silver = getIngredientStack(Material.EMERALD_BLOCK, 18)
                val flesh = getIngredientStack(Material.DARK_OAK_LOG, 64)
                val necrotic = getIngredientStack(CustomItemType.PREMIUM_SPELL_POWDER, 64)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 32)
                return EnchantmentRecipe(getRecipeKey(level), 40, silver, flesh, necrotic, lapis)
            }

            7 -> {
                val silver = getIngredientStack(Material.EMERALD_BLOCK, 36)
                val flesh = getIngredientStack(CustomItemType.PREMIUM_DARK_OAK_LOG, 15)
                val necrotic = getIngredientStack(CustomItemType.ENCHANTED_SPELL_POWDER, 15)
                val viscera = getIngredientStack(CustomItemType.REVILED_VISCERA, 1)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 64)
                return EnchantmentRecipe(getRecipeKey(level), 60, silver, flesh, necrotic, viscera, lapis)
            }

            8 -> {
                val silver = getIngredientStack(Material.EMERALD_BLOCK, 72)
                val flesh = getIngredientStack(CustomItemType.PREMIUM_DARK_OAK_LOG, 30)
                val necrotic = getIngredientStack(CustomItemType.ENCHANTED_SPELL_POWDER, 30)
                val viscera = getIngredientStack(CustomItemType.HORN_OF_WARLOCK, 2)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 16)
                return EnchantmentRecipe(getRecipeKey(level), 70, silver, flesh, necrotic, viscera, lapis)
            }

            9 -> {
                val silver = getIngredientStack(CustomItemType.ENCHANTED_EMERALD, 8)
                val flesh = getIngredientStack(CustomItemType.PREMIUM_DARK_OAK_LOG, 45)
                val necrotic = getIngredientStack(CustomItemType.ENCHANTED_SPELL_POWDER, 45)
                val viscera = getIngredientStack(CustomItemType.HORN_OF_WARLOCK, 3)
                val amalgamation = getIngredientStack(CustomItemType.CRYSTAL_BALL, 1)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 32)
                return EnchantmentRecipe(getRecipeKey(level), 80, silver, flesh, necrotic, viscera, amalgamation, lapis)
            }

            10 -> {
                val silver = getIngredientStack(CustomItemType.ENCHANTED_EMERALD, 16)
                val flesh = getIngredientStack(CustomItemType.PREMIUM_DARK_OAK_LOG, 60)
                val necrotic = getIngredientStack(CustomItemType.ENCHANTED_SPELL_POWDER, 60)
                val viscera = getIngredientStack(CustomItemType.HORN_OF_WARLOCK, 4)
                val amalgamation = getIngredientStack(CustomItemType.CRYSTAL_BALL, 2)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 64)
                return EnchantmentRecipe(getRecipeKey(level), 90, silver, flesh, necrotic, viscera, amalgamation, lapis)
            }

            else -> {
                return null
            }
        }
    }

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
            EnchantmentService.BLESSED.typedKey,
            EnchantmentService.GENESIS.typedKey,
            EnchantmentService.MUFFLE.typedKey
        )

    override val magicExperience: Int get() = level * 200 * (1 + (level * 3 / maxLevel))

    @EventHandler(priority = EventPriority.HIGH)
    fun onDamageIllager(event: CustomEntityDamageByEntityEvent) {
        // Skip non illager

        if (!isIllager(
                SMPRPG.getService(EntityService::class.java).getEntityInstance(event.damaged)
            )
        ) return

        // Skip entity if they aren't alive
        if (event.dealer !is LivingEntity) return
        val dealer = event.dealer
        if (dealer is Player) {
            val leveledPlayer = SMPRPG.getService(EntityService::class.java).getPlayerInstance(dealer)
            if (!isEnchantmentActive(dealer.equipment.itemInMainHand, leveledPlayer)) return
        }

        val level = EnchantmentUtil.getHoldingEnchantLevel(enchantment, EquipmentSlotGroup.HAND, dealer.equipment)
        if (level <= 0) return

        val multiplier: Double = 1.0 + (getPercentageIncrease(level) / 100.0)
        event.multiplyDamage(multiplier)
    }

    companion object {
        fun getPercentageIncrease(level: Int): Int { return level * 30 }

        fun isIllager(entity: LeveledEntity<*>): Boolean {
            return entity.mobTypes.contains(MobType.ILLAGER)
        }
    }
}
