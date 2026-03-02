package xyz.devvydont.smprpg.market.storage

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.SMPRPG
import java.io.File

/**
 * Handles JSON file load/save for all market data (auctions + bazaar).
 * Data is stored in plugins/SMPRPG/market/ as human-readable JSON files.
 */
class MarketDataStore(dataFolder: File) {

    private val marketDir = File(dataFolder, MARKET_DIRECTORY)
    private val auctionFile = File(marketDir, AUCTION_FILE_NAME)
    private val bazaarFile = File(marketDir, BAZAAR_FILE_NAME)

    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(ItemStack::class.java, ItemStackSerializer())
        .setPrettyPrinting()
        .create()

    var auctionData = AuctionDataFile()
        private set

    var bazaarData = BazaarDataFile()
        private set

    fun load() {
        marketDir.mkdirs()

        auctionData = loadFile(auctionFile, AuctionDataFile::class.java) ?: AuctionDataFile()
        bazaarData = loadFile(bazaarFile, BazaarDataFile::class.java) ?: BazaarDataFile()

        SMPRPG.plugin.logger.info("Market data loaded (${auctionData.auctions.size} auctions, ${bazaarData.items.size} bazaar items)")
    }

    fun save() {
        marketDir.mkdirs()

        saveFile(auctionFile, auctionData)
        saveFile(bazaarFile, bazaarData)

        SMPRPG.plugin.logger.fine("Market data saved")
    }

    fun saveAuctions() {
        marketDir.mkdirs()
        saveFile(auctionFile, auctionData)
    }

    fun saveBazaar() {
        marketDir.mkdirs()
        saveFile(bazaarFile, bazaarData)
    }

    private fun <T> loadFile(file: File, clazz: Class<T>): T? {
        if (!file.exists()) return null
        return try {
            val json = file.readText()
            gson.fromJson(json, clazz)
        } catch (e: Exception) {
            SMPRPG.plugin.logger.severe("Failed to load market data from ${file.name}: ${e.message}")
            null
        }
    }

    private fun saveFile(file: File, data: Any) {
        try {
            file.writeText(gson.toJson(data))
        } catch (e: Exception) {
            SMPRPG.plugin.logger.severe("Failed to save market data to ${file.name}: ${e.message}")
        }
    }

    companion object {
        private const val MARKET_DIRECTORY = "market"
        private const val AUCTION_FILE_NAME = "auctions.json"
        private const val BAZAAR_FILE_NAME = "bazaar.json"
    }
}
