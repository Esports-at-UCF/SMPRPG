package xyz.devvydont.smprpg.items.blueprints.sets.tungsten

import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeEntry
import xyz.devvydont.smprpg.items.interfaces.IModularToolComponent
import xyz.devvydont.smprpg.services.ItemService

class TungstenDrillHead(itemService: ItemService, type: CustomItemType) : TungstenAttributeItem(itemService, type),
    IModularToolComponent {
    override fun getAttributes(): MutableCollection<AttributeEntry?> {
        return mutableListOf(
            AdditiveAttributeEntry(AttributeWrapper.MINING_SPEED, getToolStats().speed * 1.5, attrKey),
            AdditiveAttributeEntry(AttributeWrapper.MINING_POWER, getToolStats().miningPower.toDouble(), attrKey)
        )
    }

    override fun getAttrKey(): String {
        return Companion.attrKey
    }

    override val itemClassification: ItemClassification get() = ItemClassification.ITEM

    companion object {
        const val attrKey: String = "tungsten_drill_head"
    }
}
