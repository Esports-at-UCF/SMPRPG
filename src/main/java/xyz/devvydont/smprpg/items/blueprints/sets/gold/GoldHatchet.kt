package xyz.devvydont.smprpg.items.blueprints.sets.gold

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.Tool
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.CraftingRecipe
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.SMPRPG.Companion.plugin
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemPickaxe
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemSword
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment
import xyz.devvydont.smprpg.items.interfaces.ICraftable
import xyz.devvydont.smprpg.items.interfaces.IRepairable
import xyz.devvydont.smprpg.items.tools.ItemHatchet
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.services.ItemService.Companion.generate
import xyz.devvydont.smprpg.util.crafting.builders.HatchetRecipe
import xyz.devvydont.smprpg.util.items.ToolGlobals
import xyz.devvydont.smprpg.util.items.ToolStats
import java.util.List

class GoldHatchet(itemService: ItemService, type: CustomItemType) : ItemHatchet(itemService, type), ICraftable,
    IBreakableEquipment, IRepairable {

    override val repairMaterial : MutableCollection<ItemStack> get() = mutableListOf(itemService.getCustomItem(Material.GOLD_INGOT))

    override fun getPowerRating(): Int { return ToolStats.GOLD.power }

    override val hatchetMiningPower: Double = ToolStats.GOLD.miningPower.toDouble()
    override val hatchetDamage: Double = ItemSword.getSwordDamage(Material.GOLDEN_SWORD) - 7
    override val hatchetFortune: Double = ToolStats.GOLD.miningPower.toDouble() * 0.8
    override val hatchetSpeed: Double = ToolStats.GOLD.speed.toDouble() * 0.8

    override fun getRecipeKey(): NamespacedKey {
        return ICraftable.getDefaultRecipeKey(customItemType)
    }

    override fun updateItemData(itemStack: ItemStack) {
        super.updateItemData(itemStack)
        itemStack.setData(DataComponentTypes.TOOL, TOOL_COMP)
    }

    override fun getCustomRecipe(): CraftingRecipe? {
        return HatchetRecipe(
            this,
            generate(Material.GOLD_INGOT),
            generate(Material.STICK),
            generate()
        ).build()
    }

    override fun unlockedBy(): MutableCollection<ItemStack?> {
        return mutableListOf(itemService.getCustomItem(Material.GOLD_INGOT))
    }

    override fun getMaxDurability(): Int { return ToolStats.GOLD.durability }

    companion object {
        val TOOL_COMP: Tool = Tool.tool()
            .defaultMiningSpeed(0.0001f)
            .build()
    }
}
