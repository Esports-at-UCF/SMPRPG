package xyz.devvydont.smprpg.items.blueprints.sets.orichalcum

import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeEntry
import xyz.devvydont.smprpg.items.interfaces.IModularToolComponent
import xyz.devvydont.smprpg.services.ItemService
import java.util.List

class OrichalcumDrillBase(itemService: ItemService, type: CustomItemType) : OrichalcumAttributeItem(itemService, type),
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
        const val attrKey: String = "orichalcum_drill_base"
    }
}
