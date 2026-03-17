package xyz.devvydont.smprpg.enchantments.definitions;

import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.keys.EnchantmentKeys;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.registry.set.RegistrySet;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.enchantments.EnchantmentRarity;
import xyz.devvydont.smprpg.enchantments.EnchantmentUtil;
import xyz.devvydont.smprpg.enchantments.recipe.EnchantmentRecipe;
import xyz.devvydont.smprpg.entity.MobType;
import xyz.devvydont.smprpg.entity.base.LeveledEntity;
import xyz.devvydont.smprpg.events.CustomEntityDamageByEntityEvent;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.services.AttributeService;
import xyz.devvydont.smprpg.services.EnchantmentService;
import xyz.devvydont.smprpg.services.EntityService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.persistence.KeyStore;

import java.util.Collection;
import java.util.List;

public class MinersFervorArtificeEnchantment extends CustomEnchantment implements Listener {

    public static final NamespacedKey MODIFIER_KEY = new NamespacedKey("smprpg", "fervor_boost");

    public static double getPercentageIncrease(int level) {
        return level * 0.5;
    }

    public MinersFervorArtificeEnchantment(String id) {
        super(id);
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
        return ComponentUtils.create("Miner's Fervor");
    }

    @Override
    public @NotNull TextColor getEnchantColor() {
        return ARTIFICE_COLOR;
    }

    @Override
    public Component getDescription() {
        return ComponentUtils.create("Harvest speed scales with your position relative to sea level.");
    }

    @Override
    public @NotNull Collection<Component> getLongDescription() {
        return List.of(ComponentUtils.merge(
    ComponentUtils.create("Increases harvest speed by "),
                ComponentUtils.create("+" + getPercentageIncrease(getLevel()) + "%", NamedTextColor.GREEN),
                ComponentUtils.create(" per block "),
                ComponentUtils.create("below", NamedTextColor.DARK_GRAY),
                ComponentUtils.create(" sea level", NamedTextColor.AQUA),
                ComponentUtils.create(",")),
            ComponentUtils.merge(
                ComponentUtils.create("but "),
                ComponentUtils.create("decreases", NamedTextColor.RED),
                ComponentUtils.create(" harvest speed by "),
                ComponentUtils.create("-" + (int) (getPercentageIncrease(getLevel()) * 2) + "%", NamedTextColor.RED),
                ComponentUtils.create(" per block "),
                ComponentUtils.create("above", NamedTextColor.YELLOW),
                ComponentUtils.create(" sea level", NamedTextColor.AQUA)
            )
        );
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_MINING;
    }

    @Override
    public int getAnvilCost() {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getWeight() {
        return EnchantmentRarity.ARTIFICE.getWeight();
    }

    @Override
    public EquipmentSlotGroup getEquipmentSlotGroup() {
        return EquipmentSlotGroup.MAINHAND;
    }

    @Override
    public int getSkillRequirement() {
        return 50;
    }

    /**
     * A set of enchantments that this enchantment conflicts with.
     * If there are none, this enchantment has no conflicts
     *
     * @return
     */
    @NotNull
    public RegistryKeySet<Enchantment> getConflictingEnchantments() {
        return RegistrySet.keySet(RegistryKey.ENCHANTMENT,
                EnchantmentService.VIGOROUS.getTypedKey());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerElevationChange(PlayerMoveEvent event) {
        var fromPos = event.getFrom();
        var toPos = event.getTo();

        // Optimize this at least a little bit by ignoring any non-integer changes in elevation.
        if (fromPos.getBlockY() == toPos.getBlockY())
            return;

        var player = event.getPlayer();
        var mainhandItem = player.getInventory().getItemInMainHand();

        if (!mainhandItem.containsEnchantment(this.getEnchantment()))
            return;

        var seaLevel = player.getWorld().getSeaLevel();
        var playerY = toPos.getBlockY();
        var blocksFromSeaLevel = seaLevel - playerY;
        var speedMod = blocksFromSeaLevel * (getPercentageIncrease(mainhandItem.getEnchantmentLevel(this.getEnchantment())) / 100.0);
        if (playerY > seaLevel) {
            speedMod *= 2;
        }

        var miningSpeedAttr = AttributeService.getInstance().getOrCreateAttribute(player, AttributeWrapper.MINING_SPEED);

        miningSpeedAttr.addModifier(new AttributeModifier(MODIFIER_KEY, speedMod, AttributeModifier.Operation.MULTIPLY_SCALAR_1));
        miningSpeedAttr.save(player, AttributeWrapper.MINING_SPEED);
    }

    @Override
    public int getMagicExperience() {
        return getLevel() * 200 * (1 + (getLevel() * 3 / getMaxLevel()));
    }
}
