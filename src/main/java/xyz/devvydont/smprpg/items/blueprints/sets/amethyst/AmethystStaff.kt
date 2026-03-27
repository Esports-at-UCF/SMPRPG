package xyz.devvydont.smprpg.items.blueprints.sets.amethyst

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
import xyz.devvydont.smprpg.ability.Ability
import xyz.devvydont.smprpg.ability.AbilityActivationMethod
import xyz.devvydont.smprpg.ability.AbilityCost
import xyz.devvydont.smprpg.ability.AbilityCost.Companion.of
import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeEntry
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry
import xyz.devvydont.smprpg.items.base.CustomAttributeItem
import xyz.devvydont.smprpg.items.interfaces.*
import xyz.devvydont.smprpg.items.interfaces.IAbilityCaster.AbilityEntry
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.services.ItemService.Companion.generate
import xyz.devvydont.smprpg.util.items.ToolStats
import xyz.devvydont.smprpg.util.time.TickTime

class AmethystStaff(itemService: ItemService, type: CustomItemType) : CustomAttributeItem(itemService, type),
    IBreakableEquipment, ICantCrit, IMageBeam, ICraftable, IAbilityCaster, IRepairable {

    override val itemClassification: ItemClassification get() = ItemClassification.STAFF
    override val repairMaterial: MutableCollection<ItemStack> get() = mutableListOf(itemService.getCustomItem(CustomItemType.ENCHANTED_AMETHYST))

    override fun getAttributeModifiers(item: ItemStack?): MutableCollection<AttributeEntry?> {
        return mutableListOf(
            AdditiveAttributeEntry(AttributeWrapper.STRENGTH, 15.0),
            AdditiveAttributeEntry(AttributeWrapper.INTELLIGENCE, 25.0),
            AdditiveAttributeEntry(AttributeWrapper.ARCANE_RATING, 15.0),
            MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, -.5)
        )
    }

    override fun getPowerRating(): Int { return 12 }

    override fun getRecipeKey(): NamespacedKey { return ICraftable.getDefaultRecipeKey(customItemType) }

    override fun getCustomRecipe(): CraftingRecipe {
        val recipe = ShapedRecipe(recipeKey, generate())
        recipe.shape(
            " as",
            " ss",
            "s  "
        )
        recipe.setIngredient('s', Material.STICK)
        recipe.setIngredient('a', generate(CustomItemType.ENCHANTED_AMETHYST))
        recipe.setCategory(CraftingBookCategory.EQUIPMENT)
        return recipe
    }

    override fun unlockedBy(): MutableCollection<ItemStack?> {
        return mutableListOf(
            itemService.getCustomItem(Material.AMETHYST_SHARD)
        )
    }

    override fun getActiveSlot(): EquipmentSlotGroup { return EquipmentSlotGroup.MAINHAND }

    override fun getMaxDurability(): Int { return ToolStats.IRON.durability }

    override fun getManaCost(): Int { return 10 }

    override fun getHitParticle(): Particle { return Particle.END_ROD }

    override fun getMissParticle(): Particle { return Particle.CRIT }

    override fun getParticleDensity(): Int { return 20 }

    override fun getParticleRange(): Int { return 10 }

    override fun getAbilities(item: ItemStack?): MutableCollection<AbilityEntry?> {
        return mutableListOf(
            AbilityEntry(
                Ability.SHARD_STRIKE,
                AbilityActivationMethod.RIGHT_CLICK,
                of(AbilityCost.Resource.MANA, 50)
            )
        )
    }

    override fun getCooldown(item: ItemStack?): Long { return TickTime.seconds(1) }

    override fun updateItemData(itemStack: ItemStack) {
        super.updateItemData(itemStack)
        itemStack.setData<AttackRange?>(
            DataComponentTypes.ATTACK_RANGE, AttackRange.attackRange()
                .hitboxMargin(0.15f)
                .maxReach(10.0f)
                .maxCreativeReach(10.0f)
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
                .sound(SoundEventKeys.BLOCK_AMETHYST_BLOCK_RESONATE)
                .hitSound(SoundEventKeys.BLOCK_TRIAL_SPAWNER_EJECT_ITEM)
                .build()
        )
        itemStack.setData<@IntRange(from = 1L, to = 99L) Int?>(DataComponentTypes.MAX_STACK_SIZE, 1)
    }
}
