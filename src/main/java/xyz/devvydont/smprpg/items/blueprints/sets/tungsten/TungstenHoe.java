package xyz.devvydont.smprpg.items.blueprints.sets.tungsten;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Tool;
import net.kyori.adventure.key.Key;
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
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemHoe;
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment;
import xyz.devvydont.smprpg.items.interfaces.ICraftable;
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.builders.HoeRecipe;
import xyz.devvydont.smprpg.util.items.ToolGlobals;

import java.util.Collection;
import java.util.List;

public class TungstenHoe extends CustomAttributeItem implements ICraftable, IBreakableEquipment, IModelOverridden {

    public static final Tool TOOL_COMP = Tool.tool()
            .build();

    public TungstenHoe(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.STRENGTH, ItemHoe.getHoeDamage(CustomItemType.TUNGSTEN_HOE)),
                new MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, ItemHoe.getHoeAttackSpeedDebuff(CustomItemType.TUNGSTEN_HOE)),
                new AdditiveAttributeEntry(AttributeWrapper.MINING_SPEED, ToolGlobals.TUNGSTEN_TOOL_SPEED),
                new AdditiveAttributeEntry(AttributeWrapper.FARMING_FORTUNE, ToolGlobals.TUNGSTEN_TOOL_FORTUNE)
        );
    }

    @Override
    public int getPowerRating() {
        return ToolGlobals.TUNGSTEN_TOOL_POWER;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.HOE;
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.MAINHAND;
    }

    @Override
    public int getMaxDurability() {
        return ToolGlobals.ADAMANTIUM_TOOL_DURABILITY;
    }

    @Override
    public NamespacedKey getRecipeKey() {
        return new NamespacedKey(SMPRPG.getPlugin(), getCustomItemType().getKey() + "-recipe");
    }

    @Override
    public void updateItemData(ItemStack itemStack) {
        super.updateItemData(itemStack);
        itemStack.setData(DataComponentTypes.TOOL, TOOL_COMP);
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        return new HoeRecipe(this,
                itemService.getCustomItem(CustomItemType.ADAMANTIUM_INGOT),
                itemService.getCustomItem(CustomItemType.STEEL_TOOL_SHAFT),
                generate()
        ).build();
    }

    @Override
    public Collection<ItemStack> unlockedBy() {
        return List.of(
                itemService.getCustomItem(CustomItemType.ADAMANTIUM_INGOT)
        );
    }

    @Override
    public Key getDisplayKey() {
        return IModelOverridden.ofItemType(_type);
    }
}
