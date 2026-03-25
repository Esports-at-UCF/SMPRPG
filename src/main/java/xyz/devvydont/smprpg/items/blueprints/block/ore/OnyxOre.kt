package xyz.devvydont.smprpg.items.blueprints.block.ore

import xyz.devvydont.smprpg.block.CustomBlock
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.blueprints.block.BlockBlueprint
import xyz.devvydont.smprpg.services.ItemService

class OnyxOre(itemService: ItemService, type: CustomItemType) : BlockBlueprint(itemService, type) {
    override fun getCustomBlock(): CustomBlock {
        return CustomBlock.ONYX_ORE
    }
}
