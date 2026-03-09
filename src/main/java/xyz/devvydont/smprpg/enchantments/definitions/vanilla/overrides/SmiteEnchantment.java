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
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.enchantments.EnchantmentRarity;
import xyz.devvydont.smprpg.enchantments.EnchantmentUtil;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.VanillaEnchantment;
import xyz.devvydont.smprpg.enchantments.recipe.EnchantmentRecipe;
import xyz.devvydont.smprpg.events.CustomEntityDamageByEntityEvent;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

public class SmiteEnchantment extends VanillaEnchantment implements Listener {

    public static int getPercentageIncrease(int level) {
        return level * 30;
    }

    public static boolean isUndead(EntityType type) {
        return switch (type) {
            case ZOMBIE, ZOMBIE_VILLAGER, ZOMBIE_HORSE, ZOMBIFIED_PIGLIN, DROWNED, HUSK, ZOGLIN, WITHER,
                 WITHER_SKELETON, PHANTOM, SKELETON, SKELETON_HORSE, BOGGED, STRAY -> true;
            default -> false;
        };
    }

    public SmiteEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    @Override
    public EnchantmentRecipe getRecipe(int level) {
        switch (level) {
            case 1 -> {
                var silver = getIngredientStack(CustomItemType.SILVER_INGOT, 5);
                var flesh = getIngredientStack(Material.ROTTEN_FLESH, 2);
                var lapis = getIngredientStack(Material.LAPIS_LAZULI, 8);
                return new EnchantmentRecipe(getRecipeKey(level), 0, silver, flesh, lapis);
            }
            case 2 -> {
                var silver = getIngredientStack(CustomItemType.SILVER_INGOT, 10);
                var flesh = getIngredientStack(Material.ROTTEN_FLESH, 4);
                var lapis = getIngredientStack(Material.LAPIS_LAZULI, 16);
                return new EnchantmentRecipe(getRecipeKey(level), 5, silver, flesh, lapis);
            }
            case 3 -> {
                var silver = getIngredientStack(CustomItemType.SILVER_INGOT, 20);
                var flesh = getIngredientStack(Material.ROTTEN_FLESH, 8);
                var lapis = getIngredientStack(Material.LAPIS_LAZULI, 32);
                return new EnchantmentRecipe(getRecipeKey(level), 15, silver, flesh, lapis);
            }
            case 4 -> {
                var silver = getIngredientStack(CustomItemType.SILVER_INGOT, 40);
                var flesh = getIngredientStack(Material.ROTTEN_FLESH, 16);
                var necrotic = getIngredientStack(CustomItemType.PREMIUM_NECROTIC_FLESH, 16);
                var lapis = getIngredientStack(Material.LAPIS_LAZULI, 64);
                return new EnchantmentRecipe(getRecipeKey(level), 20, silver, flesh, necrotic, lapis);
            }
            case 5 -> {
                var silver = getIngredientStack(CustomItemType.SILVER_INGOT, 80);
                var flesh = getIngredientStack(Material.ROTTEN_FLESH, 32);
                var necrotic = getIngredientStack(CustomItemType.PREMIUM_NECROTIC_FLESH, 32);
                var lapis = getIngredientStack(Material.LAPIS_BLOCK, 16);
                return new EnchantmentRecipe(getRecipeKey(level), 30, silver, flesh, necrotic, lapis);
            }
            case 6 -> {
                var silver = getIngredientStack(CustomItemType.SILVER_BLOCK, 18);
                var flesh = getIngredientStack(Material.ROTTEN_FLESH, 64);
                var necrotic = getIngredientStack(CustomItemType.PREMIUM_NECROTIC_FLESH, 64);
                var lapis = getIngredientStack(Material.LAPIS_BLOCK, 32);
                return new EnchantmentRecipe(getRecipeKey(level), 40, silver, flesh, necrotic, lapis);
            }
            case 7 -> {
                var silver = getIngredientStack(CustomItemType.SILVER_BLOCK, 36);
                var flesh = getIngredientStack(CustomItemType.PREMIUM_FLESH, 15);
                var necrotic = getIngredientStack(CustomItemType.ENCHANTED_NECROTIC_FLESH, 15);
                var viscera = getIngredientStack(CustomItemType.REVILED_VISCERA, 1);
                var lapis = getIngredientStack(Material.LAPIS_BLOCK, 64);
                return new EnchantmentRecipe(getRecipeKey(level), 60, silver, flesh, necrotic, viscera, lapis);
            }
            case 8 -> {
                var silver = getIngredientStack(CustomItemType.SILVER_BLOCK, 72);
                var flesh = getIngredientStack(CustomItemType.PREMIUM_FLESH, 30);
                var necrotic = getIngredientStack(CustomItemType.ENCHANTED_NECROTIC_FLESH, 30);
                var viscera = getIngredientStack(CustomItemType.REVILED_VISCERA, 2);
                var lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 16);
                return new EnchantmentRecipe(getRecipeKey(level), 70, silver, flesh, necrotic, viscera, lapis);
            }
            case 9 -> {
                var silver = getIngredientStack(CustomItemType.ENCHANTED_SILVER, 8);
                var flesh = getIngredientStack(CustomItemType.PREMIUM_FLESH, 45);
                var necrotic = getIngredientStack(CustomItemType.ENCHANTED_NECROTIC_FLESH, 45);
                var viscera = getIngredientStack(CustomItemType.REVILED_VISCERA, 3);
                var amalgamation = getIngredientStack(CustomItemType.VISCERAL_AMALGAMATION, 1);
                var lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 32);
                return new EnchantmentRecipe(getRecipeKey(level), 80, silver, flesh, necrotic, viscera, amalgamation, lapis);
            }
            case 10 -> {
                var silver = getIngredientStack(CustomItemType.ENCHANTED_SILVER, 16);
                var flesh = getIngredientStack(CustomItemType.PREMIUM_FLESH, 60);
                var necrotic = getIngredientStack(CustomItemType.ENCHANTED_NECROTIC_FLESH, 60);
                var viscera = getIngredientStack(CustomItemType.REVILED_VISCERA, 4);
                var amalgamation = getIngredientStack(CustomItemType.VISCERAL_AMALGAMATION, 2);
                var lapis = getIngredientStack(CustomItemType.ENCHANTED_LAPIS, 64);
                return new EnchantmentRecipe(getRecipeKey(level), 90, silver, flesh, necrotic, viscera, amalgamation, lapis);
            }
            default -> { return null; }
        }
    }

    @Override
    public @NotNull Component getDisplayName() {
        return ComponentUtils.create("Smite");
    }

    @Override
    public @NotNull Component getDescription() {
        return ComponentUtils.merge(
            ComponentUtils.create("Increases damage dealt by "),
            ComponentUtils.create("+" + getPercentageIncrease(getLevel()) + "%", NamedTextColor.GREEN),
            ComponentUtils.create(" against "),
            ComponentUtils.create("the undead", NamedTextColor.RED)
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
        return EnchantmentRarity.UNCOMMON.getWeight();
    }

    @Override
    public EquipmentSlotGroup getEquipmentSlotGroup() {
        return EquipmentSlotGroup.MAINHAND;
    }

    @Override
    public int getSkillRequirement() {
        return 0;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDamageArthropod(CustomEntityDamageByEntityEvent event) {

        // Skip non arthropods
        if (!isUndead(event.damaged.getType()))
            return;

        // Skip entity if they aren't alive
        if (!(event.dealer instanceof LivingEntity dealer))
            return;

        int level = EnchantmentUtil.getHoldingEnchantLevel(getEnchantment(), EquipmentSlotGroup.HAND, dealer.getEquipment());
        if (level <= 0)
            return;

        double multiplier = 1.0 + (getPercentageIncrease(level) / 100.0);
        event.multiplyDamage(multiplier);
    }

    /**
     * A set of enchantments that this enchantment conflicts with.
     * If there are none, this enchantment has no conflicts
     *
     * @return
     */
    @NotNull
    public RegistryKeySet<Enchantment> getConflictingEnchantments() {
        return RegistrySet.keySet(RegistryKey.ENCHANTMENT, EnchantmentKeys.SHARPNESS, EnchantmentKeys.BANE_OF_ARTHROPODS);
    }
}
