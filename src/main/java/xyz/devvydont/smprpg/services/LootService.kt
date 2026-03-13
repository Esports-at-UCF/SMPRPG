package xyz.devvydont.smprpg.services

import com.destroystokyo.paper.ParticleBuilder
import com.destroystokyo.paper.event.block.BlockDestroyEvent
import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.block.BlockState
import org.bukkit.block.Chest
import org.bukkit.block.TileState
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.minecart.StorageMinecart
import org.bukkit.event.Cancellable
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockExplodeEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.loot.LootContext
import org.bukkit.loot.LootTable
import org.bukkit.loot.LootTables
import org.bukkit.persistence.PersistentDataHolder
import org.bukkit.persistence.PersistentDataType
import xyz.devvydont.smprpg.SMPRPG.Companion.plugin
import xyz.devvydont.smprpg.util.formatting.ComponentUtils
import java.util.*

/**
 * Responsible for specifically chest looting events. Allows chest loot to be per player, as well as restock
 * for players on tweakable timeframes. Also, responsible for making sure that loot chests are indestructible.
 */
class LootService : IService, Listener {

    class LootInventoryContext(val block: Block?, val entity: Entity?, val inventory: Inventory)

    /**
     * How long in milliseconds chests should restock per player.
     */
    val RESTOCK_COOLDOWN: Long = 24 * 60 * 60 * 1000

    val rng = Random()
    val lootContainerViewers = HashMap<UUID, LootInventoryContext>()

