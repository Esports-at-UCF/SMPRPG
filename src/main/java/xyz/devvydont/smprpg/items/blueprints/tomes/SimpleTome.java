package xyz.devvydont.smprpg.items.blueprints.tomes;

import io.papermc.paper.datacomponent.DataComponentTypes;
import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomAttributeItem;
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden;
import xyz.devvydont.smprpg.services.ItemService;

import java.util.Collection;
import java.util.List;

public class SimpleTome extends CustomAttributeItem implements IModelOverridden {

    public SimpleTome(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public void updateItemData(ItemStack itemStack) {
        super.updateItemData(itemStack);
        itemStack.setData(DataComponentTypes.MAX_STACK_SIZE, 1);
    }

    @Override
    public Material getDisplayMaterial() {
        return Material.PAPER;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.INTELLIGENCE, 100),
                new AdditiveAttributeEntry(AttributeWrapper.STRENGTH, 5)
        );
    }

    @Override
    public int getPowerRating() {
        return 10;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.TOME;
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.HAND;
    }
}
