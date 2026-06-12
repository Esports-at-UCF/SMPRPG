package xyz.devvydont.smprpg.items.blueprints.sets.adamantium

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.Tool
import org.bukkit.inventory.CraftingRecipe
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeEntry
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemShovel
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment
import xyz.devvydont.smprpg.items.interfaces.ICraftable
import xyz.devvydont.smprpg.items.interfaces.ISkillRequirement
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.skills.SkillType
import xyz.devvydont.smprpg.util.crafting.builders.ShovelRecipe

class AdamantiumShovel(itemService: ItemService, type: CustomItemType) : AdamantiumAttributeItem(itemService, type),
    ICraftable, IBreakableEquipment {

    override val itemClassification: ItemClassification get() = ItemClassification.SHOVEL
    override val skillRequirements: MutableMap<SkillType, Int> get() = mutableMapOf(Pair(SkillType.MINING, toolStats.skillReqLevel))

    override fun getAttributeModifiers(item: ItemStack?): MutableCollection<AttributeEntry?> {
        return mutableListOf(
            AdditiveAttributeEntry(AttributeWrapper.MINING_POWER, toolStats.miningPower.toDouble()),
            AdditiveAttributeEntry(
                AttributeWrapper.STRENGTH,
                ItemShovel.getShovelDamage(CustomItemType.ADAMANTIUM_SHOVEL)
            ),
            AdditiveAttributeEntry(AttributeWrapper.MINING_SPEED, toolStats.speed.toDouble()),
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
