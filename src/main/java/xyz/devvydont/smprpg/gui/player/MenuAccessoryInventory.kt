package xyz.devvydont.smprpg.gui.player

import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitTask
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.SMPRPG.Companion.plugin
import xyz.devvydont.smprpg.entity.player.AccessoryInventory
import xyz.devvydont.smprpg.gui.base.MenuBase
import xyz.devvydont.smprpg.gui.base.MenuButtonClickHandler
import xyz.devvydont.smprpg.services.EntityService
import xyz.devvydont.smprpg.util.formatting.ComponentUtils
import xyz.devvydont.smprpg.util.formatting.Symbols

/*
 * Represents a viewable inventory of another player. Used as a way for admins to "peek" into someone's inventory to
 * see what they have.
 */
class MenuAccessoryInventory @JvmOverloads constructor(
    owner: Player,
    parentMenu: MenuBase? = null
) : MenuBase(owner, 6, parentMenu) {

    val leveledPlayer = SMPRPG.getService(EntityService::class.java).getPlayerInstance(player)

    override fun handleInventoryOpened(event: InventoryOpenEvent) {
        // Prepare the inventory
        event.titleOverride(ComponentUtils.create(player.name + "'s Accessories", Symbols.INVENTORY_TITLE_COLOR))
        this.clear()
        this.setSlots(BORDER_VOID)
        render()
    }

    override fun handleInventoryClicked(event: InventoryClickEvent) {
        val slot = event.slot
        if (slot !in equipmentSlots || slot != CLOSE_BUTTON) {
            event.isCancelled = true
            this.playInvalidAnimation()
        }

        val equipment = mutableListOf<ItemStack>()
        for (i in equipmentSlots) {
            equipment.add(inventory.getItem(i)!!)
        }
        leveledPlayer.accessories = AccessoryInventory(
            equipment[0],
            equipment[1],
            equipment[2],
            equipment[3],
            equipment[4]
        )
    }

    /*
     * Updates the interface to match the inventory of the player that we are currently viewing, if there is one.
     */
    private fun render() {
        // Render the players inventory
        val accessories = leveledPlayer.accessories
        var i = 0
        for (accessory in accessories.iterator()) {
            if (accessory.isEmpty) {
                this.clearSlot(equipmentSlots[i])
                i++
                continue
            }
            this.setSlot(equipmentSlots[i], accessory)
            i++
        }

        // Render the back/close button
        this.setBackButton(CLOSE_BUTTON)
    }

    companion object {

        val equipmentSlots = listOf(10, 19, 28, 37, 21)

        const val CLOSE_BUTTON: Int = 49
    }
}
