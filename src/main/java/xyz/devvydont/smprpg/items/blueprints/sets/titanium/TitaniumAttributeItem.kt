package xyz.devvydont.smprpg.items.blueprints.sets.titanium

import net.kyori.adventure.key.Key
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.SMPRPG.Companion.plugin
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden
import xyz.devvydont.smprpg.items.tools.ToolSetAttributeItem
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.items.ToolStats
import java.util.List

open class TitaniumAttributeItem(itemService: ItemService, type: CustomItemType) : ToolSetAttributeItem(itemService,
    type) {

    override val itemClassification: ItemClassification get() = ItemClassification.ITEM

    override fun getToolStats(): ToolStats {
        return ToolStats.TITANIUM
    }

    open fun getRecipeKey(): NamespacedKey {
        return NamespacedKey(plugin, getCustomItemType().getKey() + "-recipe")
    }

    override fun getCraftingMaterial(): ItemStack {
        return itemService.getCustomItem(CustomItemType.TITANIUM_INGOT)
    }

    open fun unlockedBy(): MutableCollection<ItemStack?>? {
        return mutableListOf(itemService.getCustomItem(CustomItemType.TITANIUM_INGOT))
    }

    open fun getDisplayKey(): Key {
        return IModelOverridden.ofItemType(_type)
    }

    open fun getComponentPrefix(): String {
        return "Titanium"
    }
}