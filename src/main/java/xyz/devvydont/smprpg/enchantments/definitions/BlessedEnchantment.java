package xyz.devvydont.smprpg.enchantments.definitions;

import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.keys.EnchantmentKeys;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.registry.set.RegistrySet;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.enchantments.EnchantmentRarity;
import xyz.devvydont.smprpg.enchantments.EnchantmentUtil;
import xyz.devvydont.smprpg.entity.MobType;
import xyz.devvydont.smprpg.entity.base.LeveledEntity;
import xyz.devvydont.smprpg.events.CustomEntityDamageByEntityEvent;
import xyz.devvydont.smprpg.services.EnchantmentService;
import xyz.devvydont.smprpg.services.EntityService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

public class BlessedEnchantment extends CustomEnchantment implements Listener {

    public static boolean isNether(LeveledEntity<?> entity) {
        if (entity.mobTypes.contains(MobType.NETHER))
            return true;
        return false;
    }

    public BlessedEnchantment(String id) {
        super(id);
    }

    public static int getPercentageIncrease(int level) {
        return level * 30;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return ComponentUtils.create("Blessed");
    }

    @Override
    public @NotNull Component getDescription() {
        return ComponentUtils.merge(
                ComponentUtils.create("Increases damage dealt by "),
                ComponentUtils.create("+" + getPercentageIncrease(getLevel()) + "%", NamedTextColor.GREEN),
                ComponentUtils.create(" against "),
                ComponentUtils.create(MobType.NETHER.getSymbol(), MobType.NETHER.getSymbolColor()),
                ComponentUtils.create(" Nether", NamedTextColor.RED),
                ComponentUtils.create(" mobs.")
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
        return EnchantmentRarity.COMMON.getWeight();
    }

    @Override
    public EquipmentSlotGroup getEquipmentSlotGroup() {
        return EquipmentSlotGroup.HAND;
    }

    @Override
    public int getSkillRequirement() {
        return 19;
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
                EnchantmentKeys.SMITE,
                EnchantmentKeys.BANE_OF_ARTHROPODS,
                EnchantmentService.GENESIS.getTypedKey(),
                EnchantmentService.VIGILANTE.getTypedKey(),
                EnchantmentService.MUFFLE.getTypedKey());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDamageNetherMob(CustomEntityDamageByEntityEvent event) {

        // Skip non undead
        if (!isNether(SMPRPG.getService(EntityService.class).getEntityInstance(event.damaged)))
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
}
