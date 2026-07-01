package xyz.devvydont.smprpg.items.blueprints.charms;

import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.items.AccessorySlot;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomAttributeItem;
import xyz.devvydont.smprpg.items.interfaces.IAccessory;
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;

import java.util.Collection;
import java.util.List;

public class LuckyCharm extends CustomAttributeItem implements IModelOverridden, IAccessory {

    @Override
    public void updateItemData(ItemStack itemStack) {
        super.updateItemData(itemStack);
        itemStack.setData(DataComponentTypes.MAX_STACK_SIZE, 1);
    }

    public LuckyCharm(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Key getDisplayKey() {
        return IModelOverridden.ofMaterial(Material.TOTEM_OF_UNDYING);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                AttributeEntry.additive(AttributeWrapper.LUCK, 30)
        );
    }

    @Override
    public int getPowerRating() {
        return 25;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.CHARM;
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.SADDLE;
    }

    @Override
    public AccessorySlot getSlot(@NotNull ItemStack item) {
        return AccessorySlot.CHARM;
    }
}
