package xyz.devvydont.smprpg.items.blueprints.sets.orichalcum

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
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemArmor
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment
import xyz.devvydont.smprpg.items.interfaces.ICraftable
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.crafting.builders.BootsRecipe
import java.util.List

class OrichalcumBoots(itemService: ItemService, type: CustomItemType) : OrichalcumArmorSet(itemService, type),
    IBreakableEquipment, ICraftable, IModelOverridden {

    override fun getAttributeModifiers(item: ItemStack?): MutableCollection<AttributeEntry?> {
        return List.of<AttributeEntry?>(
            AdditiveAttributeEntry(AttributeWrapper.DEFENSE, ItemArmor.getDefenseFromItemType(customItemType).toDouble()),
            ScalarAttributeEntry(AttributeWrapper.STRENGTH, .25)
        )
    }

    override fun getActiveSlot(): EquipmentSlotGroup {
        return EquipmentSlotGroup.FEET
    }

    override fun getMaxDurability(): Int {
        return 520
    }

    override fun getCustomRecipe(): CraftingRecipe? {
        return BootsRecipe(this, getCraftingMaterial(), generate()).build()
    }

    override val itemClassification: ItemClassification get() = ItemClassification.BOOTS
}
