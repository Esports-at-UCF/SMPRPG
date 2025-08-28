package xyz.devvydont.smprpg.items.blueprints.sets.rosegold;

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
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemArmor;
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment;
import xyz.devvydont.smprpg.items.interfaces.ICraftable;
import xyz.devvydont.smprpg.items.interfaces.IDyeable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.builders.HelmetRecipe;
import xyz.devvydont.smprpg.util.items.ToolGlobals;

import java.util.Collection;
import java.util.List;

public class RoseGoldHelmet extends RoseGoldArmorSet implements ICraftable, IBreakableEquipment, IDyeable {

    public RoseGoldHelmet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, ItemArmor.getDefenseFromItemType(CustomItemType.ROSE_GOLD_HELMET)),
                new AdditiveAttributeEntry(AttributeWrapper.MINING_SPEED, 50),
                new ScalarAttributeEntry(AttributeWrapper.STRENGTH, .2)
        );
    }

    @Override
    public int getPowerRating() { return ToolGlobals.ROSE_GOLD_TOOL_POWER; }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.HEAD;
    }

    @Override
    public NamespacedKey getRecipeKey() {
        return new NamespacedKey(SMPRPG.getInstance(), getCustomItemType().getKey()+"-recipe");
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        return new HelmetRecipe(this, itemService.getCustomItem(CustomItemType.ROSE_GOLD_INGOT), generate()).build();
    }

    @Override
    public Collection<ItemStack> unlockedBy() {
        return List.of(itemService.getCustomItem(CustomItemType.ROSE_GOLD_INGOT));
    }

    @Override
    public int getMaxDurability() {
        return 800;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.HELMET;
    }

    @Override
    public Color getColor() {
        return Color.fromRGB(0xf9801d);
    }
}
