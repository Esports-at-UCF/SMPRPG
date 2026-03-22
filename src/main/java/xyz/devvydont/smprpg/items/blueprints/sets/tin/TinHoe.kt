package xyz.devvydont.smprpg.items.blueprints.sets.tin

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.Tool
import org.bukkit.Material
import org.bukkit.inventory.CraftingRecipe
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeEntry
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemHoe
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment
import xyz.devvydont.smprpg.items.interfaces.ICraftable
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.crafting.builders.HoeRecipe
import xyz.devvydont.smprpg.util.items.ToolGlobals
import java.util.List

class TinHoe(itemService: ItemService, type: CustomItemType) : TinAttributeItem(itemService, type), ICraftable, IBreakableEquipment {

    override val itemClassification: ItemClassification get() = ItemClassification.HOE

    override fun getAttributeModifiers(item: ItemStack?): MutableCollection<AttributeEntry?>? {
        return mutableListOf<AttributeEntry?>(
            AdditiveAttributeEntry(AttributeWrapper.STRENGTH, ItemHoe.getHoeDamage(CustomItemType.TIN_HOE)),
            MultiplicativeAttributeEntry(
                AttributeWrapper.ATTACK_SPEED,
                ItemHoe.getHoeAttackSpeedDebuff(CustomItemType.TIN_HOE)
            ),
            AdditiveAttributeEntry(AttributeWrapper.MINING_SPEED, ToolGlobals.TIN_TOOL_SPEED.toDouble()),
            AdditiveAttributeEntry(AttributeWrapper.FARMING_FORTUNE, ToolGlobals.TIN_TOOL_FORTUNE.toDouble())
        )
    }

    override fun updateItemData(itemStack: ItemStack) {
        super.updateItemData(itemStack)
        itemStack.setData(DataComponentTypes.TOOL, TOOL_COMP)
    }

    override fun getCustomRecipe(): CraftingRecipe? {
        return HoeRecipe(
            this,
            getCraftingMaterial(),
            itemService.getCustomItem(Material.STICK),
            generate()
        ).build()
    }

    companion object {
        val TOOL_COMP: Tool = Tool.tool().build()
    }
}
