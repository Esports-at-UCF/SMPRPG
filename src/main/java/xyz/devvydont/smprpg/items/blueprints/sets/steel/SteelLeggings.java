package xyz.devvydont.smprpg.items.blueprints.sets.steel;

import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemArmor;
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment;
import xyz.devvydont.smprpg.items.interfaces.ICraftable;
import xyz.devvydont.smprpg.items.interfaces.IDyeable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.builders.LeggingsRecipe;
import xyz.devvydont.smprpg.util.items.ToolGlobals;

import java.util.Collection;
import java.util.List;

public class SteelLeggings extends SteelArmorSet implements IBreakableEquipment, ICraftable, IDyeable {

    public SteelLeggings(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, ItemArmor.getDefenseFromItemType(CustomItemType.STEEL_LEGGINGS)),
                new AdditiveAttributeEntry(AttributeWrapper.KNOCKBACK_RESISTANCE, .125)
        );
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.LEGS;
    }

    @Override
    public int getPowerRating() { return ToolGlobals.STEEL_TOOL_POWER; }

    @Override
    public int getMaxDurability() {
        return 850;
    }


    @Override
    public NamespacedKey getRecipeKey() {
        return new NamespacedKey(SMPRPG.getPlugin(), getCustomItemType().getKey()+"-recipe");
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        return new LeggingsRecipe(this, itemService.getCustomItem(CustomItemType.STEEL_INGOT), generate()).build();
    }

    @Override
    public Collection<ItemStack> unlockedBy() {
        return List.of(itemService.getCustomItem(CustomItemType.STEEL_INGOT));
    }

    @Override
    public Color getColor() {
        return Color.fromRGB(0xfed83d);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.LEGGINGS;
    }
}
