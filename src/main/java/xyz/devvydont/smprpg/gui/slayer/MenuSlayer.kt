package xyz.devvydont.smprpg.fishing.gui

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.SMPRPG.Companion.plugin
import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.entity.CustomEntityType
import xyz.devvydont.smprpg.entity.fishing.SeaCreature
import xyz.devvydont.smprpg.entity.slayer.shambling.ShamblingAbominationParent
import xyz.devvydont.smprpg.fishing.loot.FishingLootType
import xyz.devvydont.smprpg.fishing.loot.FishingLootTypeSelector
import xyz.devvydont.smprpg.fishing.utils.FishingContext
import xyz.devvydont.smprpg.gui.InterfaceUtil.getNamedItemWithDescription
import xyz.devvydont.smprpg.gui.base.MenuBase
import xyz.devvydont.smprpg.gui.base.MenuButtonClickHandler
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.services.EntityService
import xyz.devvydont.smprpg.slayer.quest.SlayerQuest
import xyz.devvydont.smprpg.slayer.quest.SlayerType
import xyz.devvydont.smprpg.util.formatting.ComponentUtils

/**
 * A menu that allows a player to view their current "fishing context", so they know what they are rolling for.
 */
class MenuSlayer : MenuBase {
    constructor(player: Player) : super(player, 3) {
        render()
    }

    constructor(player: Player, parentMenu: MenuBase?) : super(player, 3, parentMenu) {
        render()
    }

    /**
     * Render the menu.
     */
    fun render() {
        this.sounds.setMenuOpen(Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 2.0f)
        this.sounds.setMenuOpenSub(Sound.ENTITY_ENDER_DRAGON_HURT, 1f, 1.0f)
        this.sounds.setMenuClose(Sound.ENTITY_ENDER_DRAGON_HURT, 1f, 0.5f)
        this.setBorderFull()

        this.setBackButton(22)
        // Place buttons for each slayer

        // Dummy out some slots for NYI slayers
        for (i in SLAYERS_START..<SLAYERS_START + NUM_SLAYERS) {
            this.setButton(
                i, getNamedItemWithDescription(
                    Material.COAL_BLOCK,
                    ComponentUtils.create("Future Slayer Boss", NamedTextColor.DARK_PURPLE),
                    ComponentUtils.EMPTY,
                    ComponentUtils.merge(
                        ComponentUtils.create("This boss has not been "),
                        ComponentUtils.create("developed", NamedTextColor.YELLOW)
                    ),
                    ComponentUtils.create("yet, come back later!")
                ), { e: InventoryClickEvent? ->
                    playInvalidAnimation()
                })
        }

        // Shambling Abomination
        this.setButton(
            SLAYERS_START, getNamedItemWithDescription(
                CustomItemType.NECROTIC_FLESH,
                ComponentUtils.create("Shambling Abomination", NamedTextColor.DARK_PURPLE, TextDecoration.BOLD),
                ComponentUtils.EMPTY,
                ComponentUtils.create("Resurrected via rituals", NamedTextColor.GRAY),
                ComponentUtils.merge(
                    ComponentUtils.create("from the ", NamedTextColor.GRAY),
                    ComponentUtils.create("Necronomicon", NamedTextColor.DARK_RED),
                    ComponentUtils.create(",", NamedTextColor.GRAY),
                ),
                ComponentUtils.create("this affront to the living preys", NamedTextColor.GRAY),
                ComponentUtils.create("on those who dare awaken it.", NamedTextColor.GRAY)
            ), { e: InventoryClickEvent? ->
                MenuSlayerQuest(this.player, this, SlayerType.SHAMBLING_ABOMINATION).openMenu()
            })
    }

    override fun handleInventoryOpened(event: InventoryOpenEvent) {
        event.titleOverride(Component.text("Slayer"))
    }

    override fun handleInventoryClicked(event: InventoryClickEvent) {
        event.setCancelled(true)
        this.playInvalidAnimation(true)
    }

    companion object {
        // GUI positions.
        const val SLAYERS_START: Int = 10
        const val NUM_SLAYERS = 7
    }
}