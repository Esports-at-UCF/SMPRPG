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
import xyz.devvydont.smprpg.services.SlayerService
import xyz.devvydont.smprpg.slayer.quest.SlayerClassification
import xyz.devvydont.smprpg.slayer.quest.SlayerQuest
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

        // <editor-fold desc="Shambling Abomination">
        this.setButton(
            SLAYERS_START, getNamedItemWithDescription(
                CustomItemType.NECROTIC_FLESH,
                ComponentUtils.create("Shambling Abomination I", NamedTextColor.DARK_PURPLE, TextDecoration.BOLD),
                ComponentUtils.EMPTY,
                ComponentUtils.create("Not-so-shambling Gait", NamedTextColor.DARK_RED, TextDecoration.UNDERLINED),
                ComponentUtils.create("The Shambling Abomination will", NamedTextColor.GRAY),
                ComponentUtils.create("chase you down at a relatively", NamedTextColor.GRAY),
                ComponentUtils.create("quick pace, and strike when close.", NamedTextColor.GRAY)
            ), { e: InventoryClickEvent? -> startQuest(SlayerClassification.SHAMBLING_HORROR_1)})

        this.setButton(
            SLAYERS_START + 1, getNamedItemWithDescription(
                CustomItemType.NECROTIC_FLESH,
                ComponentUtils.create("Shambling Abomination II", NamedTextColor.DARK_PURPLE, TextDecoration.BOLD),
                ComponentUtils.EMPTY,
                ComponentUtils.create("Ferocious Frenzy", NamedTextColor.DARK_RED, TextDecoration.UNDERLINED),
                ComponentUtils.create("The Shambling Abomination will", NamedTextColor.GRAY),
                ComponentUtils.create("drastically speed up at half health,", NamedTextColor.GRAY),
                ComponentUtils.merge(
                    ComponentUtils.create("and ", NamedTextColor.GRAY),
                    ComponentUtils.create("double", NamedTextColor.RED),
                    ComponentUtils.create(" its attack rate.", NamedTextColor.GRAY),
                ),
                ComponentUtils.EMPTY,
                ComponentUtils.merge(
                    ComponentUtils.create("Shambling Abomination II", NamedTextColor.DARK_PURPLE, TextDecoration.BOLD),
                    ComponentUtils.create(" also inherits", NamedTextColor.GRAY)),
                ComponentUtils.create("all abilities from previous boss tiers.", NamedTextColor.GRAY)
            ), { e: InventoryClickEvent? -> startQuest(SlayerClassification.SHAMBLING_HORROR_2)})

        this.setButton(
            SLAYERS_START + 2, getNamedItemWithDescription(
                CustomItemType.NECROTIC_FLESH,
                ComponentUtils.create("Shambling Abomination III", NamedTextColor.DARK_PURPLE, TextDecoration.BOLD),
                ComponentUtils.EMPTY,
                ComponentUtils.create("Furious Frenzy", NamedTextColor.DARK_RED, TextDecoration.UNDERLINED),
                ComponentUtils.merge(
                    ComponentUtils.create("The ", NamedTextColor.GRAY),
                    ComponentUtils.create("Ferocious Frenzy", NamedTextColor.DARK_RED, TextDecoration.UNDERLINED),
                    ComponentUtils.create(" ability now also grants", NamedTextColor.GRAY),
                ),
                ComponentUtils.merge(
                    ComponentUtils.create("a ", NamedTextColor.GRAY),
                    ComponentUtils.create("50%", NamedTextColor.RED),
                    ComponentUtils.create(" increase in strength when activated.", NamedTextColor.GRAY),
                ),
                ComponentUtils.EMPTY,
                ComponentUtils.merge(
                    ComponentUtils.create("Shambling Abomination III", NamedTextColor.DARK_PURPLE, TextDecoration.BOLD),
                    ComponentUtils.create(" also inherits", NamedTextColor.GRAY)),
                ComponentUtils.create("all abilities from previous boss tiers.", NamedTextColor.GRAY)
            ), { e: InventoryClickEvent? -> startQuest(SlayerClassification.SHAMBLING_HORROR_3)})

        this.setButton(
            SLAYERS_START + 3, getNamedItemWithDescription(
                CustomItemType.NECROTIC_FLESH,
                ComponentUtils.create("Shambling Abomination IV", NamedTextColor.DARK_PURPLE, TextDecoration.BOLD),
                ComponentUtils.EMPTY,
                ComponentUtils.create("Spontaneous Human(?) Combustion", NamedTextColor.DARK_RED, TextDecoration.UNDERLINED),
                ComponentUtils.merge(
                    ComponentUtils.create("The ", NamedTextColor.GRAY),
                    ComponentUtils.create("Shambling Horror", NamedTextColor.DARK_PURPLE, TextDecoration.BOLD),
                    ComponentUtils.create(" will implode every", NamedTextColor.GRAY),
                ),
                ComponentUtils.merge(
                    ComponentUtils.create("10 seconds", NamedTextColor.RED),
                    ComponentUtils.create(", dealing ", NamedTextColor.GRAY),
                    ComponentUtils.create("500", NamedTextColor.RED),
                    ComponentUtils.create(" true damage to players ", NamedTextColor.GRAY),
                ),
                ComponentUtils.create("that are caught in range.", NamedTextColor.GRAY),
                ComponentUtils.EMPTY,
                ComponentUtils.merge(
                    ComponentUtils.create("Shambling Abomination IV", NamedTextColor.DARK_PURPLE, TextDecoration.BOLD),
                    ComponentUtils.create(" also inherits", NamedTextColor.GRAY)),
                ComponentUtils.create("all abilities from previous boss tiers.", NamedTextColor.GRAY)
            ), { e: InventoryClickEvent? -> startQuest(SlayerClassification.SHAMBLING_HORROR_4)})

        this.setButton(
            SLAYERS_START + 4, getNamedItemWithDescription(
                CustomItemType.NECROTIC_FLESH,
                ComponentUtils.create("Shambling Abomination V", NamedTextColor.DARK_PURPLE, TextDecoration.BOLD),
                ComponentUtils.EMPTY,
                ComponentUtils.create("Syphon", NamedTextColor.DARK_RED, TextDecoration.UNDERLINED),
                ComponentUtils.merge(
                    ComponentUtils.create("The "),
                    ComponentUtils.create("Shambling Abomination", NamedTextColor.DARK_PURPLE, TextDecoration.BOLD),
                    ComponentUtils.create(" will syphon ", NamedTextColor.GRAY),
                    ComponentUtils.create("10%", NamedTextColor.RED),
                    ComponentUtils.create(" of your max health every 3", NamedTextColor.GRAY)),
                ComponentUtils.merge(
                    ComponentUtils.create("seconds, and return ", NamedTextColor.GRAY),
                    ComponentUtils.create("1000x", NamedTextColor.RED),
                    ComponentUtils.create(" the syphoned amount as healing to itself.", NamedTextColor.GRAY),
                ),
                ComponentUtils.EMPTY,
                ComponentUtils.create("Ragnarok Rage", NamedTextColor.DARK_RED, TextDecoration.UNDERLINED),
                ComponentUtils.merge(
                    ComponentUtils.create("Furious Frenzy", NamedTextColor.DARK_RED, TextDecoration.UNDERLINED),
                    ComponentUtils.create(" now triggers at ", NamedTextColor.GRAY),
                    ComponentUtils.create("35%", NamedTextColor.RED),
                    ComponentUtils.create(" health,", NamedTextColor.GRAY),
                ),
                ComponentUtils.merge(
                    ComponentUtils.create("quickens", NamedTextColor.RED),
                    ComponentUtils.create(" the rate that ", NamedTextColor.GRAY),
                    ComponentUtils.create("Spontaneous Human(?) Combustion", NamedTextColor.DARK_RED, TextDecoration.UNDERLINED)),
                ComponentUtils.merge(
                    ComponentUtils.create("triggers by", NamedTextColor.GRAY),
                    ComponentUtils.create(" 2x", NamedTextColor.RED),
                    ComponentUtils.create(", and increases the true damage dealt to ", NamedTextColor.GRAY),
                    ComponentUtils.create("1000", NamedTextColor.RED),
                    ComponentUtils.create(".", NamedTextColor.GRAY)
                ),
                ComponentUtils.EMPTY,
                ComponentUtils.merge(
                    ComponentUtils.create("Shambling Abomination V", NamedTextColor.DARK_PURPLE, TextDecoration.BOLD),
                    ComponentUtils.create(" also inherits", NamedTextColor.GRAY)),
                ComponentUtils.create("all abilities from previous boss tiers.", NamedTextColor.GRAY)
            ), { e: InventoryClickEvent? -> startQuest(SlayerClassification.SHAMBLING_HORROR_5)})
        //</editor-fold>
    }

    override fun handleInventoryOpened(event: InventoryOpenEvent) {
        event.titleOverride(Component.text("Slayer"))
    }

    override fun handleInventoryClicked(event: InventoryClickEvent) {
        event.setCancelled(true)
        this.playInvalidAnimation(true)
    }

    fun startQuest(slayerInfo : SlayerClassification) {
        val questOwner = SMPRPG.getService(EntityService::class.java).getPlayerInstance(player)
        val quest = SlayerQuest(questOwner, slayerInfo)
        if (!SMPRPG.getService(SlayerService::class.java).registerQuest(quest)) {
            playInvalidAnimation()
            return
        }

        // If we get here, we have started the quest.
        player.playSound(player, Sound.ENTITY_WITHER_DEATH, 1.0f, 1.5f)
        closeMenu()
    }

    companion object {
        // GUI positions.
        const val SLAYERS_START: Int = 11
        const val SLAYER_TIERS = 5
    }
}