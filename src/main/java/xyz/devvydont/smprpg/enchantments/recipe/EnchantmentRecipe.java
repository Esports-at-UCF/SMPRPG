package xyz.devvydont.smprpg.enchantments.recipe;

import com.google.common.collect.ImmutableSet;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class EnchantmentRecipe implements Keyed {
    NamespacedKey key;
    Set<ItemStack> ingredients;

    public EnchantmentRecipe(@NotNull NamespacedKey key, @NotNull ItemStack... ingredients) {
        this.key = key;
        this.ingredients = ImmutableSet.copyOf(ingredients);
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return key;
    }

    public Set<ItemStack> getIngredients() {
        return ingredients;
    }
}