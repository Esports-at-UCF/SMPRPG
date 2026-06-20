package xyz.devvydont.smprpg.items.blueprints.sets.cultivator

import net.kyori.adventure.key.Key
import org.bukkit.inventory.CraftingRecipe
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.interfaces.IEquippableAssetOverride
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.crafting.builders.ChestplateRecipe

class CultivatorChestplate(itemService: ItemService, type: CustomItemType) : CultivatorArmorSet(itemService, type), IEquippableAssetOverride {

    override val itemClassification: ItemClassification get() = ItemClassification.CHESTPLATE

    override fun getMaxDurability(): Int { return 512 }

    override fun getCustomRecipe(): CraftingRecipe { return ChestplateRecipe(this, itemService.getCustomItem(CustomItemType.PREMIUM_HAY_BLOCK), generate()).build() }

    override fun getAssetId(): Key { return assetKey }
}
