package xyz.devvydont.smprpg.items.blueprints.sets.cobalt

import org.bukkit.inventory.CraftingRecipe
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeEntry
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemSword
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment
import xyz.devvydont.smprpg.items.interfaces.ICraftable
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.crafting.builders.SwordRecipe
import java.util.List

class CobaltSword(itemService: ItemService, type: CustomItemType) : CobaltAttributeItem(itemService, type),
    ICraftable, IBreakableEquipment, IModelOverridden {

    override fun getAttributeModifiers(item: ItemStack?): MutableCollection<AttributeEntry?>? {
        return List.of<AttributeEntry?>(
            AdditiveAttributeEntry(AttributeWrapper.STRENGTH, ItemSword.getSwordDamage(_type)),
            MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, ItemSword.SWORD_ATTACK_SPEED_DEBUFF * .75),
            ScalarAttributeEntry(AttributeWrapper.MOVEMENT_SPEED, .2)
        )
    }

    override fun getItemClassification(): ItemClassification? {
        return ItemClassification.SWORD
    }

    override fun getActiveSlot(): EquipmentSlotGroup? {
        return EquipmentSlotGroup.MAINHAND
    }

    override fun getCustomRecipe(): CraftingRecipe? {
        return SwordRecipe(
            this,
            getCraftingMaterial(),
            itemService.getCustomItem(CustomItemType.SULFUR_TREATED_TOOL_SHAFT),
            generate()
        ).build()
    }
}
