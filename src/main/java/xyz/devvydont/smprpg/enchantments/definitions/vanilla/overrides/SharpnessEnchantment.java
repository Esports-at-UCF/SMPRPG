package xyz.devvydont.smprpg.enchantments.definitions.vanilla.overrides;

import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.keys.EnchantmentKeys;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.registry.set.RegistrySet;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.enchantments.EnchantmentRarity;
import xyz.devvydont.smprpg.enchantments.base.AttributeEnchantment;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.VanillaEnchantment;
import xyz.devvydont.smprpg.enchantments.recipe.EnchantmentRecipe;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeModifierType;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.Collection;
import java.util.List;

public class SharpnessEnchantment extends VanillaEnchantment implements AttributeEnchantment {

    public SharpnessEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public AttributeModifierType getAttributeModifierType() {
        return AttributeModifierType.ENCHANTMENT;
    }

    @Override
    public Collection<AttributeEntry> getHeldAttributes() {
        return List.of(
                AttributeEntry.multiplicative(AttributeWrapper.STRENGTH, getLevel() * 10 / 100.0)
        );
    }

    @Override
    public int getPowerRating() {
        return getLevel()/2;
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
            case 6 -> {
                var amethyst = getIngredientStack(CustomItemType.ENCHANTED_AMETHYST, 18);
                var flint = getIngredientStack(CustomItemType.ENCHANTED_FLINT, 2);
                var quartz = getIngredientStack(Material.QUARTZ, 80);
                var lapis = getIngredientStack(Material.LAPIS_BLOCK, 32);
                return new EnchantmentRecipe(getRecipeKey(level), 40, amethyst, flint, quartz, lapis);
            }
            case 7 -> {
                var amethyst = getIngredientStack(CustomItemType.ENCHANTED_AMETHYST, 36);
                var flint = getIngredientStack(CustomItemType.ENCHANTED_FLINT, 4);
                var quartz = getIngredientStack(CustomItemType.ENCHANTED_QUARTZ, 18);
                var diamond = getIngredientStack(Material.DIAMOND, 20);
                var lapis = getIngredientStack(Material.LAPIS_BLOCK, 64);
                return new EnchantmentRecipe(getRecipeKey(level), 60, amethyst, flint, quartz, diamond, lapis);
            }
            case 8 -> {
                var amethyst = getIngredientStack(CustomItemType.ENCHANTED_AMETHYST, 72);
                var flint = getIngredientStack(CustomItemType.ENCHANTED_FLINT, 8);
                var quartz = getIngredientStack(CustomItemType.ENCHANTED_QUARTZ, 36);
                var diamond = getIngredientStack(Material.DIAMOND, 40);
                var lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 16);
                return new EnchantmentRecipe(getRecipeKey(level), 70, amethyst, flint, quartz, diamond, lapis);
            }
            case 9 -> {
                var amethyst = getIngredientStack(CustomItemType.ENCHANTED_AMETHYST_BLOCK, 8);
                var flint = getIngredientStack(CustomItemType.ENCHANTED_FLINT, 16);
                var quartz = getIngredientStack(CustomItemType.ENCHANTED_QUARTZ, 72);
                var diamond = getIngredientStack(Material.DIAMOND, 80);
                var dragonsteel = getIngredientStack(CustomItemType.DRAGONSTEEL_INGOT, 2);
                var lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 32);
                return new EnchantmentRecipe(getRecipeKey(level), 80, amethyst, flint, quartz, diamond, dragonsteel, lapis);
            }
            case 10 -> {
                var amethyst = getIngredientStack(CustomItemType.ENCHANTED_AMETHYST_BLOCK, 16);
                var flint = getIngredientStack(CustomItemType.ENCHANTED_FLINT, 32);
                var quartz = getIngredientStack(CustomItemType.ENCHANTED_QUARTZ_BLOCK, 8);
                var diamond = getIngredientStack(CustomItemType.ENCHANTED_DIAMOND, 2);
                var dragonsteel = getIngredientStack(CustomItemType.DRAGONSTEEL_INGOT, 4);
                var lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 64);
                return new EnchantmentRecipe(getRecipeKey(level), 90, amethyst, flint, quartz, diamond, dragonsteel, lapis);
            }
            default -> { return null; }
        }
    }

    @Override
    public @NotNull Component getDisplayName() {
        return ComponentUtils.create("Sharpness");
    }

    @Override
    public @NotNull Component getDescription() {
        return ComponentUtils.merge(
            ComponentUtils.create("Increases damage by "),
            ComponentUtils.create("+" + getLevel() * 10 + "%", NamedTextColor.GREEN)
        );
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_SHARP_WEAPON;
    }

    @Override
    public int getAnvilCost() {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 10;
    }

    @Override
    public int getWeight() {
        return EnchantmentRarity.COMMON.getWeight();
    }

    @Override
    public EquipmentSlotGroup getEquipmentSlotGroup() {
        return EquipmentSlotGroup.MAINHAND;
    }

    @Override
    public int getSkillRequirement() {
        return 0;
    }

    /**
     * A set of enchantments that this enchantment conflicts with.
     * If there are none, this enchantment has no conflicts
     *
     * @return
     */
    @NotNull
    public RegistryKeySet<Enchantment> getConflictingEnchantments() {
        return RegistrySet.keySet(RegistryKey.ENCHANTMENT, EnchantmentKeys.SMITE, EnchantmentKeys.BANE_OF_ARTHROPODS);
    }
}
