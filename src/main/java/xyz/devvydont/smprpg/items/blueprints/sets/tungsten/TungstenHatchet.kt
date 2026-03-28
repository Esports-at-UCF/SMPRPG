package xyz.devvydont.smprpg.items.blueprints.sets.tungsten

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.Tool
import org.bukkit.inventory.CraftingRecipe
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeEntry
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemAxe
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemSword
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment
import xyz.devvydont.smprpg.items.interfaces.ICraftable
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.skills.SkillType
import xyz.devvydont.smprpg.util.crafting.builders.HatchetRecipe

class TungstenHatchet(itemService: ItemService, type: CustomItemType) : TungstenAttributeItem(itemService, type),
    ICraftable, IBreakableEquipment, IModelOverridden {

    override val itemClassification: ItemClassification get() = ItemClassification.HATCHET
    override val skillRequirements: MutableMap<SkillType, Int> get() = mutableMapOf(
        Pair(SkillType.WOODCUTTING, toolStats.skillReqLevel),
        Pair(SkillType.FARMING, toolStats.skillReqLevel)
    )

    override fun getAttributeModifiers(item: ItemStack?): MutableCollection<AttributeEntry?>? {
        return mutableListOf(
            AdditiveAttributeEntry(AttributeWrapper.MINING_POWER, toolStats.miningPower.toDouble()),
            AdditiveAttributeEntry(
                AttributeWrapper.STRENGTH,
                ItemSword.getSwordDamage(CustomItemType.TUNGSTEN_SWORD) - 5
            ),
            MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, ItemAxe.AXE_ATTACK_SPEED_DEBUFF),
            AdditiveAttributeEntry(AttributeWrapper.MINING_SPEED, toolStats.speed * 0.8),
            AdditiveAttributeEntry(AttributeWrapper.WOODCUTTING_FORTUNE, toolStats.fortune * 0.8),
            AdditiveAttributeEntry(AttributeWrapper.FARMING_FORTUNE, toolStats.fortune * 0.8)
        )
    }

    override fun getActiveSlot(): EquipmentSlotGroup? {
        return EquipmentSlotGroup.MAINHAND
    }

    override fun updateItemData(itemStack: ItemStack) {
        super.updateItemData(itemStack)
        itemStack.setData<Tool?>(DataComponentTypes.TOOL, TOOL_COMP)
    }

    override fun getCustomRecipe(): CraftingRecipe? {
        return HatchetRecipe(
            this,
            getCraftingMaterial(),
            itemService.getCustomItem(CustomItemType.SULFUR_TREATED_TOOL_SHAFT),
            generate()
        ).build()
    }

    companion object {
        val TOOL_COMP: Tool = Tool.tool()
            .defaultMiningSpeed(0.0001f)
            .build()
    }
}
