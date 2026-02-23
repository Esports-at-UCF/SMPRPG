package xyz.devvydont.smprpg.items.blueprints.sets.mithril;

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
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemAxe;
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment;
import xyz.devvydont.smprpg.items.interfaces.ICraftable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.builders.AxeRecipe;
import xyz.devvydont.smprpg.util.items.ToolGlobals;

import java.util.Collection;
import java.util.List;

import static xyz.devvydont.smprpg.items.blueprints.vanilla.ItemAxe.AXE_ATTACK_SPEED_DEBUFF;

public class MithrilAxe extends CustomAttributeItem implements ICraftable, IBreakableEquipment {

    public static final Tool TOOL_COMP = Tool.tool()
            .build();

    public MithrilAxe(ItemService itemService, CustomItemType type) { super(itemService, type); }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.MINING_POWER, ToolGlobals.MITHRIL_TOOL_MINING_POWER),
                new AdditiveAttributeEntry(AttributeWrapper.STRENGTH, ItemAxe.getAxeDamage(CustomItemType.MITHRIL_AXE)),
                new MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, AXE_ATTACK_SPEED_DEBUFF),
                new AdditiveAttributeEntry(AttributeWrapper.MINING_SPEED, ToolGlobals.MITHRIL_TOOL_SPEED),
                new AdditiveAttributeEntry(AttributeWrapper.WOODCUTTING_FORTUNE, ToolGlobals.MITHRIL_TOOL_FORTUNE)
        );
    }

    @Override
    public int getPowerRating() {
        return ToolGlobals.MITHRIL_TOOL_POWER;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.AXE;
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.MAINHAND;
    }

    @Override
    public int getMaxDurability() {
        return ToolGlobals.MITHRIL_TOOL_DURABILITY;
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
        return new AxeRecipe(this,
                itemService.getCustomItem(CustomItemType.MITHRIL_INGOT),
                itemService.getCustomItem(CustomItemType.STEEL_TOOL_SHAFT),
                generate()
        ).build();
    }

    @Override
    public Collection<ItemStack> unlockedBy() {
        return List.of(
                itemService.getCustomItem(CustomItemType.MITHRIL_INGOT)
        );
    }

}
