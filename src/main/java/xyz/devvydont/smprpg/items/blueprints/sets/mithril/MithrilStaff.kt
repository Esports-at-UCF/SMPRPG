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
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.services.ItemService.Companion.generate
import xyz.devvydont.smprpg.util.items.ToolStats

class MithrilStaff(itemService: ItemService, type: CustomItemType) : CustomAttributeItem(itemService, type),
    IBreakableEquipment, ICantCrit, IMageBeam, ICraftable {

    override val itemClassification: ItemClassification get() = ItemClassification.STAFF

    override fun getAttributeModifiers(item: ItemStack?): MutableCollection<AttributeEntry?> {
        return mutableListOf(
            AdditiveAttributeEntry(AttributeWrapper.STRENGTH, 25.0),
            AdditiveAttributeEntry(AttributeWrapper.INTELLIGENCE, 60.0),
            AdditiveAttributeEntry(AttributeWrapper.ARCANE_RATING, 20.0),
            MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, -.5)
        )
    }

    override fun getPowerRating(): Int {
        return ToolStats.MITHRIL.power
    }

    override fun getRecipeKey(): NamespacedKey {
        return NamespacedKey(plugin, customItemType.key + "-recipe")
    }

    override fun getCustomRecipe(): CraftingRecipe {
        val recipe = ShapedRecipe(recipeKey, generate())
        recipe.shape(
            " dm",
            " mm",
            "m  "
        )
        recipe.setIngredient('d', Material.DIAMOND_BLOCK)
        recipe.setIngredient('m', generate(CustomItemType.MITHRIL_BLOCK))
        recipe.setCategory(CraftingBookCategory.EQUIPMENT)
        return recipe
    }

    override fun unlockedBy(): MutableCollection<ItemStack?> {
        return mutableListOf(
            SMPRPG.getService(ItemService::class.java).getCustomItem(CustomItemType.MITHRIL_INGOT)
        )
    }

    override fun getActiveSlot(): EquipmentSlotGroup {
        return EquipmentSlotGroup.MAINHAND
    }

    override fun getMaxDurability(): Int {
        return ToolStats.MITHRIL.durability
    }

    override fun getManaCost(): Int {
        return 20
    }

    override fun getHitParticle(): Particle {
        return Particle.END_ROD
    }

    override fun getMissParticle(): Particle {
        return Particle.ENCHANTED_HIT
    }

    override fun getParticleDensity(): Int {
        return 22
    }

    override fun getParticleRange(): Int {
        return 11
    }

    override fun updateItemData(itemStack: ItemStack) {
        super.updateItemData(itemStack)
        itemStack.setData<AttackRange?>(
            DataComponentTypes.ATTACK_RANGE, AttackRange.attackRange()
                .hitboxMargin(0.2f)
                .maxReach(11.0f)
                .maxCreativeReach(11.0f)
                .build()
        )
        itemStack.setData<Weapon?>(DataComponentTypes.WEAPON, Weapon.weapon().build())
        itemStack.setData<SwingAnimation?>(
            DataComponentTypes.SWING_ANIMATION, SwingAnimation.swingAnimation()
                .type(SwingAnimation.Animation.STAB)
                .duration(10)
                .build()
        )
        itemStack.setData<PiercingWeapon?>(
            DataComponentTypes.PIERCING_WEAPON, PiercingWeapon.piercingWeapon()
                .dealsKnockback(false)
                .dismounts(false)
                .sound(SoundEventKeys.ENTITY_BLAZE_SHOOT)
                .hitSound(SoundEventKeys.ENTITY_EXPERIENCE_ORB_PICKUP)
                .build()
        )
        itemStack.setData<@IntRange(from = 1L, to = 99L) Int?>(DataComponentTypes.MAX_STACK_SIZE, 1)
    }
}
