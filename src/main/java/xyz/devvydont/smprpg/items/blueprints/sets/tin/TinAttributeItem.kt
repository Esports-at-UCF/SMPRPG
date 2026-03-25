package xyz.devvydont.smprpg.items.blueprints.sets.tin

import net.kyori.adventure.key.Key
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden
import xyz.devvydont.smprpg.items.tools.ToolSetAttributeItem
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.items.ToolStats

open class TinAttributeItem(itemService: ItemService, type: CustomItemType) : ToolSetAttributeItem(itemService, type) {

    override val itemClassification: ItemClassification get() = ItemClassification.ITEM

    override fun getToolStats(): ToolStats {
        return ToolStats.TIN
    }

    override fun getCraftingMaterial(): ItemStack {
        return itemService.getCustomItem(CustomItemType.TIN_INGOT)
    }

    open fun unlockedBy(): MutableCollection<ItemStack?>? {
        return mutableListOf(itemService.getCustomItem(CustomItemType.TIN_INGOT))
    }

    open fun getDisplayKey(): Key {
        return IModelOverridden.ofItemType(customItemType)
    }

    open fun getComponentPrefix(): String {
        return "Tin"
    }

    override fun getActiveSlot(): EquipmentSlotGroup? {
        return EquipmentSlotGroup.MAINHAND
    }
}