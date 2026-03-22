package xyz.devvydont.smprpg.items.blueprints.sets.tin

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.Tool
import org.bukkit.Material
import org.bukkit.inventory.CraftingRecipe
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeEntry
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemAxe
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment
import xyz.devvydont.smprpg.items.interfaces.ICraftable
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.crafting.builders.AxeRecipe
import xyz.devvydont.smprpg.util.items.ToolGlobals
import java.util.List

class TinAxe(itemService: ItemService, type: CustomItemType) : TinAttributeItem(itemService, type), ICraftable, IBreakableEquipment {

    override val itemClassification: ItemClassification get() = ItemClassification.AXE

    override fun getAttributeModifiers(item: ItemStack?): MutableCollection<AttributeEntry?>? {
        return mutableListOf(
            AdditiveAttributeEntry(AttributeWrapper.MINING_POWER, ToolGlobals.TIN_TOOL_MINING_POWER.toDouble()),
            AdditiveAttributeEntry(AttributeWrapper.STRENGTH, ItemAxe.getAxeDamage(CustomItemType.TIN_AXE)),
            MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, ItemAxe.AXE_ATTACK_SPEED_DEBUFF),
            AdditiveAttributeEntry(AttributeWrapper.MINING_SPEED, ToolGlobals.TIN_TOOL_SPEED.toDouble()),
            AdditiveAttributeEntry(AttributeWrapper.WOODCUTTING_FORTUNE, ToolGlobals.TIN_TOOL_FORTUNE.toDouble())
        )
    }

    override fun updateItemData(itemStack: ItemStack) {
        super.updateItemData(itemStack)
        itemStack.setData(DataComponentTypes.TOOL, TOOL_COMP)
    }

    override fun getCustomRecipe(): CraftingRecipe? {
        return AxeRecipe(
            this,
            itemService.getCustomItem(CustomItemType.TIN_INGOT),
            itemService.getCustomItem(Material.STICK),
            generate()
        ).build()
    }

    companion object {
        val TOOL_COMP: Tool = Tool.tool().build()
    }
}
