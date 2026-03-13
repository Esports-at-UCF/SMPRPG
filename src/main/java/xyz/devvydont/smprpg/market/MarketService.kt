package xyz.devvydont.smprpg.market

import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.scheduler.BukkitRunnable
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.market.auction.AuctionManager
import xyz.devvydont.smprpg.market.bazaar.BazaarManager
import xyz.devvydont.smprpg.market.storage.MarketDataStore
import xyz.devvydont.smprpg.services.IService
import xyz.devvydont.smprpg.util.formatting.ComponentUtils
import xyz.devvydont.smprpg.util.time.TickTime

class MarketService : IService, Listener {

    lateinit var auctionManager: AuctionManager
        private set

    lateinit var bazaarManager: BazaarManager
        private set

    private lateinit var dataStore: MarketDataStore
    private var autoSaveTask: BukkitRunnable? = null
    private var expiryCheckTask: BukkitRunnable? = null

    override fun setup() {
        val plugin = SMPRPG.plugin

        dataStore = MarketDataStore(plugin.dataFolder)
        dataStore.load()

        auctionManager = AuctionManager(dataStore)
        bazaarManager = BazaarManager(dataStore)
        bazaarManager.initializeDefaults()
        bazaarManager.migrateIfNeeded()

        autoSaveTask = object : BukkitRunnable() {
            override fun run() {
                dataStore.save()
            }
        }
        autoSaveTask!!.runTaskTimerAsynchronously(
            plugin,
            TickTime.minutes(MarketConstants.AUTO_SAVE_INTERVAL_MINUTES),
            TickTime.minutes(MarketConstants.AUTO_SAVE_INTERVAL_MINUTES)
        )

        expiryCheckTask = object : BukkitRunnable() {
            override fun run() {
                auctionManager.processExpiredAuctions()
            }
        }
        expiryCheckTask!!.runTaskTimer(
            plugin,
            TickTime.minutes(MarketConstants.AUCTION_EXPIRY_CHECK_INTERVAL_MINUTES),
            TickTime.minutes(MarketConstants.AUCTION_EXPIRY_CHECK_INTERVAL_MINUTES)
        )

        plugin.logger.info("Market service initialized")
    }

    override fun cleanup() {
        autoSaveTask?.cancel()
        expiryCheckTask?.cancel()
        dataStore.save()
        SMPRPG.plugin.logger.info("Market service cleaned up")
    }

    @EventHandler
    @Suppress("unused")
    private fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        val playerUUID = player.uniqueId.toString()

        val cmd = "/ah claim"
        val cmdHoverTooltip = ComponentUtils.merge(
            ComponentUtils.create("Click to run "),
            ComponentUtils.create(cmd, NamedTextColor.GREEN),
            ComponentUtils.create("!"),
        )

        if (auctionManager.hasUnclaimedItems(playerUUID)) {
            val msg = ComponentUtils.merge(
                ComponentUtils.create("You have unclaimed auction items! Use ", NamedTextColor.GOLD),
                ComponentUtils.create(cmd, NamedTextColor.GREEN)
                    .clickEvent(ClickEvent.runCommand(cmd))
                    .hoverEvent(cmdHoverTooltip),
                ComponentUtils.create(" to collect them.", NamedTextColor.GOLD)
            )
            player.sendMessage(ComponentUtils.alert(msg, NamedTextColor.GREEN))
            player.playSound(player.location, Sound.ENTITY_VILLAGER_YES, 1f, 1f)
        }
    }
}
