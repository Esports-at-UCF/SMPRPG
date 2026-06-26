package xyz.devvydont.smprpg.listeners.crafting

import org.bukkit.Keyed
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.inventory.PrepareItemCraftEvent
import org.bukkit.inventory.Recipe
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.events.skills.SkillExperienceGainEvent
import xyz.devvydont.smprpg.recipe.core.CustomRecipe
import xyz.devvydont.smprpg.services.RecipeService
import xyz.devvydont.smprpg.util.listeners.ToggleableListener

/**
 * Enforces recipe requirements and grants recipe rewards on the VANILLA crafting paths — the player's 2x2
 * inventory grid and the recipe book. The custom crafting GUI ([xyz.devvydont.smprpg.gui.crafting.MenuCraftingTable])
 * handles those itself, so it never fires these events.
 *
 * Bukkit recipes are matched back to the data-driven registry by their key; anything that isn't one of our
 * recipes (vanilla recipes, compression recipes — none of which carry requirements/rewards) passes through.
 */
class CustomRecipeCraftListener : ToggleableListener() {

    /** Block the preview result when the crafter doesn't meet a registry recipe's requirements. */
    @EventHandler
    @Suppress("unused")
    private fun onPrepareCraft(event: PrepareItemCraftEvent) {
        val recipe = coreRecipe(event.recipe) ?: return
        if (recipe.requirements.isEmpty) return
        val player = event.view.player as? Player ?: return
        if (!recipe.requirements.meets(player))
            event.inventory.result = null
    }

    /** Re-check requirements and grant rewards when a registry recipe is crafted on a vanilla path. */
    @EventHandler
    @Suppress("unused")
    private fun onCraft(event: CraftItemEvent) {
        val recipe = coreRecipe(event.recipe) ?: return
        val player = event.whoClicked as? Player ?: return
        if (!recipe.requirements.meets(player)) {
            event.isCancelled = true
            return
        }
        if (recipe.rewards.isEmpty) return
        repeat(craftCount(event)) {
            recipe.rewards.grant(player, SkillExperienceGainEvent.ExperienceSource.FORGE)
        }
    }

    /** The data-driven recipe behind a Bukkit recipe, or null if it isn't one of ours. */
    private fun coreRecipe(recipe: Recipe?): CustomRecipe? {
        val key = (recipe as? Keyed)?.key ?: return null
        if (key.namespace != "smprpg") return null
        return SMPRPG.getService(RecipeService::class.java).getRegistry().byKey(key.asString())
    }

    /** Crafts produced by this event: 1 normally, or the limiting ingredient stack on a shift-click. */
    private fun craftCount(event: CraftItemEvent): Int {
        if (!event.isShiftClick) return 1
        return event.inventory.matrix
            .filterNotNull()
            .filter { !it.isEmpty }
            .minOfOrNull { it.amount } ?: 1
    }
}
