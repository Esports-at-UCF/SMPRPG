package xyz.devvydont.smprpg.util.items

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.inventory.meta.Repairable
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.enchantments.CustomEnchantment
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemEnchantedBook
import xyz.devvydont.smprpg.services.EnchantmentService
import xyz.devvydont.smprpg.services.ItemService

@JvmRecord
data class EnchantmentCombination(val result: ItemStack, val cost: Int)

/**
 * Combines the enchantments of a 2nd item stack on to the first. Respects limits such as ItemRarity as well as
 * returns a cost to use if this was an anvil event.
 * @return A combination result from two inputs.
 */
fun combineEnchantments(input: ItemStack, combine: ItemStack): EnchantmentCombination {
    // Retrieve the blueprints of both items

    val inputBlueprint = SMPRPG.getService(ItemService::class.java).getBlueprint(input)
    val combineBlueprint = SMPRPG.getService(ItemService::class.java).getBlueprint(combine)

    // Edge case, do we have two different types of enchanted books? We can't do this
    if (inputBlueprint is ItemEnchantedBook && combineBlueprint is ItemEnchantedBook) {
        val inputEnchantment = inputBlueprint.getEnchantment(input)
        val combineEnchantment = combineBlueprint.getEnchantment(combine)
        if (inputEnchantment != null && inputEnchantment != combineEnchantment) return EnchantmentCombination(
            input,
            0
        )
    }

    // Grab the enchantments we are trying to apply to the first item
    val toApply: MutableList<CustomEnchantment> = ArrayList()
    // If the item is a book, we have to retrieve the enchants differently
    val combineEnchantMap =
        if
                (!combineBlueprint.isCustom && combine.type == Material.ENCHANTED_BOOK) (combine.itemMeta as EnchantmentStorageMeta).storedEnchants
        else
            combine.enchantments

    // Transform vanilla enchants to our enchant wrapper
    for (entry in combineEnchantMap.entries) toApply.add(
        SMPRPG.getService(EnchantmentService::class.java).getEnchantment(entry.key)!!
            .build(entry.value!!)
    )

    // Now we actually do the enchantment application. First, we need to figure out how many new enchants
    // this item is allowed to have so it doesn't go over its limit.
    var newEnchantmentSlots = inputBlueprint.getMaxAllowedEnchantments(input) - input.enchantments.size

    // Attempt to apply all the enchantments from the 2nd item.
    var cost = 0
    val result = input.clone()
    toApply.shuffle()
    var resultIsDifferent = false
    for (enchantment in toApply) {
        var levelPresent = result.getEnchantmentLevel(enchantment.enchantment)
        var levelToUse = combine.getEnchantmentLevel(enchantment.enchantment)

        // Fix for book logic
        if (result.itemMeta is EnchantmentStorageMeta) {
            val meta = result.itemMeta as EnchantmentStorageMeta
            levelPresent = meta.getStoredEnchantLevel(enchantment.enchantment)
        }
        if (combine.itemMeta is EnchantmentStorageMeta) {
            val meta = result.itemMeta as EnchantmentStorageMeta
            levelPresent = meta.getStoredEnchantLevel(enchantment.enchantment)
        }

        // Skip this enchantment if it is not valid for the input item if it is not a book.
        if (inputBlueprint !is ItemEnchantedBook && !inputBlueprint.itemClassification.itemTagKeys
                .contains(enchantment.itemTypeTag)
        ) continue

        // Skip this enchantment if the input already contains a conflicting enchant.
        var conflicts = false
        for (appliedEnchantment in result.enchantments.keys) if (appliedEnchantment != enchantment.enchantment && appliedEnchantment.conflictsWith(
                enchantment.enchantment
            )
        ) conflicts = true
        if (conflicts) continue

        // Skip this enchantment if a higher level is already present.
        if (levelPresent > levelToUse) continue

        // Skip this enchantment if we don't have room for another enchantment and we don't have it.
        if (levelPresent <= 0 && newEnchantmentSlots <= 0) continue

        // Skip this enchantment if the two levels are equal and also the max level
        if (levelPresent == levelToUse && levelToUse >= enchantment.enchantment.maxLevel) continue

        // If this enchantment is new, decrement the slots we have
        if (levelPresent <= 0) newEnchantmentSlots--

        // If the enchantment levels are equal, we should use a level higher if the level can go higher
        if (levelPresent == levelToUse && levelToUse < enchantment.enchantment.maxLevel) levelToUse++

        // Apply the enchantment to the result, again depending on what item we have we have to do it differently
        if (result.itemMeta is EnchantmentStorageMeta) {
            val meta = result.itemMeta as EnchantmentStorageMeta
            meta.addStoredEnchant(enchantment.enchantment, levelToUse, true)
            result.setItemMeta(meta)
        } else result.addUnsafeEnchantment(enchantment.enchantment, levelToUse)

        resultIsDifferent = true
        cost += 0
    }

    // Did we even make a change? mark this combo as invalid
    if (!resultIsDifferent) return EnchantmentCombination(result, 0)

    // Now add on the repair cost from the item
    if (result.itemMeta is Repairable){
        val repairable = result.itemMeta as Repairable
        cost += repairable.repairCost
    }

    val resultBlueprint = SMPRPG.getService(ItemService::class.java).getBlueprint(result)
    resultBlueprint.updateItemData(result)
    return EnchantmentCombination(result, cost)
}