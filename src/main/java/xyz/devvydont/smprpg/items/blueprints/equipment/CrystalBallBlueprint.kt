package xyz.devvydont.smprpg.items.blueprints.equipment

import net.kyori.adventure.key.Key
import org.bukkit.NamespacedKey
import org.bukkit.inventory.CraftingRecipe
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.recipe.CraftingBookCategory
import xyz.devvydont.smprpg.SMPRPG.Companion.plugin
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.interfaces.ICraftable
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden
import xyz.devvydont.smprpg.items.interfaces.ISellable
import xyz.devvydont.smprpg.reforge.ReforgeType
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.services.ItemService.Companion.generate
import java.util.List

class CrystalBallBlueprint(itemService: ItemService, type: CustomItemType) : ReforgeStone(itemService, type),
    ISellable, IModelOverridden {

    override fun getReforgeType(): ReforgeType { return ReforgeType.CRYPTIC }

    override fun getExperienceCost(): Int { return 50 }

    override fun getWorth(item: ItemStack): Int { return 100_000 * item.amount }

    override fun getDisplayKey(): Key { return IModelOverridden.ofItemTypeInDirectory(customItemType, "reforge_stones") }
}
