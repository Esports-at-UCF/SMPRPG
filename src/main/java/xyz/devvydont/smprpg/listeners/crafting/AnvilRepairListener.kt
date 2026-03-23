package xyz.devvydont.smprpg.listeners.crafting

import com.destroystokyo.paper.event.inventory.PrepareResultEvent
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.Repairable
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.inventory.PrepareAnvilEvent
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.blueprints.tools.augments.RepairCore
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment
import xyz.devvydont.smprpg.items.interfaces.IRepairable
import xyz.devvydont.smprpg.items.interfaces.ReforgeApplicator
import xyz.devvydont.smprpg.reforge.ReforgeType
import xyz.devvydont.smprpg.services.EntityService
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.listeners.ToggleableListener
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * Fixes anvil logic to conform to our plugin's rules. When you combine two items in an anvil, we need to dynamically
 * work out a result since there are many scenarios that can occur from an anvil combination.
 */
class AnvilRepairListener : ToggleableListener() {

    /**
     * Force all anvil interactions to not use Minecraft XP
     *
     * @param event
     */
    @EventHandler
    @Suppress("unused")
    private fun onAnvilCombination(event: PrepareAnvilEvent) {
        event.view.maximumRepairCost = 999999
        event.view.repairCost = 0
    }

    /**
     * Calculate repair amount based on supplied item and materials
     *
     * @param event
     */
    @EventHandler
    @Suppress("unused")
    private fun onPrepareRepair(event: PrepareAnvilEvent) {
        val firstItem = event.inventory.firstItem
        val secondItem = event.inventory.secondItem

        // First, check that we have an item in both slots. We can't repair anything otherwise.
        if (firstItem == null || secondItem == null) return

        val itemService = SMPRPG.getService(ItemService::class.java)

        // Is the item actually in need of a repair?
        if (firstItem.getDataOrDefault(DataComponentTypes.DAMAGE, 0) == 0) return

        // If the second item is a repair core, we don't need to check if we are using the right material for the item
        val firstItemBlueprint = itemService.getBlueprint(firstItem)
        val secondItemBlueprint = itemService.getBlueprint(secondItem)
        var repairAmount : Int
        val maxDurability = firstItem.getDataOrDefault(DataComponentTypes.MAX_DAMAGE, 0)!!
        if (secondItemBlueprint is RepairCore) {
            // No need to check if an item is specifically a repairable, just if it can take damage
            if (firstItemBlueprint !is IBreakableEquipment) return

            // Mining skill bonus does NOT apply to repair cores
            repairAmount = (RepairCore.getRepairValue(secondItem, firstItem) * maxDurability).roundToInt()
            event.view.setRepairItemCountCost(1)  // If, for whatever freak reason we get a stacked repair core, only allow one core to be used at a time.
        }
        else {
            // Is the first slot a repairable item?
            if (firstItemBlueprint !is IRepairable) return

            // Now that we know the item is repairable, check if our second item is the material we need to repair with
            if (!secondItem.isSimilar(firstItemBlueprint.repairMaterial)) return

            // Calculate how much durability should be restored, using the player's mining level as a factor as well
            val leveledPlayer = SMPRPG.getService(EntityService::class.java).getPlayerInstance(event.view.player as Player)
            val repairPerMat = (BASE_REPAIR_AMOUNT + (MINING_REPAIR_BONUS_PER_LEVEL * leveledPlayer.miningSkill.level))
            repairAmount = ((maxDurability * repairPerMat) * secondItem.amount).roundToInt()
            val numItemsRequired = ceil(1 / (maxDurability * repairPerMat)).toInt()

            event.view.setRepairItemCountCost(numItemsRequired)
        }
        // Generate our result item
        val result = firstItem.clone()
        result.setData(
            DataComponentTypes.DAMAGE,
            max((result.getData(DataComponentTypes.DAMAGE)!! - repairAmount), 0)
        )
        firstItemBlueprint.updateItemData(result)
        event.result = result
    }

    /*
     * Listen for when we are trying to apply a reforge to an item. This should calculate AFTER we do our enchantment
     * combining shenanigans since this case is much simpler.
     */
    @EventHandler(priority = EventPriority.HIGH)
    @Suppress("unused")
    private fun onCombineReforgeWithItem(event: PrepareAnvilEvent) {
        // First, we need to make sure we are trying to do something valid before we can proceed.
        // Are both item slots filled?

        if (event.inventory.firstItem == null || event.inventory.secondItem == null) return

        // Is the second slot a reforge stone?
        val secondBlueprint = SMPRPG.getService(ItemService::class.java).getBlueprint(event.inventory.secondItem!!)
        if (secondBlueprint !is ReforgeApplicator)
            return
        val reforgeApplicator = secondBlueprint as ReforgeApplicator

        // Can the first item have the reforge applied to it? And is the reforge actually valid?
        val reforgeType: ReforgeType = reforgeApplicator.getReforgeType()
        val reforge = SMPRPG.getService(ItemService::class.java).getReforge(reforgeType)
        if (reforge == null) return

        val input = SMPRPG.getService(ItemService::class.java).getBlueprint(event.inventory.firstItem!!)
        // Can this reforge type be applied to this type of item?
        if (!reforgeType.isAllowed(input.itemClassification)) return

        // The item input is allowed to have this reforge stone's reforge. Let's set a result for them to choose if
        // they want.
        val result = event.inventory.firstItem!!.clone()
        reforge.apply(result)
        event.result = result
        event.view.repairCost = 0
    }

    companion object {
        const val BASE_REPAIR_AMOUNT = 0.1
        const val MINING_REPAIR_BONUS_PER_LEVEL = 0.005
    }
}
