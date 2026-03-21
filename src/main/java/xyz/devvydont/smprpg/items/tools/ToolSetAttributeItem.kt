package xyz.devvydont.smprpg.items.tools

import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.attribute.AttributeEntry
import xyz.devvydont.smprpg.items.base.CustomAttributeItem
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.items.ToolStats

open class ToolSetAttributeItem(itemService: ItemService, type: CustomItemType) : CustomAttributeItem(itemService,
    type) {

    override val itemClassification: ItemClassification get() = ItemClassification.ITEM

    override fun getAttributeModifiers(item: ItemStack?): Collection<AttributeEntry?>? {
        return listOf()
    }

    open fun getCraftingMaterial(): ItemStack {
        throw NotImplementedError("Crafting material should be defined by child class. Use null if there should not be a recipe.")
    }

    open fun getToolStats(): ToolStats {
        throw NotImplementedError("Tool stats must be defined by child class.")
    }

    override fun getPowerRating(): Int {
        return getToolStats().power
    }

    open fun getMaxDurability(): Int {
        return getToolStats().durability
    }

    override fun getActiveSlot(): EquipmentSlotGroup? {
        throw NotImplementedError("Active slot must be defined by child class.")
    }
}