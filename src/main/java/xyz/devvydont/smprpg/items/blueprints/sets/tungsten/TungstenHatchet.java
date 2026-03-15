package xyz.devvydont.smprpg.items.blueprints.sets.tungsten;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Tool;
import net.kyori.adventure.key.Key;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomAttributeItem;
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemSword;
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment;
import xyz.devvydont.smprpg.items.interfaces.ICraftable;
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.items.ToolGlobals;

import java.util.Collection;
import java.util.List;

import static xyz.devvydont.smprpg.items.blueprints.vanilla.ItemAxe.AXE_ATTACK_SPEED_DEBUFF;

public class TungstenHatchet extends CustomAttributeItem implements ICraftable, IBreakableEquipment, IModelOverridden {

    public TungstenHatchet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    public static final Tool TOOL_COMP = Tool.tool()
            .defaultMiningSpeed(0.0001f)
            .build();

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.MINING_POWER, ToolGlobals.TUNGSTEN_TOOL_MINING_POWER),
                new AdditiveAttributeEntry(AttributeWrapper.STRENGTH, ItemSword.getSwordDamage(CustomItemType.TUNGSTEN_SWORD) - 5),
                new MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, AXE_ATTACK_SPEED_DEBUFF),
                new AdditiveAttributeEntry(AttributeWrapper.MINING_SPEED, ToolGlobals.TUNGSTEN_TOOL_SPEED * 0.8),
                new AdditiveAttributeEntry(AttributeWrapper.WOODCUTTING_FORTUNE, ToolGlobals.TUNGSTEN_TOOL_FORTUNE * 0.8),
                new AdditiveAttributeEntry(AttributeWrapper.FARMING_FORTUNE, ToolGlobals.TUNGSTEN_TOOL_FORTUNE * 0.8)
        );
    }

    @Override
    public int getPowerRating() { return ToolGlobals.TUNGSTEN_TOOL_POWER; }

    @Override
    public ItemClassification getItemClassification() { return ItemClassification.HATCHET; }

    @Override
    public EquipmentSlotGroup getActiveSlot() { return EquipmentSlotGroup.MAINHAND; }

    @Override
    public int getMaxDurability() { return ToolGlobals.TUNGSTEN_TOOL_DURABILITY; }

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
        ShapedRecipe recipe = new ShapedRecipe(getRecipeKey(), generate());
        recipe.shape(
                "t ",
                "ts",
                " s"
        );
        recipe.setIngredient('t', itemService.getCustomItem(CustomItemType.TUNGSTEN_INGOT));
        recipe.setIngredient('s', itemService.getCustomItem(CustomItemType.SULFUR_TREATED_TOOL_SHAFT));
        recipe.setCategory(CraftingBookCategory.EQUIPMENT);
        return recipe;
    }

    @Override
    public Collection<ItemStack> unlockedBy() {
        return List.of(
                itemService.getCustomItem(CustomItemType.TUNGSTEN_INGOT)
        );
    }

    @Override
    public Key getDisplayKey() {
        return IModelOverridden.ofItemType(_type);
    }
}
