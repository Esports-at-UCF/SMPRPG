package xyz.devvydont.smprpg.items.blueprints.sets.inferno

import net.kyori.adventure.key.Key
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.RecipeChoice.ExactChoice
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden
import xyz.devvydont.smprpg.items.interfaces.ISellable
import xyz.devvydont.smprpg.items.interfaces.ISmeltable
import xyz.devvydont.smprpg.items.interfaces.ISmeltable.RecipeType
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.services.ItemService.Companion.generate
import xyz.devvydont.smprpg.util.time.TickTime

class CeramicPlate(itemService: ItemService, type: CustomItemType) : CustomItemBlueprint(itemService, type),
    ISmeltable, IModelOverridden, ISellable {

    override val itemClassification: ItemClassification get() = ItemClassification.ITEM

    override fun getIngredient(): RecipeChoice { return ExactChoice(generate(CustomItemType.UNFIRED_CERAMIC_PLATE)) }

    override fun getExperience(): Float { return 5f }

    override fun getCookingTime(): Long { return TickTime.seconds(10) }

    override fun getRecipeType(): RecipeType { return RecipeType.DEFAULT }

    override fun getDisplayKey(): Key? { return IModelOverridden.ofItemTypeInDirectory(customItemType, "materials") }

    override fun getWorth(item: ItemStack): Int { return item.amount * 6}

}
