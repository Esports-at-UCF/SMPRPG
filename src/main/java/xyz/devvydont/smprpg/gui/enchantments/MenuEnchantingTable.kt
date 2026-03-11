package xyz.devvydont.smprpg.gui.enchantments

import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent
import io.papermc.paper.registry.RegistryKey
import io.papermc.paper.registry.TypedKey
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.data.type.NoteBlock
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.SMPRPG.Companion.plugin
import xyz.devvydont.smprpg.enchantments.CustomEnchantment
import xyz.devvydont.smprpg.entity.player.LeveledPlayer
import xyz.devvydont.smprpg.gui.InterfaceUtil.getNamedItem
import xyz.devvydont.smprpg.gui.base.MenuBase
import xyz.devvydont.smprpg.gui.items.MenuReforge.Companion.BUTTON_SLOT
import xyz.devvydont.smprpg.gui.items.MenuReforge.Companion.INPUT_SLOT
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.blueprints.resources.scrolls.DynamicEnchantingScroll
import xyz.devvydont.smprpg.reforge.ReforgeType
import xyz.devvydont.smprpg.services.EconomyService
import xyz.devvydont.smprpg.services.EconomyService.Companion.formatMoney
import xyz.devvydont.smprpg.services.EnchantmentService
import xyz.devvydont.smprpg.services.EntityService
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.services.SkillService
import xyz.devvydont.smprpg.util.extensions.takeIfPresent
import xyz.devvydont.smprpg.util.formatting.ComponentUtils
import xyz.devvydont.smprpg.util.formatting.Symbols
import java.util.function.Consumer

const val ITEM_SLOT = 13;
const val SCROLL_SLOT = 10;
const val BOOKSHELF_SLOT = 44;
val       INGREDIENT_SLOTS = intArrayOf(30, 31, 32, 39, 40, 41);

val       WHITELISTED_SLOTS = intArrayOf(ITEM_SLOT, SCROLL_SLOT)
const val ACTION_SLOT = 16

enum class ActionButtonState {
    CALCULATING,
    DISABLED,
    ENABLED
}

class MenuEnchantingTable(owner: Player, private val shelfPower: Int, private val runeBlocks : ArrayList<NoteBlock>) : MenuBase(owner, 5) {
    private var actionButtonState = ActionButtonState.DISABLED

    init {
        this.sounds.setMenuOpen(Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1f, 1f)
        this.sounds.setMenuClose(Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 1f, 1f)
    }

    override fun handleInventoryOpened(event: InventoryOpenEvent) {
        // Prepare the inventory
        event.titleOverride(
            ComponentUtils.merge(
                ComponentUtils.create(Symbols.OFFSET_NEG_1 + Symbols.ENCHANTING_MENU, NamedTextColor.WHITE),
                ComponentUtils.create(
                    Symbols.OVERLAY_BG_OFFSET_STANDARD + "Enchant",
                    NamedTextColor.BLACK
                )
            )
        )
        this.setMaxStackSize(100)

        // Render the UI
        this.render()
    }

    override fun handleInventoryClicked(event: InventoryClickEvent) {
        super.handleInventoryClicked(event)
        val slot = event.slot

        if (event.clickedInventory == null) return

        if (event.isShiftClick) {
            handleShiftClicks(event)
            actionButtonState = ActionButtonState.CALCULATING
            Bukkit.getScheduler()
                .runTaskLater(plugin, Runnable { setSlot(ACTION_SLOT, generateEnchantButton()) }, 0L)
            return
        }

        // If we are clicking in the player inventory allow it to happen. We need to allow them to manage items.
        if (event.clickedInventory!!.type == InventoryType.PLAYER) {
            event.isCancelled = false
            return
        }

        if (slot == ACTION_SLOT) {
            if (actionButtonState != ActionButtonState.ENABLED) {
                event.isCancelled = true
                return
            }
            else {
                doEnchant()
            }
        }

        if (slot !in WHITELISTED_SLOTS) {
            event.isCancelled = true
            return
        }

        // Ingredient slots are not player accessible, they are there only to show cost.
        if (slot in INGREDIENT_SLOTS) {
            event.isCancelled = true
            return
        }

        // Scroll slot should ONLY accept dynamic scrolls.
        if (slot == SCROLL_SLOT) {
            val itemKey = SMPRPG.getService(ItemService::class.java).itemTypeKey
            val cursor = event.cursor
            if (!cursor.isEmpty) {
                if (cursor.persistentDataContainer.getOrDefault(
                        itemKey,
                        PersistentDataType.STRING,
                        "ERROR"
                    ) != CustomItemType.ENCHANTING_SCROLL.name.lowercase())
                {
                    actionButtonState = ActionButtonState.CALCULATING
                    Bukkit.getScheduler()
                        .runTaskLater(plugin, Runnable { setSlot(ACTION_SLOT, generateEnchantButton()) }, 0L)
                    event.isCancelled = true
                    return
                }
            }
            actionButtonState = ActionButtonState.CALCULATING
            Bukkit.getScheduler()
                .runTaskLater(plugin, Runnable { setSlot(ACTION_SLOT, generateEnchantButton()) }, 0L)
        }

        // Item slot accepts any item, but we need to determine if the enchantment in the scroll slot is valid.
        if (slot == ITEM_SLOT) {
            // Use our cursor item for logic, since it's what actually is going to be used for the enchantment
            actionButtonState = ActionButtonState.CALCULATING
            Bukkit.getScheduler().runTaskLater(plugin, Runnable { setSlot(ACTION_SLOT, generateEnchantButton()) }, 0L)
        }
    }

