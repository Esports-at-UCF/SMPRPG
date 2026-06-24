package xyz.devvydont.smprpg.items.blueprints.sets.bone

import org.bukkit.Material
import org.bukkit.inventory.CraftingRecipe
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.crafting.builders.HelmetRecipe

class BoneHelmet(itemService: ItemService, type: CustomItemType) : BoneArmorSet(itemService, type) {

    override val itemClassification: ItemClassification get() = ItemClassification.HELMET
    override val defense: Int get() = 35

    override fun getCustomRecipe(): CraftingRecipe? { return HelmetRecipe(this, itemService.getCustomItem(Material.BONE), generate()).build() }

    override fun getMaxDurability(): Int { return ARMOR_DURABILITY_UNIT * 5 }
}
