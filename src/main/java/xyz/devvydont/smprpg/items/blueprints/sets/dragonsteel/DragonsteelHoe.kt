package xyz.devvydont.smprpg.items.blueprints.sets.dragonsteel

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.Tool
import io.papermc.paper.registry.keys.tags.BlockTypeTagKeys
import net.kyori.adventure.util.TriState
import org.bukkit.Material
import org.bukkit.inventory.CraftingRecipe
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

class DragonsteelHoe(itemService: ItemService, type: CustomItemType) : DragonsteelAttributeItem(itemService, type),
    ICraftable, IBreakableEquipment {

    override val itemClassification: ItemClassification get() = ItemClassification.HOE

    override fun getAttributeModifiers(item: ItemStack?): MutableCollection<AttributeEntry?> {
        return mutableListOf(
            AdditiveAttributeEntry(AttributeWrapper.STRENGTH, 30.0),
            MultiplicativeAttributeEntry(
                AttributeWrapper.ATTACK_SPEED,
                ItemHoe.getHoeAttackSpeedDebuff(Material.NETHERITE_HOE)
            ),
            AdditiveAttributeEntry(AttributeWrapper.MINING_SPEED, getToolStats().speed.toDouble()),
            AdditiveAttributeEntry(AttributeWrapper.FARMING_FORTUNE, getToolStats().fortune.toDouble())
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
            itemService.getCustomItem(CustomItemType.OBSIDIAN_TOOL_ROD),
            generate()
        ).build()
    }

    companion object {
        val TOOL_COMP: Tool = Tool.tool()
            .defaultMiningSpeed(1.0f)
            .addRule(
                Tool.rule(
                    ToolGlobals.blockRegistry.getTag(BlockTypeTagKeys.INCORRECT_FOR_DIAMOND_TOOL),
                    1.0f,
                    TriState.FALSE
                )
            )
            .addRule(Tool.rule(ToolGlobals.blockRegistry.getTag(BlockTypeTagKeys.MINEABLE_HOE), 11.0f, TriState.TRUE))
            .build()
    }
}
