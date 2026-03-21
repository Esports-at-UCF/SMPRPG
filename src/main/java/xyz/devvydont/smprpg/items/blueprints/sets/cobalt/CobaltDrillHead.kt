package xyz.devvydont.smprpg.items.blueprints.sets.cobalt

import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeEntry
import xyz.devvydont.smprpg.items.interfaces.IModularToolComponent
import xyz.devvydont.smprpg.services.ItemService
import java.util.List

class CobaltDrillHead(itemService: ItemService, type: CustomItemType) : CobaltAttributeItem(itemService, type),
    IModularToolComponent {
    override fun getAttributes(): MutableCollection<AttributeEntry?> {
        return List.of<AttributeEntry?>(
            AdditiveAttributeEntry(AttributeWrapper.MINING_SPEED, getToolStats().speed * 1.5, getAttrKey()),
            AdditiveAttributeEntry(AttributeWrapper.MINING_POWER, getToolStats().miningPower.toDouble(), getAttrKey())
        )
    }

    override fun getAttrKey(): String {
        return Companion.attrKey
    }

    override val itemClassification: ItemClassification get() = ItemClassification.ITEM

    companion object {
        const val attrKey: String = "cobalt_drill_head"
    }
}
