package xyz.devvydont.smprpg.block.entity

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.CustomModelData
import io.papermc.paper.datacomponent.item.TooltipDisplay
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.format.TextDecoration
import net.momirealms.craftengine.bukkit.block.entity.SimpleStorageBlockEntity
import net.momirealms.craftengine.bukkit.item.BukkitItemManager
import net.momirealms.craftengine.bukkit.util.LocationUtils
import net.momirealms.craftengine.core.block.ImmutableBlockState
import net.momirealms.craftengine.core.world.BlockPos
import net.momirealms.craftengine.core.world.CEWorld
import net.momirealms.craftengine.core.world.Vec3d
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryMoveItemEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import xyz.devvydont.smprpg.SMPRPG.Companion.plugin
import xyz.devvydont.smprpg.recipe.freezer.FreezerRecipe
import xyz.devvydont.smprpg.recipe.freezer.FreezerRecipes
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.extensions.transfer
import xyz.devvydont.smprpg.util.formatting.ComponentUtils
import xyz.devvydont.smprpg.util.time.TickTime
import kotlin.math.max

class FreezerBlockEntity(val pos: BlockPos, val blockState: ImmutableBlockState) : SimpleStorageBlockEntity(pos, blockState),
    Listener {

    private var fuelTime = 0    // How many ticks are left of fuel
    private var fuelSupply = 0  // How many ticks the most recent freezer fuel gave for burn
    private var freezeTime = 0  // How long the current item has been freezing for, in ticks
    private var recipeTime = 0  // How long the current item needs to freeze for before being finished.
    private var recipe: FreezerRecipe? = null  // The current working recipe
    private var bordersInitialized = false

    var BORDER: ItemStack? = null

    init {
        plugin.server.pluginManager.registerEvents(this, plugin)

        val border = ItemStack(Material.BLACK_STAINED_GLASS_PANE)
        val meta = border.itemMeta
        meta.displayName(ComponentUtils.EMPTY.decoration(TextDecoration.ITALIC, false))
        border.setItemMeta(meta)
        border.setData(DataComponentTypes.ITEM_MODEL, Key.key("smprpg:empty"))
        border.setData(DataComponentTypes.TOOLTIP_DISPLAY,
            TooltipDisplay.tooltipDisplay().hideTooltip(true).build())

        BORDER = border
    }

    override fun updateOpenBlockState(open: Boolean) {
        return
    }

    override fun preRemove() {
        inventory().close()
        val pos = Vec3d.atCenterOf(this.pos)
        for (idx in ALLOWED_SLOTS) {
            val stack = inventory().getItem(idx)
            if (stack != null) {
                super.world.world().dropItemNaturally(pos, BukkitItemManager.instance().wrap(stack))
            }
        }
        inventory().clear()
        HandlerList.unregisterAll(this)
    }

    fun isLit() : Boolean {
        return fuelTime > 0
    }

    companion object {
        const val INGREDIENT_SLOT = 3
        const val FUEL_SLOT = 21
        const val OUTPUT_SLOT = 15
        const val FLAME_SLOT = 12
        const val ARROW_SLOT = 13
        val ALLOWED_SLOTS = listOf(INGREDIENT_SLOT, FUEL_SLOT, OUTPUT_SLOT)

        fun tick(ceWorld: CEWorld, blockPos: BlockPos, state: ImmutableBlockState, freezer: FreezerBlockEntity) {
            if (!freezer.bordersInitialized) {
                for (i in 0..26) {
                    if (i !in ALLOWED_SLOTS) {
                        freezer.inventory().setItem(i, freezer.BORDER)
                        if (i == FLAME_SLOT)
                            freezer.inventory().getItem(i)!!.setData(DataComponentTypes.ITEM_MODEL, NamespacedKey(plugin, "ui/freezer_burn_flame"))
                        if (i == ARROW_SLOT)
                            freezer.inventory().getItem(i)!!.setData(DataComponentTypes.ITEM_MODEL, NamespacedKey(plugin, "ui/progress_arrow_freezer"))
                    }
                }
                freezer.bordersInitialized = true
            }

            val bukkitWorld = ceWorld.world.platformWorld() as World
            if (freezer.isLit()) {
                freezer.fuelTime--
                bukkitWorld.spawnParticle(
                    Particle.SNOWFLAKE,
                    Location(bukkitWorld, blockPos.x + 0.5, blockPos.y + 1.0, blockPos.z + 0.5),
                    3,
                    0.15,
                    0.0,
                    0.15,
                    0.01)
                if (freezer.fuelTime % 40 == 0) {
                    Bukkit.getScheduler().runTask(plugin, Runnable {
                        bukkitWorld.playSound(Location(bukkitWorld, blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble()),
                            Sound.ENTITY_BREEZE_IDLE_GROUND,
                            0.5f,
                            0.5f)
                    })
                }
            }

            val inv = freezer.inventory()
            val hasIngredient = inv.getItem(INGREDIENT_SLOT) != null
            val fuel = inv.getItem(FUEL_SLOT)
            var fuelTime = 0
            val hasFuel = fuel != null
            if (hasFuel)
                fuelTime = fuel.persistentDataContainer.getOrDefault(ItemService.FREEZER_FUEL_KEY, PersistentDataType.INTEGER, 0)

            if (!hasIngredient) {
                freezer.recipeTime = 0
                freezer.recipe = null
            }

            if (freezer.isLit() || hasFuel && hasIngredient) {
                val ingredient = inv.getItem(INGREDIENT_SLOT)
                if (freezer.recipeTime == 0) {
                    for (entry in FreezerRecipes.entries) {
                        if (entry.recipe.input.isSimilar(ingredient)) {
                            freezer.recipe = entry.recipe
                            val result = inv.getItem(OUTPUT_SLOT)
                            if (result == null || result.isSimilar(freezer.recipe!!.result))
                                freezer.recipeTime = entry.recipe.freezeTime
                            break
                        }
                    }
                }

                val fuel = inv.getItem(FUEL_SLOT)
                if (freezer.recipeTime != 0) {
                    if (!freezer.isLit() && fuelTime > 0) {
                        freezer.fuelTime += fuelTime
                        freezer.fuelSupply = fuelTime
                        fuel!!.amount--
                    }
                    freezer.freezeTime++
                }
                else {
                    freezer.freezeTime = max(freezer.freezeTime--, 0)
                }

                if ((freezer.freezeTime > freezer.recipeTime) && (freezer.recipe != null)) {
                    ingredient?.amount--  // Remove from our input
                    freezer.freezeTime = 0
                    val result = inv.getItem(OUTPUT_SLOT)
                    // Add to our output
                    if (result != null)
                        result.add()
                    else
                        inv.setItem(OUTPUT_SLOT, freezer.recipe!!.result)
                }
            }
            else {
                freezer.freezeTime = max(freezer.freezeTime--, 0)
            }
            var fuelRatio = 0.0f
            if (freezer.fuelSupply != 0)
                fuelRatio = (freezer.fuelTime / freezer.fuelSupply.toFloat())
            inv.getItem(FLAME_SLOT)!!.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData().addFloat(fuelRatio).build())
            var progressRatio = 0.0f
            if (freezer.recipeTime != 0)
                progressRatio = (freezer.freezeTime / freezer.recipeTime.toFloat())
            inv.getItem(ARROW_SLOT)!!.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData().addFloat(progressRatio).build())
        }
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (event.isShiftClick) {
            val inv = event.inventory
            // We only care about custom filtering for shift clicking INTO the freezer
            if (inv == inventory()) {
                if (event.clickedInventory != null) {
                    if (event.clickedInventory!!.type != InventoryType.PLAYER) {
                        if (event.slot !in ALLOWED_SLOTS)
                            event.isCancelled = true
                        return
                    }
                }
                event.isCancelled = true  // We aren't trusting vanilla for this one
                val clickedItem = event.currentItem
                if (clickedItem == null) return

                // First, try to fill fuel items into the fuel slot
                if (clickedItem.persistentDataContainer.getOrDefault(ItemService.FREEZER_FUEL_KEY, PersistentDataType.INTEGER, 0) != 0) {
                    val fuelItem = inv.getItem(FUEL_SLOT)

                    // No item here, move the entire stack over
                    if (fuelItem == null) {
                        inv.setItem(FUEL_SLOT, clickedItem.clone())
                        clickedItem.amount = 0
                    }
                    else if (fuelItem.isSimilar(clickedItem)) {
                        val amtToTake = fuelItem.maxStackSize - fuelItem.amount
                        clickedItem.transfer(amtToTake, fuelItem)
                    }
                    return  // Return here so we don't let overflow spill into ingredient slot
                }

                // Next, try to move items to the input slot
                val ingItem = inv.getItem(INGREDIENT_SLOT)

                // No item here, move the entire stack over
                if (ingItem == null) {
                    inv.setItem(INGREDIENT_SLOT, clickedItem.clone())
                    clickedItem.amount = 0
                }
                else if (ingItem.isSimilar(clickedItem)) {
                    val amtToTake = ingItem.maxStackSize - ingItem.amount
                    clickedItem.transfer(amtToTake, ingItem)
                }
            }
        }
        else {
            val inv = event.clickedInventory
            if (inv == inventory()) {
                if (event.slot !in ALLOWED_SLOTS) {
                    event.isCancelled = true
                    return
                }

                val clickedItem = event.currentItem
                val cursorItem = event.cursor

                when (event.slot) {
                    FUEL_SLOT -> {
                        if (!cursorItem.isEmpty) {
                            if (cursorItem.persistentDataContainer.getOrDefault(ItemService.FREEZER_FUEL_KEY, PersistentDataType.INTEGER, 0) == 0) {
                                event.isCancelled = true
                            }
                        }
                    }

                    OUTPUT_SLOT -> {
                        val output = inv.getItem(OUTPUT_SLOT)
                        event.isCancelled = true
                        if (cursorItem.isEmpty || cursorItem.isSimilar(output)) {
                            // If cursor is empty, use vanilla logic and just take the item
                            if (cursorItem.isEmpty) {
                                event.isCancelled = false
                            } else {
                                val amtToTake = cursorItem.maxStackSize - cursorItem.amount
                                output!!.transfer(amtToTake, cursorItem)
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    fun onHopperTransfer(event: InventoryMoveItemEvent) {
        val inv = inventory()
        if (event.source == inv || event.destination == inv) {
            event.isCancelled = true  // We are just gonna do our own hopper logic so that this won't be absolute hell.
            val hopperPos = LocationUtils.toBlockPos(event.initiator.location)

            // Extracting output
            if (pos.below() == hopperPos) {
                if (event.initiator.firstEmpty() == -1) return  // No-op when the hopper is full
                val outputItem = inv.getItem(OUTPUT_SLOT) ?: ItemStack.empty()
                val transferItem = outputItem.clone()
                if (!outputItem.isEmpty) {
                    transferItem.amount = 1
                    outputItem.amount--
                }
                val itemsDidntFit = event.destination.addItem(transferItem)
                for (item in itemsDidntFit) {
                    event.initiator.addItem(item.value)
                }
                return
            }

            Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                // Input
                if (pos.above() == hopperPos) {
                    var itemToMove = ItemStack.empty()
                    for (stack in event.source) {
                        if (stack != null) {
                            itemToMove = stack
                            break
                        }
                    }
                    if (itemToMove.isEmpty) return@Runnable

                    val ingItem = inv.getItem(INGREDIENT_SLOT) ?: ItemStack.empty()
                    if (ingItem.isSimilar(itemToMove) || ingItem.isEmpty) {
                        if (ingItem.isEmpty) {
                            val initItem = itemToMove.clone()
                            initItem.amount = 1
                            inv.setItem(INGREDIENT_SLOT, initItem)
                            itemToMove.amount--
                        } else if (ingItem.amount < ingItem.maxStackSize) {
                            ingItem.amount++
                            itemToMove.amount--
                        }
                    }
                    return@Runnable
                }

                // Fuel
                val sides = listOf(pos.west(), pos.east(), pos.north(), pos.south())
                if (hopperPos in sides) {
                    var itemToMove = ItemStack.empty()
                    for (stack in event.source) {
                        if (stack != null) {
                            if (stack.persistentDataContainer.getOrDefault(ItemService.FREEZER_FUEL_KEY, PersistentDataType.INTEGER, 0) != 0) {
                                itemToMove = stack
                                break
                            }
                        }
                    }
                    if (itemToMove.isEmpty) return@Runnable

                    val fuelItem = inv.getItem(FUEL_SLOT) ?: ItemStack.empty()
                    if (fuelItem.isSimilar(itemToMove) || fuelItem.isEmpty) {
                        if (fuelItem.isEmpty) {
                            val initItem = itemToMove.clone()
                            initItem.amount = 1
                            inv.setItem(FUEL_SLOT, initItem)
                            itemToMove.amount--
                        } else if (fuelItem.amount < fuelItem.maxStackSize) {
                            fuelItem.amount++
                            itemToMove.amount--
                        }
                    }
                }
            }, TickTime.TICK)
        }
    }
}