    override fun handleInventoryClosed(event: InventoryCloseEvent) {
        super.handleInventoryClosed(event)
        giveItemToPlayer(ITEM_SLOT, true)
        giveItemToPlayer(SCROLL_SLOT, true)
    }

    fun generateEnchantButton(): ItemStack {
        val input = getItem(ITEM_SLOT)
        val scroll = getItem(SCROLL_SLOT)
        val book = getNamedItem(
            Material.BOOK,
            ComponentUtils.create("Enchant Item", NamedTextColor.DARK_AQUA).decoration(TextDecoration.ITALIC, false)
        )
        val valid = validateEnchant(input, scroll)
        val lore: MutableList<Component?> = ArrayList()

        actionButtonState = ActionButtonState.DISABLED
        // Check our validity cases.
        if (!valid) {
            clearIngredientDisplay()
            // Input item is missing
            if (input == null || input.type == Material.AIR) {
                lore.add(ComponentUtils.EMPTY)
                lore.add(ComponentUtils.create("Input an item to enchant.", NamedTextColor.GRAY))
                book.editMeta(Consumer { meta: ItemMeta? ->
                    meta!!.lore(ComponentUtils.cleanItalics(lore))
                })
                return book
            }

            // Input exists at this point, is a scroll missing?
            if (scroll == null || scroll.type == Material.AIR) {
                lore.add(ComponentUtils.EMPTY)
                lore.add(ComponentUtils.create("You need a Scroll of Imbuement to enchant!", NamedTextColor.RED))
                book.editMeta(Consumer { meta: ItemMeta? ->
                    meta!!.lore(ComponentUtils.cleanItalics(lore))
                })
                return book.withType(Material.BARRIER)
            }

            // Scroll and Input both exist at this point, only case left for invalid check is invalid enchantment
            val enchant = getEnchant(scroll)!!
            lore.add(ComponentUtils.EMPTY)
            var hasConflict = false
            for (ench in input.enchantments.keys) {
                if (enchant.conflictsWith(ench) && ench.key != enchant.key) {
                    hasConflict = true
                    lore.add(ComponentUtils.create("This enchantment conflicts with one or more enchantments!", NamedTextColor.RED))
                    break
                }
            }

            if (!hasConflict)
                lore.add(ComponentUtils.create("This enchantment cannot be applied to this item!", NamedTextColor.RED))
            book.editMeta(Consumer { meta: ItemMeta? ->
                meta!!.lore(ComponentUtils.cleanItalics(lore))
            })
            return book.withType(Material.BARRIER)
        }

        // Populate our recipe components
        val enchant = getEnchant(scroll)!!
        val enchantLevel = input!!.getEnchantmentLevel(enchant) + 1
        var recipe = SMPRPG.getService(EnchantmentService::class.java).getEnchantment(enchant)?.getRecipe(enchantLevel)
        if (recipe == null) {
            // Either this recipe isn't implemented, or we are max level.
            lore.add(ComponentUtils.EMPTY)
            if (enchantLevel > enchant.maxLevel)
                lore.add(ComponentUtils.create("This enchantment has already reached its maximum potential!", NamedTextColor.GOLD))
            else
                lore.add(ComponentUtils.create("This is a missing recipe! Contact a developer to let them know.", NamedTextColor.RED))
            book.editMeta(Consumer { meta: ItemMeta? ->
                meta!!.lore(ComponentUtils.cleanItalics(lore))
            })
            return book.withType(Material.BARRIER)
        }

        val reqLevel = recipe.power
        val lvlPlayer = SMPRPG.getService(EntityService::class.java).getPlayerInstance(player)
        if (reqLevel > lvlPlayer.magicSkill.level) {
            lore.add(ComponentUtils.EMPTY)
            lore.add(ComponentUtils.create("Your magic level is not high enough to apply this enchantment!", NamedTextColor.RED))
            lore.add(ComponentUtils.create("Required level: $reqLevel", NamedTextColor.RED))
            book.editMeta(Consumer { meta: ItemMeta? ->
                meta!!.lore(ComponentUtils.cleanItalics(lore))
            })
            return book.withType(Material.BARRIER)
        }

        if (shelfPower < reqLevel) {
            lore.add(ComponentUtils.EMPTY)
            lore.add(ComponentUtils.create("Your bookshelf power is not high enough to apply this enchantment!", NamedTextColor.RED))
            lore.add(ComponentUtils.create("Required level: $reqLevel", NamedTextColor.RED))
            book.editMeta(Consumer { meta: ItemMeta? ->
                meta!!.lore(ComponentUtils.cleanItalics(lore))
            })
            return book.withType(Material.BARRIER)
        }

        var i = 0
        val ingredients = recipe.ingredients!!
        if (ingredients.size > 6)
            throw IllegalStateException("Enchanting recipes cannot be longer than 6 ItemStacks.")
        for (ingredient in ingredients) {
            setSlot(INGREDIENT_SLOTS[i], ingredient)
            i++
        }

        // Do we have the required materials to enchant?
        for (ingredient in ingredients) {
            // We need to use the base blueprint for the item, as we adjust the item stack size for display
            val bp = ItemService.blueprint(ingredient)
            val trueIngr = bp.generate()
            bp.updateItemData(trueIngr)
            if (!(player.inventory.containsAtLeast(trueIngr, ingredient.amount))) {
                lore.add(ComponentUtils.EMPTY)
                lore.add(ComponentUtils.create("This enchantment is missing reagents!", NamedTextColor.RED))
                book.editMeta(Consumer { meta: ItemMeta? ->
                    meta!!.lore(ComponentUtils.cleanItalics(lore))
                })
                return book.withType(Material.BARRIER)
            }
        }

        actionButtonState = ActionButtonState.ENABLED
        // Valid item
        lore.add(ComponentUtils.EMPTY)
        lore.add(ComponentUtils.create("Click to enchant this item!", NamedTextColor.GRAY))
        book.editMeta(Consumer { meta: ItemMeta? ->
            meta!!.lore(ComponentUtils.cleanItalics(lore))
            meta.setEnchantmentGlintOverride(true)
        })

        return book.withType(Material.ENCHANTED_BOOK)
    }

