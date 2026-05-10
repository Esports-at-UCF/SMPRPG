package xyz.devvydont.smprpg.block.entity

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.CustomModelData
import io.papermc.paper.datacomponent.item.TooltipDisplay
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.momirealms.craftengine.bukkit.api.BukkitAdaptors
import net.momirealms.craftengine.bukkit.block.entity.SimpleStorageBlockEntity
import net.momirealms.craftengine.bukkit.item.BukkitItemManager
import net.momirealms.craftengine.core.block.ImmutableBlockState
import net.momirealms.craftengine.core.block.properties.BooleanProperty
import net.momirealms.craftengine.core.world.BlockPos
import net.momirealms.craftengine.core.world.CEWorld
import net.momirealms.craftengine.core.world.Vec3d
import net.momirealms.craftengine.libraries.nbt.CompoundTag
import org.bukkit.*
import org.bukkit.block.data.type.Furnace
import org.bukkit.craftbukkit.CraftWorld
import org.bukkit.craftbukkit.block.CraftBlock
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.SMPRPG.Companion.plugin
import xyz.devvydont.smprpg.events.skills.SkillExperienceGainEvent
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.recipe.cookingpot.CookingPotRecipe
import xyz.devvydont.smprpg.recipe.cookingpot.CookingPotRecipes
import xyz.devvydont.smprpg.services.EntityService
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.extensions.transfer
import xyz.devvydont.smprpg.util.formatting.ComponentUtils
import xyz.devvydont.smprpg.util.time.TickTime
import java.util.UUID
import kotlin.math.max
import net.momirealms.craftengine.core.util.Key as CEKey

