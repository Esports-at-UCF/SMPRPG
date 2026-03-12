package xyz.devvydont.smprpg.enchantments.definitions;

import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.enchantments.EnchantmentRarity;
import xyz.devvydont.smprpg.enchantments.base.AttributeEnchantment;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.VanillaEnchantment;
import xyz.devvydont.smprpg.enchantments.recipe.EnchantmentRecipe;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeModifierType;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.persistence.KeyStore;

import java.util.Collection;
import java.util.List;

public class AmplificationBlessing extends CustomEnchantment implements AttributeEnchantment {

    public AmplificationBlessing(String id) {
        super(id);
    }

    public static int getDamageIncrease(int level) {
        return level * 20;
    }

    @Override
    public AttributeModifierType getAttributeModifierType() {
        return AttributeModifierType.ENCHANTMENT;
    }

    @Override
    public Collection<AttributeEntry> getHeldAttributes() {
        return List.of(
                AttributeEntry.multiplicative(AttributeWrapper.STRENGTH, getDamageIncrease(getLevel()) / 100.0)
        );
    }

    @Override
    public int getPowerRating() {
        return getLevel();
    }

    @Override
    public EnchantmentRecipe getRecipe(int level) {
        switch (level) {
            case 1 -> {
                var amethyst = getIngredientStack(Material.AMETHYST_SHARD, 20);
                var flint = getIngredientStack(Material.FLINT, 5);
                var lapis = getIngredientStack(Material.LAPIS_LAZULI, 8);
                return new EnchantmentRecipe(getRecipeKey(level), 0, amethyst, flint, lapis);
            }
            case 2 -> {
                var amethyst = getIngredientStack(Material.AMETHYST_SHARD, 40);
                var flint = getIngredientStack(Material.FLINT, 10);
                var lapis = getIngredientStack(Material.LAPIS_LAZULI, 16);
                return new EnchantmentRecipe(getRecipeKey(level), 5, amethyst, flint, lapis);
            }
            case 3 -> {
                var amethyst = getIngredientStack(Material.AMETHYST_SHARD, 80);
                var flint = getIngredientStack(Material.FLINT, 20);
                var lapis = getIngredientStack(Material.LAPIS_LAZULI, 32);
                return new EnchantmentRecipe(getRecipeKey(level), 15, amethyst, flint, lapis);
            }
            case 4 -> {
                var amethyst = getIngredientStack(Material.AMETHYST_BLOCK, 40);
                var flint = getIngredientStack(Material.FLINT, 40);
                var quartz = getIngredientStack(Material.QUARTZ, 10);
                var lapis = getIngredientStack(Material.LAPIS_LAZULI, 64);
                return new EnchantmentRecipe(getRecipeKey(level), 20, amethyst, flint, quartz, lapis);
            }
            case 5 -> {
                var amethyst = getIngredientStack(Material.AMETHYST_BLOCK, 80);
                var flint = getIngredientStack(Material.FLINT, 80);
                var quartz = getIngredientStack(Material.QUARTZ, 40);
                var lapis = getIngredientStack(Material.LAPIS_BLOCK, 16);
                return new EnchantmentRecipe(getRecipeKey(level), 30, amethyst, flint, quartz, lapis);
            }
            default -> { return null; }
        }
    }

    @Override
    public @NotNull Component getDisplayName() {
        return ComponentUtils.create("Blessing of Amplification", NamedTextColor.YELLOW);
    }

    @Override
    public @NotNull TextColor getEnchantColor() {
        return NamedTextColor.YELLOW;
    }

    @Override
    public @NotNull Component getDescription() {
        return ComponentUtils.merge(
            ComponentUtils.create("Increases damage by "),
            ComponentUtils.create("+" + getDamageIncrease(getLevel()) + "%", NamedTextColor.GREEN)
        );
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_WEAPON;
    }

    @Override
    public int getAnvilCost() {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getWeight() {
        return EnchantmentRarity.BLESSING.getWeight();
    }

    @Override
    public boolean isBlessing() { return true; }

    @Override
    public EquipmentSlotGroup getEquipmentSlotGroup() {
        return EquipmentSlotGroup.MAINHAND;
    }

    @Override
    public int getSkillRequirement() {
        return 0;
    }
}
