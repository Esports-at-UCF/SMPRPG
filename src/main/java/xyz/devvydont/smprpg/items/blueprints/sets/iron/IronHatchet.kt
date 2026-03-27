package xyz.devvydont.smprpg.items.blueprints.sets.iron

import io.papermc.paper.datacomponent.item.Tool
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.CraftingRecipe
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemSword
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment
import xyz.devvydont.smprpg.items.interfaces.ICraftable
import xyz.devvydont.smprpg.items.interfaces.IRepairable
import xyz.devvydont.smprpg.items.tools.ItemHatchet
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.crafting.builders.HatchetRecipe
import xyz.devvydont.smprpg.util.items.ToolStats

class IronHatchet(itemService: ItemService, type: CustomItemType) : ItemHatchet(itemService, type), ICraftable,
    IBreakableEquipment, IRepairable {

    override val repairMaterial: MutableCollection<ItemStack> get() = mutableListOf(itemService.getCustomItem(Material.IRON_INGOT))

    override fun getPowerRating(): Int { return ToolStats.IRON.power }

    override val hatchetMiningPower: Double = ToolStats.IRON.miningPower.toDouble()
    override val hatchetDamage: Double = ItemSword.getSwordDamage(Material.IRON_SWORD) - 7
    override val hatchetFortune: Double = ToolStats.IRON.fortune * 0.8
    override val hatchetSpeed: Double = ToolStats.IRON.speed * 0.8

    override fun getRecipeKey(): NamespacedKey { return ICraftable.getDefaultRecipeKey(customItemType) }

    override fun getCustomRecipe(): CraftingRecipe {
        return HatchetRecipe(
            this,
            ItemService.generate(Material.IRON_INGOT),
            ItemService.generate(Material.STICK),
            generate()
        ).build()
    }

    override fun unlockedBy(): MutableCollection<ItemStack?> { return mutableListOf(itemService.getCustomItem(Material.IRON_INGOT)) }

    override fun getMaxDurability(): Int { return ToolStats.IRON.durability }

    companion object {
        val TOOL_COMP: Tool = Tool.tool()
            .defaultMiningSpeed(0.0001f)
            .build()
    }
}
