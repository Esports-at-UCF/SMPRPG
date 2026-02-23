package xyz.devvydont.smprpg.crafting

import org.bukkit.Material
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.RecipeChoice.MaterialChoice

object CraftingGlobals {
    /**
     * A shortcut to "wildcard" wood plank selections for crafting recipes.
     */
    @JvmField
    val WOODEN_PLANKS_WILDCARD: RecipeChoice = RecipeChoice.itemType(Material.OAK_PLANKS.asItemType()!!)

    /**
     * A shortcut to "wildcard" log selections for crafting recipes.
     */
    @JvmField
    val WOODEN_LOGS_WILDCARD: RecipeChoice = RecipeChoice.itemType(Material.OAK_LOG.asItemType()!!)

    /**
     * A shortcut to "wildcard" cobblestone selections for crafting recipes.
     */
    val COBBLESTONE_WILDCARD: RecipeChoice = RecipeChoice.itemType(Material.COBBLESTONE.asItemType()!!)
}
