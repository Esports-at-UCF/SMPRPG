package xyz.devvydont.smprpg.items.blueprints.sets.tin;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Tool;
import io.papermc.paper.registry.keys.tags.BlockTypeTagKeys;
import net.kyori.adventure.util.TriState;
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
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemPickaxe;
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment;
import xyz.devvydont.smprpg.items.interfaces.ICraftable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.builders.PickaxeRecipe;
import xyz.devvydont.smprpg.util.items.ToolGlobals;

import java.util.Collection;
import java.util.List;

import static xyz.devvydont.smprpg.items.blueprints.vanilla.ItemPickaxe.PICKAXE_ATTACK_SPEED_DEBUFF;

public class TinPickaxe extends CustomAttributeItem implements IBreakableEquipment, ICraftable {

    public static final Tool TOOL_COMP = Tool.tool()
            .addRule(Tool.rule(ToolGlobals.blockRegistry.getTag(BlockTypeTagKeys.MINEABLE_PICKAXE), 7.0f, TriState.TRUE))
            .build();

    public TinPickaxe(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.MINING_POWER, ToolGlobals.TIN_TOOL_MINING_POWER),
                new AdditiveAttributeEntry(AttributeWrapper.STRENGTH, ItemPickaxe.getPickaxeDamage(Material.WOODEN_PICKAXE)),
                new MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, PICKAXE_ATTACK_SPEED_DEBUFF),
                new AdditiveAttributeEntry(AttributeWrapper.MINING_SPEED, ToolGlobals.TIN_TOOL_SPEED),
                new AdditiveAttributeEntry(AttributeWrapper.MINING_FORTUNE, ToolGlobals.TIN_TOOL_FORTUNE)
        );
    }

    @Override
    public int getPowerRating() {
        return ToolGlobals.TIN_TOOL_POWER;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.PICKAXE;
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.MAINHAND;
    }

    @Override
    public int getMaxDurability() {
        return ToolGlobals.TIN_TOOL_DURABILITY;
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
        return new PickaxeRecipe(this,
                itemService.getCustomItem(CustomItemType.TIN_INGOT),
                itemService.getCustomItem(Material.STICK),
                generate()
        ).build();
    }

    @Override
    public Collection<ItemStack> unlockedBy() {
        return List.of(
                itemService.getCustomItem(CustomItemType.TIN_INGOT)
        );
    }
}
