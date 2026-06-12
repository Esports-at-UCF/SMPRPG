package xyz.devvydont.smprpg.items.blueprints.sets.bedrock

import org.bukkit.inventory.CraftingRecipe
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.crafting.builders.LeggingsRecipe

class BedrockLeggings(itemService: ItemService, type: CustomItemType) : BedrockArmorSet(itemService, type) {

    override val itemClassification: ItemClassification get() = ItemClassification.CHESTPLATE
    override val defense: Int get() = 275

    override fun getCustomRecipe(): CraftingRecipe? { return LeggingsRecipe(this, itemService.getCustomItem(CustomItemType.DEEPSLATE_SINGULARITY), generate()).build() }

    override fun getMaxDurability(): Int { return ARMOR_DURABILITY_UNIT * 7 }
}
