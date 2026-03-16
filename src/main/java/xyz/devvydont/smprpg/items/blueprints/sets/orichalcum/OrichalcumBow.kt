package xyz.devvydont.smprpg.items.blueprints.sets.orichalcum

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
import java.util.List

class OrichalcumBow(itemService: ItemService, type: CustomItemType) : OrichalcumAttributeItem(itemService, type),
    ICraftable, IBreakableEquipment {

    override fun getAttributeModifiers(item: ItemStack?): MutableCollection<AttributeEntry?>? {
        return List.of<AttributeEntry?>(
            AdditiveAttributeEntry(AttributeWrapper.STRENGTH, ItemSword.getSwordDamage(CustomItemType.ORICHALCUM_SWORD))
        )
    }

    override fun getItemClassification(): ItemClassification? {
        return ItemClassification.BOW
    }

    override fun getActiveSlot(): EquipmentSlotGroup? {
        return EquipmentSlotGroup.HAND
    }

    override fun getCustomRecipe(): CraftingRecipe? {
        return BowRecipe(
            this,
            getCraftingMaterial(),
            itemService.getCustomItem(Material.STRING),
            generate()
        ).build()
    }
}
