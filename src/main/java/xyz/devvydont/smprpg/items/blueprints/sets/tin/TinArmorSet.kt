package xyz.devvydont.smprpg.items.blueprints.sets.tin

import net.kyori.adventure.key.Key
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.SMPRPG.Companion.plugin
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.base.CustomAttributeItem
import xyz.devvydont.smprpg.items.interfaces.IEquippableAssetOverride
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.items.ToolStats

abstract class TinArmorSet(itemService: ItemService?, type: CustomItemType?) :
    CustomAttributeItem(itemService, type), IEquippableAssetOverride {

    override fun getAssetId(): Key {
        return key
    }

    override fun getPowerRating(): Int {
        return ToolStats.TIN.power
    }

    open fun getCraftingMaterial(): ItemStack = itemService.getCustomItem(CustomItemType.TIN_INGOT)

    open fun getRecipeKey(): NamespacedKey {
        return NamespacedKey(plugin, _type.key + "-recipe")
    }

    open fun unlockedBy(): MutableCollection<ItemStack?>? {
        return mutableListOf(itemService.getCustomItem(CustomItemType.TIN_INGOT))
    }

    open fun getDisplayKey(): Key {
        return IModelOverridden.ofItemType(_type)
    }

    companion object {
        private val key = Key.key(plugin, "tin")
    }
}
