package xyz.devvydont.smprpg.items.blueprints.sets.cobalt

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.KineticWeapon
import io.papermc.paper.registry.keys.SoundEventKeys
import org.bukkit.Material
import org.bukkit.inventory.CraftingRecipe
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeEntry
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemSpear
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment
import xyz.devvydont.smprpg.items.interfaces.ICraftable
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.crafting.builders.SpearRecipe

class CobaltSpear(itemService: ItemService, type: CustomItemType) : CobaltAttributeItem(itemService, type), ICraftable, IBreakableEquipment, IModelOverridden {

    override val itemClassification: ItemClassification get() = ItemClassification.SPEAR

    override fun getAttributeModifiers(item: ItemStack?): MutableCollection<AttributeEntry?>? {
        return mutableListOf(
            AdditiveAttributeEntry(AttributeWrapper.STRENGTH, ItemSpear.getSpearDamage(_type)),
            MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, ItemSpear.getSpearRecovery(_type))
        )
    }

    override fun updateItemData(itemStack: ItemStack) {
        super.updateItemData(itemStack)
        itemStack.setData(DataComponentTypes.KINETIC_WEAPON, KINETIC_COMP)
    }

    override fun getCustomRecipe(): CraftingRecipe? {
        return SpearRecipe(
            this,
            getCraftingMaterial(),
            itemService.getCustomItem(Material.STICK),
            generate()
        ).build()
    }

    companion object {
        val KINETIC_COMP : KineticWeapon = KineticWeapon.kineticWeapon()
            .dismountConditions(KineticWeapon.condition(50, 8.0f, 0.0f))
            .forwardMovement(0.38f)
            .hitSound(SoundEventKeys.ITEM_SPEAR_HIT)
            .sound(SoundEventKeys.ITEM_SPEAR_USE)
            .knockbackConditions(KineticWeapon.condition(135, 5.1f, 0.0f))
            .delayTicks(12)
            .damageMultiplier(0.95f)
            .damageConditions(KineticWeapon.condition(225, 0.0f, 4.6f))
            .build()
    }
}
