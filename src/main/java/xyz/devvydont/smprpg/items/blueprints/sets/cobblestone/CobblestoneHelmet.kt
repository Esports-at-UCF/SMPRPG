package xyz.devvydont.smprpg.items.blueprints.sets.cobblestone

import org.bukkit.inventory.CraftingRecipe
import org.bukkit.inventory.EquipmentSlotGroup
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.crafting.builders.HelmetRecipe

class CobblestoneHelmet(itemService: ItemService, type: CustomItemType) : CobblestoneArmorSet(itemService, type) {

    override val itemClassification: ItemClassification get() = ItemClassification.HELMET
    override val defense: Int get() = 45

    override fun getActiveSlot(): EquipmentSlotGroup { return EquipmentSlotGroup.HEAD }

    override fun getCustomRecipe(): CraftingRecipe? { return HelmetRecipe(this, itemService.getCustomItem(CustomItemType.COMPRESSED_COBBLESTONE), generate()).build() }

    override fun getMaxDurability(): Int { return ARMOR_DURABILITY_UNIT * 5 }

}
