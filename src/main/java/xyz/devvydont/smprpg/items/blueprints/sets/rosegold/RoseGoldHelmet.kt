package xyz.devvydont.smprpg.items.blueprints.sets.rosegold

import org.bukkit.inventory.CraftingRecipe
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeEntry
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemArmor
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment
import xyz.devvydont.smprpg.items.interfaces.ICraftable
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.crafting.builders.HelmetRecipe

class RoseGoldHelmet(itemService: ItemService, type: CustomItemType) : RoseGoldArmorSet(itemService, type),
    ICraftable, IBreakableEquipment {

    override val itemClassification: ItemClassification get() = ItemClassification.HELMET

    override fun getAttributeModifiers(item: ItemStack?): MutableCollection<AttributeEntry?> {
        return mutableListOf(
            AdditiveAttributeEntry(
                AttributeWrapper.DEFENSE,
                ItemArmor.getDefenseFromItemType(CustomItemType.ROSE_GOLD_HELMET).toDouble()
            ),
            AdditiveAttributeEntry(AttributeWrapper.MINING_SPEED, 50.0),
            ScalarAttributeEntry(AttributeWrapper.STRENGTH, .2)
        )
    }

    override fun getActiveSlot(): EquipmentSlotGroup {
        return EquipmentSlotGroup.HEAD
    }

    override fun getCustomRecipe(): CraftingRecipe? {
        return HelmetRecipe(this, getCraftingMaterial(), generate()).build()
    }

    override fun getMaxDurability(): Int {
        return 800
    }

}
