package xyz.devvydont.smprpg.items.blueprints.sets.inferno

import net.kyori.adventure.key.Key
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.CraftingRecipe
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.recipe.CraftingBookCategory
import xyz.devvydont.smprpg.SMPRPG.Companion.plugin
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
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry
import xyz.devvydont.smprpg.items.base.CustomAttributeItem
import xyz.devvydont.smprpg.items.interfaces.*
import xyz.devvydont.smprpg.items.interfaces.IAbilityCaster.AbilityEntry
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.services.ItemService.Companion.generate
import xyz.devvydont.smprpg.skills.SkillType
import xyz.devvydont.smprpg.util.time.TickTime
import java.util.List

class InfernoSaber(itemService: ItemService, type: CustomItemType) : CustomAttributeItem(itemService, type), ICraftable,
    IModelOverridden, IBreakableEquipment, IAbilityCaster, IRepairable, ISkillRequirement {

    override val itemClassification: ItemClassification get() = ItemClassification.SWORD
    override val repairMaterial: MutableCollection<ItemStack> get() = mutableListOf(itemService.getCustomItem(CustomItemType.INFERNO_REMNANT))
    override val skillRequirements: MutableMap<SkillType, Int> = mutableMapOf(Pair(SkillType.COMBAT, 35))

    override fun getDisplayKey(): Key? {
        return IModelOverridden.ofMaterial(Material.BLAZE_ROD)
    }

    override fun getAttributeModifiers(item: ItemStack?): MutableCollection<AttributeEntry?> {
        return mutableListOf(
            AdditiveAttributeEntry(AttributeWrapper.STRENGTH, 125.0),
            MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, -.6),
            AdditiveAttributeEntry(AttributeWrapper.CRITICAL_DAMAGE, 50.0),
            ScalarAttributeEntry(AttributeWrapper.MOVEMENT_SPEED, .25)
        )
    }

    override fun getPowerRating(): Int {
        return InfernoArmorSet.POWER
    }

    override fun getActiveSlot(): EquipmentSlotGroup {
        return EquipmentSlotGroup.MAINHAND
    }


    override fun getRecipeKey(): NamespacedKey {
        return NamespacedKey(plugin, customItemType.getKey() + "-recipe")
    }

    override fun getCustomRecipe(): CraftingRecipe {
        val recipe = ShapedRecipe(getRecipeKey(), generate())
        recipe.shape("r", "r", "r")
        recipe.setIngredient('r', generate(InfernoArmorSet.CRAFTING_COMPONENT))
        recipe.setCategory(CraftingBookCategory.EQUIPMENT)
        return recipe
    }

    override fun unlockedBy(): MutableCollection<ItemStack?> {
        return mutableListOf(generate(InfernoArmorSet.CRAFTING_COMPONENT))
    }

    /**
     * Get the abilities this item has, and how they can be cast.
     *
     * @param item The item.
     * @return A list of abilities.
     */
    override fun getAbilities(item: ItemStack): MutableCollection<AbilityEntry> {
        return mutableListOf(
            AbilityEntry(
                Ability.HOT_SHOT,
                AbilityActivationMethod.RIGHT_CLICK,
                of(AbilityCost.Resource.MANA, 150)
            )
        )
    }

    /**
     * Get the cooldown in between item uses.
     * Keep in mind this is more for preventing strange things from happening via casting on the same tick or teleporting,
     * so it needs to be per item since we use the default cooldown system.
     *
     * @param item The item.
     * @return The cooldown in ticks.
     */
    override fun getCooldown(item: ItemStack): Long {
        return TickTime.seconds(1)
    }

    override fun getMaxDurability(): Int {
        return 50000
    }

}
