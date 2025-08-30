package xyz.devvydont.smprpg.items.tools;

import io.papermc.paper.datacomponent.DataComponentTypes;
import org.bukkit.NamespacedKey;
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

import java.util.Collection;
import java.util.List;

public class ItemDrill extends CustomAttributeItem implements IFueledEquipment, Listener {

    public final String BREAK_SOUND = "audio:tools.drill.break";
    public final String DRILL_NAME_EXTENSTION = " Drill";

    public final NamespacedKey drillHeadKey = new NamespacedKey(SMPRPG.getInstance(), "drill_head");
    public final NamespacedKey drillBaseKey = new NamespacedKey(SMPRPG.getInstance(), "drill_base");

    public ItemStack getDrillPartItem(ItemStack item, NamespacedKey key) {
        return itemService.getCustomItem(item.getPersistentDataContainer().getOrDefault(key, PersistentDataType.STRING, null));
    }

    public Collection<AttributeEntry> getDrillHeadStats(ItemStack item) {
        ItemStack drillHead = getDrillPartItem(item, drillHeadKey);
        if (drillHead != null) {
            var head = (IModularToolComponent) ItemService.blueprint(drillHead);
            return head.getAttributes();
        }
        return List.of();
    }
//
    public Collection<AttributeEntry> getDrillBaseStats(ItemStack item) {
        ItemStack drillBase = getDrillPartItem(item, drillBaseKey);
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
        return 50_000;
    }

    @Override
    public void updateItemData(ItemStack itemStack) {
        var itemPdc = itemStack.getPersistentDataContainer();
        itemStack.editPersistentDataContainer(pdc -> pdc.set(IFueledEquipment.maxFuelKey, PersistentDataType.INTEGER, getMaxFuel()));

        itemStack.editPersistentDataContainer(pdc -> pdc.set(drillHeadKey, PersistentDataType.STRING, "steel_drill_head"));
        itemStack.editPersistentDataContainer(pdc -> pdc.set(drillBaseKey, PersistentDataType.STRING, "steel_drill_base"));

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
        ItemStack drillHead = getDrillPartItem(item, drillHeadKey);
        if (drillHead != null)
            return ItemService.blueprint(drillHead).getRarity(drillHead);
        return ItemRarity.COMMON;
    }

    @Override
    public String getItemName(ItemStack item) {
        String retString = "";
        String headPrefix = "";
        String basePrefix;
        ItemStack drillHead = getDrillPartItem(item, drillHeadKey);
        if (drillHead != null) {
            var headBp = (IModularToolComponent) ItemService.blueprint(drillHead);
            headPrefix = headBp.getComponentPrefix();
            retString += headPrefix;
        }
        ItemStack drillBase = getDrillPartItem(item, drillBaseKey);
        if (drillBase != null) {
            var drillBp = (IModularToolComponent) ItemService.blueprint(drillHead);
            basePrefix = drillBp.getComponentPrefix();
            if (headPrefix != basePrefix) {  // Prevents double names like (Steel-Steel Drill)
                retString += "-" + basePrefix;
            }
        }
        retString += " Drill";
        return retString;
    }
}
