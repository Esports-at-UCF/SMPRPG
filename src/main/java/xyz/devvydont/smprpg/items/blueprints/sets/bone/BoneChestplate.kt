package xyz.devvydont.smprpg.items.blueprints.sets.bone

import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.inventory.CraftingRecipe
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.interfaces.IDyeable
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.crafting.builders.ChestplateRecipe

class BoneChestplate(itemService: ItemService, type: CustomItemType) : BoneArmorSet(itemService, type), IDyeable {

    override val itemClassification: ItemClassification get() = ItemClassification.CHESTPLATE
    override val defense: Int get() = 15

    override fun getColor(): Color { return Color.fromRGB(0x9d9d97) }

    override fun getCustomRecipe(): CraftingRecipe? { return ChestplateRecipe(this, itemService.getCustomItem(Material.BONE), generate()).build() }

    override fun getMaxDurability(): Int { return ARMOR_DURABILITY_UNIT * 8 }
}
