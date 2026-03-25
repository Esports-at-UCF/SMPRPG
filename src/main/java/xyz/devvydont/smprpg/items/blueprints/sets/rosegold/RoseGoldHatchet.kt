package xyz.devvydont.smprpg.items.blueprints.sets.rosegold

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
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemSword
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment
import xyz.devvydont.smprpg.items.interfaces.ICraftable
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.crafting.builders.HatchetRecipe

class RoseGoldHatchet(itemService: ItemService, type: CustomItemType) : RoseGoldAttributeItem(itemService, type),
    ICraftable, IBreakableEquipment {

    override val itemClassification: ItemClassification get() = ItemClassification.HATCHET

    override fun getAttributeModifiers(item: ItemStack?): MutableCollection<AttributeEntry?> {
        return mutableListOf(
            AdditiveAttributeEntry(AttributeWrapper.MINING_POWER, getToolStats().miningPower.toDouble()),
            AdditiveAttributeEntry(
                AttributeWrapper.STRENGTH,
                ItemSword.getSwordDamage(CustomItemType.ROSE_GOLD_SWORD) - 5
            ),
            MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, ItemAxe.AXE_ATTACK_SPEED_DEBUFF),
            AdditiveAttributeEntry(AttributeWrapper.MINING_SPEED, getToolStats().speed * 0.8),
            AdditiveAttributeEntry(AttributeWrapper.WOODCUTTING_FORTUNE, getToolStats().fortune * 0.8),
            AdditiveAttributeEntry(AttributeWrapper.FARMING_FORTUNE, getToolStats().fortune * 0.8)
        )
    }

    override fun updateItemData(itemStack: ItemStack) {
        super.updateItemData(itemStack)
        itemStack.setData(DataComponentTypes.TOOL, TOOL_COMP)
    }

    override fun getCustomRecipe(): CraftingRecipe {
        return HatchetRecipe(
            this,
            getCraftingMaterial(),
            itemService.getCustomItem(Material.STICK),
            generate()
        ).build()
    }

    companion object {
        val TOOL_COMP: Tool = Tool.tool()
            .defaultMiningSpeed(0.0001f)
            .build()
    }
}
