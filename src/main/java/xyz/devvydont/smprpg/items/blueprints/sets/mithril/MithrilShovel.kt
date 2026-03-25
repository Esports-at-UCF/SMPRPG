package xyz.devvydont.smprpg.items.blueprints.sets.mithril

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.Tool
import org.bukkit.NamespacedKey
import org.bukkit.inventory.CraftingRecipe
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.SMPRPG.Companion.plugin
import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeEntry
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry
import xyz.devvydont.smprpg.items.base.CustomAttributeItem
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemShovel
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment
import xyz.devvydont.smprpg.items.interfaces.ICraftable
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.crafting.builders.ShovelRecipe
import xyz.devvydont.smprpg.util.items.ToolGlobals
import java.util.List

class MithrilShovel(itemService: ItemService, type: CustomItemType) : MithrilAttributeItem(itemService, type),
    ICraftable, IBreakableEquipment {

    override val itemClassification: ItemClassification get() = ItemClassification.SHOVEL

    override fun getAttributeModifiers(item: ItemStack?): MutableCollection<AttributeEntry?> {
        return mutableListOf(
            AdditiveAttributeEntry(
                AttributeWrapper.STRENGTH,
                ItemShovel.getShovelDamage(CustomItemType.MITHRIL_SHOVEL)
            ),
            AdditiveAttributeEntry(AttributeWrapper.MINING_SPEED, getToolStats().speed.toDouble()),
            MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, ItemShovel.SHOVEL_ATTACK_SPEED_DEBUFF)
        )
    }

    override fun updateItemData(itemStack: ItemStack) {
        super.updateItemData(itemStack)
        itemStack.setData<Tool?>(DataComponentTypes.TOOL, TOOL_COMP)
    }

    override fun getCustomRecipe(): CraftingRecipe? {
        return ShovelRecipe(
            this,
            getCraftingMaterial(),
            itemService.getCustomItem(CustomItemType.STEEL_TOOL_SHAFT),
            generate()
        ).build()
    }

    companion object {
        val TOOL_COMP: Tool = Tool.tool().build()
    }
}
