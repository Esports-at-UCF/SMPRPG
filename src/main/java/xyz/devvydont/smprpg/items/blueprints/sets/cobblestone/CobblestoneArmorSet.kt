package xyz.devvydont.smprpg.items.blueprints.sets.cobblestone

import net.kyori.adventure.key.Key
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeEntry
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry
import xyz.devvydont.smprpg.items.base.CustomAttributeItem
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment
import xyz.devvydont.smprpg.items.interfaces.ICraftable
import xyz.devvydont.smprpg.items.interfaces.IEquippableAssetOverride
import xyz.devvydont.smprpg.items.interfaces.IRepairable
import xyz.devvydont.smprpg.services.ItemService

abstract class CobblestoneArmorSet(itemService: ItemService, type: CustomItemType) :
    CustomAttributeItem(itemService, type), IEquippableAssetOverride, ICraftable, IBreakableEquipment, IRepairable {

    override val repairMaterial: MutableCollection<ItemStack> get() = mutableListOf(itemService.getCustomItem(CustomItemType.COMPRESSED_COBBLESTONE))

    abstract val defense : Int

    override fun getAssetId(): Key { return key }

    override fun getAttributeModifiers(item: ItemStack?): MutableCollection<AttributeEntry?> {
        return mutableListOf(
            AdditiveAttributeEntry(AttributeWrapper.DEFENSE, this.defense.toDouble()),
            ScalarAttributeEntry(AttributeWrapper.MOVEMENT_SPEED, -0.15)
        )
    }

    override fun getPowerRating(): Int { return 5 }

    override fun getRecipeKey(): NamespacedKey { return ICraftable.getDefaultRecipeKey(customItemType) }

    override fun unlockedBy(): MutableCollection<ItemStack?> { return mutableListOf(itemService.getCustomItem(Material.COBBLESTONE)) }

    companion object {
        const val ARMOR_DURABILITY_UNIT : Int = 8
        private val key = Key.key("cobblestone")
    }
}
