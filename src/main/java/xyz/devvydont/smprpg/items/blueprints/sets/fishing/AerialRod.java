package xyz.devvydont.smprpg.items.blueprints.sets.fishing;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomAttributeItem;
import xyz.devvydont.smprpg.items.interfaces.*;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.items.ToolGlobals;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class AerialRod extends CustomAttributeItem implements IBreakableEquipment, IFishingRod, ICraftable, ISellable, IRepairable {

    public AerialRod(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.ROD;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                AttributeEntry.additive(AttributeWrapper.STRENGTH, getStrength()),
                AttributeEntry.multiplicative(AttributeWrapper.ATTACK_SPEED, ToolGlobals.FISHING_ROD_COOLDOWN),
                AttributeEntry.additive(AttributeWrapper.FISHING_RATING, getFishingRating()),
                AttributeEntry.additive(AttributeWrapper.FISHING_CREATURE_CHANCE, getChance()),
                AttributeEntry.additive(AttributeWrapper.FISHING_TREASURE_CHANCE, getChance()),
                AttributeEntry.additive(AttributeWrapper.FISHING_SPEED, getSpeed())
        );
    }

    @Override
    public int getPowerRating() {
        return switch (getCustomItemType()) {
            case AERCLOUD_ROD -> 20;
            case ENDER_ROD -> 25;
            case COMET_ROD -> 30;
            case NEBULA_ROD -> 45;
            default -> 1;
        };
    };

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.HAND;
    }

    @Override
    public int getMaxDurability() {
        return getPowerRating() * 1_000;
    }

    @Override
    public Set<FishingFlag> getFishingFlags() {
        return Set.of(FishingFlag.AERIAL);
    }

    @Override
    public NamespacedKey getRecipeKey() {
        return new NamespacedKey(SMPRPG.getPlugin(), getCustomItemType().getKey() + "_recipe");
    }

    /**
     * Work out which fishing rod this rod will be crafted from.
     */
    private RecipeChoice getTransmuteComponent() {
        return switch (this.getCustomItemType()) {
            case AERCLOUD_ROD -> new RecipeChoice.ExactChoice(ItemService.generate(CustomItemType.COLD_AERCLOUD));
            case ETHER_ROD -> new RecipeChoice.ExactChoice(ItemService.generate(CustomItemType.AERCLOUD_ROD));
            case MERCURIAL_ROD -> new RecipeChoice.ExactChoice(ItemService.generate(CustomItemType.ETHER_ROD));
            case ZEPHYRUS_ROD -> new RecipeChoice.ExactChoice(ItemService.generate(CustomItemType.MERCURIAL_ROD));
            default -> new RecipeChoice.ExactChoice(ItemService.generate(Material.BARRIER));
        };
    }

    /**
     * Get the material used for crafting the rod part of the rod.
     */
    private RecipeChoice getCraftingMaterial() {
        return switch (this.getCustomItemType()) {
            case AERCLOUD_ROD -> new RecipeChoice.ExactChoice(ItemService.generate(CustomItemType.COLD_AERCLOUD));
            case ETHER_ROD -> new RecipeChoice.ExactChoice(ItemService.generate(CustomItemType.GOLD_AERCLOUD));
            case MERCURIAL_ROD -> new RecipeChoice.ExactChoice(ItemService.generate(CustomItemType.AETHERIUM_INGOT));
            case ZEPHYRUS_ROD -> new RecipeChoice.ExactChoice(ItemService.generate(CustomItemType.ENCHANTED_BREEZE_ROD));
            default -> new RecipeChoice.ExactChoice(ItemService.generate(Material.BARRIER));
        };
    }

    /**
     * Get the material used for crafting the rod part of the rod.
     */
    private RecipeChoice getStringMaterial() {
        return switch (this.getCustomItemType()) {
            case AERCLOUD_ROD -> new RecipeChoice.ExactChoice(ItemService.generate(CustomItemType.PREMIUM_STRING));
            case ETHER_ROD -> new RecipeChoice.ExactChoice(ItemService.generate(CustomItemType.ENCHANTED_STRING));
            case MERCURIAL_ROD -> new RecipeChoice.ExactChoice(ItemService.generate(CustomItemType.ASTRAL_FILAMENT));
            case ZEPHYRUS_ROD -> new RecipeChoice.ExactChoice(ItemService.generate(CustomItemType.ETHEREAL_FIBER));
            default -> new RecipeChoice.ExactChoice(ItemService.generate(Material.BARRIER));
        };
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        var recipe = new ShapedRecipe(getRecipeKey(), generate());
        recipe.shape(
                "  m",
                " ts",
                "m s"
        );
        recipe.setIngredient('m', getCraftingMaterial());
        recipe.setIngredient('t', getTransmuteComponent());
        recipe.setIngredient('s', getStringMaterial());
        recipe.setCategory(CraftingBookCategory.EQUIPMENT);
        recipe.setGroup("void_rod");
        return recipe;
    }

    /**
     * A collection of items that will unlock the recipe for this item. Typically will be one of the components
     * of the recipe itself, but can be set to whatever is desired
     *
     * @return
     */
    @Override
    public Collection<ItemStack> unlockedBy() {
        if (getCraftingMaterial() instanceof RecipeChoice.ExactChoice exact)
            return List.of(exact.getItemStack());
        return List.of(ItemService.generate(Material.END_STONE));
    }

    private int getFishingRating() {
        return switch (getCustomItemType()) {
            case AERCLOUD_ROD -> 30;
            case ETHER_ROD -> 50;
            case MERCURIAL_ROD -> 75;
            case ZEPHYRUS_ROD -> 125;
            default -> 0;
        };
    };

    private int getStrength() {
        return switch (getCustomItemType()) {
            case AERCLOUD_ROD -> 30;
            case ETHER_ROD -> 50;
            case MERCURIAL_ROD -> 70;
            case ZEPHYRUS_ROD -> 100;
            default -> 0;
        };
    };

    private int getChance() {
        return switch (getCustomItemType()) {
            case AERCLOUD_ROD -> 1;
            case ETHER_ROD -> 2;
            case MERCURIAL_ROD -> 3;
            case ZEPHYRUS_ROD -> 4;
            default -> 0;
        };
    };

    private int getSpeed() {
        return switch (this.getCustomItemType()) {
            case AERCLOUD_ROD -> 5;
            case ETHER_ROD -> 20;
            case MERCURIAL_ROD -> 40;
            case ZEPHYRUS_ROD -> 70;
            default -> 0;
        };
    }

    @Override
    public int getWorth(ItemStack item) {
        var base =  super.getWorth(item);

        return base + switch (this.getCustomItemType()) {
            case AERCLOUD_ROD -> 50;
            case ETHER_ROD -> 500;
            case MERCURIAL_ROD -> 15_000;
            case ZEPHYRUS_ROD -> 500_000;
            default -> 0;
        };
    }

    @Override
    public @NotNull Collection<@NotNull ItemStack> getRepairMaterial() {
        return switch (getCustomItemType()) {
            case AERCLOUD_ROD -> List.of(itemService.getCustomItem(CustomItemType.COLD_AERCLOUD));
            case ETHER_ROD -> List.of(itemService.getCustomItem(CustomItemType.GOLD_AERCLOUD));
            case MERCURIAL_ROD -> List.of(itemService.getCustomItem(CustomItemType.ZANITE));
            case ZEPHYRUS_ROD -> List.of(itemService.getCustomItem(CustomItemType.AETHERIUM_INGOT));
            default -> List.of();
        };
    }
}
