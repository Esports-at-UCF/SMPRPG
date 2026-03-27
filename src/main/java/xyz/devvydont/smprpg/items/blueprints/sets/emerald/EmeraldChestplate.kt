package xyz.devvydont.smprpg.items.blueprints.sets.emerald

import org.bukkit.inventory.CraftingRecipe
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.crafting.builders.ChestplateRecipe

class EmeraldChestplate(itemService: ItemService, type: CustomItemType) : EmeraldArmorSet(itemService, type) {

    override val itemClassification: ItemClassification get() = ItemClassification.CHESTPLATE

    override val defense: Double get() = 75.0
    override val health: Double get() = 30.0

    override fun getCustomRecipe(): CraftingRecipe? { return ChestplateRecipe(this, itemService.getCustomItem(INGREDIENT), generate()).build() }

    override fun getMaxDurability(): Int { return ARMOR_DURABILITY_UNIT * 8 }

}
