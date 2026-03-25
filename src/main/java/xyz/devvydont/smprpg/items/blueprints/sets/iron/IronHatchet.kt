package xyz.devvydont.smprpg.items.blueprints.sets.iron

import io.papermc.paper.datacomponent.item.Tool
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.CraftingRecipe
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.recipe.CraftingBookCategory
import xyz.devvydont.smprpg.SMPRPG.Companion.plugin
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemPickaxe
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemSword
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment
import xyz.devvydont.smprpg.items.interfaces.ICraftable
import xyz.devvydont.smprpg.items.tools.ItemHatchet
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.crafting.builders.HatchetRecipe
import xyz.devvydont.smprpg.util.items.ToolGlobals
import xyz.devvydont.smprpg.util.items.ToolStats
import java.util.List

class IronHatchet(itemService: ItemService, type: CustomItemType) : ItemHatchet(itemService, type), ICraftable,
    IBreakableEquipment {
    override fun getPowerRating(): Int {
        return ToolStats.IRON.power
    }

    override fun getHatchetMiningPower(): Double {
        return ToolStats.IRON.miningPower.toDouble()
    }

    override fun getHatchetDamage(): Double {
        return ItemSword.getSwordDamage(Material.IRON_SWORD) - 7
    }

    override fun getHatchetFortune(): Double {
        return ToolStats.IRON.fortune * 0.8
    }

    override fun getHatchetSpeed(): Double {
        return ToolStats.IRON.speed * 0.8
    }

    override fun getRecipeKey(): NamespacedKey {
        return ICraftable.getDefaultRecipeKey(customItemType)
    }

    override fun getCustomRecipe(): CraftingRecipe {
        return HatchetRecipe(
            this,
            ItemService.generate(Material.IRON_INGOT),
            ItemService.generate(Material.STICK),
            generate()
        ).build()
    }

    override fun unlockedBy(): MutableCollection<ItemStack?> {
        return mutableListOf(itemService.getCustomItem(Material.IRON_INGOT))
    }

    override fun getMaxDurability(): Int {
        return ToolStats.IRON.durability
    }

    companion object {
        val TOOL_COMP: Tool = Tool.tool()
            .defaultMiningSpeed(0.0001f)
            .build()
    }
}
