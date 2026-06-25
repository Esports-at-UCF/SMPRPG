package xyz.devvydont.smprpg.gui.items

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.ItemContainerContents
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.gui.base.MenuBase
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.blueprints.equipment.PocketCompressorBlueprint
import xyz.devvydont.smprpg.items.interfaces.ICompressible
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.formatting.Symbols

class MenuCompressor(val owner: Player, val compressor: ItemStack, val compressorBp: PocketCompressorBlueprint) : MenuBase(owner, 3) {

    override fun handleInventoryOpened(event: InventoryOpenEvent) {
        // Prepare the inventory
        event.titleOverride(compressorBp.getNameComponent(compressor).color(Symbols.INVENTORY_TITLE_COLOR))

        // Render the UI
        this.renderMenu()
    }

    override fun handleInventoryClicked(event: InventoryClickEvent) {
        // This UI uses buttons, so there's no code here.
        // But we still need to cancel the event to prevent stealing the borders.
        when (event.clickedInventory?.type) {
            InventoryType.PLAYER -> {
                if (event.isShiftClick) {
                    event.isCancelled = true
                    this.playInvalidAnimation()
                    return
                }
            }  // Allow all interactions in player inventory, except shift clicks
            else -> {
                if (event.isShiftClick) {
                    event.isCancelled = true
                    this.playInvalidAnimation()
                    return
                }

                val clickedSlotItem = event.clickedInventory?.getItem(event.slot) ?: return
                if (clickedSlotItem.isSimilar(BORDER_VOID)) {
                    event.isCancelled = true
                    this.playInvalidAnimation()
                    return
                }
            }
        }
    }

    /**
     * Clears and re-renders the menu UI.
     */
    private fun renderMenu() {
        // Reset the UI
        this.clear()
        this.setSlots(BORDER_VOID)

        // Starts at row 2 column 2 (slot 10)
        var startSlot = when (compressorBp.type) {
            CustomItemType.MEDIUM_POCKET_COMPRESSOR -> 12
            CustomItemType.LARGE_POCKET_COMPRESSOR -> 11
            CustomItemType.GIGANTIC_POCKET_COMPRESSOR -> 10
            CustomItemType.COLOSSAL_POCKET_COMPRESSOR -> 9
            else -> 13
        }
        val compressorConfig = compressor.getData(DataComponentTypes.CONTAINER)
        for (itemIdx in 0..<compressorBp.compressorSlots) {
            val configItem = compressorConfig!!.contents().get(itemIdx)
            var item: ItemStack
            if (configItem.type == Material.BARRIER) item = ItemStack.empty()
            else item = configItem

            val newConfig = mutableListOf<ItemStack>()
            for (idx in 0..<compressorConfig.contents().size) newConfig.add(compressorConfig.contents()[idx])
            this.setButton(startSlot + itemIdx, item) { e: InventoryClickEvent ->
                e.isCancelled = true

                val cursorBp = ItemService.blueprint(e.cursor)
                if (e.cursor.isEmpty) {
                    if (e.slot < startSlot || e.slot > (startSlot + compressorBp.compressorSlots)) {
                        return@setButton
                    }
                    newConfig[e.slot - startSlot] = ItemStack.of(PocketCompressorBlueprint.DUMMY_MATERIAL)
                }
                else if (cursorBp is ICompressible) {
                    if (cursorBp.decompressor == null) {
                        return@setButton
                    }
                    if (e.slot < startSlot || e.slot > (startSlot + compressorBp.compressorSlots)) {
                        return@setButton
                    }
                    val itemToSet = e.cursor.clone()
                    itemToSet.amount = 1
                    newConfig[e.slot - startSlot] = itemToSet
                }
                compressor.setData(DataComponentTypes.CONTAINER, ItemContainerContents.containerContents(newConfig))
                owner.playSound(owner, Sound.UI_BUTTON_CLICK, 1f, 1f)
                compressorBp.updateItemData(compressor)
                renderMenu()
            }
        }
    }
}