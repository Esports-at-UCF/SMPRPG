package xyz.devvydont.smprpg.items.blueprints.sets.emerald

import org.bukkit.inventory.CraftingRecipe
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.crafting.builders.HelmetRecipe

class EmeraldHelmet(itemService: ItemService, type: CustomItemType) : EmeraldArmorSet(itemService, type) {

    override val itemClassification: ItemClassification get() = ItemClassification.HELMET

    override val defense: Double get() = 50.0
    override val health: Double get() = 20.0

    override fun getCustomRecipe(): CraftingRecipe? { return HelmetRecipe(this, itemService.getCustomItem(INGREDIENT), generate()).build() }

    override fun getMaxDurability(): Int { return ARMOR_DURABILITY_UNIT * 5 }

}
