package xyz.devvydont.smprpg.items.blueprints.sets.silver

import net.kyori.adventure.key.Key
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.SMPRPG.Companion.plugin
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.base.CustomAttributeItem
import xyz.devvydont.smprpg.items.interfaces.IEquippableAssetOverride
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden
import xyz.devvydont.smprpg.items.interfaces.IRepairable
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.items.ToolStats

abstract class SilverArmorSet(itemService: ItemService, type: CustomItemType) :
    CustomAttributeItem(itemService, type), IEquippableAssetOverride, IRepairable {

    override val repairMaterial: ItemStack get() = getCraftingMaterial()

    override fun getAssetId(): Key { return key }

    override fun getPowerRating(): Int { return ToolStats.SILVER.power }

    open fun getCraftingMaterial(): ItemStack = itemService.getCustomItem(CustomItemType.SILVER_INGOT)

    open fun unlockedBy(): MutableCollection<ItemStack?>? {
        return mutableListOf(itemService.getCustomItem(CustomItemType.SILVER_INGOT))
    }

    open fun getDisplayKey(): Key {
        return IModelOverridden.ofItemType(customItemType)
    }

    open fun getRecipeKey(): NamespacedKey {
        return NamespacedKey(plugin, customItemType.key + "-recipe")
    }

    companion object {
        private val key = Key.key("silver")
    }
}
