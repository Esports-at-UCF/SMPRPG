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

    @Override
    public String getBreakSound() {
        return BREAK_SOUND;
    }
}
