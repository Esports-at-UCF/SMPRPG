package xyz.devvydont.smprpg.items.blueprints.sets.tungsten

import net.kyori.adventure.key.Key
import org.bukkit.NamespacedKey
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.SMPRPG.Companion.plugin
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden
import xyz.devvydont.smprpg.items.tools.ToolSetAttributeItem
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.items.ToolStats
import java.util.List

open class TungstenAttributeItem(itemService: ItemService, type: CustomItemType) : ToolSetAttributeItem(itemService,
    type) {

    override val itemClassification: ItemClassification get() = ItemClassification.ITEM

    override fun getToolStats(): ToolStats {
        return ToolStats.TUNGSTEN
    }

    override fun getCraftingMaterial(): ItemStack {
        return itemService.getCustomItem(CustomItemType.TUNGSTEN_INGOT)
    }

    open fun unlockedBy(): MutableCollection<ItemStack?>? {
        return List.of<ItemStack?>(itemService.getCustomItem(CustomItemType.TUNGSTEN_INGOT))
    }

    open fun getDisplayKey(): Key {
        return IModelOverridden.ofItemType(customItemType)
    }

    open fun getComponentPrefix(): String {
        return "Tungsten"
    }

    override fun getActiveSlot(): EquipmentSlotGroup? {
        return EquipmentSlotGroup.MAINHAND
    }
}