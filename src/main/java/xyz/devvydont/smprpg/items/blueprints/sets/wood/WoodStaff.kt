package xyz.devvydont.smprpg.items.blueprints.sets.wood

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
import xyz.devvydont.smprpg.util.items.ToolStats

class WoodStaff(itemService: ItemService, type: CustomItemType) : CustomAttributeItem(itemService, type),
    IBreakableEquipment, ICantCrit, IMageBeam, ICraftable, IRepairable {

    override val itemClassification: ItemClassification get() = ItemClassification.STAFF
    override val repairMaterial : MutableCollection<ItemStack> = mutableListOf(itemService.getCustomItem(Material.OAK_PLANKS), itemService.getCustomItem(Material.BIRCH_PLANKS), itemService.getCustomItem(Material.SPRUCE_PLANKS),
        itemService.getCustomItem(Material.JUNGLE_PLANKS), itemService.getCustomItem(Material.DARK_OAK_PLANKS), itemService.getCustomItem(Material.ACACIA_PLANKS),
        itemService.getCustomItem(Material.CRIMSON_PLANKS), itemService.getCustomItem(Material.WARPED_PLANKS), itemService.getCustomItem(Material.MANGROVE_PLANKS),
        itemService.getCustomItem(Material.CHERRY_PLANKS), itemService.getCustomItem(Material.PALE_OAK_PLANKS), itemService.getCustomItem(Material.BAMBOO_PLANKS))

    override fun getAttributeModifiers(item: ItemStack?): MutableCollection<AttributeEntry?> {
        return mutableListOf(
            AdditiveAttributeEntry(AttributeWrapper.STRENGTH, 7.0),
            AdditiveAttributeEntry(AttributeWrapper.INTELLIGENCE, 10.0),
            AdditiveAttributeEntry(AttributeWrapper.ARCANE_RATING, 10.0),
            MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, -.5)
        )
    }

    override fun getPowerRating(): Int { return ToolStats.WOOD.power }

    override fun getRecipeKey(): NamespacedKey { return ICraftable.getDefaultRecipeKey(customItemType) }

    override fun getCustomRecipe(): CraftingRecipe {
        val recipe = ShapedRecipe(recipeKey, generate())
        recipe.shape(
            "  s",
            " ss",
            "s  "
        )
        recipe.setIngredient('s', Material.STICK)
        recipe.setCategory(CraftingBookCategory.EQUIPMENT)
        return recipe
    }

    override fun unlockedBy(): MutableCollection<ItemStack?> {
        return mutableListOf(
            SMPRPG.getService(ItemService::class.java).getCustomItem(Material.STICK)
        )
    }

    override fun getActiveSlot(): EquipmentSlotGroup { return EquipmentSlotGroup.MAINHAND }

    override fun getMaxDurability(): Int { return ToolStats.WOOD.durability }

    override fun getManaCost(): Int { return 5 }

    override fun getHitParticle(): Particle { return Particle.ENCHANTED_HIT }

    override fun getMissParticle(): Particle { return Particle.CRIT }

    override fun getParticleDensity(): Int { return 16 }

    override fun getParticleRange(): Int { return 8 }

    override fun updateItemData(itemStack: ItemStack) {
        super.updateItemData(itemStack)
        itemStack.setData<AttackRange?>(
            DataComponentTypes.ATTACK_RANGE, AttackRange.attackRange()
                .hitboxMargin(0.1f)
                .maxReach(8.0f)
                .maxCreativeReach(8.0f)
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
                .sound(SoundEventKeys.ENTITY_PLAYER_ATTACK_SWEEP)
                .hitSound(SoundEventKeys.BLOCK_TRIAL_SPAWNER_EJECT_ITEM)
                .build()
        )
        itemStack.setData<@IntRange(from = 1L, to = 99L) Int?>(DataComponentTypes.MAX_STACK_SIZE, 1)
    }
}
