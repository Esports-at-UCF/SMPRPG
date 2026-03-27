package xyz.devvydont.smprpg.items.blueprints.sets.amethyst

import org.bukkit.inventory.CraftingRecipe
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.meta.trim.TrimPattern
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.crafting.builders.LeggingsRecipe

class AmethystLeggings(itemService: ItemService, type: CustomItemType) : AmethystArmorSet(itemService, type) {

    override val itemClassification: ItemClassification get() = ItemClassification.LEGGINGS
    override val defense: Int get() = 10
    override val health: Int get() = 10

    override fun getActiveSlot(): EquipmentSlotGroup {
        return EquipmentSlotGroup.LEGS
    }

    override fun getTrimPattern(): TrimPattern? {
        return TrimPattern.SILENCE
    }

    override fun getCustomRecipe(): CraftingRecipe? {
        return LeggingsRecipe(this, itemService.getCustomItem(CustomItemType.ENCHANTED_AMETHYST), generate()).build()
    }

    override fun getMaxDurability(): Int {
        return armorDurabilityUnit * 7
    }

}
