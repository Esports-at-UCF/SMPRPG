package xyz.devvydont.smprpg.items.blueprints.sets.araxys

import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeEntry
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry
import xyz.devvydont.smprpg.services.ItemService

class AraxysBoots(itemService: ItemService, type: CustomItemType) : AraxysArmorSet(itemService, type) {

    override val itemClassification: ItemClassification get() = ItemClassification.BOOTS

    override fun getActiveSlot(): EquipmentSlotGroup { return EquipmentSlotGroup.FEET }

    override fun getAttributeModifiers(item: ItemStack?): MutableCollection<AttributeEntry?> {
        return mutableListOf(
            AdditiveAttributeEntry(AttributeWrapper.DEFENSE, AraxysChestplate.Companion.DEFENSE.toDouble() / 2),
            AdditiveAttributeEntry(AttributeWrapper.HEALTH, AraxysChestplate.Companion.HEALTH.toDouble() / 2),
            ScalarAttributeEntry(AttributeWrapper.STRENGTH, AraxysChestplate.Companion.STRENGTH),
            AdditiveAttributeEntry(AttributeWrapper.CRITICAL_DAMAGE, AraxysChestplate.Companion.CRIT.toDouble()),
            ScalarAttributeEntry(AttributeWrapper.MOVEMENT_SPEED, .15),
            AdditiveAttributeEntry(AttributeWrapper.STEP, 1.0)
        )
    }

    override fun getMaxDurability(): Int { return ARMOR_DURABILITY_UNIT * 4 }

}
