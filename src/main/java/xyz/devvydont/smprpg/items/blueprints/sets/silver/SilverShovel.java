package xyz.devvydont.smprpg.items.blueprints.sets.silver;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Tool;
import org.bukkit.Material;
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
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomAttributeItem;
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemShovel;
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment;
import xyz.devvydont.smprpg.items.interfaces.ICraftable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.builders.ShovelRecipe;
import xyz.devvydont.smprpg.util.items.ToolGlobals;

import java.util.Collection;
import java.util.List;


public class SilverShovel extends CustomAttributeItem implements ICraftable, IBreakableEquipment {

    public static final Tool TOOL_COMP = Tool.tool()
            .build();

    public SilverShovel(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.STRENGTH, ItemShovel.getShovelDamage(CustomItemType.SILVER_SHOVEL)),
                new AdditiveAttributeEntry(AttributeWrapper.MINING_SPEED, ToolGlobals.SILVER_TOOL_SPEED),
                new MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, ItemShovel.SHOVEL_ATTACK_SPEED_DEBUFF)
        );
    }

    @Override
    public int getPowerRating() { return ToolGlobals.SILVER_TOOL_POWER; }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.TOOL;
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.MAINHAND;
    }

    @Override
    public int getMaxDurability() {
        return ToolGlobals.SILVER_TOOL_DURABILITY;
    }

    @Override
    public NamespacedKey getRecipeKey() {
        return new NamespacedKey(SMPRPG.getInstance(), getCustomItemType().getKey() + "-recipe");
    }

    @Override
    public void updateItemData(ItemStack itemStack) {
        super.updateItemData(itemStack);
        itemStack.setData(DataComponentTypes.TOOL, TOOL_COMP);
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        return new ShovelRecipe(this,
                itemService.getCustomItem(CustomItemType.SILVER_INGOT),
                itemService.getCustomItem(Material.STICK),
                generate()
        ).build();
    }

    @Override
    public Collection<ItemStack> unlockedBy() {
        return List.of(
                itemService.getCustomItem(CustomItemType.SILVER_INGOT)
        );
    }

}
