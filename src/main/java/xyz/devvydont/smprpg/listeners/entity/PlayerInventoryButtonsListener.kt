package xyz.devvydont.smprpg.listeners.entity

import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.MenuType
import xyz.devvydont.smprpg.gui.player.InterfaceStats
import xyz.devvydont.smprpg.gui.player.InterfaceWardrobe
import xyz.devvydont.smprpg.gui.player.MenuStatsRoot
import xyz.devvydont.smprpg.util.listeners.ToggleableListener

class PlayerInventoryButtonsListener: ToggleableListener() {

    @EventHandler
    private fun onPlayerClickCraftButton(event: InventoryClickEvent) {
        val clickedInventory = event.clickedInventory
        if (clickedInventory == null) return

        if (clickedInventory.type == InventoryType.CRAFTING) {
            val player = event.whoClicked as Player
            when (event.slot) {
                1 -> {
                    MenuType.CRAFTING.builder()
                        .location(player.location)
                        .checkReachable(false)
                        .title(Component.text("Crafting"))
                        .build(player)
                        .open()
                }
                2 -> InterfaceWardrobe(null, player, player).openMenu()
                3 -> InterfaceStats(player, player, null).openMenu()
                4 -> MenuStatsRoot(player, player, null).openMenu()
                -999 -> return
                else -> event.isCancelled = true
            }
            player.updateInventory()
        }
    }
}