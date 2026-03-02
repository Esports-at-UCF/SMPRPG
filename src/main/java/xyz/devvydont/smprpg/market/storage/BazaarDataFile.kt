package xyz.devvydont.smprpg.market.storage

import xyz.devvydont.smprpg.market.bazaar.BazaarItem

/**
 * Serializable container for all bazaar state.
 * Persisted as JSON via Gson.
 */
data class BazaarDataFile(
    val items: MutableMap<String, BazaarItem> = mutableMapOf()
)
