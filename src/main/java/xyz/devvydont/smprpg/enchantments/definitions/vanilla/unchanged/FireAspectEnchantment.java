package xyz.devvydont.smprpg.enchantments.definitions.vanilla.unchanged;

import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.UnchangedEnchantment;
import xyz.devvydont.smprpg.enchantments.recipe.EnchantmentRecipe;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.persistence.KeyStore;

public class FireAspectEnchantment extends UnchangedEnchantment {

    public static int getSecondsOfBurn(int level) {
        return level * 4;
    }

    public FireAspectEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return ComponentUtils.create("Fire Aspect");
    }

    @Override
    public @NotNull Component getDescription() {
        return ComponentUtils.merge(
            ComponentUtils.create("Ignites enemies for "),
            ComponentUtils.create(getSecondsOfBurn(getLevel()) + "s", NamedTextColor.GOLD),
            ComponentUtils.create(" and "),
            ComponentUtils.create("smelts", NamedTextColor.RED),
            ComponentUtils.create(" drops")
        );
    }

    @Override
    public EnchantmentRecipe getRecipe(int level) {
        switch (level) {
            case 1 -> {
                var blaze = getIngredientStack(CustomItemType.PREMIUM_BLAZE_ROD, 30);
                var magma = getIngredientStack(Material.MAGMA_CREAM, 80);
                var lapis = getIngredientStack(Material.LAPIS_BLOCK, 12);
                return new EnchantmentRecipe(getRecipeKey(level), 15, blaze, magma, lapis);
            }
            case 2 -> {
                var blaze = getIngredientStack(CustomItemType.ENCHANTED_BLAZE_ROD, 8);
                var magma = getIngredientStack(CustomItemType.INFERNO_RESIDUE, 16);
                var lapis = getIngredientStack(Material.LAPIS_BLOCK, 32);
                return new EnchantmentRecipe(getRecipeKey(level), 25, blaze, magma, lapis);
            }
            default -> { return null; }
        }
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_FIRE_ASPECT;
    }

    @Override
    public int getSkillRequirement() {
        return 16;
    }
}
