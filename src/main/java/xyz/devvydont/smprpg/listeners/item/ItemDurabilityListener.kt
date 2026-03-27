package xyz.devvydont.smprpg.listeners.item

import io.papermc.paper.datacomponent.DataComponentTypes
import org.bukkit.Particle
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerItemDamageEvent
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.listeners.ToggleableListener

class ItemDurabilityListener : ToggleableListener() {
    /**
     * Listens for item damage events, and locks item durability at 1 so that vanilla breaking does not occur.
     */
    @EventHandler
    @Suppress("unused")
    private fun onItemDamage(event: PlayerItemDamageEvent) {
        val item = event.item
        val currItemDamage = item.getData(DataComponentTypes.DAMAGE) as Int
        val maxAllowedDamage = item.getData(DataComponentTypes.MAX_DAMAGE) as Int - 1

        // If we are at x-1/x durability or about to pass it
        if (currItemDamage + event.damage >= maxAllowedDamage) {
            item.setData(DataComponentTypes.DAMAGE, maxAllowedDamage)

            // Force an update to our item stack to get our attributes to recalculate.
            val itemBp = ItemService.blueprint(item)
            itemBp.updateItemData(item)
            event.isCancelled = true

            // Play the break sound of the item at the player to notify them
            val player = event.player
            player.playSound(player.location, item.getData(DataComponentTypes.BREAK_SOUND).toString(), 1f, 1f)
            player.world.spawnParticle(Particle.ITEM, player.eyeLocation, 10, 0.2, 0.0, 0.2, 0.1, item);
        }
    }
}