package xyz.devvydont.smprpg.listeners.crafting

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.gui.anvil.MenuAnvil
import xyz.devvydont.smprpg.util.listeners.ToggleableListener

/**
 * Intercepts every attempt to open a vanilla anvil (both the vanilla block and our custom netherite anvil,
 * which opens a vanilla anvil window via CraftEngine) and replaces it with our fully custom [MenuAnvil].
 * This keeps anvil behavior entirely under our control and avoids the bugs of the vanilla anvil GUI.
 */
class AnvilMenuListener : ToggleableListener() {

    @EventHandler
    @Suppress("unused")
    private fun onAnvilOpened(event: InventoryOpenEvent) {
        if (event.inventory.type != InventoryType.ANVIL) return

        val player = event.player as? Player ?: return
        event.isCancelled = true

        // Remember which anvil block was used so the menu can mimic vanilla anvil wear.
        val anvilLocation = resolveAnvilLocation(event.inventory, player)

        // Opening an inventory from within an InventoryOpenEvent is unsafe, so defer it a tick.
        Bukkit.getScheduler().runTaskLater(SMPRPG.plugin, Runnable { MenuAnvil(player, anvilLocation).openMenu() }, 1L)
    }

    /**
     * Resolves the world location of the anvil block backing this inventory. Falls back to the block the
     * player is looking at, since anvil inventories do not always expose their location. Returns null if no
     * anvil block can be determined (e.g. a virtual anvil), in which case no block wear occurs.
     */
    private fun resolveAnvilLocation(inventory: Inventory, player: Player): Location? {
        inventory.location?.let { return it }
        return player.getTargetBlockExact(ANVIL_REACH)?.location
    }

    companion object {
        private const val ANVIL_REACH = 6
    }
}
