package xyz.devvydont.smprpg.items.blueprints.sets.bone

import org.bukkit.Material
import org.bukkit.inventory.CraftingRecipe
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.crafting.builders.LeggingsRecipe

class BoneLeggings(itemService: ItemService, type: CustomItemType) : BoneArmorSet(itemService, type) {

    override val itemClassification: ItemClassification get() = ItemClassification.LEGGINGS
    override val defense: Int get() = 45

    override fun getCustomRecipe(): CraftingRecipe? { return LeggingsRecipe(this, itemService.getCustomItem(Material.BONE), generate()).build() }

    override fun getMaxDurability(): Int { return ARMOR_DURABILITY_UNIT * 7 }

}
