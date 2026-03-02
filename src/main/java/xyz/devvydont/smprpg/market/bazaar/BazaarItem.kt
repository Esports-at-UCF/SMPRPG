package xyz.devvydont.smprpg.market.bazaar

/**
 * Represents a single bazaar-tradeable item with dynamic pricing based on stock levels.
 * The key is either a CustomItemType key (lowercase enum name) or a Material name.
 * The category is a path-based string (e.g. "Mining/Ores", "Farming").
 */
data class BazaarItem(
    val key: String,
    val displayName: String,
    val category: String,
    val minPrice: Long,
    val maxPrice: Long,
    val maxStock: Int,
    var currentStock: Int,
    val canGoOutOfStock: Boolean = true
)
