package xyz.devvydont.smprpg.items.blueprints.sets.titanium

import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeEntry
import xyz.devvydont.smprpg.items.interfaces.IModularToolComponent
import xyz.devvydont.smprpg.services.ItemService

class TitaniumDrillBase(itemService: ItemService, type: CustomItemType) : TitaniumAttributeItem(itemService, type),
    IModularToolComponent {

    override val itemClassification: ItemClassification get() = ItemClassification.ITEM

    override fun getAttributes(): MutableCollection<AttributeEntry?> {
        return mutableListOf(
            AdditiveAttributeEntry(AttributeWrapper.MINING_FORTUNE, getToolStats().fortune * 1.5, ATTR_KEY)
        )
    }

    override fun getAttrKey(): String {
        return ATTR_KEY
    }

    override fun getComponentPrefix(): String {
        return "Titanium"
    }

    companion object {
        const val ATTR_KEY: String = "titanium_drill_base"
    }
}
