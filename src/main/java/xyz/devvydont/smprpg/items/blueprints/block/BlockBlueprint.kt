package xyz.devvydont.smprpg.items.blueprints.block

import io.papermc.paper.datacomponent.DataComponentTypes
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint
import xyz.devvydont.smprpg.items.interfaces.ICustomBlock
import xyz.devvydont.smprpg.services.ItemService

abstract class BlockBlueprint(itemService: ItemService, type: CustomItemType) : CustomItemBlueprint(itemService, type),
    ICustomBlock {

    override val itemClassification: ItemClassification get() = ItemClassification.BLOCK

    override fun updateItemData(itemStack: ItemStack) {
        itemStack.unsetData(DataComponentTypes.CONSUMABLE)
        super.updateItemData(itemStack)
    }
}
