package xyz.devvydont.smprpg.enchantments.definitions.vanilla.overrides

import io.papermc.paper.registry.TypedKey
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys
import io.papermc.paper.registry.tag.TagKey
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Color
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemDamageEvent
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemType
import xyz.devvydont.smprpg.enchantments.EnchantmentRarity
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.VanillaEnchantment
import xyz.devvydont.smprpg.util.formatting.ComponentUtils
import java.util.*

class UnbreakingEnchantment(key: TypedKey<Enchantment>) : VanillaEnchantment(key), Listener {
    private val random = Random()

    override val displayName: Component get() = ComponentUtils.create("Unbreaking")
    override val description: Component get() = ComponentUtils.merge(
            ComponentUtils.create("Durability is ignored "),
            ComponentUtils.create(
                getDurabilityIgnoreChance(level).toString() + "%",
                NamedTextColor.GREEN
            ),
            ComponentUtils.create(" of the time when used")
        )
    override val scrollBindingColor: Color get() = Color.fromRGB(89, 89, 89)

    override val itemTypeTag: TagKey<ItemType> get()           = ItemTypeTagKeys.ENCHANTABLE_DURABILITY
    override val maxLevel: Int get()                           = 10
    override val weight: Int get()                             = EnchantmentRarity.COMMON.weight
    override val equipmentSlotGroup: EquipmentSlotGroup? get() = EquipmentSlotGroup.ANY
    override val skillRequirement: Int get()                   = 0

    @EventHandler(priority = EventPriority.HIGHEST)
    private fun onUnbreakingProc(event: PlayerItemDamageEvent) {
        val chanceToIgnore: Int = getDurabilityIgnoreChance(event.item.getEnchantmentLevel(Enchantment.UNBREAKING))
        if (chanceToIgnore <= 0) return

        if (random.nextInt(100) < chanceToIgnore) event.isCancelled =true
    }

    companion object {
        fun getDurabilityIgnoreChance(level: Int): Int { return ((1 - (1.0 / (level + 1))) * 100).toInt() }
    }
}