@Suppress("UnstableApiUsage")
class CookingPotBlockEntity(val pos: BlockPos, val blockState: ImmutableBlockState) : SimpleStorageBlockEntity(pos, blockState),
    Listener {

    private var ticker = 0    // Generic ticker used for visual effects on the pot when heated.
    private var cookTime = 0  // How long the current item has been cooking for, in ticks
    private var recipeTime = 0  // How long the current item needs to cook for before being finished.
    private var recipe: CookingPotRecipe? = null  // The current working recipe
    private var bordersInitialized = false
    private var speed = 1  // How many ticks should the recipe progress per tick?

    var BORDER: ItemStack? = null
    var heated = false
    var boundPlayer: UUID? = null


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
        type = SMPRPGBlockEntityTypes.COOKING_POT  // We need to override this since we are inheriting from SimpleStorage

        // Check 1 tick after placement/reload if we are on an active heat source
        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            updateHeated()
        }, TickTime.TICK)
    }

    fun updateHeated() {
        val level = (world().world.platformWorld() as CraftWorld).handle
        val bukkitBlock = CraftBlock.at(level, net.minecraft.core.BlockPos(pos.x, pos.y - 1, pos.z))

        // Lit furnaces will progress the pot speed at 4x speed
        if (bukkitBlock.blockData is Furnace) {
            if ((bukkitBlock.blockData as Furnace).isLit) {
                heated = true
                speed = 4
            }
            else {
                heated = false
                speed = 1
            }
            return
        }
        speed = 1
        val blockState = BukkitAdaptors.adapt(bukkitBlock).blockState()
        if (blockState.hasTag(HEAT_SOURCE_TAG))
        {
            heated = true
        }
        else heated = false
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

    override fun saveCustomData(tag: CompoundTag) {
        super.saveCustomData(tag)
        if (boundPlayer != null)
            tag.putUUID("bound_player", boundPlayer!!)
    }

    override fun loadCustomData(tag: CompoundTag) {
        super.loadCustomData(tag)
        boundPlayer = tag.getUUID("bound_player")
    }

    fun addXpReward() {
        Bukkit.getScheduler().runTask(plugin, Runnable {
            if (boundPlayer == null) return@Runnable
            val player = Bukkit.getPlayer(boundPlayer!!)
            if (player != null) {
                val leveledPlayer = SMPRPG.getService(EntityService::class.java).getPlayerInstance(player)
                if (recipe!!.skillXpReward != null) recipe!!.skillXpReward!!.apply(leveledPlayer, SkillExperienceGainEvent.ExperienceSource.COOK)
            }
        })
    }

    companion object {
        val HEAT_SOURCE_TAG: CEKey = CEKey.of("smprpg:cooking_pot_heat_sources")
        //val ACTIVE_HEAT_SOURCES = listOf()

        val INGREDIENT_SLOTS = listOf(1, 2, 3, 10, 11, 12)
        const val OUTPUT_SLOT = 15
        const val FLAME_SLOT = 20
        const val ARROW_SLOT = 13
        const val PLATING_SLOT = 33
        val PLATING_ITEMS = listOf(  // Hacky as hell, TODO: Make this adapative
            ItemService.generate(Material.BOWL),
            ItemService.generate(Material.GLASS_BOTTLE),
            ItemService.generate(CustomItemType.CERAMIC_PLATE)
        )
        val ALLOWED_SLOTS = listOf(OUTPUT_SLOT, PLATING_SLOT) + INGREDIENT_SLOTS

        fun tick(ceWorld: CEWorld, blockPos: BlockPos, state: ImmutableBlockState, pot: CookingPotBlockEntity) {
            if (!pot.bordersInitialized) {
                for (i in 0..35) {
                    if (i !in ALLOWED_SLOTS) {
                        pot.inventory().setItem(i, pot.BORDER)
                        if (i == FLAME_SLOT)
                            pot.inventory().getItem(i)!!.setData(DataComponentTypes.ITEM_MODEL, NamespacedKey(plugin, "ui/pot_flame"))
                        if (i == ARROW_SLOT)
                            pot.inventory().getItem(i)!!.setData(DataComponentTypes.ITEM_MODEL, NamespacedKey(plugin, "ui/progress_arrow"))
                    }
                }
                pot.bordersInitialized = true
            }

            val bukkitWorld = ceWorld.world.platformWorld() as World
            if (pot.heated) {
                pot.ticker++
                if (pot.ticker % 4 == 0) {
                    bukkitWorld.spawnParticle(
                        Particle.BUBBLE,
                        Location(bukkitWorld, blockPos.x + 0.5, blockPos.y + 0.625, blockPos.z + 0.5),
                        3,
                        0.0625,
                        0.0,
                        0.0625,
                        0.5
                    )
                    bukkitWorld.spawnParticle(
                        Particle.BUBBLE_POP,
                        Location(bukkitWorld, blockPos.x + 0.5, blockPos.y + 0.625, blockPos.z + 0.5),
                        1,
                        0.0625,
                        0.0,
                        0.0625,
                        0.05
                    )
                }
                if (pot.ticker % 40 == 0) {
                    pot.ticker = 0
                    Bukkit.getScheduler().runTask(plugin, Runnable {
                        bukkitWorld.playSound(Location(bukkitWorld, blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble()),
                            Sound.BLOCK_BUBBLE_COLUMN_UPWARDS_AMBIENT,
                            0.5f,
                            2.0f)
                    })
                }
            }

            val inv = pot.inventory()
            var hasIngredient = false
            for (slot in INGREDIENT_SLOTS) {
                val ing = inv.getItem(slot) ?: continue
                if (!ing.isEmpty) {
                    hasIngredient = true
                    break
                }
            }

            val currResult = inv.getItem(OUTPUT_SLOT)
            if (!hasIngredient || ((currResult != null) && currResult.amount >= currResult.maxStackSize)) {
                pot.recipeTime = 0
                pot.recipe = null
            }
            else if (pot.heated) {
                pot.recipe = CookingPotRecipe.getFirstRecipeMatch(pot)
                if (pot.recipe != null) {
                    pot.recipeTime = pot.recipe!!.cookTime
                    pot.cookTime += pot.speed
                }
                else {
                    pot.cookTime = max(pot.cookTime - 2, 0)
                }


                if ((pot.cookTime > pot.recipeTime) && (pot.recipe != null)) {
                    pot.cookTime = 0
                    val result = inv.getItem(OUTPUT_SLOT)
                    // Add to our output
                    if (result == null) {
                        inv.setItem(OUTPUT_SLOT, pot.recipe!!.result)
                        CookingPotRecipe.takeIngredients(pot, pot.recipe!!)
                        pot.addXpReward()
                    }
                    else if (result.isSimilar(pot.recipe!!.recipeResult) && result.amount < result.maxStackSize) {
                        result.add()
                        CookingPotRecipe.takeIngredients(pot, pot.recipe!!)
                        pot.addXpReward()
                    }

                    val newOutput = inv.getItem(OUTPUT_SLOT)!!
                    if (newOutput.amount >= newOutput.maxStackSize) {
                        pot.recipeTime = 0
                        pot.recipe = null
                    }
                }
            }
            else {
                pot.cookTime = max(pot.cookTime - 2, 0)
            }
            if (pot.heated) inv.getItem(FLAME_SLOT)!!.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData().addString("on").build())
            else inv.getItem(FLAME_SLOT)!!.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData().addString("off").build())
            var progressRatio = 0.0f
            if (pot.recipeTime != 0)
                progressRatio = (pot.cookTime / pot.recipeTime.toFloat())
            inv.getItem(ARROW_SLOT)!!.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData().addFloat(progressRatio).build())
        }
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (event.isShiftClick) {
            val inv = event.inventory
            // We only care about custom filtering for shift clicking INTO the pot
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

                // First, try to fill plating items into the plating slot
                for (usablePlate in PLATING_ITEMS) {
                    if (clickedItem.isSimilar(usablePlate)) {
                        val platingItem = inv.getItem(PLATING_SLOT)

                        // No item here, move the entire stack over
                        if (platingItem == null) {
                            inv.setItem(PLATING_SLOT, clickedItem.clone())
                            clickedItem.amount = 0
                        } else if (platingItem.isSimilar(clickedItem)) {
                            val amtToTake = platingItem.maxStackSize - platingItem.amount
                            clickedItem.transfer(amtToTake, platingItem)
                        }
                        return  // Return here so we don't let overflow spill into ingredient slot
                    }
                }

                // Next, try to move items to the input slots
                for (slot in INGREDIENT_SLOTS) {
                    val ingItem = inv.getItem(slot)

                    // No item here, move the entire stack over
                    if (ingItem == null) {
                        inv.setItem(slot, clickedItem.clone())
                        clickedItem.amount = 0
                    } else if (ingItem.isSimilar(clickedItem)) {
                        val amtToTake = ingItem.maxStackSize - ingItem.amount
                        clickedItem.transfer(amtToTake, ingItem)
                    }
                }
            }
        }
        else {
            val inv = event.clickedInventory ?: return
            if (event.inventory == inventory() && event.clickedInventory == inventory()) {
                if (event.slot !in ALLOWED_SLOTS) {
                    event.isCancelled = true
                    return
                }

                val clickedItem = event.currentItem
                val cursorItem = event.cursor

                when (event.slot) {
                    PLATING_SLOT -> {
                        if (!cursorItem.isEmpty) {
                            for (plate in PLATING_ITEMS) {
                                if (!cursorItem.isSimilar(plate)) {
                                    event.isCancelled = true
                                }
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

    // Bind a player to the inventory when opened.
    @EventHandler
    fun onInventoryOpen(event: InventoryOpenEvent) {
        if (event.inventory == inventory()) {
            if (boundPlayer == null) {
                boundPlayer = event.player.uniqueId
                event.player.sendMessage(ComponentUtils.success(ComponentUtils.create("This cooking pot has been bound to you for skill XP gain.", NamedTextColor.GRAY)))
            }
        }
    }
}