    fun generateBookshelfButton() : ItemStack {
        val shelf = getNamedItem(
            Material.BOOKSHELF,
            ComponentUtils.create("Bookshelf Power", NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false)
        )
        val lore: MutableList<Component?> = ArrayList()
        lore.add(ComponentUtils.EMPTY)
        lore.add(ComponentUtils.create("Bookshelf Power increases the maximum potential", NamedTextColor.DARK_GRAY))
        lore.add(ComponentUtils.create("for enchantments from this table. Each Bookshelf", NamedTextColor.DARK_GRAY))
        lore.add(ComponentUtils.create("grants 1 Bookshelf Power.", NamedTextColor.DARK_GRAY))
        lore.add(ComponentUtils.EMPTY)
        lore.add(ComponentUtils.merge(
            ComponentUtils.create("This Enchanting Table currently has ", NamedTextColor.GRAY),
            ComponentUtils.create(shelfPower, NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD),
            ComponentUtils.create(" Bookshelf Power.", NamedTextColor.GRAY)
        ))
        shelf.editMeta(Consumer { meta: ItemMeta? ->
            meta!!.lore(ComponentUtils.cleanItalics(lore))
        })
        return shelf
    }

    fun validateEnchant(itemToEnchant : ItemStack?, scrollItem : ItemStack?) : Boolean {
        if (itemToEnchant == null) {
            return false
        }
        val scrollItem = this.getItem(SCROLL_SLOT)
        if (scrollItem != null) {
            val enchant : Enchantment = getEnchant(scrollItem)!!
            if (enchant.canEnchantItem(itemToEnchant)) {  // canEnchant does not check for conflicts, so we now need to check for that.
                for (ench in itemToEnchant.enchantments.keys) {
                    if (ench.conflictsWith(enchant) && ench.key != enchant.key)  // Failfast if we find a conflicting enchantment
                        return false
                }
                return true
            }
        }
        return false
    }

