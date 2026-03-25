package xyz.devvydont.smprpg.items.blueprints.block.decoration

import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.block.CustomBlock
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.blueprints.block.BlockBlueprint
import xyz.devvydont.smprpg.items.interfaces.ISellable
import xyz.devvydont.smprpg.services.ItemService

class GrimstoneBlock(itemService: ItemService, type: CustomItemType) : BlockBlueprint(itemService, type), ISellable {
    override fun getCustomBlock(): CustomBlock {
        return CustomBlock.GRIMSTONE
    }

    override fun getWorth(item: ItemStack): Int {
        return 3 * item.amount
    }
}
