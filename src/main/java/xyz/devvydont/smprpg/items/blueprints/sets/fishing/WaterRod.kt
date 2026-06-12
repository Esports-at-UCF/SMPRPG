package xyz.devvydont.smprpg.items.blueprints.sets.fishing

import net.kyori.adventure.key.Key
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.*
import org.bukkit.inventory.RecipeChoice.ExactChoice
import org.bukkit.inventory.recipe.CraftingBookCategory
import xyz.devvydont.smprpg.SMPRPG.Companion.plugin
import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.attribute.AttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeEntry.Companion.additive
import xyz.devvydont.smprpg.items.attribute.AttributeEntry.Companion.multiplicative
import xyz.devvydont.smprpg.items.base.CustomAttributeItem
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemSword.Companion.getSwordDamage
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment
import xyz.devvydont.smprpg.items.interfaces.ICraftable
import xyz.devvydont.smprpg.items.interfaces.IFishingRod
import xyz.devvydont.smprpg.items.interfaces.IFishingRod.FishingFlag
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden
import xyz.devvydont.smprpg.items.interfaces.IRepairable
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.services.ItemService.Companion.generate
import xyz.devvydont.smprpg.util.items.ToolGlobals
import kotlin.math.max

class WaterRod(itemService: ItemService, type: CustomItemType) : CustomAttributeItem(itemService, type),
    IBreakableEquipment, IFishingRod, ICraftable, IModelOverridden, IRepairable {
    override val itemClassification: ItemClassification get() = ItemClassification.ROD
    override val repairMaterial: MutableCollection<ItemStack> get() = getRepairMaterialFromType()

    override fun getAttributeModifiers(item: ItemStack?): MutableCollection<AttributeEntry?> {
        return mutableListOf(
            additive(AttributeWrapper.STRENGTH, this.strength.toDouble()),
            multiplicative(AttributeWrapper.ATTACK_SPEED, -.5),
            additive(AttributeWrapper.FISHING_RATING, this.fishingRating.toDouble()),
            additive(
                AttributeWrapper.FISHING_CREATURE_CHANCE,
                this.chance
            ),
            additive(
                AttributeWrapper.FISHING_TREASURE_CHANCE,
                this.chance
            ),
            additive(AttributeWrapper.FISHING_SPEED, this.speed.toDouble())
        )
    }

    private val speed: Int
        get() = when (this.customItemType) {
            CustomItemType.IRON_ROD -> 10
            CustomItemType.MITHRIL_ROD -> 25
            CustomItemType.PRISMARINE_ROD -> 40
            else -> 0
        }

    override fun getPowerRating(): Int {
        return when (customItemType) {
            CustomItemType.IRON_ROD -> ToolGlobals.IRON_TOOL_POWER
            CustomItemType.MITHRIL_ROD -> ToolGlobals.MITHRIL_TOOL_POWER
            CustomItemType.PRISMARINE_ROD -> ToolGlobals.NETHERITE_TOOL_POWER - 5
            else -> 0
        }
    }

    override fun getActiveSlot(): EquipmentSlotGroup {
        return EquipmentSlotGroup.HAND
    }

    override fun getMaxDurability(): Int {
        return max(1, powerRating * 1000)
    }

    override fun getFishingFlags(): MutableSet<FishingFlag?> {
        return mutableSetOf(FishingFlag.NORMAL)
    }

    override fun getRecipeKey(): NamespacedKey {
        return NamespacedKey(plugin, customItemType.key + "_recipe")
    }

    private val transmuteComponent: RecipeChoice
        /**
         * Work out which fishing rod this rod will be crafted from.
         */
        get() = when (this.customItemType) {
            CustomItemType.IRON_ROD -> ExactChoice(generate(Material.FISHING_ROD))
            CustomItemType.MITHRIL_ROD -> ExactChoice(generate(CustomItemType.IRON_ROD))
            CustomItemType.PRISMARINE_ROD -> ExactChoice(generate(CustomItemType.MITHRIL_ROD))
            else -> ExactChoice(generate(Material.BARRIER))
        }

    private val craftingMaterial: RecipeChoice
        /**
         * Get the material used for crafting the rod part of the rod.
         */
        get() = when (this.customItemType) {
            CustomItemType.IRON_ROD -> ExactChoice(generate(Material.IRON_INGOT))
            CustomItemType.MITHRIL_ROD -> ExactChoice(generate(CustomItemType.MITHRIL_INGOT))
            CustomItemType.PRISMARINE_ROD -> ExactChoice(generate(CustomItemType.ENCHANTED_PRISMARINE_CRYSTAL))
            else -> ExactChoice(generate(Material.BARRIER))
        }

    private val stringMaterial: RecipeChoice
        /**
         * Get the material used for crafting the rod part of the rod.
         */
        get() = when (this.customItemType) {
            CustomItemType.PRISMARINE_ROD -> ExactChoice(generate(CustomItemType.PREMIUM_STRING))
            else -> ExactChoice(generate(Material.STRING))
        }

    override fun getCustomRecipe(): CraftingRecipe {
        val recipe = ShapedRecipe(recipeKey, generate())
        recipe.shape(
            "  m",
            " ts",
            "m s"
        )
        recipe.setIngredient('m', this.craftingMaterial)
        recipe.setIngredient('t', this.transmuteComponent)
        recipe.setIngredient('s', this.stringMaterial)
        recipe.setCategory(CraftingBookCategory.EQUIPMENT)
        recipe.setGroup("water_rod")
        return recipe
    }

    /**
     * A collection of items that will unlock the recipe for this item. Typically will be one of the components
     * of the recipe itself, but can be set to whatever is desired
     *
     * @return
     */
    override fun unlockedBy(): MutableCollection<ItemStack?> {
        if (this.craftingMaterial is ExactChoice) {
            return mutableListOf(this.craftingMaterial.itemStack)
        }
        return mutableListOf(generate(Material.IRON_INGOT))
    }

    fun getRepairMaterialFromType() : MutableCollection<ItemStack> {
        return when (customItemType) {
            CustomItemType.IRON_ROD -> mutableListOf(itemService.getCustomItem(Material.IRON_INGOT))
            CustomItemType.MITHRIL_ROD -> mutableListOf(itemService.getCustomItem(CustomItemType.MITHRIL_INGOT))
            CustomItemType.PRISMARINE_ROD -> mutableListOf(itemService.getCustomItem(CustomItemType.ENCHANTED_PRISMARINE_CRYSTAL))
            else -> mutableListOf()
        }
    }

    private val fishingRating: Int
        get() = when (customItemType) {
            CustomItemType.IRON_ROD -> 10
            CustomItemType.MITHRIL_ROD -> 25
            CustomItemType.PRISMARINE_ROD -> 45
            else -> 0
        }

    private val strength: Int
        get() = when (customItemType) {
            CustomItemType.IRON_ROD -> getSwordDamage(Material.IRON_SWORD) / 2
            CustomItemType.GOLD_ROD -> getSwordDamage(Material.GOLDEN_SWORD) / 2
            CustomItemType.MITHRIL_ROD -> getSwordDamage(CustomItemType.TITANIUM_SWORD) / 2
            CustomItemType.PRISMARINE_ROD -> getSwordDamage(Material.DIAMOND_SWORD) / 2 + 10
            else -> 0
        }.toInt()

    private val chance: Double
        get() = when (customItemType) {
            CustomItemType.IRON_ROD -> 0.5
            CustomItemType.MITHRIL_ROD -> 1
            CustomItemType.PRISMARINE_ROD -> 2
            else -> 0
        }.toDouble()

    override fun getDisplayKey(): Key { return IModelOverridden.ofItemType(customItemType) }
}
