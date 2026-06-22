package xyz.devvydont.smprpg.enchantments.definitions

import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys
import io.papermc.paper.registry.tag.TagKey
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.Sound
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
import xyz.devvydont.smprpg.events.CustomEntityDamageByEntityEvent
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.services.EntityService
import xyz.devvydont.smprpg.util.formatting.ComponentUtils

class BurdenEnchantment(id: String) : CustomEnchantment(id), Listener {
    override val displayName: Component get() = ComponentUtils.create("Burden")
    override val description: Component get() = ComponentUtils.merge(
            ComponentUtils.create("Recover "),
            ComponentUtils.create(
                "+" + getManastealPercent(level) + "%",
                NamedTextColor.GREEN
            ),
            ComponentUtils.create(" of max mana when hurting an enemy")
        )
    override val scrollBindingColor: Color get() = Color.fromRGB(92, 117, 148)

    override val itemTypeTag: TagKey<ItemType> get()           = ItemTypeTagKeys.ENCHANTABLE_WEAPON
    override val maxLevel: Int get()                           = 5
    override val weight: Int get()                             = EnchantmentRarity.UNCOMMON.weight
    override val equipmentSlotGroup: EquipmentSlotGroup? get() = EquipmentSlotGroup.MAINHAND
    override val skillRequirement: Int get()                   = 15

    override fun getRecipe(level: Int): EnchantmentRecipe? {
        when (level) {
            1 -> {
                val powder = getIngredientStack(CustomItemType.SPELL_POWDER, 16)
                val amethyst = getIngredientStack(Material.AMETHYST_SHARD, 16)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 8)
                return EnchantmentRecipe(getRecipeKey(level), 15, powder, amethyst, lapis)
            }

            2 -> {
                val powder = getIngredientStack(CustomItemType.SPELL_POWDER, 32)
                val amethyst = getIngredientStack(Material.AMETHYST_SHARD, 32)
                val pearl = getIngredientStack(Material.ENDER_PEARL, 16)
                val lapis = getIngredientStack(Material.LAPIS_LAZULI, 16)
                return EnchantmentRecipe(getRecipeKey(level), 31, powder, amethyst, pearl, lapis)
            }

            3 -> {
                val powder = getIngredientStack(CustomItemType.PREMIUM_SPELL_POWDER, 16)
                val amethyst = getIngredientStack(CustomItemType.ENCHANTED_AMETHYST, 8)
                val pearl = getIngredientStack(CustomItemType.PREMIUM_ENDER_PEARL, 12)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 8)
                return EnchantmentRecipe(getRecipeKey(level), 47, powder, amethyst, pearl, lapis)
            }

            4 -> {
                val powder = getIngredientStack(CustomItemType.PREMIUM_SPELL_POWDER, 32)
                val amethyst = getIngredientStack(CustomItemType.ENCHANTED_AMETHYST, 16)
                val pearl = getIngredientStack(CustomItemType.ENCHANTED_ENDER_PEARL, 8)
                val lapis = getIngredientStack(Material.LAPIS_BLOCK, 16)
                return EnchantmentRecipe(getRecipeKey(level), 63, powder, amethyst, pearl, lapis)
            }

            5 -> {
                val powder = getIngredientStack(CustomItemType.ENCHANTED_SPELL_POWDER, 16)
                val amethyst = getIngredientStack(CustomItemType.ENCHANTED_AMETHYST_BLOCK, 8)
                val pearl = getIngredientStack(CustomItemType.ENCHANTED_ENDER_PEARL, 16)
                val horn = getIngredientStack(CustomItemType.HORN_OF_WARLOCK, 4)
                val lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 16)
                return EnchantmentRecipe(getRecipeKey(level), 80, powder, amethyst, pearl, horn, lapis)
            }

            else -> return null
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onPlayerBurdenedEntity(event: CustomEntityDamageByEntityEvent) {
        if (event.dealer !is Player) return

        val dealer = event.dealer
        val leveledPlayer = SMPRPG.getService(EntityService::class.java).getPlayerInstance(dealer)
        if (!isEnchantmentActive(dealer.equipment.itemInMainHand, leveledPlayer)) return


        val player = SMPRPG.getService(EntityService::class.java).getPlayerInstance(event.dealer)
        val maxMana = player.getMaxMana()

        // Is this player holding the enchantment?
        val leechLevels = EnchantmentUtil.getHoldingEnchantLevel(this, EquipmentSlotGroup.HAND, player.player.equipment)
        if (leechLevels <= 0) return

        // Heal for a percentage of their max HP
        player.gainMana((getManastealPercent(leechLevels) / 100.0 * maxMana).toInt())
        player.player.world.playSound(event.damaged, Sound.ENTITY_EVOKER_PREPARE_SUMMON, .3f, 2.0f)
    }

    companion object {
        fun getManastealPercent(level: Int): Double { return 0.5 * level }
    }
}
