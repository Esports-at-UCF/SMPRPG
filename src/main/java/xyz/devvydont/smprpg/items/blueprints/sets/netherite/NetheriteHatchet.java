package xyz.devvydont.smprpg.items.blueprints.sets.netherite;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Tool;
import io.papermc.paper.registry.keys.tags.BlockTypeTagKeys;
import net.kyori.adventure.util.TriState;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.tools.ItemHatchet;
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemPickaxe;
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemSword;
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment;
import xyz.devvydont.smprpg.items.interfaces.ICraftable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.items.ToolGlobals;

import java.util.Collection;
import java.util.List;

public class NetheriteHatchet extends ItemHatchet implements ICraftable, IBreakableEquipment {

    public static final Tool TOOL_COMP = Tool.tool()
            .defaultMiningSpeed(0.0001f)
            .build();

    public NetheriteHatchet(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public int getPowerRating() {
        return ItemSword.getSwordRating(Material.NETHERITE_SWORD);
    }

    @Override
    public double getHatchetMiningPower() { return ToolGlobals.NETHERITE_TOOL_MINING_POWER; }

    @Override
    public double getHatchetDamage() { return ItemSword.getSwordDamage(Material.NETHERITE_SWORD) - 25; }

    @Override
    public double getHatchetFortune() { return ItemPickaxe.getPickaxeFortune(Material.NETHERITE_PICKAXE) * 0.8; }

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
                "n ",
                "ns",
                " s"
        );
        recipe.setIngredient('n', itemService.getCustomItem(Material.NETHERITE_INGOT));
        recipe.setIngredient('s', itemService.getCustomItem(Material.STICK));
        recipe.setCategory(CraftingBookCategory.EQUIPMENT);
        return recipe;
    }

    @Override
    public Collection<ItemStack> unlockedBy() {
        return List.of(itemService.getCustomItem(Material.NETHERITE_INGOT));
    }

    @Override
    public int getMaxDurability() {
        return ToolGlobals.NETHERITE_TOOL_DURABILITY;
    }

}
