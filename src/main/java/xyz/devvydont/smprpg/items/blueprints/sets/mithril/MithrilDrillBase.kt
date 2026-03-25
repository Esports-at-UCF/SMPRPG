package xyz.devvydont.smprpg.items.blueprints.sets.mithril

import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeEntry
import xyz.devvydont.smprpg.items.interfaces.IModularToolComponent
import xyz.devvydont.smprpg.services.ItemService

class MithrilDrillBase(itemService: ItemService, type: CustomItemType) : MithrilAttributeItem(itemService, type),
    IModularToolComponent {
    override fun getAttributes(): MutableCollection<AttributeEntry?> {
        return mutableListOf(
            AdditiveAttributeEntry(AttributeWrapper.MINING_POWER, 1.0, ATTR_KEY)  // Special bonus is to add +1 BP
        )
    }

    override fun getAttrKey(): String { return ATTR_KEY }

    override val itemClassification: ItemClassification get() = ItemClassification.ITEM

    companion object {
        const val ATTR_KEY: String = "mithril_drill_base"
    }
}
