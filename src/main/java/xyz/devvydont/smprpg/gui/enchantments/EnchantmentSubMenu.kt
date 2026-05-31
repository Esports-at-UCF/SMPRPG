package xyz.devvydont.smprpg.gui.enchantments

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.enchantments.CustomEnchantment
import xyz.devvydont.smprpg.gui.base.MenuBase
import xyz.devvydont.smprpg.gui.base.MenuButtonClickHandler
import xyz.devvydont.smprpg.items.blueprints.resources.scrolls.DynamicEnchantingScroll
import xyz.devvydont.smprpg.services.EntityService
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.formatting.ComponentUtils
import xyz.devvydont.smprpg.util.formatting.Symbols
import java.util.List
import java.util.function.Consumer

/*
 * Represents the menu that is displayed when specific enchantment details are queried using the EnchantmentMenu.
 * This sub menu displays data for a specific enchantment.
 */
class EnchantmentSubMenu(
    player: Player, parentMenu: MenuBase?, // The enchantment we are expanding details on
    private val enchantment: CustomEnchantment
) : MenuBase(player, ROWS, parentMenu) {
    fun createEnchantmentButton(level: Int): ItemStack {
        val item = createNamedItem(
            Material.ENCHANTED_BOOK,
            enchantment.enchantment.displayName(level).color(enchantment.enchantColor)
        )
        val magicLvl = SMPRPG.getService(EntityService::class.java).getPlayerInstance(player).magicSkill.level
        val isUnlocked = magicLvl >= enchantment.getSkillRequirementForLevel(level)
        val check =
            if (isUnlocked) ComponentUtils.create(Symbols.CHECK, NamedTextColor.GREEN) else ComponentUtils.create(
                Symbols.X,
                NamedTextColor.RED
            )
        val req = ComponentUtils.merge(
            ComponentUtils.create(
                "Magic Skill Level Requirement: ",
                if (isUnlocked) NamedTextColor.GRAY else NamedTextColor.RED
            ),
            ComponentUtils.create(
                enchantment.getSkillRequirementForLevel(level).toString(),
                if (isUnlocked) NamedTextColor.LIGHT_PURPLE else NamedTextColor.DARK_RED
            )
        )
        item.editMeta(Consumer { meta: ItemMeta ->
            val comps = mutableListOf<Component>()
            comps.add(enchantment.build(level).description)
            comps.add(ComponentUtils.EMPTY)
            val recipe = enchantment.getRecipe(level)
            if (recipe != null) {
                comps.add(ComponentUtils.create("Reagents required:", NamedTextColor.GOLD))
                for (ing in recipe.ingredients) {
                    comps.add(
                        ComponentUtils.merge(
                            ing.displayName(),
                            ComponentUtils.create(" x${ing.amount}")
                    ))
                }
                comps.add(ComponentUtils.EMPTY)
            }
            comps.add(ComponentUtils.merge(check, ComponentUtils.SPACE, req))
            meta.lore(
                ComponentUtils.cleanItalics(
                    comps
                )
            )
        })
        return item
    }

    private val indexesToPopulate: MutableList<Int>
        /**
         * Generates a list of inventory indexes to populate that aligns with the max level of the enchantment.
         * Handles the ugly logic on which slots to use based on what the max level of the enchantment is.
         * @return A list of integers representing inventory index slots.
         */
        get() {
            val indexes: MutableList<Int> = ArrayList()

            // Sanity checks, is there no "levels" to display?
            if (enchantment.maxLevel <= 0) return indexes

            // Is the max level too big for us to show? Our UI only supports 21 levels
            if (enchantment.maxLevel > 21) {
                for (slot in ALL_SLOTS) indexes.add(slot)
                return indexes
            }

            // todo figure out big brain centering logic so it looks pretty, for now i am gonna be a moron and just give numbers in order
            for (level in 1..enchantment.maxLevel) indexes.add(ALL_SLOTS[level - 1])

            return indexes
        }

    override fun handleInventoryClicked(event: InventoryClickEvent) {
        super.handleInventoryClicked(event)
        event.isCancelled = true
    }

    override fun handleInventoryOpened(event: InventoryOpenEvent) {
        event.titleOverride(enchantment.displayName)
        this.setBorderFull()

        // Populate slots with the levels of the enchantment.
        val indexes = this.indexesToPopulate
        for (level in 1..enchantment.maxLevel) this.setButton(
            indexes[level - 1],
            createEnchantmentButton(level)
        ) { e: InventoryClickEvent ->
            val player = e.whoClicked as Player
            if (player.gameMode == GameMode.CREATIVE) {
                if (e.isShiftClick && e.isLeftClick) {
                    this.playSound(Sound.ENTITY_ITEM_PICKUP, 1f, .5f)
                    this.playSound(Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1f, 2f)
                    val item = DynamicEnchantingScroll.getScrollWithEnchantment(enchantment)
                    SMPRPG.getService(ItemService::class.java).ensureItemStackUpdated(item)
                    player.inventory.addItem(item)
                }
            }
        }

        this.setButton(
            (ROWS - 1) * 9 + 4,
            BUTTON_BACK
        ) { e: InventoryClickEvent -> this.openParentMenu() }
    }

    companion object {
        const val ROWS: Int = 5
        val ALL_SLOTS: IntArray = intArrayOf(
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34
        )
    }
}
