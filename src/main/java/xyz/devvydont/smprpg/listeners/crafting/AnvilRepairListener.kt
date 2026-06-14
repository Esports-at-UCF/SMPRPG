package xyz.devvydont.smprpg.listeners.crafting

import com.destroystokyo.paper.event.inventory.PrepareResultEvent
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.Repairable
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.PrepareAnvilEvent
import org.bukkit.inventory.AnvilInventory
import org.bukkit.persistence.PersistentDataType
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.events.skills.SkillExperienceGainEvent
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.blueprints.equipment.ReforgeStone
import xyz.devvydont.smprpg.items.blueprints.resources.slayer.drops.NecronomiconExcerpts
import xyz.devvydont.smprpg.items.blueprints.tomes.TomeBlueprint
import xyz.devvydont.smprpg.items.blueprints.tools.augments.RepairCore
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment
import xyz.devvydont.smprpg.items.interfaces.IRepairable
import xyz.devvydont.smprpg.items.interfaces.ReforgeApplicator
import xyz.devvydont.smprpg.reforge.ReforgeType
import xyz.devvydont.smprpg.services.EntityService
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.skills.SkillType
import xyz.devvydont.smprpg.util.listeners.ToggleableListener
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.pow
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

        // If the second item is a repair core, we don't need to check if we are using the right material for the item
        val firstItemBlueprint = itemService.getBlueprint(firstItem)
        val secondItemBlueprint = itemService.getBlueprint(secondItem)
        var repairAmount : Int
        when (secondItemBlueprint) {
            is RepairCore -> {
                // Is the item actually in need of a repair?
                if (firstItem.getDataOrDefault(DataComponentTypes.DAMAGE, 0) == 0) return
                val maxDurability = firstItem.getDataOrDefault(DataComponentTypes.MAX_DAMAGE, 0)!!

                // No need to check if an item is specifically a repairable, just if it can take damage
                if (firstItemBlueprint !is IBreakableEquipment) return

                // Mining skill bonus does NOT apply to repair cores
                repairAmount = (RepairCore.getRepairValue(secondItem, firstItem) * maxDurability).roundToInt()
                event.view.setRepairItemCountCost(1)  // If, for whatever freak reason we get a stacked repair core, only allow one core to be used at a time.

                // Generate our result item
                val result = firstItem.clone()
                result.setData(
                    DataComponentTypes.DAMAGE,
                    max((result.getData(DataComponentTypes.DAMAGE)!! - repairAmount * secondItem.amount), 0)
                )
                firstItemBlueprint.updateItemData(result)
                event.result = result
            }
            is NecronomiconExcerpts -> {
                // Make sure our first item is a tome.
                if (firstItemBlueprint !is TomeBlueprint) return

                // Make sure our tome isn't maxed out on excerpts.
                val numExcerpts = firstItem.persistentDataContainer.getOrDefault(NecronomiconExcerpts.TOME_SPELL_COUNT_MODIFIER, PersistentDataType.INTEGER, 0)
                if (numExcerpts < NecronomiconExcerpts.MAX_EXCERPTS) {
                    // Generate our result item
                    val result = firstItem.clone()
                    NecronomiconExcerpts.addExcerptsToTome(result)
                    event.view.setRepairItemCountCost(1)  // We should only ever take one page at a time.
                    firstItemBlueprint.updateItemData(result)
                    event.result = result
                }
            }
            else -> {
                // Is the first slot a repairable item?
                if (firstItemBlueprint !is IRepairable) return

                // Is the item actually in need of a repair?
                if (firstItem.getDataOrDefault(DataComponentTypes.DAMAGE, 0) == 0) return
                val maxDurability = firstItem.getDataOrDefault(DataComponentTypes.MAX_DAMAGE, 0)!!

                // Now that we know the item is repairable, check if our second item is the material we need to repair with
                var isValidMaterial : Boolean = false
                for (repairItem in firstItemBlueprint.repairMaterial) {
                    if (ItemService.isInternalIdMatch(repairItem, secondItem)) {
                        isValidMaterial = true
                        break
                    }
                }
                if (!isValidMaterial) return

                // Calculate how much durability should be restored, using the player's mining level as a factor as well
                val leveledPlayer = SMPRPG.getService(EntityService::class.java).getPlayerInstance(event.view.player as Player)
                val repairPercentPerMat = (BASE_REPAIR_AMOUNT + (MINING_REPAIR_BONUS_PER_LEVEL * leveledPlayer.miningSkill.level))
                repairAmount = (maxDurability * repairPercentPerMat).roundToInt()
                var currentDamage = firstItem.getDataOrDefault(DataComponentTypes.DAMAGE, 0) as Int
                var numItemsRequired = 0
                while (currentDamage > 0) {
                    currentDamage -= repairAmount
                    numItemsRequired++
                }

                event.view.setRepairItemCountCost(numItemsRequired)
                // Generate our result item
                val result = firstItem.clone()
                result.setData(
                    DataComponentTypes.DAMAGE,
                    max((result.getData(DataComponentTypes.DAMAGE)!! - repairAmount * secondItem.amount), 0)
                )
                firstItemBlueprint.updateItemData(result)
                event.result = result
            }
        }
    }

    /**
     * Award skill experience for repairing.
     *
     * @param event
     */
    @EventHandler
    @Suppress("unused")
    private fun onGrabRepair(event: InventoryClickEvent) {
        if (event.clickedInventory is AnvilInventory) {
            val inventory = event.clickedInventory as AnvilInventory
            if (inventory.result == null) return

            if (event.slot == 2) {
                when (event.action) {
                    InventoryAction.NOTHING, InventoryAction.PLACE_ALL, InventoryAction.PLACE_SOME,
                    InventoryAction.PLACE_ONE, InventoryAction.UNKNOWN, InventoryAction.PLACE_FROM_BUNDLE,
                    InventoryAction.PLACE_ALL_INTO_BUNDLE, InventoryAction.PLACE_SOME_INTO_BUNDLE -> return
                    else -> {
                        val firstItemBlueprint = ItemService.blueprint(inventory.firstItem!!)
                        val secondItemBlueprint = ItemService.blueprint(inventory.secondItem!!)

                        val leveledPlayer = SMPRPG.getService(EntityService::class.java).getPlayerInstance(event.view.player as Player)
                        val xpReward = leveledPlayer.generateSkillExperienceReward()
                        if (secondItemBlueprint is ReforgeStone) {
                            // We are picking up a reforged item, so let's award some magic xp + mining xp
                            val rarityOrdinal = (firstItemBlueprint.getRarity(inventory.firstItem!!).ordinal + 1)
                            val baseXp = (rarityOrdinal * 5.0.pow(secondItemBlueprint.reforge.powerRating)).toInt()
                            xpReward.add(SkillType.MAGIC, baseXp)
                            xpReward.add(SkillType.MINING, (baseXp / 2.0).roundToInt())
                            xpReward.apply(leveledPlayer, SkillExperienceGainEvent.ExperienceSource.ANVIL)
                        }
                        else if (firstItemBlueprint is IBreakableEquipment) {
                            // We are picking up a repaired item
                            var firstItemDurability = inventory.firstItem!!.getDataOrDefault(DataComponentTypes.MAX_DAMAGE, 0) as Int
                            firstItemDurability -= inventory.firstItem!!.getDataOrDefault(DataComponentTypes.DAMAGE, 0) as Int

                            var resultItemDurability = inventory.result!!.getDataOrDefault(DataComponentTypes.MAX_DAMAGE, 0) as Int
                            resultItemDurability -= inventory.result!!.getDataOrDefault(DataComponentTypes.DAMAGE, 0) as Int

                            val difference = resultItemDurability - firstItemDurability

                            val leveledPlayer = SMPRPG.getService(EntityService::class.java).getPlayerInstance(event.view.player as Player)
                            val xpReward = leveledPlayer.generateSkillExperienceReward()
                            xpReward.add(SkillType.MINING,
                                (firstItemBlueprint.getRarity(inventory.firstItem!!).ordinal + 1) * (difference * 3))
                            xpReward.apply(leveledPlayer, SkillExperienceGainEvent.ExperienceSource.ANVIL)
                        }
                    }
                }
            }
        }
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
