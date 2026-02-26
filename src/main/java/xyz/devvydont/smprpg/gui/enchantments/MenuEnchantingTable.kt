package xyz.devvydont.smprpg.gui.enchantments

import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.gui.base.MenuBase
import xyz.devvydont.smprpg.items.interfaces.ISellable
import xyz.devvydont.smprpg.services.EconomyService
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.formatting.ComponentUtils
import xyz.devvydont.smprpg.util.formatting.Symbols

class MenuEnchantingTable(owner: Player) : MenuBase(owner, 5) {
    private val itemService: ItemService = SMPRPG.Companion.getService(ItemService::class.java)
    private val economyService: EconomyService = SMPRPG.Companion.getService(EconomyService::class.java)

    init {
        this.sounds.setMenuOpen(Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1f, 1f)
        this.sounds.setMenuClose(Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 1f, 1f)
        // render() TODO
    }

    override fun handleInventoryOpened(event: InventoryOpenEvent) {
        // Prepare the inventory
        event.titleOverride(
            ComponentUtils.merge(
                ComponentUtils.create(Symbols.OFFSET_NEG_1 + Symbols.ENCHANTING_MENU, NamedTextColor.WHITE),
                ComponentUtils.create(
                    Symbols.OFFSET_NEG_128 + Symbols.OFFSET_NEG_32 + Symbols.OFFSET_NEG_2 + "Enchant",
                    NamedTextColor.BLACK
                )
            )
        )
        this.setMaxStackSize(100)

        // Render the UI
        this.clear()
    }

    override fun handleInventoryClicked(event: InventoryClickEvent) {
        // If an inventory was clicked and the slot clicked was empty, allow it
        if (event.clickedInventory != null && event.clickedInventory!!.getItem(event.slot) == null) {
            event.isCancelled = false
            return
        }

        // No item involved, don't allow it
        if (event.getCurrentItem() == null) {
            event.isCancelled = true
            return
        }

        // If the item clicked is not sellable, we can't do anything with it.
        val itemBlueprint = this.itemService.getBlueprint(event.getCurrentItem()!!)
        event.isCancelled = itemBlueprint !is ISellable

        // If the item clicked is enchanted or reforged, we should prevent the item from being shift clicked.
        if (event.isShiftClick && (!event.getCurrentItem()!!.enchantments.isEmpty() || itemBlueprint.isReforged(
                event.getCurrentItem()
            ))
        ) {
            val error = ComponentUtils.merge(
                ComponentUtils.create("WHOA THERE!", NamedTextColor.RED, TextDecoration.BOLD),
                ComponentUtils.create(" Do you really want to sell this? Manually drag the item in the menu if so.")
            )
            player.sendMessage(ComponentUtils.alert(error, NamedTextColor.RED))
            player.playSound(player.location, Sound.ENTITY_VILLAGER_DEATH, 1f, 1.5f)
            event.isCancelled = true
        }
    }

    override fun handleInventoryClosed(event: InventoryCloseEvent) {
        // Work out how much was deposited.
        var quantitySold = 0
        var amountToCredit = 0
        for (depositedItem in this.items) {
            if (depositedItem == null) {
                continue
            }

            val itemBlueprint = this.itemService.getBlueprint(depositedItem)
            if (itemBlueprint is ISellable) {
                quantitySold += depositedItem.amount
                amountToCredit += itemBlueprint.getWorth(depositedItem)
                depositedItem.amount = 0
            }
        }

        // Ignore if nothing was deposited.
        if (amountToCredit <= 0) {
            return
        }

        this.economyService.addMoney(this.player, amountToCredit.toDouble())
        this.player.sendMessage(
            ComponentUtils.success(
                ComponentUtils.merge(
                    ComponentUtils.create("You sold ", NamedTextColor.GREEN),
                    ComponentUtils.create(quantitySold.toString(), NamedTextColor.AQUA),
                    ComponentUtils.create(" items totaling ", NamedTextColor.GREEN),
                    ComponentUtils.create(EconomyService.Companion.formatMoney(amountToCredit), NamedTextColor.GOLD),
                    ComponentUtils.create("! Your balance is now ", NamedTextColor.GREEN),
                    ComponentUtils.create(this.economyService.formatMoney(this.player), NamedTextColor.GOLD)
                )
            )
        )
        this.sounds.playActionConfirm()
    }
}