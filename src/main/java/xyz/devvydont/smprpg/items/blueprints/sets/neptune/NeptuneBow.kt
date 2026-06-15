package xyz.devvydont.smprpg.items.blueprints.sets.neptune

import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.inventory.CraftingRecipe
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.SMPRPG.Companion.plugin
import xyz.devvydont.smprpg.ability.Passive
import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeEntry.Companion.additive
import xyz.devvydont.smprpg.items.attribute.AttributeEntry.Companion.multiplicative
import xyz.devvydont.smprpg.items.base.CustomShortbow
import xyz.devvydont.smprpg.items.blueprints.sets.netherite.NetheriteBow
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment
import xyz.devvydont.smprpg.items.interfaces.ICraftable
import xyz.devvydont.smprpg.items.interfaces.IPassiveProvider
import xyz.devvydont.smprpg.items.interfaces.IRepairable
import xyz.devvydont.smprpg.items.interfaces.ISkillRequirement
import xyz.devvydont.smprpg.items.interfaces.IUnderwaterBow
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.skills.SkillType
import xyz.devvydont.smprpg.util.crafting.builders.BowRecipe
import java.util.List
import java.util.Set

class NeptuneBow(itemService: ItemService, type: CustomItemType) : CustomShortbow(itemService, type),
    IBreakableEquipment, ICraftable, IPassiveProvider, IRepairable, IUnderwaterBow, ISkillRequirement, Listener {

    override val repairMaterial: MutableCollection<ItemStack> get() = mutableListOf(itemService.getCustomItem(CustomItemType.PLUTOS_ARTIFACT))
    override val skillRequirements: MutableMap<SkillType, Int> = mutableMapOf(Pair(SkillType.COMBAT, 15))

    override fun wantNerfedSellPrice(): Boolean { return false }

    override fun getAttributeModifiers(item: ItemStack?): MutableCollection<AttributeEntry?> {
        return mutableListOf(
            AdditiveAttributeEntry(AttributeWrapper.STRENGTH, NetheriteBow.DAMAGE - 10),
            multiplicative(AttributeWrapper.ATTACK_SPEED, -.5),
            AdditiveAttributeEntry(AttributeWrapper.CRITICAL_DAMAGE, 25.0),
            additive(AttributeWrapper.CRITICAL_CHANCE, 35.0)
        )
    }

    override fun getPowerRating(): Int { return NeptuneArmorSet.POWER_LEVEL }

    override fun getRecipeKey(): NamespacedKey { return ICraftable.getDefaultRecipeKey(customItemType) }

    override fun getCustomRecipe(): CraftingRecipe? {
        return BowRecipe(
            this,
            itemService.getCustomItem(CustomItemType.DIAMOND_TOOL_ROD),
            itemService.getCustomItem(CustomItemType.PLUTOS_ARTIFACT),
            generate()
        ).build()
    }

    override fun unlockedBy(): MutableCollection<ItemStack?> {
        return mutableListOf(
            itemService.getCustomItem(CustomItemType.PLUTO_FRAGMENT)
        )
    }

    override fun getPassives(): MutableSet<Passive?> {
        return mutableSetOf(Passive.ABYSSAL_ANNIHILATION)
    }

    override fun getMaxDurability(): Int {
        return NeptuneArmorSet.DURABILITY
    }

}