    /**
     * Set up the service. When this method executes, all other services will be instantiated, making SMPRPG.getService()
     * calls safe to run. Run any initialization code that wasn't fit at construction time.
     * @exception RuntimeException Thrown when the service was unable to startup.
     */
    override fun setup() {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    /**
     * Clean up the service. Run any code that this required for graceful cleanup of this service.
     */
    override fun cleanup() {
        HandlerList.unregisterAll(this)
    }

    /**
     * Formats the message meant for the title of the loot inventory.
     */
    private fun formatRestockMessage(timeDiff: Long): Component {

        // Did we just open this? We can tell because the time diff will be negative.
        if (timeDiff < 0)
            return Component.text("Loot")

        val prefix = Component.text("Restock timer: ")

        // Hours left?
        if (timeDiff > (1000 * 60 * 60 * 2))
            return prefix.append(ComponentUtils.create("${timeDiff/(60*60*1000)}h", NamedTextColor.RED))

        // Minutes left?
        if (timeDiff > (1000 * 60))
            return prefix.append(ComponentUtils.create("${timeDiff/(60*1000)}m", NamedTextColor.YELLOW))

        // Very soon?
        return prefix.append(ComponentUtils.create("VERY SOON!", NamedTextColor.GREEN))
    }

    /**
     * The key used for player loot storage in a loot container.
     */
    private fun getPlayerInventoryStorageKey(player: Player): NamespacedKey {
        return NamespacedKey(plugin, "${player.uniqueId}-loot")
    }

    /**
     * The key used for player timestamp storage in a loot container.
     */
    private fun getPlayerTimestampKey(player: Player): NamespacedKey {
        return NamespacedKey(plugin, "${player.uniqueId}-time")
    }

    /**
     * Takes the inventory of a loot container and serializes it to a PDC friendly data structure.
     */
    private fun serializeItemCollection(inventory: Inventory): ByteArray {
        val items = ArrayList<ItemStack>()
        for (level in 0 until inventory.size) {
            var item = inventory.getItem(level)
            if (item == null)
                item = ItemStack(Material.AIR)
            items.add(item)
        }
        return ItemStack.serializeItemsAsBytes(items)
    }

    /**
     * Transforms a serialized collection of items into a list of items that can be used to populate an inventory.
     * This method will return null if something goes wrong in the deserialization process, in which a server admin
     * should check the traceback in the logs.
     */
    private fun deserializeItemCollection(bytes: ByteArray): List<ItemStack>? {
        try {
            return ItemStack.deserializeItemsFromBytes(bytes).toList()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * Spawns particles at a given location. Used to signify that a chest was protected for any reason.
     */
    private fun spawnParticlesAroundChest(location: Location) {
        ParticleBuilder(Particle.END_ROD)
            .location(location.toCenterLocation())
            .count(50)
            .offset(.35, .25, .35)
            .extra(0.0)
            .spawn()
    }

    private fun handleLootableInteraction(event: Cancellable, player: Player, holder: PersistentDataHolder) {
        // At this point we know that the player is trying to open a loot chest.
        event.isCancelled = true

        if (holder is Chest)
            holder.open()

        val now = System.currentTimeMillis()
        val lastLooted = holder.persistentDataContainer.getOrDefault(getPlayerTimestampKey(player),
            PersistentDataType.LONG, 0)
        var timeDiff = now
        if (lastLooted != null)
            timeDiff = now - lastLooted
        val restocksIn = RESTOCK_COOLDOWN - timeDiff

        val key = getPlayerInventoryStorageKey(player)

        val popupInventory = Bukkit.createInventory(player, 27, formatRestockMessage(restocksIn))
        val playerLoot = holder.persistentDataContainer.get(key, PersistentDataType.BYTE_ARRAY)

        // Loot needs to be regenerated.
        var loc = player.location
        var table: LootTable = LootTables.BAT.lootTable  // Works as a "dummy" loot table.
        var entity: Entity? = null
        var block: Block? = null
        if (holder is Chest) {
            loc = holder.location
            block = holder.block
            if (holder.lootTable != null)
                table = holder.lootTable!!
        }
        if (holder is Entity) {
            loc = holder.location
            entity = holder
            if (holder is StorageMinecart && holder.lootTable != null)
                table = holder.lootTable!!
        }
        val ctx = LootContext.Builder(loc).build()

        // Handle the easy case where we only need to read already present items.
        if (playerLoot != null && restocksIn > 0) {
            val items = deserializeItemCollection(playerLoot)
            if (items == null) {
                player.sendMessage(ComponentUtils.error("Something went wrong trying to deserialize the collection of items for this LootContainer. Tell a server admin to check the logs!"))
                return
            }
            popupInventory.clear()
            for (itemIndex in 0 until items.size) {
                popupInventory.setItem(itemIndex, items[itemIndex])
            }
            player.openInventory(popupInventory)
            lootContainerViewers[player.uniqueId] = LootInventoryContext(block, entity, popupInventory)
            return
        }

        table.fillInventory(popupInventory, rng, ctx)
        holder.persistentDataContainer.set(key, PersistentDataType.BYTE_ARRAY, serializeItemCollection(popupInventory))
        holder.persistentDataContainer.set(getPlayerTimestampKey(player), PersistentDataType.LONG, now)

        if (holder is BlockState)
            holder.update()

        player.openInventory(popupInventory)
        lootContainerViewers[player.uniqueId] = LootInventoryContext(block, entity, popupInventory)
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private fun onInteractWithMinecartLoot(event: PlayerInteractEntityEvent) {

        val entity = event.rightClicked
        if (entity !is StorageMinecart)
            return

        handleLootableInteraction(event, event.player, entity)
    }

    /**
     * When a player interacts with a loot chest, we need to completely override the behavior of opening and viewing
     * the chest contents as if it were a vanilla interaction. The reason we do this rather than utilizing Paper's
     * built in refillable loot system is so we can instance loot to be per player.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private fun onInteractWithLootChest(event: PlayerInteractEvent) {

        // Filter out this event so that we only listen to right clicks on chests that are tagged as loot containers.
        if (event.action != Action.RIGHT_CLICK_BLOCK)
            return

        val block = event.clickedBlock!!
        val state = block.state

        if (state !is Chest)
            return

        if (!state.hasLootTable())
            return

        handleLootableInteraction(event, event.player, state)
    }

    /**
     * When a player closes a custom inventory that is tied to the loot for a chest, we need to update the state of
     * the inventory to apply to the chest so that if the player were to re-open the loot chest it is just as they left
     * it. We also listen for when we should close the chest here as well.
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private fun onPlayerCloseLootInventory(event: InventoryCloseEvent) {

        val player = event.player as Player

        // Grab the loot context of the player. If it doesn't exist, or there's an inventory mismatch, this event
        // is most likely not for us.
        val ctx = lootContainerViewers[player.uniqueId]
            ?: return

        if (ctx.inventory != event.inventory)
            return

        // Resolve the holder so we can update its relative data.
        var holder: PersistentDataHolder? = null
        if (ctx.block != null && ctx.block.state is TileState)
            holder = ctx.block.state as TileState
        else if (ctx.entity != null)
            holder = ctx.entity

        if (holder == null)
            return

        // Update the loot to the container so the player can review it later.
        holder.persistentDataContainer.set(getPlayerInventoryStorageKey(player), PersistentDataType.BYTE_ARRAY, serializeItemCollection(ctx.inventory))

        if (holder is TileState)
            holder.update()

        lootContainerViewers.remove(player.uniqueId)

        // This section is specific to chests to get the lid opening/closing to function.
        if (holder !is Chest)
            return

        // Check everybody viewing loot containers. If nobody is viewing this location, we can close the chest.
        var viewing = false
        for (entry in lootContainerViewers.entries)
            if (entry.value.block == ctx.block)
                viewing = true

        if (!viewing)
            holder.close()
    }

    /**
     * Completely cancel any sort of player breaking block event on chests that are considered lootable.
     * We should however still allow players in creative mode to bypass this just in case.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    private fun onPlayerAttemptBreakLootBlock(event: BlockBreakEvent) {

        val player = event.player
        val state = event.block.state

        if (state !is Chest)
            return

        if (!state.hasLootTable())
            return

        if (player.gameMode.isInvulnerable)
            return

        if (event.isCancelled)
            return

        event.player.sendMessage(ComponentUtils.error("You can't break lootable chests!"))
        event.player.playSound(event.player.location, Sound.ENTITY_ITEM_BREAK, 1f, .5f)
        spawnParticlesAroundChest(event.block.location)
        event.isCancelled = true
    }

    /**
     * If a block explodes for whatever reason, we should make sure any loot chests are protected.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    private fun onLootBlockExplode(event: BlockExplodeEvent) {

        val blocksToSave = mutableListOf<Block>()

        // Save any blocks that are chests with loot tables.
        for (block in event.blockList()) {
            val state = block.state
            if (state is Chest && state.hasLootTable())
                blocksToSave.add(block)
        }

        // Remove the blocks.
        event.blockList().removeAll(blocksToSave)
        for (block in blocksToSave)
            spawnParticlesAroundChest(block.location)
    }

    /**
     * If an entity explodes for whatever reason, we should make sure any loot chests are protected.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    private fun onLootBlockExplode(event: EntityExplodeEvent) {

        val blocksToSave = mutableListOf<Block>()

        // Save any blocks that are chests with loot tables.
        for (block in event.blockList()) {
            val state = block.state
            if (state is Chest && state.hasLootTable())
                blocksToSave.add(block)
        }

        // Remove the blocks.
        event.blockList().removeAll(blocksToSave)
        for (block in blocksToSave)
            spawnParticlesAroundChest(block.location)
    }

    /**
     * If a loot chest tries to break naturally for whatever reason, we should make sure it is protected.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    private fun onLootBlockBreakNaturally(event: BlockDestroyEvent) {

        val state = event.block.state

        if (state !is Chest)
            return

        if (!state.hasLootTable())
            return

        event.isCancelled = true
        spawnParticlesAroundChest(event.block.location)
    }

    /**
     * Storage minecrafts w/ loot tables cannot die.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    private fun onLootMinecraftAddToWorld(event: EntityAddToWorldEvent) {
        val entity = event.entity
        if (entity !is StorageMinecart)
            return

        if (!entity.hasLootTable())
            return

        entity.isInvulnerable = true
    }
}