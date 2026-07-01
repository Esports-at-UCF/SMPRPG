package xyz.devvydont.smprpg.items.interfaces

import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.ability.Ability
import xyz.devvydont.smprpg.ability.AbilityActivationMethod
import xyz.devvydont.smprpg.ability.AbilityCost
import xyz.devvydont.smprpg.items.AccessorySlot
import xyz.devvydont.smprpg.services.ItemService

/**
 * Represents an item that can cast an ability.
 */
interface IAccessory {

    fun getSlot(item: ItemStack): AccessorySlot
}
