package xyz.devvydont.smprpg.items.listeners;

import io.papermc.paper.datacomponent.DataComponentTypes;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import xyz.devvydont.smprpg.items.interfaces.IFueledEquipment;
import xyz.devvydont.smprpg.items.interfaces.IFurnaceFuel;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.listeners.ToggleableListener;

public class ToolListeners extends ToggleableListener {

    @EventHandler
    public void onBreakBlock(BlockBreakEvent event) {
        var player = event.getPlayer();

        var itemUsedToBreak = player.getEquipment().getItemInMainHand();
        var itemUsedToBreakBlueprint = ItemService.blueprint(itemUsedToBreak);

        if (itemUsedToBreakBlueprint instanceof IFueledEquipment) {
            IFueledEquipment bp = (IFueledEquipment) ItemService.blueprint(itemUsedToBreak);
            player.getWorld().playSound(player.getLocation(), ((IFueledEquipment) itemUsedToBreakBlueprint).getBreakSound(), 0.5F, 1.0F);

            // Decrement fuel
            bp.setFuelUsed(itemUsedToBreak, itemUsedToBreak.getData(DataComponentTypes.DAMAGE) + 1);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void __onAttemptPerformTransmuteUpgradeRecipe(PrepareItemCraftEvent event) {

        // If there's a recipe involved, there's nothing to check.
        if (event.getRecipe() != null)
            return;

        int refuelableIndex = -1;
        int totalRefuel = 0;
        int totalItems = 0;
        var i = 0;
        // Get our crafting matrix
        var matrix = event.getInventory().getMatrix();
        for (var itemStack : matrix) {
            i++;
            if (itemStack == null)
                continue;

            var bp = ItemService.blueprint(itemStack);
            var isEquip = (bp instanceof IFueledEquipment);
            var isFuel = (bp instanceof IFurnaceFuel) || itemStack.getType().isFuel();
            if (!isEquip && !isFuel) {
                // Something in here is not a furnace fuel, or refuelable, abort.
                return;
            }

            if (isEquip) {
                if (refuelableIndex != -1) {
                    // We already have a refuelable found??? Don't be trying to charge two drills on me!
                    return;
                }
                refuelableIndex = i - 1;

            } else {
                if (bp instanceof IFurnaceFuel)
                    totalRefuel += ((IFurnaceFuel) bp).getBurnTime() / 20;  // Convert from ticks to seconds
                else
                    totalRefuel += itemStack.getType().asItemType().getBurnDuration() / 20;
            }
            totalItems++;
        }
        if (totalItems <= 1) {
            // Kinda hard to refuel if there is just one item in the grid, huh?
            return;
        }
        ItemStack returnRefuelable = matrix[refuelableIndex].clone();
        IFueledEquipment blueprint = (IFueledEquipment) ItemService.blueprint(returnRefuelable);
        var fuel = blueprint.getFuelUsed(returnRefuelable);
        var newFuel = Math.max(0, fuel - totalRefuel);
        blueprint.setFuelUsed(returnRefuelable, newFuel);
        ItemService.blueprint(returnRefuelable).updateItemData(returnRefuelable);
        event.getInventory().setResult(returnRefuelable);
    }
}
