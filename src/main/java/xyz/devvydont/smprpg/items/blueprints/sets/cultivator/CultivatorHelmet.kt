package xyz.devvydont.smprpg.items.blueprints.sets.cultivator

import io.papermc.paper.datacomponent.item.Equippable
import org.bukkit.inventory.CraftingRecipe
import org.bukkit.inventory.EquipmentSlot
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.interfaces.IEquippableOverride
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.crafting.builders.HelmetRecipe

class CultivatorHelmet(itemService: ItemService, type: CustomItemType) : CultivatorArmorSet(itemService, type), IEquippableOverride {

    override val itemClassification: ItemClassification get() = ItemClassification.HELMET

    override fun getMaxDurability(): Int {
        return 320
    }

    override fun getCustomRecipe(): CraftingRecipe {
        return HelmetRecipe(this, itemService.getCustomItem(CustomItemType.PREMIUM_HAY_BLOCK), generate()).build()
    }

    override fun getEquipmentOverride(): Equippable {
        return IEquippableOverride.generateDefault(EquipmentSlot.HEAD)
    }
}
