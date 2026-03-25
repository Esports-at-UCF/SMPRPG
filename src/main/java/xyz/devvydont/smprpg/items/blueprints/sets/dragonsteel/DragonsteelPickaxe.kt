package xyz.devvydont.smprpg.items.blueprints.sets.dragonsteel

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.Tool
import io.papermc.paper.registry.keys.tags.BlockTypeTagKeys
import net.kyori.adventure.util.TriState
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
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemPickaxe
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment
import xyz.devvydont.smprpg.items.interfaces.ICraftable
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.crafting.builders.PickaxeRecipe
import xyz.devvydont.smprpg.util.items.ToolGlobals
import java.util.List

class DragonsteelPickaxe(itemService: ItemService, type: CustomItemType) : DragonsteelAttributeItem(itemService, type),
    IBreakableEquipment, ICraftable {

    override val itemClassification: ItemClassification get() = ItemClassification.PICKAXE

    override fun getAttributeModifiers(item: ItemStack?): MutableCollection<AttributeEntry?> {
        return mutableListOf(
            AdditiveAttributeEntry(AttributeWrapper.MINING_POWER, getToolStats().miningPower.toDouble()),
            AdditiveAttributeEntry(AttributeWrapper.STRENGTH, 40.0),
            MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, ItemPickaxe.PICKAXE_ATTACK_SPEED_DEBUFF),
            AdditiveAttributeEntry(AttributeWrapper.MINING_SPEED, getToolStats().speed.toDouble()),
            AdditiveAttributeEntry(AttributeWrapper.MINING_FORTUNE, getToolStats().fortune.toDouble())
        )
    }

    override fun updateItemData(itemStack: ItemStack) {
        super.updateItemData(itemStack)
        itemStack.setData<Tool?>(DataComponentTypes.TOOL, TOOL_COMP)
    }

    override fun getCustomRecipe(): CraftingRecipe? {
        return PickaxeRecipe(
            this,
            getCraftingMaterial(),
            itemService.getCustomItem(CustomItemType.OBSIDIAN_TOOL_ROD),
            generate()
        ).build()
    }

    companion object {
        val TOOL_COMP: Tool = Tool.tool()
            .defaultMiningSpeed(0.0001f)
            .addRule(
                Tool.rule(
                    ToolGlobals.blockRegistry.getTag(BlockTypeTagKeys.INCORRECT_FOR_DIAMOND_TOOL),
                    0.0001f,
                    TriState.FALSE
                )
            )
            .addRule(
                Tool.rule(
                    ToolGlobals.blockRegistry.getTag(BlockTypeTagKeys.MINEABLE_PICKAXE),
                    0.0001f,
                    TriState.TRUE
                )
            )
            .build()
    }
}
