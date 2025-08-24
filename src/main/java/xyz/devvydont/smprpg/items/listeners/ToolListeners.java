package xyz.devvydont.smprpg.items.listeners;

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
            player.getWorld().playSound(player.getLocation(), ((IFueledEquipment) itemUsedToBreakBlueprint).getBreakSound(), 0.5F, 1.0F);

            // Decrement fuel
            var maxFuel = itemUsedToBreak.getPersistentDataContainer().get(IFueledEquipment.maxFuelKey, PersistentDataType.INTEGER);
            var fuel = itemUsedToBreak.getPersistentDataContainer().get(IFueledEquipment.fuelKey, PersistentDataType.INTEGER);

            // Try to query the value if it was present.
            if (fuel == null) {
                fuel = maxFuel;  // Really just a failsafe in case fuel isn't initialized for whatever reason.
            }

            final int finalFuel = Math.min(fuel + 1, ((IFueledEquipment) itemUsedToBreakBlueprint).getMaxFuel() - IFueledEquipment.FUEL_OFFSET);;
            itemUsedToBreak.editPersistentDataContainer(pdc -> pdc.set(IFueledEquipment.fuelKey, PersistentDataType.INTEGER, finalFuel));

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
        var maxFuel = blueprint.getMaxFuel();
        var fuel = returnRefuelable.getPersistentDataContainer().getOrDefault(IFueledEquipment.fuelKey, PersistentDataType.INTEGER, 1);
        var newFuel = Math.min(maxFuel - IFueledEquipment.FUEL_OFFSET, fuel - totalRefuel);
        returnRefuelable.editPersistentDataContainer(pdc -> pdc.set(IFueledEquipment.fuelKey, PersistentDataType.INTEGER, newFuel));
        ItemService.blueprint(returnRefuelable).updateItemData(returnRefuelable);
        event.getInventory().setResult(returnRefuelable);
    }
}
