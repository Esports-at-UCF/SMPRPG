package xyz.devvydont.smprpg.services

import com.sun.net.httpserver.HttpServer
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerResourcePackStatusEvent
import xyz.devvydont.smprpg.SMPRPG.Companion.plugin
import xyz.devvydont.smprpg.util.formatting.ComponentUtils
import xyz.devvydont.smprpg.util.time.TickTime
import java.net.InetSocketAddress
import java.security.MessageDigest

class ResourcePackService : IService, Listener {

    companion object {
        private const val CONFIG_KEY_HOST = "resource-pack-host"
        private const val CONFIG_KEY_PORT = "resource-pack-port"
        private const val DEFAULT_PORT = 8080
        private const val HTTP_PATH = "/resourcepack.zip"
        private const val CONTENT_TYPE_ZIP = "application/zip"
        private const val SHA1_ALGORITHM = "SHA-1"
        private const val CLASSPATH_RESOURCE = "resourcepack.zip"
        private const val HTTP_OK = 200
        private const val HTTP_BACKLOG = 0
        private val SEND_DELAY_TICKS = TickTime.seconds(2)
    }

    private var httpServer: HttpServer? = null
    private var packBytes: ByteArray? = null
    private var packSha1Bytes: ByteArray? = null
    private var packUrl: String? = null
    private var enabled = false

    override fun setup() {
        val config = plugin.config
        val host = config.getString(CONFIG_KEY_HOST, "")!!
        val port = config.getInt(CONFIG_KEY_PORT, DEFAULT_PORT)

        if (host.isBlank()) {
            plugin.logger.warning("ResourcePackService is disabled: '$CONFIG_KEY_HOST' is not set in config.yml")
            return
        }

        val resourceStream = javaClass.classLoader.getResourceAsStream(CLASSPATH_RESOURCE)
        if (resourceStream == null) {
            plugin.logger.severe("ResourcePackService: Could not find $CLASSPATH_RESOURCE on classpath. Was the plugin built correctly?")
            return
        }

        packBytes = resourceStream.use { it.readBytes() }
        packSha1Bytes = MessageDigest.getInstance(SHA1_ALGORITHM).digest(packBytes)
        val sha1Hex = packSha1Bytes!!.joinToString("") { "%02x".format(it) }

        try {
            httpServer = HttpServer.create(InetSocketAddress(port), HTTP_BACKLOG)
        } catch (e: java.io.IOException) {
            plugin.logger.severe("ResourcePackService: Failed to bind HTTP server on port $port — ${e.message}")
            return
        }

        httpServer!!.createContext(HTTP_PATH) { exchange ->
            val bytes = packBytes!!
            exchange.responseHeaders.set("Content-Type", CONTENT_TYPE_ZIP)
            exchange.sendResponseHeaders(HTTP_OK, bytes.size.toLong())
            exchange.responseBody.use { it.write(bytes) }
        }
        httpServer!!.executor = null
        httpServer!!.start()

        packUrl = "http://$host:$port$HTTP_PATH"
        enabled = true

        plugin.logger.info("ResourcePackService: Hosting resource pack at $packUrl (SHA-1: $sha1Hex)")

        for (player in Bukkit.getOnlinePlayers()) {
            sendResourcePack(player)
        }
    }

    override fun cleanup() {
        httpServer?.stop(0)
        httpServer = null
        packBytes = null
        packSha1Bytes = null
        packUrl = null
        enabled = false
    }

    private fun sendResourcePack(player: Player) {
        if (!enabled) return
        player.setResourcePack(packUrl!!, packSha1Bytes!!, Component.text("This server requires a resource pack."), true)
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        if (!enabled) return
        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            if (event.player.isOnline) {
                sendResourcePack(event.player)
            }
        }, SEND_DELAY_TICKS)
    }

    @EventHandler
    fun onResourcePackStatus(event: PlayerResourcePackStatusEvent) {
        if (!enabled) return
        when (event.status) {
            PlayerResourcePackStatusEvent.Status.DECLINED ->
                event.player.kick(ComponentUtils.error("You must accept the resource pack to play on this server."))
            PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD ->
                event.player.kick(ComponentUtils.error("Resource pack download failed. Please try reconnecting."))
            PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED ->
                plugin.logger.fine("${event.player.name} successfully loaded the resource pack.")
            else -> {}
        }
    }
}
