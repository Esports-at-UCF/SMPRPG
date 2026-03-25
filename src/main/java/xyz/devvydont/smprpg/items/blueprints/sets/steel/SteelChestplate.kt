package xyz.devvydont.smprpg.items.blueprints.sets.steel

import org.bukkit.inventory.CraftingRecipe
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeEntry
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemArmor
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment
import xyz.devvydont.smprpg.items.interfaces.ICraftable
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.crafting.builders.ChestplateRecipe

class SteelChestplate(itemService: ItemService, type: CustomItemType) : SteelArmorSet(itemService, type),
    IBreakableEquipment, ICraftable {

    override val itemClassification: ItemClassification get() = ItemClassification.CHESTPLATE

    override fun getAttributeModifiers(item: ItemStack?): MutableCollection<AttributeEntry?> {
        return mutableListOf(
            AdditiveAttributeEntry(
                AttributeWrapper.DEFENSE,
                ItemArmor.getDefenseFromItemType(CustomItemType.STEEL_CHESTPLATE).toDouble()
            ),
            AdditiveAttributeEntry(AttributeWrapper.EXPLOSION_KNOCKBACK_RESISTANCE, .25)
        )
    }

    override fun getActiveSlot(): EquipmentSlotGroup {
        return EquipmentSlotGroup.CHEST
    }

    override fun getMaxDurability(): Int {
        return 1000
    }

    override fun getCustomRecipe(): CraftingRecipe? {
        return ChestplateRecipe(this, itemService.getCustomItem(CustomItemType.STEEL_INGOT), generate()).build()
    }

}
