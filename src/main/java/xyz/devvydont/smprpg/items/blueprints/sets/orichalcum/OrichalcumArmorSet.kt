package xyz.devvydont.smprpg.items.blueprints.sets.orichalcum

import net.kyori.adventure.key.Key
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.SMPRPG.Companion.plugin
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.base.CustomAttributeItem
import xyz.devvydont.smprpg.items.interfaces.IEquippableAssetOverride
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.items.ToolStats
import java.util.List

abstract class OrichalcumArmorSet(itemService: ItemService?, type: CustomItemType?) :
    CustomAttributeItem(itemService, type), IEquippableAssetOverride {

    override fun getAssetId(): Key {
        return key
    }

    override fun getPowerRating(): Int {
        return ToolStats.TUNGSTEN.power
    }

    open fun getCraftingMaterial(): ItemStack = itemService.getCustomItem(CustomItemType.ORICHALCUM_INGOT)

    open fun unlockedBy(): MutableCollection<ItemStack?>? {
        return List.of<ItemStack?>(itemService.getCustomItem(CustomItemType.ORICHALCUM_INGOT))
    }

    open fun getDisplayKey(): Key {
        return IModelOverridden.ofItemType(_type)
    }

    companion object {
        private val key = Key.key(plugin, "tungsten")
    }
}
