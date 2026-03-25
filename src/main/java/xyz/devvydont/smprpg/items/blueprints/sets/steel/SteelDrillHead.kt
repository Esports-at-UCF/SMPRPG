package xyz.devvydont.smprpg.items.blueprints.sets.steel

import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeEntry
import xyz.devvydont.smprpg.items.interfaces.IModularToolComponent
import xyz.devvydont.smprpg.services.ItemService

class SteelDrillHead(itemService: ItemService, type: CustomItemType) : SteelAttributeItem(itemService, type),
    IModularToolComponent {

    override val itemClassification: ItemClassification get() = ItemClassification.ITEM

    override fun getAttributes(): MutableCollection<AttributeEntry?> {
        return mutableListOf(
            AdditiveAttributeEntry(AttributeWrapper.MINING_SPEED, getToolStats().speed * 1.5, ATTR_KEY),
            AdditiveAttributeEntry(AttributeWrapper.MINING_POWER, getToolStats().miningPower.toDouble(), ATTR_KEY)
        )
    }

    override fun getAttrKey(): String {
        return ATTR_KEY
    }

    companion object {
        const val ATTR_KEY: String = "steel_drill_head"
    }
}
