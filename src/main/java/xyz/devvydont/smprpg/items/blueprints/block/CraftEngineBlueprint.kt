package xyz.devvydont.smprpg.items.blueprints.block

import io.papermc.paper.datacomponent.DataComponentType
import net.kyori.adventure.key.Key
import net.momirealms.craftengine.bukkit.api.CraftEngineItems
import net.momirealms.craftengine.bukkit.item.behavior.BlockItemBehavior
import net.momirealms.craftengine.core.item.ItemBuildContext
import net.momirealms.craftengine.core.util.Key as CEKey
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden
import xyz.devvydont.smprpg.items.interfaces.ISellable
import xyz.devvydont.smprpg.services.ItemService

open class CraftEngineBlueprint(itemService: ItemService, type: CustomItemType) : CustomItemBlueprint(itemService, type),
    IModelOverridden, ISellable {

    override val itemClassification: ItemClassification get() = ItemClassification.ITEM

    override fun generate(): ItemStack {
        // Make the starting item.

        var itemStack : ItemStack
        val craftEngineItem = CraftEngineItems.byId(CEKey.of("smprpg:${customItemType.key}"))
        if (craftEngineItem != null) {
            itemStack = craftEngineItem.buildItemStack()
        }
        else {
            itemStack = ItemStack.of(customItemType.DisplayMaterial)
        }


        // Apply updates to this item according to our blueprint's spec.
        updateItemData(itemStack)
        return itemStack
    }

    /**
     * Craft Engine items automatically create model entries in smprpg:items, so we just grab that model.
     */
    override fun getDisplayKey() : Key {
        return Key.key(SMPRPG.plugin, this.customItemType.name.lowercase())
    }

    override fun getWorth(item: ItemStack): Int {
        val individualWorth = item.persistentDataContainer.getOrDefault(ItemService.SELL_VALUE_KEY, PersistentDataType.INTEGER, 0)
        return individualWorth * item.amount
    }
}
