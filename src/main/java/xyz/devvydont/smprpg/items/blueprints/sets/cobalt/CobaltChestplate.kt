package xyz.devvydont.smprpg.items.blueprints.sets.cobalt

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
import xyz.devvydont.smprpg.util.crafting.builders.ChestplateRecipe
import java.util.List

class CobaltChestplate(itemService: ItemService, type: CustomItemType) : CobaltArmorSet(itemService, type),
    IBreakableEquipment, ICraftable, IModelOverridden {

    override fun getAttributeModifiers(item: ItemStack?): MutableCollection<AttributeEntry?> {
        return List.of<AttributeEntry?>(
            AdditiveAttributeEntry(AttributeWrapper.DEFENSE, ItemArmor.getDefenseFromItemType(customItemType).toDouble()),
            ScalarAttributeEntry(AttributeWrapper.MINING_SPEED, .10),
            ScalarAttributeEntry(AttributeWrapper.MOVEMENT_SPEED, .05)
        )
    }

    override fun getActiveSlot(): EquipmentSlotGroup? {
        return EquipmentSlotGroup.CHEST
    }

    override val itemClassification: ItemClassification get() = ItemClassification.CHESTPLATE

    override fun getMaxDurability(): Int {
        return 800
    }

    override fun getCustomRecipe(): CraftingRecipe? {
        return ChestplateRecipe(this, getCraftingMaterial(), generate()).build()
    }

}
