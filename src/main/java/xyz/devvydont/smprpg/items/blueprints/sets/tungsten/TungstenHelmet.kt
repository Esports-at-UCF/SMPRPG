package xyz.devvydont.smprpg.items.blueprints.sets.tungsten

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
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemArmor
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment
import xyz.devvydont.smprpg.items.interfaces.ICraftable
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.crafting.builders.HelmetRecipe
import java.util.List

class TungstenHelmet(itemService: ItemService?, type: CustomItemType?) : TungstenArmorSet(itemService, type),
    ICraftable, IBreakableEquipment, IModelOverridden {

    override fun getAttributeModifiers(item: ItemStack?): MutableCollection<AttributeEntry?> {
        return List.of<AttributeEntry?>(
            AdditiveAttributeEntry(AttributeWrapper.DEFENSE, ItemArmor.getDefenseFromItemType(_type).toDouble()),
            AdditiveAttributeEntry(AttributeWrapper.CRITICAL_DAMAGE, 10.0)
        )
    }

    override fun getActiveSlot(): EquipmentSlotGroup {
        return EquipmentSlotGroup.HEAD
    }

    override fun getRecipeKey(): NamespacedKey {
        return NamespacedKey(plugin, getCustomItemType().getKey() + "-recipe")
    }

    override fun getCustomRecipe(): CraftingRecipe? {
        return HelmetRecipe(this, getCraftingMaterial(), generate()).build()
    }

    override fun getMaxDurability(): Int {
        return 640
    }

    override val itemClassification: ItemClassification get() = ItemClassification.HELMET
}
