package xyz.devvydont.smprpg.items.blueprints.sets.bedrock

import org.bukkit.Color
import org.bukkit.inventory.CraftingRecipe
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.interfaces.IDyeable
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.crafting.builders.ChestplateRecipe

class BedrockChestplate(itemService: ItemService, type: CustomItemType) : BedrockArmorSet(itemService, type),
    IDyeable {

    override val itemClassification: ItemClassification get() = ItemClassification.CHESTPLATE
    override val defense: Int get() = 300

    override fun getColor(): Color { return Color.fromRGB(0x1d1d21) }

    override fun getCustomRecipe(): CraftingRecipe? { return ChestplateRecipe(this, itemService.getCustomItem(CustomItemType.DEEPSLATE_SINGULARITY), generate()).build() }

    override fun getMaxDurability(): Int { return ARMOR_DURABILITY_UNIT * 8 }
}
