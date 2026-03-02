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
import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.entity.fishing.SeaCreature
import xyz.devvydont.smprpg.fishing.loot.FishingLootType
import xyz.devvydont.smprpg.fishing.loot.FishingLootTypeSelector
import xyz.devvydont.smprpg.fishing.utils.FishingContext
import xyz.devvydont.smprpg.gui.InterfaceUtil.getNamedItemWithDescription
import xyz.devvydont.smprpg.gui.base.MenuBase
import xyz.devvydont.smprpg.gui.base.MenuButtonClickHandler
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.slayer.quest.SlayerType
import xyz.devvydont.smprpg.util.formatting.ComponentUtils

/**
 * A menu that allows a player to view their current "fishing context", so they know what they are rolling for.
 */
class MenuSlayerQuest : MenuBase {
    constructor(player: Player, slayerType : SlayerType) : super(player, 5) {
        render()
    }

    constructor(player: Player, parentMenu: MenuBase?, slayerType : SlayerType) : super(player, 5, parentMenu) {
        render()
    }

    /**
     * Render the menu.
     */
    fun render() {
        this.sounds.setMenuClose(Sound.ENTITY_ENDER_DRAGON_HURT, 1f, 2f)
        this.setBorderFull()

        this.setBackButton(40)
        // Place buttons for each slayer tier

        // Dummy out some slots for NYI slayers
        for (i in SLAYERS_START..<SLAYERS_START + SLAYER_TIERS) {
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
                playInvalidAnimation()//FishingPoolViewerMenu(this.player, this, FishingLootType.FISH).openMenu()
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
        const val SLAYERS_START: Int = 11
        const val SLAYER_TIERS = 5
    }
}