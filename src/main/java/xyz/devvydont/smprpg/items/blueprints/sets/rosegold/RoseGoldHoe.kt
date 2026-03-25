package xyz.devvydont.smprpg.items.blueprints.sets.rosegold

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.Tool
import org.bukkit.Material
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

import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemHoe
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment
import xyz.devvydont.smprpg.items.interfaces.ICraftable
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.crafting.builders.HoeRecipe
import xyz.devvydont.smprpg.util.items.ToolGlobals

class RoseGoldHoe(itemService: ItemService, type: CustomItemType) : RoseGoldAttributeItem(itemService, type), ICraftable,
    IBreakableEquipment {

    override val itemClassification: ItemClassification get() = ItemClassification.HOE

    override fun getAttributeModifiers(item: ItemStack?): MutableCollection<AttributeEntry?> {
        return mutableListOf(
            AdditiveAttributeEntry(AttributeWrapper.STRENGTH, ItemHoe.getHoeDamage(CustomItemType.ROSE_GOLD_HOE)),
            MultiplicativeAttributeEntry(
                AttributeWrapper.ATTACK_SPEED,
                ItemHoe.getHoeAttackSpeedDebuff(CustomItemType.ROSE_GOLD_HOE)
            ),
            AdditiveAttributeEntry(AttributeWrapper.MINING_SPEED, getToolStats().speed.toDouble()),
            AdditiveAttributeEntry(AttributeWrapper.FARMING_FORTUNE, getToolStats().fortune.toDouble())
        )
    }

    override fun getPowerRating(): Int {
        return ToolGlobals.ROSE_GOLD_TOOL_POWER
    }

    override fun getActiveSlot(): EquipmentSlotGroup {
        return EquipmentSlotGroup.MAINHAND
    }

    override fun getMaxDurability(): Int {
        return ToolGlobals.ROSE_GOLD_TOOL_DURABILITY
    }

    override fun getRecipeKey(): NamespacedKey {
        return NamespacedKey(plugin, customItemType.key + "-recipe")
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

    override fun unlockedBy(): MutableCollection<ItemStack?> {
        return mutableListOf(
            getCraftingMaterial()
        )
    }

    companion object {
        val TOOL_COMP: Tool = Tool.tool().build()
    }
}
