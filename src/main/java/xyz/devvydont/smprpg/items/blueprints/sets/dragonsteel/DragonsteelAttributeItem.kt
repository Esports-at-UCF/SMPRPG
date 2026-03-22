package xyz.devvydont.smprpg.items.blueprints.sets.dragonsteel

import net.kyori.adventure.key.Key
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden
import xyz.devvydont.smprpg.items.tools.ToolSetAttributeItem
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.items.ToolStats
import java.util.List

open class DragonsteelAttributeItem(itemService: ItemService, type: CustomItemType) : ToolSetAttributeItem(itemService,
    type) {

    override val itemClassification: ItemClassification get() = ItemClassification.ITEM

    override fun getToolStats(): ToolStats {
        return ToolStats.DRAGONSTEEL
    }

    open fun getRecipeKey(): NamespacedKey {
        return NamespacedKey(SMPRPG.Companion.plugin, getCustomItemType().getKey() + "-recipe")
    }

    override fun getCraftingMaterial(): ItemStack {
        return itemService.getCustomItem(CustomItemType.DRAGONSTEEL_INGOT)
    }

    open fun unlockedBy(): MutableCollection<ItemStack?>? {
        return List.of<ItemStack?>(itemService.getCustomItem(CustomItemType.DRAGONSTEEL_INGOT))
    }

    open fun getDisplayKey(): Key {
        return IModelOverridden.ofItemType(_type)
    }

    open fun getComponentPrefix(): String {
        return "Dragonsteel"
    }
}