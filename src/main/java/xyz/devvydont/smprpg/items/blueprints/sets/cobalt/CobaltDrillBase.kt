package xyz.devvydont.smprpg.items.blueprints.sets.cobalt

import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeEntry
import xyz.devvydont.smprpg.items.interfaces.IModularToolComponent
import xyz.devvydont.smprpg.services.ItemService
import java.util.List

class CobaltDrillBase(itemService: ItemService, type: CustomItemType) : CobaltAttributeItem(itemService, type),
    IModularToolComponent {
    override fun getAttributes(): MutableCollection<AttributeEntry?> {
        return List.of<AttributeEntry?>(
            AdditiveAttributeEntry(AttributeWrapper.MINING_FORTUNE, getToolStats().fortune * 1.5, getAttrKey())
        )
    }

    override fun getAttrKey(): String {
        return Companion.attrKey
    }

    override fun getItemClassification(): ItemClassification? {
        return ItemClassification.ITEM
    }

    companion object {
        const val attrKey: String = "cobalt_drill_base"
    }
}
