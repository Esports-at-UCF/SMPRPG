package xyz.devvydont.smprpg.enchantments.definitions

import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys
import io.papermc.paper.registry.tag.TagKey
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemType
import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.enchantments.CustomEnchantment
import xyz.devvydont.smprpg.enchantments.EnchantmentRarity
import xyz.devvydont.smprpg.enchantments.base.AttributeEnchantment
import xyz.devvydont.smprpg.items.attribute.AttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeModifierType
import xyz.devvydont.smprpg.util.formatting.ComponentUtils

class ProficientEnchantment(id: String) : CustomEnchantment(id), AttributeEnchantment {
    override val displayName: Component get() = ComponentUtils.create("Proficient")
    override val description: Component get() = ComponentUtils.merge(
            ComponentUtils.create("Increases "),
            ComponentUtils.create(AttributeWrapper.PROFICIENCY.DisplayName, NamedTextColor.GOLD),
            ComponentUtils.create(" by "),
            ComponentUtils.create(
                String.format("+%d", getProficiency(level)),
                NamedTextColor.GREEN
            )
        )

    override val itemTypeTag: TagKey<ItemType> get() = ItemTypeTagKeys.ENCHANTABLE_VANISHING
    override val maxLevel: Int get() = 5
    override val weight: Int get() = EnchantmentRarity.RARE.weight
    override val equipmentSlotGroup: EquipmentSlotGroup? get() = EquipmentSlotGroup.ANY
    override val skillRequirement: Int get() = 22

    override fun getPowerRating(): Int { return level / 3 }
    override fun getAttributeModifierType(): AttributeModifierType { return AttributeModifierType.ENCHANTMENT }
    override fun getHeldAttributes(): MutableCollection<AttributeEntry?> {
        return mutableListOf(
            AttributeEntry.additive(AttributeWrapper.PROFICIENCY, getProficiency(level).toDouble())
        )
    }

    companion object {
        fun getProficiency(level: Int): Int { return level * 5 }
    }
}
