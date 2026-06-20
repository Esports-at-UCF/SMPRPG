package xyz.devvydont.smprpg.items.blueprints.sets.cultivator

import net.kyori.adventure.key.Key
import org.bukkit.inventory.CraftingRecipe
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.interfaces.IEquippableAssetOverride
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.crafting.builders.LeggingsRecipe

class CultivatorLeggings(itemService: ItemService, type: CustomItemType) : CultivatorArmorSet(itemService, type),
    IEquippableAssetOverride {
    override val itemClassification: ItemClassification get() = ItemClassification.LEGGINGS

    override fun getMaxDurability(): Int { return 448 }

    override fun getCustomRecipe(): CraftingRecipe { return LeggingsRecipe(this, itemService.getCustomItem(CustomItemType.PREMIUM_HAY_BLOCK), generate()).build() }

    override fun getAssetId(): Key { return assetKey }
}
