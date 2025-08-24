package xyz.devvydont.smprpg.items.tools;

import io.papermc.paper.datacomponent.DataComponentTypes;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomAttributeItem;
import xyz.devvydont.smprpg.items.interfaces.IFueledEquipment;
import xyz.devvydont.smprpg.items.interfaces.IFurnaceFuel;
import xyz.devvydont.smprpg.services.ItemService;

import java.util.Collection;
import java.util.List;

public class ItemDrill extends CustomAttributeItem implements IFueledEquipment, Listener {

    public final String BREAK_SOUND = "audio:tools.drill.break";

    public double getDrillMiningPower() {
        return 0;
    }

    public double getDrillDamage() {
        return 1;
    }

    public double getDrillFortune() {
        return 1;
    }

    public double getDrillSpeed() {
        return 1;
    }

    public int getPowerRating() {
        return 0;
    }

    public static double DRILL_ATTACK_SPEED_DEBUFF = -0.5;

    public ItemDrill(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.DRILL;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.MINING_POWER, getDrillMiningPower()),
                new AdditiveAttributeEntry(AttributeWrapper.STRENGTH, getDrillDamage()),
                new MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, DRILL_ATTACK_SPEED_DEBUFF),
                new AdditiveAttributeEntry(AttributeWrapper.MINING_SPEED, getDrillSpeed()),
                new AdditiveAttributeEntry(AttributeWrapper.MINING_FORTUNE, getDrillFortune())
        );
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.MAINHAND;
    }

    @Override
    public int getMaxFuel() {
        return 50_000;
    }

    @Override
    public void updateItemData(ItemStack itemStack) {
        var itemPdc = itemStack.getPersistentDataContainer();
        itemStack.editPersistentDataContainer(pdc -> pdc.set(IFueledEquipment.maxFuelKey, PersistentDataType.INTEGER, getMaxFuel()));
        // Fuel our drill to full on first pickup/craft
        itemStack.editPersistentDataContainer(pdc -> pdc.set(IFueledEquipment.fuelKey,
                                                                                  PersistentDataType.INTEGER,
                                                                                  itemPdc.getOrDefault(IFueledEquipment.fuelKey,
                                                                                                       PersistentDataType.INTEGER,
                                                                                                       1)));
        itemStack.setData(DataComponentTypes.MAX_STACK_SIZE, 1);
        super.updateItemData(itemStack);
    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent event) {
        var player = event.getPlayer();

        var itemUsedToBreak = player.getEquipment().getItemInMainHand();
        var itemUsedToBreakBlueprint = ItemService.blueprint(itemUsedToBreak);

        if (itemUsedToBreakBlueprint instanceof IFueledEquipment) {
            player.getWorld().playSound(player.getLocation(), BREAK_SOUND, 0.5F, 1.0F);

            // Decrement fuel
            var maxFuel = itemUsedToBreak.getPersistentDataContainer().get(IFueledEquipment.maxFuelKey, PersistentDataType.INTEGER);
            var fuel = itemUsedToBreak.getPersistentDataContainer().get(IFueledEquipment.fuelKey, PersistentDataType.INTEGER);

            // Try to query the value if it was present.
            if (fuel == null) {
                fuel = maxFuel;  // Really just a failsafe in case fuel isn't initialized for whatever reason.
            }

            final int finalFuel = Math.min(fuel + 1, getMaxFuel() - IFueledEquipment.FUEL_OFFSET);;
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
