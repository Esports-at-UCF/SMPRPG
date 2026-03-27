package xyz.devvydont.smprpg.items.blueprints.sets.adamantium

import org.bukkit.inventory.CraftingRecipe
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeEntry
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemArmor
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment
import xyz.devvydont.smprpg.items.interfaces.ICraftable
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.crafting.builders.LeggingsRecipe

class AdamantiumLeggings(itemService: ItemService, type: CustomItemType) : AdamantiumArmorSet(itemService, type),
    IBreakableEquipment, ICraftable {

    override val itemClassification: ItemClassification get() = ItemClassification.LEGGINGS

    override fun getAttributeModifiers(item: ItemStack?): MutableCollection<AttributeEntry?> {
        return mutableListOf(
            AdditiveAttributeEntry(
                AttributeWrapper.DEFENSE,
                ItemArmor.getDefenseFromItemType(CustomItemType.ADAMANTIUM_LEGGINGS).toDouble()
            ),
            AdditiveAttributeEntry(AttributeWrapper.KNOCKBACK_RESISTANCE, .125)
        )
    }

    override fun getActiveSlot(): EquipmentSlotGroup {
        return EquipmentSlotGroup.LEGS
    }

    override fun getMaxDurability(): Int {
        return armorDurabilityUnit * 7
    }

    override fun getCustomRecipe(): CraftingRecipe? {
        return LeggingsRecipe(this, getCraftingMaterial(), generate()).build()
    }

}
