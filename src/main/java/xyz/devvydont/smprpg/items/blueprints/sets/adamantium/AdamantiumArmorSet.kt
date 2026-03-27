package xyz.devvydont.smprpg.items.blueprints.sets.adamantium

import net.kyori.adventure.key.Key
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.SMPRPG.Companion.plugin
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.base.CustomAttributeItem
import xyz.devvydont.smprpg.items.interfaces.ICraftable
import xyz.devvydont.smprpg.items.interfaces.IEquippableAssetOverride
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden
import xyz.devvydont.smprpg.items.interfaces.IRepairable
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.items.ToolStats

abstract class AdamantiumArmorSet(itemService: ItemService, type: CustomItemType) :
    CustomAttributeItem(itemService, type), IEquippableAssetOverride, IRepairable {

    override val repairMaterial : MutableCollection<ItemStack> get() = mutableListOf(getCraftingMaterial())
    val armorDurabilityUnit: Int get() = ToolStats.ADAMANTIUM.getArmorUnitDurability().toInt()

    override fun getAssetId(): Key { return key }

    override fun getPowerRating(): Int { return ToolStats.ADAMANTIUM.power }

    open fun getCraftingMaterial(): ItemStack = itemService.getCustomItem(CustomItemType.ADAMANTIUM_INGOT)

    open fun getRecipeKey(): NamespacedKey { return ICraftable.getDefaultRecipeKey(customItemType) }

    open fun unlockedBy(): MutableCollection<ItemStack?>? { return mutableListOf(itemService.getCustomItem(CustomItemType.ADAMANTIUM_INGOT)) }

    open fun getDisplayKey(): Key { return IModelOverridden.ofItemType(customItemType) }

    companion object {
        private val key = Key.key("adamantium")
    }
}
