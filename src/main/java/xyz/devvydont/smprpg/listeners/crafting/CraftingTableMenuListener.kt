package xyz.devvydont.smprpg.listeners.crafting

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.inventory.InventoryType
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.gui.crafting.MenuCraftingTable
import xyz.devvydont.smprpg.util.listeners.ToggleableListener

/**
 * Intercepts every attempt to open a vanilla crafting table (workbench) and replaces it with our fully
 * custom [MenuCraftingTable], which supports the data-driven recipe registry (including per-slot count
 * requirements) and falls back to vanilla recipes.
 *
 * Note: the player's built-in 2x2 inventory crafting grid is a separate inventory ([InventoryType.CRAFTING])
 * and is intentionally NOT replaced — it continues to use Bukkit recipes.
 */
class CraftingTableMenuListener : ToggleableListener() {

    @EventHandler
    @Suppress("unused")
    private fun onWorkbenchOpened(event: InventoryOpenEvent) {
        if (event.inventory.type != InventoryType.WORKBENCH) return
        val player = event.player as? Player ?: return
        event.isCancelled = true

        // Opening an inventory from within an InventoryOpenEvent is unsafe, so defer it a tick.
        Bukkit.getScheduler().runTaskLater(SMPRPG.plugin, Runnable { MenuCraftingTable(player).openMenu() }, 1L)
    }
}
