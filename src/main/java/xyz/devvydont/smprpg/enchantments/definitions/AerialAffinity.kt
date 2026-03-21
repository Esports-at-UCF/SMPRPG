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
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry
import xyz.devvydont.smprpg.util.formatting.ComponentUtils

class AerialAffinity(id: String) : CustomEnchantment(id), AttributeEnchantment {
    override val displayName: Component get() = ComponentUtils.create("Aerial Affinity")
    override val description: Component get() = ComponentUtils.merge(
            ComponentUtils.create("Improves "),
            ComponentUtils.create("airborne", NamedTextColor.YELLOW),
            ComponentUtils.create(" harvest speed")
        )

    override val itemTypeTag: TagKey<ItemType> get()           = ItemTypeTagKeys.ENCHANTABLE_HEAD_ARMOR
    override val maxLevel: Int get()                           = 10
    override val weight: Int get()                             = EnchantmentRarity.RARE.weight
    override val equipmentSlotGroup: EquipmentSlotGroup? get() = EquipmentSlotGroup.HEAD
    override val skillRequirement: Int get()                   = 18

    override fun getPowerRating(): Int { return 2 }
    override fun getAttributeModifierType(): AttributeModifierType { return AttributeModifierType.ENCHANTMENT }
    override fun getHeldAttributes(): MutableCollection<AttributeEntry?> {
        return mutableListOf(
            ScalarAttributeEntry(AttributeWrapper.AIRBORNE_MINING, 4.0)
        )
    }
}
