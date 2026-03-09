package xyz.devvydont.smprpg.util.extensions

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

fun Inventory.takeIfPresent(vararg req: Pair<ItemStack, Int>): Boolean {

    if (req.isEmpty()) return true

    val remaining = req.associate { it.first to it.second }.toMutableMap()

    // PASS 1 — verify we have enough items
    for (stack in contents) {
        if (stack == null) continue

        for ((target, count) in remaining) {
            if (count <= 0) continue
            if (!stack.isSimilar(target)) continue

            val newRemaining = count - stack.amount
            remaining[target] = maxOf(0, newRemaining)
        }
    }

    if (remaining.values.any { it > 0 })
        return false

    // PASS 2 — remove items
    val toRemove = req.associate { it.first to it.second }.toMutableMap()

    for (stack in contents) {
        if (stack == null) continue

        for ((target, count) in toRemove) {
            if (count <= 0) continue
            if (!stack.isSimilar(target)) continue

            val taken = minOf(stack.amount, count)
            stack.amount -= taken
            toRemove[target] = count - taken
        }
    }

    return true
}

/**
 * Given a list of ItemStacks, use their amounts as the take value
 * for the item type.
 */
fun Inventory.takeIfPresent(vararg req : ItemStack) : Boolean {
    var itemsToTake = ArrayList<Pair<ItemStack, Int>>()
    for (i in req) {
        itemsToTake.add(Pair(i, i.amount))
    }
    return takeIfPresent(*itemsToTake.toTypedArray())
}