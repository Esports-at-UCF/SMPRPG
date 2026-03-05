package xyz.devvydont.smprpg.items.blueprints.resources.slayer.drops;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.ICraftable;
import xyz.devvydont.smprpg.items.interfaces.IHeaderDescribable;
import xyz.devvydont.smprpg.items.interfaces.ISellable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.Collection;
import java.util.List;

public class VisceralAmalgamation extends CustomItemBlueprint implements ICraftable, ISellable, IHeaderDescribable {

    public VisceralAmalgamation(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    /**
     * Determine what type of item this is.
     */
    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.ITEM;
    }

    /**
     * Given this item stack, how much should it be able to sell for?
     * Keep in mind that the size of the stack needs to considered as well!
     *
     * @param item The item that can be sold.
     * @return The worth of the item.
     */
    @Override
    public int getWorth(ItemStack item) {
        return 138_172 * item.getAmount();
    }

    @Override
    public List<Component> getHeader(ItemStack itemStack) {
        return List.of(
                ComponentUtils.merge(
                    ComponentUtils.create("A horrifying concoction consisting of "),
                    ComponentUtils.create("absurd amounts", NamedTextColor.DARK_RED)
                ),
                ComponentUtils.merge(

                    ComponentUtils.create("of "),
                    ComponentUtils.create("flesh at various stages of decomposition, ", NamedTextColor.RED)
                ),
                ComponentUtils.create("chunks of viscera and gored remains, ", NamedTextColor.GOLD),
                ComponentUtils.create("and of course, to bind it all together..."),
                ComponentUtils.merge(
                    ComponentUtils.create("slime", NamedTextColor.GREEN, TextDecoration.BOLD),
                    ComponentUtils.create(".")
                )
        );
    }

    @Override
    public NamespacedKey getRecipeKey() {
        return new NamespacedKey(SMPRPG.getPlugin(), getCustomItemType().getKey() + "-recipe");
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(getRecipeKey(), generate());
        recipe.shape("fvf", "vsv", "fvf");
        recipe.setCategory(CraftingBookCategory.MISC);
        recipe.setIngredient('s', ItemService.generate(CustomItemType.PREMIUM_SLIME));
        recipe.setIngredient('v', ItemService.generate(CustomItemType.REVILED_VISCERA));
        recipe.setIngredient('f', ItemService.generate(CustomItemType.NECROTIC_FLESH_SINGULARITY));
        return recipe;
    }

    @Override
    public Collection<ItemStack> unlockedBy() {
        return List.of(
                ItemService.generate(CustomItemType.REVILED_VISCERA)
        );
    }
}
