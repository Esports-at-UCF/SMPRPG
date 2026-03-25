package xyz.devvydont.smprpg.items.blueprints.sets.iron

import org.bukkit.Material
import org.bukkit.inventory.CraftingRecipe
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeEntry
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemSword
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment
import xyz.devvydont.smprpg.items.interfaces.ICraftable
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.crafting.builders.BowRecipe

class IronBow(itemService: ItemService, type: CustomItemType) : IronAttributeItem(itemService, type), ICraftable,
    IBreakableEquipment {

    override val itemClassification: ItemClassification get() = ItemClassification.BOW

    override fun getAttributeModifiers(item: ItemStack?): MutableCollection<AttributeEntry?>? {
        return mutableListOf(
            AdditiveAttributeEntry(AttributeWrapper.STRENGTH, ItemSword.getSwordDamage(Material.IRON_SWORD))
        )
    }

    override fun getCustomRecipe(): CraftingRecipe {
        return BowRecipe(
            this,
            getCraftingMaterial(),
            itemService.getCustomItem(Material.STRING),
            generate()
        ).build()
    }

    override fun getActiveSlot(): EquipmentSlotGroup? {
        return EquipmentSlotGroup.HAND
    }

}
