package xyz.devvydont.smprpg.items.tools;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemContainerContents;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.ItemRarity;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomAttributeItem;
import xyz.devvydont.smprpg.items.interfaces.IFueledEquipment;
import xyz.devvydont.smprpg.items.interfaces.IModularToolComponent;
import xyz.devvydont.smprpg.services.ItemService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ItemDrill extends CustomAttributeItem implements IFueledEquipment, Listener {

    public final String BREAK_SOUND = "audio:tools.drill.break";
    public final String DRILL_NAME_EXTENSTION = " Drill";

    public final NamespacedKey drillHeadKey = new NamespacedKey(SMPRPG.getInstance(), "drill_head");
    public final NamespacedKey drillBaseKey = new NamespacedKey(SMPRPG.getInstance(), "drill_base");
    public final NamespacedKey drillTankKey = new NamespacedKey(SMPRPG.getInstance(), "drill_tank");

    public final int DRILL_BASE_SLOT_INDEX = 0;
    public final int DRILL_HEAD_SLOT_INDEX = 1;
    public final int DRILL_TANK_SLOT_INDEX = 2;

    public ItemStack getDrillPartItem(ItemStack item, int index) {
        var data = item.getData(DataComponentTypes.CONTAINER);
        if (data != null) {
            List<ItemStack> toolContainer = data.contents();
            return toolContainer.get(index);
        }
        return null;
    }

    public Collection<AttributeEntry> getDrillHeadStats(ItemStack item) {
        ItemStack drillHead = getDrillPartItem(item, DRILL_HEAD_SLOT_INDEX);
        if (drillHead != null) {
            var head = (IModularToolComponent) ItemService.blueprint(drillHead);
            return head.getAttributes();
        }
        return List.of();
    }
//
    public Collection<AttributeEntry> getDrillBaseStats(ItemStack item) {
        ItemStack drillBase = getDrillPartItem(item, DRILL_BASE_SLOT_INDEX);
        if (drillBase != null) {
            var base = (IModularToolComponent) ItemService.blueprint(drillBase);
            return base.getAttributes();
        }
        return List.of();
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
        var headStats = getDrillHeadStats(item);
        var baseStats = getDrillBaseStats(item);

        Collection<AttributeEntry> retList = new java.util.ArrayList<>(List.of());
        retList.add(new MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, DRILL_ATTACK_SPEED_DEBUFF));
        retList.addAll(headStats);
        retList.addAll(baseStats);
        return retList;
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.MAINHAND;
    }

    @Override
    public int getMaxFuel() {
        return 50_000 + FUEL_OFFSET;
    }

    public int getModularSlots() {
        return 3;
    }

    public ArrayList<ItemStack> initialize(ItemStack backpack) {

        // Create enough air items to fill all the slots.
        var items = new ArrayList<ItemStack>();
        for (var i = 0; i < getModularSlots(); i++)
            items.add(ItemStack.of(Material.AIR));

        // Set the stored contents.
        backpack.setData(DataComponentTypes.CONTAINER, ItemContainerContents.containerContents(items));
        return items;
    }

    @Override
    public void updateItemData(ItemStack itemStack) {
        var itemPdc = itemStack.getPersistentDataContainer();
        itemStack.editPersistentDataContainer(pdc -> pdc.set(IFueledEquipment.maxFuelKey, PersistentDataType.INTEGER, getMaxFuel()));

        itemStack.editPersistentDataContainer(pdc -> pdc.set(drillHeadKey, PersistentDataType.STRING, "adamantium_drill_head"));
        itemStack.editPersistentDataContainer(pdc -> pdc.set(drillBaseKey, PersistentDataType.STRING, "mithril_drill_base"));
        var data = itemStack.getData(DataComponentTypes.CONTAINER);
        ArrayList<ItemStack> items;
        if (data == null || data.contents().isEmpty())
            items = initialize(itemStack);
        else
            items = new ArrayList<>(data.contents());

        items.set(0, ItemService.generate(CustomItemType.MITHRIL_DRILL_BASE));
        items.set(1, ItemService.generate(CustomItemType.ADAMANTIUM_DRILL_HEAD));
        itemStack.setData(DataComponentTypes.CONTAINER, ItemContainerContents.containerContents(items));

        // Fuel our drill to full on first pickup/craft
        itemStack.editPersistentDataContainer(pdc -> pdc.set(IFueledEquipment.fuelKey,
                PersistentDataType.INTEGER,
                itemPdc.getOrDefault(IFueledEquipment.fuelKey,
                        PersistentDataType.INTEGER,
                        1)));
        itemStack.editPersistentDataContainer(pdc -> pdc.set(IFueledEquipment.maxFuelKey, PersistentDataType.INTEGER, getMaxFuel()));
        itemStack.setData(DataComponentTypes.MAX_STACK_SIZE, 1);
        super.updateItemData(itemStack);
    }

    @Override
    public String getBreakSound() {
        return BREAK_SOUND;
    }

    @Override
    public ItemRarity getRarity(ItemStack item) {
        ItemStack drillHead = getDrillPartItem(item, DRILL_HEAD_SLOT_INDEX);
        if (drillHead != null)
            return ItemService.blueprint(drillHead).getRarity(drillHead);
        return ItemRarity.COMMON;
    }
}
