package xyz.devvydont.smprpg.gui.crafting

import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.SMPRPG.Companion.plugin
import xyz.devvydont.smprpg.gui.InterfaceUtil
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.gui.base.MenuBase
import xyz.devvydont.smprpg.recipe.CraftingRecipeMatcher
import xyz.devvydont.smprpg.util.formatting.ComponentUtils

/**
 * A fully custom crafting table interface. The player fills a 3x3 grid and the result slot previews the
 * output, committing it when clicked. Matching is driven by [CraftingRecipeMatcher]: data-driven custom
 * recipes (count-aware) are tried first, with vanilla recipes as a fallback.
 */
class MenuCraftingTable(player: Player) : MenuBase(player, ROWS) {

    override fun handleInventoryOpened(event: InventoryOpenEvent) {
        super.handleInventoryOpened(event)
        event.titleOverride(ComponentUtils.create("Crafting Table", NamedTextColor.DARK_GRAY))
        render()
    }

    private fun render() {
        setBorderFull()
        for (slot in GRID_SLOTS)
            clearSlot(slot)
        refreshResult()
    }

    /** Read the 3x3 grid into a row-major list of 9 cells. */
    private fun readGrid(): List<ItemStack?> = GRID_SLOTS.map { getItem(it) }

    private fun computeResult(): CraftingRecipeMatcher.Match? =
        CraftingRecipeMatcher.match(readGrid(), player.world)

    private fun refreshResult() {
        val match = computeResult()
        if (match == null) {
            setButton(RESULT_SLOT, IDLE_INDICATOR) { _: InventoryClickEvent -> }
            return
        }
        setButton(RESULT_SLOT, match.result) { event: InventoryClickEvent -> commit(event) }
    }

    /**
     * Commit the previewed craft. Recomputes from the live grid each iteration so a stale preview can never be
     * committed. A shift-click crafts as many as the grid allows (capped at one result stack's worth).
     */
    private fun commit(event: InventoryClickEvent) {
        val itemService = SMPRPG.getService(ItemService::class.java)
        val maxCrafts = if (event.isShiftClick) 64 else 1
        var crafted = 0

        while (crafted < maxCrafts) {
            val match = computeResult() ?: break
            if (match.consumption.isEmpty()) break

            for ((cell, amount) in match.consumption) {
                val slot = GRID_SLOTS[cell]
                val item = getItem(slot)
                consume(slot, amount)
                // Vanilla container ingredients (buckets, bottles) leave a remainder when crafted.
                if (item != null && itemService.getItemKey(item) == null) {
                    val remainder = item.type.craftingRemainingItem
                    if (remainder != null) giveItemToPlayer(ItemStack(remainder, amount))
                }
            }
            giveItemToPlayer(match.result.clone())
            crafted++
        }

        if (crafted == 0) {
            playInvalidAnimation()
            return
        }
        playSound(Sound.ENTITY_ITEM_PICKUP, 0.6f, 1.0f)
        refreshResult()
    }

    /** Remove [amount] items from the given slot, clearing it entirely if nothing remains. */
    private fun consume(slotIndex: Int, amount: Int) {
        val item = getItem(slotIndex) ?: return
        val remaining = item.amount - amount
        if (remaining <= 0) {
            clearSlot(slotIndex)
            return
        }
        item.amount = remaining
        setSlot(slotIndex, item)
    }

    override fun handleInventoryClicked(event: InventoryClickEvent) {
        super.handleInventoryClicked(event)

        // Recompute the result once the click has settled and the grid contents have updated.
        Bukkit.getScheduler().runTaskLater(plugin, Runnable { refreshResult() }, 1L)

        // Players may freely manage their own inventory (shift-clicks flow into the grid).
        val clicked = event.clickedInventory
        if (clicked != null && clicked.type == InventoryType.PLAYER) return

        // Inside the menu, only the 3x3 grid slots may be edited directly.
        if (clicked == inventory && event.rawSlot in GRID_SLOTS) return

        event.isCancelled = true
    }

    /**
     * Prevents dragging items onto protected slots (borders and the result), which would otherwise strand items
     * that never return to the player. Drags across the grid are allowed and refresh the result.
     */
    @EventHandler
    @Suppress("unused")
    private fun onItemDragged(event: InventoryDragEvent) {
        if (event.inventory != inventory) return
        val touchesProtectedSlot = event.rawSlots.any { it < inventorySize && it !in GRID_SLOTS }
        if (touchesProtectedSlot) {
            event.isCancelled = true
            return
        }
        Bukkit.getScheduler().runTaskLater(plugin, Runnable { refreshResult() }, 1L)
    }

    /** Return the grid items to the player so nothing is lost when the menu closes. */
    override fun handleInventoryClosed(event: InventoryCloseEvent) {
        super.handleInventoryClosed(event)
        for (slot in GRID_SLOTS)
            giveItemToPlayer(slot, true)
    }

    companion object {
        const val ROWS = 5

        // 3x3 grid laid out row-major in the upper-left, with the result to the right of center.
        val GRID_SLOTS = listOf(10, 11, 12, 19, 20, 21, 28, 29, 30)
        const val RESULT_SLOT = 24

        private val IDLE_INDICATOR: ItemStack = InterfaceUtil.getNamedItemWithDescription(
            Material.GRAY_STAINED_GLASS_PANE,
            ComponentUtils.create("Result", NamedTextColor.GRAY),
            ComponentUtils.EMPTY,
            ComponentUtils.create("Place ingredients in the grid to craft.", NamedTextColor.DARK_GRAY)
        )
    }
}