    fun clearIngredientDisplay() {
        for (slot in INGREDIENT_SLOTS) {
            clearSlot(slot)
        }
    }

    fun doEnchant() : Boolean {
        val scrollItem : ItemStack = this.getItem(SCROLL_SLOT)!!
        val enchant : Enchantment = getEnchant(scrollItem)!!
        val enchantItem : ItemStack = this.getItem(ITEM_SLOT)!!
        val enchantLevel = enchantItem.getEnchantmentLevel(enchant) + 1
        var recipe = SMPRPG.getService(EnchantmentService::class.java).getEnchantment(enchant)?.getRecipe(enchantLevel)
        val ingredients = recipe!!.ingredients!!

        if (player.inventory.takeIfPresent(*ingredients.toTypedArray())) {
            enchantItem.addEnchantment(enchant, enchantItem.getEnchantmentLevel(enchant) + 1)
            scrollItem.amount--
            player.playSound(player.location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1f, 1f)
            ItemService.blueprint(enchantItem).updateItemData(enchantItem)
            generateEnchantButton()
            return true
        }
        return false
    }

    fun getEnchant(scroll : ItemStack?) : Enchantment? {
        if (scroll != null) {
            val enchantId = scroll.persistentDataContainer.getOrDefault(
                DynamicEnchantingScroll.SCROLL_ENCHANT_TYPE_KEY,
                PersistentDataType.STRING, ""
            )
            val enchant: Enchantment = SMPRPG.getService(EnchantmentService::class.java).getEnchantment(
                TypedKey.create(
                    RegistryKey.ENCHANTMENT, enchantId
                )
            )
            return enchant
        }
        return null
    }

    fun render() {
        this.setBorderFull()

        for (slotIdx in INGREDIENT_SLOTS + WHITELISTED_SLOTS) {
            this.clearSlot(slotIdx)
        }
        this.setSlot(ACTION_SLOT, generateEnchantButton())
        this.setSlot(BOOKSHELF_SLOT, generateBookshelfButton())
    }

    fun handleShiftClicks(event : InventoryClickEvent) {
        val fromInv : Inventory
        val toInv : Inventory

        if (event.clickedInventory!!.type == InventoryType.PLAYER) {
            fromInv = player.inventory
            toInv = this.inventory
        }
        else if (event.clickedInventory == this.inventory) {
            fromInv = this.inventory
            toInv = player.inventory

            if (event.slot !in WHITELISTED_SLOTS) {
                event.isCancelled = true
                return
            }
        }
        else
            return

        val clickedItem = event.clickedInventory!!.getItem(event.slot)
        if (clickedItem == null)
            return

        val clickedBp = ItemService.blueprint(clickedItem)
        if (clickedBp is DynamicEnchantingScroll) {
            val scrollItem = this.inventory.getItem(SCROLL_SLOT)
            if (toInv == this.inventory) {
                // Scroll slot is empty, fill it up
                if (scrollItem == null) {
                    toInv.setItem(SCROLL_SLOT, clickedItem)
                    // Remove entire stack since it filled the slot.
                    fromInv.setItem(event.slot, null)
                }
                else {
                    val maxStack = scrollItem.maxStackSize

                    // Scroll is already full, return
                    if (scrollItem.amount == maxStack)
                        return

                    if (scrollItem.isSimilar(clickedItem)) {
                        val maxRemovable = maxStack - scrollItem.amount
                        val removed = clickedItem.amount - maxRemovable
                        scrollItem.amount = maxStack + removed
                    }
                }
            }
        }
        else {
            // Anything else goes into the item slot, as long as there is room.
            if (toInv == this.inventory) {
                if (toInv.getItem(ITEM_SLOT) == null) {
                    val clickedItem = event.clickedInventory!!.getItem(event.slot)
                    if (clickedItem == null)
                        return

                    toInv.setItem(ITEM_SLOT, clickedItem)
                    fromInv.setItem(event.slot, null)
                }
            }
        }
    }

    @EventHandler
    fun onPlayerInventoryChange(event : PlayerInventorySlotChangeEvent) {
        // Reset our icon if our inventory changes
        if ((event.player == player) && actionButtonState == ActionButtonState.ENABLED) {
            generateEnchantButton()
        }
    }
}