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
                var silver = ItemService.generate(CustomItemType.SILVER_INGOT);
                silver.setAmount(5);
                var flesh = ItemService.generate(Material.ROTTEN_FLESH);
                flesh.setAmount(2);
                var lapis = ItemService.generate(Material.LAPIS_LAZULI);
                lapis.setAmount(8);
                return new EnchantmentRecipe(getRecipeKey(level), silver, flesh, lapis);
            }
            case 2 -> {
                var silver = ItemService.generate(CustomItemType.SILVER_INGOT);
                silver.setAmount(10);
                var flesh = ItemService.generate(Material.ROTTEN_FLESH);
                flesh.setAmount(5);
                var lapis = ItemService.generate(Material.LAPIS_LAZULI);
                lapis.setAmount(16);
                return new EnchantmentRecipe(getRecipeKey(level), silver, flesh, lapis);
            }
            case 3 -> {
                var silver = ItemService.generate(CustomItemType.SILVER_INGOT);
                silver.setAmount(20);
                var flesh = ItemService.generate(CustomItemType.PREMIUM_FLESH);
                flesh.setAmount(2);
                var lapis = ItemService.generate(Material.LAPIS_LAZULI);
                lapis.setAmount(32);
                return new EnchantmentRecipe(getRecipeKey(level), silver, flesh, lapis);
            }
            case 4 -> {
                var silver = ItemService.generate(CustomItemType.SILVER_INGOT);
                silver.setAmount(64);
                var flesh = ItemService.generate(CustomItemType.PREMIUM_FLESH);
                flesh.setAmount(4);
                var necrotic = ItemService.generate(CustomItemType.PREMIUM_NECROTIC_FLESH);
                necrotic.setAmount(4);
                var lapis = ItemService.generate(Material.LAPIS_LAZULI);
                lapis.setAmount(64);
                return new EnchantmentRecipe(getRecipeKey(level), silver, flesh, necrotic, lapis);
            }
            case 5 -> {
                var silver = ItemService.generate(CustomItemType.SILVER_BLOCK);
                silver.setAmount(8);
                var flesh = ItemService.generate(CustomItemType.ENCHANTED_FLESH);
                flesh.setAmount(1);
                var necrotic = ItemService.generate(CustomItemType.ENCHANTED_NECROTIC_FLESH);
                necrotic.setAmount(1);
                var lapis = ItemService.generate(Material.LAPIS_BLOCK);
                lapis.setAmount(16);
                return new EnchantmentRecipe(getRecipeKey(level), silver, flesh, necrotic, lapis);
            }
            case 6 -> {
                var silver = ItemService.generate(CustomItemType.SILVER_BLOCK);
                silver.setAmount(12);
                var flesh = ItemService.generate(CustomItemType.ENCHANTED_FLESH);
                flesh.setAmount(2);
                var necrotic = ItemService.generate(CustomItemType.ENCHANTED_NECROTIC_FLESH);
                necrotic.setAmount(2);
                var lapis = ItemService.generate(Material.LAPIS_BLOCK);
                lapis.setAmount(32);
                return new EnchantmentRecipe(getRecipeKey(level), silver, flesh, necrotic, lapis);
            }
            case 7 -> {
                var silver = ItemService.generate(CustomItemType.SILVER_BLOCK);
                silver.setAmount(16);
                var flesh = ItemService.generate(CustomItemType.ENCHANTED_FLESH);
                flesh.setAmount(4);
                var necrotic = ItemService.generate(CustomItemType.ENCHANTED_NECROTIC_FLESH);
                necrotic.setAmount(4);
                var viscera = ItemService.generate(CustomItemType.REVILED_VISCERA);
                viscera.setAmount(1);
                var lapis = ItemService.generate(Material.LAPIS_BLOCK);
                lapis.setAmount(64);
                return new EnchantmentRecipe(getRecipeKey(level), silver, flesh, necrotic, viscera, lapis);
            }
            case 8 -> {
                var silver = ItemService.generate(CustomItemType.SILVER_BLOCK);
                silver.setAmount(20);
                var flesh = ItemService.generate(CustomItemType.ENCHANTED_FLESH);
                flesh.setAmount(8);
                var necrotic = ItemService.generate(CustomItemType.ENCHANTED_NECROTIC_FLESH);
                necrotic.setAmount(8);
                var viscera = ItemService.generate(CustomItemType.REVILED_VISCERA);
                viscera.setAmount(2);
                var lapis = ItemService.generate(CustomItemType.ENCHANTED_LAPIS);
                lapis.setAmount(16);
                return new EnchantmentRecipe(getRecipeKey(level), silver, flesh, necrotic, viscera, lapis);
            }
            case 9 -> {
                var silver = ItemService.generate(CustomItemType.SILVER_BLOCK);
                silver.setAmount(24);
                var flesh = ItemService.generate(CustomItemType.ENCHANTED_FLESH);
                flesh.setAmount(12);
                var necrotic = ItemService.generate(CustomItemType.ENCHANTED_NECROTIC_FLESH);
                necrotic.setAmount(12);
                var viscera = ItemService.generate(CustomItemType.REVILED_VISCERA);
                viscera.setAmount(3);
                var amalgamation = ItemService.generate(CustomItemType.VISCERAL_AMALGAMATION);
                amalgamation.setAmount(1);
                var lapis = ItemService.generate(CustomItemType.ENCHANTED_LAPIS);
                lapis.setAmount(32);
                return new EnchantmentRecipe(getRecipeKey(level), silver, flesh, necrotic, viscera, amalgamation, lapis);
            }
            case 10 -> {
                var silver = ItemService.generate(CustomItemType.SILVER_BLOCK);
                silver.setAmount(32);
                var flesh = ItemService.generate(CustomItemType.ENCHANTED_FLESH);
                flesh.setAmount(16);
                var necrotic = ItemService.generate(CustomItemType.ENCHANTED_NECROTIC_FLESH);
                necrotic.setAmount(16);
                var viscera = ItemService.generate(CustomItemType.REVILED_VISCERA);
                viscera.setAmount(4);
                var amalgamation = ItemService.generate(CustomItemType.VISCERAL_AMALGAMATION);
                amalgamation.setAmount(2);
                var lapis = ItemService.generate(CustomItemType.ENCHANTED_LAPIS);
                lapis.setAmount(64);
                return new EnchantmentRecipe(getRecipeKey(level), silver, flesh, necrotic, viscera, amalgamation, lapis);
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
