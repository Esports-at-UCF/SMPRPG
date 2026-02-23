package xyz.devvydont.smprpg.items.blueprints.wardrobe;

import net.kyori.adventure.text.Component;
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
import xyz.devvydont.smprpg.services.ItemService;

import net.kyori.adventure.text.format.NamedTextColor;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.Collection;
import java.util.List;

public class WardrobeSlotToken extends CustomItemBlueprint implements ICraftable, IHeaderDescribable {

    public WardrobeSlotToken(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public List<Component> getHeader(ItemStack itemStack) {
        return List.of(
                ComponentUtils.create("Consume in the Wardrobe Upgrades"),
                ComponentUtils.create("menu to permanently unlock a"),
                ComponentUtils.merge(ComponentUtils.create("new "), ComponentUtils.create("wardrobe slot", NamedTextColor.AQUA), ComponentUtils.create("!"))
        );
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.ITEM;
    }

    @Override
    public NamespacedKey getRecipeKey() {
        return new NamespacedKey(SMPRPG.getPlugin(), getCustomItemType().getKey() + "-recipe");
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        return switch (_type) {
            case WARDROBE_SLOT_COMMON -> commonRecipe();
            case WARDROBE_SLOT_UNCOMMON -> uncommonRecipe();
            case WARDROBE_SLOT_RARE -> rareRecipe();
            case WARDROBE_SLOT_EPIC -> epicRecipe();
            case WARDROBE_SLOT_LEGENDARY -> legendaryRecipe();
            default -> throw new IllegalStateException("Unknown wardrobe token type: " + _type);
        };
    }

    @Override
    public Collection<ItemStack> unlockedBy() {
        return switch (_type) {
            case WARDROBE_SLOT_COMMON -> List.of(itemService.getCustomItem(Material.LEATHER));
            case WARDROBE_SLOT_UNCOMMON -> List.of(itemService.getCustomItem(CustomItemType.WARDROBE_SLOT_COMMON));
            case WARDROBE_SLOT_RARE -> List.of(itemService.getCustomItem(CustomItemType.WARDROBE_SLOT_UNCOMMON));
            case WARDROBE_SLOT_EPIC -> List.of(itemService.getCustomItem(CustomItemType.WARDROBE_SLOT_RARE));
            case WARDROBE_SLOT_LEGENDARY -> List.of(itemService.getCustomItem(CustomItemType.WARDROBE_SLOT_EPIC));
            default -> List.of();
        };
    }

    private ShapedRecipe commonRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(getRecipeKey(), generate());
        recipe.shape("lwl", "waw", "lwl");
        recipe.setIngredient('a', itemService.getCustomItem(Material.ARMOR_STAND));
        recipe.setIngredient('l', itemService.getCustomItem(Material.LEATHER));
        recipe.setIngredient('w', itemService.getCustomItem(Material.WHITE_WOOL));
        recipe.setCategory(CraftingBookCategory.MISC);
        return recipe;
    }

    private ShapedRecipe uncommonRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(getRecipeKey(), generate());
        recipe.shape("isi", "sts", "isi");
        recipe.setIngredient('t', ItemService.generate(CustomItemType.WARDROBE_SLOT_COMMON));
        recipe.setIngredient('i', itemService.getCustomItem(Material.IRON_INGOT));
        recipe.setIngredient('s', itemService.getCustomItem(Material.STRING));
        recipe.setCategory(CraftingBookCategory.MISC);
        return recipe;
    }

    private ShapedRecipe rareRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(getRecipeKey(), generate());
        recipe.shape("gdg", "dtd", "gdg");
        recipe.setIngredient('t', ItemService.generate(CustomItemType.WARDROBE_SLOT_UNCOMMON));
        recipe.setIngredient('g', itemService.getCustomItem(Material.GOLD_INGOT));
        recipe.setIngredient('d', itemService.getCustomItem(Material.DIAMOND));
        recipe.setCategory(CraftingBookCategory.MISC);
        return recipe;
    }

    private ShapedRecipe epicRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(getRecipeKey(), generate());
        recipe.shape("ene", "ntn", "ene");
        recipe.setIngredient('t', ItemService.generate(CustomItemType.WARDROBE_SLOT_RARE));
        recipe.setIngredient('e', itemService.getCustomItem(Material.EMERALD));
        recipe.setIngredient('n', itemService.getCustomItem(Material.NETHERITE_INGOT));
        recipe.setCategory(CraftingBookCategory.MISC);
        return recipe;
    }

    private ShapedRecipe legendaryRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(getRecipeKey(), generate());
        recipe.shape("sns", "ntn", "sns");
        recipe.setIngredient('t', ItemService.generate(CustomItemType.WARDROBE_SLOT_EPIC));
        recipe.setIngredient('n', itemService.getCustomItem(Material.NETHER_STAR));
        recipe.setIngredient('s', itemService.getCustomItem(Material.NETHERITE_INGOT));
        recipe.setCategory(CraftingBookCategory.MISC);
        return recipe;
    }
}
