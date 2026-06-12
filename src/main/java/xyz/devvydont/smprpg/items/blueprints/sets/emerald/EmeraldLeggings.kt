package xyz.devvydont.smprpg.items.blueprints.sets.emerald

import org.bukkit.inventory.CraftingRecipe
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.crafting.builders.LeggingsRecipe

class EmeraldLeggings(itemService: ItemService, type: CustomItemType) : EmeraldArmorSet(itemService, type) {

    override val itemClassification: ItemClassification get() = ItemClassification.LEGGINGS

    override val defense: Double get() = 65.0
    override val health: Double get() = 30.0

    override fun getCustomRecipe(): CraftingRecipe? { return LeggingsRecipe(this, itemService.getCustomItem(INGREDIENT), generate()).build() }
    override fun getMaxDurability(): Int { return ARMOR_DURABILITY_UNIT * 7 }

}
