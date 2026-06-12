package xyz.devvydont.smprpg.items.blueprints.sets.elderflame

import net.kyori.adventure.key.Key
import org.bukkit.NamespacedKey
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.base.CustomAttributeItem
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment
import xyz.devvydont.smprpg.items.interfaces.ICraftable
import xyz.devvydont.smprpg.items.interfaces.IEquippableAssetOverride
import xyz.devvydont.smprpg.items.interfaces.IRepairable
import xyz.devvydont.smprpg.services.ItemService

abstract class ElderflameArmorSet(itemService: ItemService, type: CustomItemType) :
    CustomAttributeItem(itemService, type), IBreakableEquipment, ICraftable, IEquippableAssetOverride, IRepairable {

    override val repairMaterial: MutableCollection<ItemStack> get() = mutableListOf(itemService.getCustomItem(CustomItemType.DRACONIC_CRYSTAL))

    override fun getActiveSlot(): EquipmentSlotGroup { return EquipmentSlotGroup.ARMOR }

    override fun getPowerRating(): Int { return 50 }

    override fun getRecipeKey(): NamespacedKey { return ICraftable.getDefaultRecipeKey(customItemType) }

    override fun getAssetId(): Key { return key }

    override fun unlockedBy(): MutableCollection<ItemStack?> { return mutableListOf(itemService.getCustomItem(CustomItemType.DRAGON_SCALES)) }

    companion object {
        const val ARMOR_DURABILITY_UNIT : Int = 192
        private val key = Key.key("elderflame")
    }
}
