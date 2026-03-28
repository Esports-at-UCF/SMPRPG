package xyz.devvydont.smprpg.items.blueprints.sets.mithril

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.AttackRange
import io.papermc.paper.datacomponent.item.PiercingWeapon
import io.papermc.paper.datacomponent.item.SwingAnimation
import io.papermc.paper.datacomponent.item.Weapon
import io.papermc.paper.registry.keys.SoundEventKeys
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Particle
import org.bukkit.inventory.CraftingRecipe
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.recipe.CraftingBookCategory
import org.checkerframework.common.value.qual.IntRange
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.SMPRPG.Companion.plugin
import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeEntry
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry
import xyz.devvydont.smprpg.items.base.CustomAttributeItem
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment
import xyz.devvydont.smprpg.items.interfaces.ICantCrit
import xyz.devvydont.smprpg.items.interfaces.ICraftable
import xyz.devvydont.smprpg.items.interfaces.IMageBeam
import xyz.devvydont.smprpg.items.interfaces.IRepairable
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.services.ItemService.Companion.generate
import xyz.devvydont.smprpg.util.items.ToolStats

class MithrilStaff(itemService: ItemService, type: CustomItemType) : CustomAttributeItem(itemService, type),
    IBreakableEquipment, ICantCrit, IMageBeam, ICraftable, IRepairable {

    override val itemClassification: ItemClassification get() = ItemClassification.STAFF
    override val repairMaterial: MutableCollection<ItemStack> get() = mutableListOf(itemService.getCustomItem(CustomItemType.MITHRIL_INGOT))

    override fun getAttributeModifiers(item: ItemStack?): MutableCollection<AttributeEntry?> {
        return mutableListOf(
            AdditiveAttributeEntry(AttributeWrapper.STRENGTH, 25.0),
            AdditiveAttributeEntry(AttributeWrapper.INTELLIGENCE, 60.0),
            AdditiveAttributeEntry(AttributeWrapper.ARCANE_RATING, 20.0),
            MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, -.5)
        )
    }

    override fun getPowerRating(): Int { return ToolStats.MITHRIL.power }

    override fun getRecipeKey(): NamespacedKey { return NamespacedKey(plugin, customItemType.key + "-recipe") }

    override fun getCustomRecipe(): CraftingRecipe {
        val recipe = ShapedRecipe(recipeKey, generate())
        recipe.shape(
            " dm",
            " mm",
            "m  "
        )
        recipe.setIngredient('d', Material.DIAMOND_BLOCK)
        recipe.setIngredient('m', generate(CustomItemType.MITHRIL_INGOT))
        recipe.setCategory(CraftingBookCategory.EQUIPMENT)
        return recipe
    }

    override fun unlockedBy(): MutableCollection<ItemStack?> {
        return mutableListOf(
            SMPRPG.getService(ItemService::class.java).getCustomItem(CustomItemType.MITHRIL_INGOT)
        )
    }

    override fun getActiveSlot(): EquipmentSlotGroup { return EquipmentSlotGroup.MAINHAND }

    override fun getMaxDurability(): Int { return ToolStats.MITHRIL.durability }

    override val manaCost: Int get() = 20
    override val hitParticle: Particle get() = Particle.END_ROD
    override val missParticle: Particle get() = Particle.ENCHANTED_HIT
    override val particleRange: Int get() = 11
    override val particleDensity: Int get() = particleRange * 2

    override fun updateItemData(itemStack: ItemStack) {
        super.updateItemData(itemStack)
        IMageBeam.updateStaffComponents(itemStack, particleRange, 0.2f, SoundEventKeys.ENTITY_BLAZE_SHOOT, SoundEventKeys.ENTITY_EXPERIENCE_ORB_PICKUP)
    }
}
