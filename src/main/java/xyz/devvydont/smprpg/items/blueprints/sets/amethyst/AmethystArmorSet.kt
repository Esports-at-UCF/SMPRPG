package xyz.devvydont.smprpg.items.blueprints.sets.amethyst

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.trim.TrimMaterial
import xyz.devvydont.smprpg.SMPRPG.Companion.plugin
import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeEntry
import xyz.devvydont.smprpg.items.base.CustomAttributeItem
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment
import xyz.devvydont.smprpg.items.interfaces.ICraftable
import xyz.devvydont.smprpg.items.interfaces.IRepairable
import xyz.devvydont.smprpg.items.interfaces.ITrimmable
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.items.ToolStats

abstract class AmethystArmorSet(itemService: ItemService, type: CustomItemType) :
    CustomAttributeItem(itemService, type), ITrimmable, IBreakableEquipment, ICraftable, IRepairable {

    val armorDurabilityUnit : Int = ToolStats.IRON.getArmorUnitDurability().toInt()
    override val repairMaterial : MutableCollection<ItemStack> get() = mutableListOf(itemService.getCustomItem(CustomItemType.ENCHANTED_AMETHYST))

    override fun getAttributeModifiers(item: ItemStack?): MutableCollection<AttributeEntry?> {
        return mutableListOf(
            AdditiveAttributeEntry(AttributeWrapper.DEFENSE, this.defense.toDouble()),
            AdditiveAttributeEntry(AttributeWrapper.HEALTH, this.health.toDouble()),
            AdditiveAttributeEntry(AttributeWrapper.INTELLIGENCE, 30.0)
        )
    }

    override fun getRecipeKey(): NamespacedKey {
        return NamespacedKey(plugin, customItemType.key + "-recipe")
    }

    override fun unlockedBy(): MutableCollection<ItemStack?> {
        return mutableListOf(
            itemService.getCustomItem(Material.AMETHYST_SHARD)
        )
    }

    abstract val health: Int
    abstract val defense: Int

    override fun getPowerRating(): Int {
        return 12
    }

    override fun getTrimMaterial(): TrimMaterial? {
        return TrimMaterial.AMETHYST
    }
}
