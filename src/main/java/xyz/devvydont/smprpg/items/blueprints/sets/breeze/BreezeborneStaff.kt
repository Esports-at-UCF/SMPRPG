package xyz.devvydont.smprpg.items.blueprints.sets.breeze

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.AttackRange
import io.papermc.paper.datacomponent.item.PiercingWeapon
import io.papermc.paper.datacomponent.item.SwingAnimation
import io.papermc.paper.datacomponent.item.UseCooldown
import io.papermc.paper.datacomponent.item.Weapon
import io.papermc.paper.registry.keys.SoundEventKeys
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.inventory.CraftingRecipe
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.recipe.CraftingBookCategory
import org.checkerframework.common.value.qual.IntRange
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.SMPRPG.Companion.plugin
import xyz.devvydont.smprpg.ability.Ability
import xyz.devvydont.smprpg.ability.AbilityActivationMethod
import xyz.devvydont.smprpg.ability.AbilityCost
import xyz.devvydont.smprpg.ability.AbilityCost.Companion.of
import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.events.CustomEntityDamageByEntityEvent
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
import xyz.devvydont.smprpg.util.formatting.ComponentUtils
import xyz.devvydont.smprpg.util.items.AbilityUtil
import xyz.devvydont.smprpg.util.time.TickTime
import java.util.List

class BreezeborneStaff(itemService: ItemService, type: CustomItemType) : CustomAttributeItem(itemService, type),
    IBreakableEquipment, ICantCrit, IMageBeam, ICraftable, IAbilityCaster, IModelOverridden, IHeaderDescribable,
    Listener {

    override val itemClassification: ItemClassification get() = ItemClassification.STAFF

    override fun getAttributeModifiers(item: ItemStack?): MutableCollection<AttributeEntry?> {
        return mutableListOf(
            AdditiveAttributeEntry(AttributeWrapper.STRENGTH, 180.0),
            AdditiveAttributeEntry(AttributeWrapper.INTELLIGENCE, 160.0),
            AdditiveAttributeEntry(AttributeWrapper.ARCANE_RATING, 25.0),
            MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, -.5)
        )
    }

    override fun getPowerRating(): Int { return 30 }

    override fun getHeader(itemStack: ItemStack?): MutableList<Component?> {
        val components: MutableList<Component?> = ArrayList()
        components.add(AbilityUtil.getAbilityComponent("Air Shot", passive = true))
        components.add(
            ComponentUtils.create("Attacks deal ")
                .append(ComponentUtils.create(DAMAGE_MULT.toInt().toString() + "x", NamedTextColor.GREEN))
                .append(ComponentUtils.create(" damage"))
        )
        components.add(ComponentUtils.create("against mobs that are not grounded."))

        return components
    }

    override fun getRecipeKey(): NamespacedKey {
        return ICraftable.getDefaultRecipeKey(customItemType)
    }

    override fun getCustomRecipe(): CraftingRecipe {
        val recipe = ShapedRecipe(getRecipeKey(), generate())
        recipe.shape(
            " hb",
            " bb",
            "b  "
        )
        recipe.setIngredient('h', generate(Material.HEAVY_CORE))
        recipe.setIngredient('b', generate(Material.BREEZE_ROD))
        recipe.setCategory(CraftingBookCategory.EQUIPMENT)
        return recipe
    }

    override fun unlockedBy(): MutableCollection<ItemStack?> {
        return mutableListOf(
            itemService.getCustomItem(Material.BREEZE_ROD)
        )
    }

    override fun getActiveSlot(): EquipmentSlotGroup { return EquipmentSlotGroup.MAINHAND }

    override fun getMaxDurability(): Int { return 10000 }

    override val manaCost : Int get() = 30
    override val hitParticle: Particle get() = Particle.END_ROD
    override val missParticle: Particle get() = Particle.ENCHANTED_HIT
    override val particleRange: Int get() = 13
    override val particleDensity: Int get() = particleRange * 2

    override fun updateItemData(itemStack: ItemStack) {
        super.updateItemData(itemStack)
        IMageBeam.updateStaffComponents(itemStack, particleRange, 0.2f, SoundEventKeys.ENTITY_BLAZE_SHOOT, SoundEventKeys.ENTITY_EXPERIENCE_ORB_PICKUP)
        itemStack.setData(DataComponentTypes.USE_COOLDOWN, UseCooldown.useCooldown(0.0001f).cooldownGroup(COOLDOWN_GROUP))
    }

    override fun getAbilities(item: ItemStack): Collection<AbilityEntry> {
        return mutableListOf(
            AbilityEntry(
                Ability.WIND_STORM,
                AbilityActivationMethod.SNEAK_RIGHT_CLICK,
                of(AbilityCost.Resource.MANA, 200)
            ),
            AbilityEntry(
                Ability.WIND_ATTUNED,
                AbilityActivationMethod.EXCLUSIVE_RIGHT_CLICK,
                of(AbilityCost.Resource.MANA, 10)
            )
        )
    }

    override fun getCooldown(item: ItemStack): Long { return TickTime.seconds(3) }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun __onAirshotHit(event: CustomEntityDamageByEntityEvent) {
        // Is the attacked target a living entity?

        if (event.damaged !is LivingEntity) return

        // Did the attacker use the cutlass?
        if (event.dealer !is LivingEntity) return
        val dealer = event.dealer
        val damaged = event.damaged

        if (dealer.equipment == null) return

        if (!isItemOfType(dealer.equipment!!.itemInMainHand)) return

        // Is this a direct event?
        if (event.isIndirect) return

        // Increase our damage if they are in the air.
        if (!damaged.isOnGround) {
            event.multiplyDamage(DAMAGE_MULT)
            damaged.world.playSound(damaged.location, Sound.ENTITY_BREEZE_CHARGE, .5f, 2.0f)
            damaged.world.spawnParticle(Particle.END_ROD, damaged.eyeLocation, 5)
        }
    }

    override fun getDisplayKey(): Key {
        return NamespacedKey(plugin, customItemType.key)
    }

    companion object {
        const val DAMAGE_MULT = 3.0
        val COOLDOWN_GROUP = NamespacedKey(plugin, "breezeborne_staff_ability")
    }
}
