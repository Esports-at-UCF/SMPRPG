package xyz.devvydont.smprpg.enchantments.definitions;

import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.registry.set.RegistrySet;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.ability.AbilityCost;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.enchantments.EnchantmentRarity;
import xyz.devvydont.smprpg.events.abilities.AbilityCastEvent;
import xyz.devvydont.smprpg.services.ActionBarService;
import xyz.devvydont.smprpg.services.DropsService;
import xyz.devvydont.smprpg.services.EnchantmentService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.Map;
import java.util.UUID;

public class IgnoranceBlessing extends CustomEnchantment implements Listener {

    public IgnoranceBlessing(String id) {
        super(id);
    }

    public static int getManaCostReduction(int level) {
        return level * 10;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return ComponentUtils.create("Blessing of Ignorance", NamedTextColor.YELLOW);
    }

    @Override
    public @NotNull TextColor getEnchantColor() {
        return NamedTextColor.YELLOW;
    }

    @Override
    public @NotNull Component getDescription() {
        return ComponentUtils.merge(
            ComponentUtils.create("Reduces the cost of "),
            ComponentUtils.create("mana based attacks and abilities", NamedTextColor.AQUA),
            ComponentUtils.create(" by " + getManaCostReduction(getLevel()) + "%")
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
    public EquipmentSlotGroup getEquipmentSlotGroup() {
        return EquipmentSlotGroup.HEAD;
    }

    @Override
    public int getSkillRequirement() {
        return 25;
    }

    @Override
    public @NotNull RegistryKeySet<Enchantment> getConflictingEnchantments() {
        return RegistrySet.keySet(RegistryKey.ENCHANTMENT,
                EnchantmentService.KEEPING_BLESSING.getTypedKey(),
                EnchantmentService.MERCY_BLESSING.getTypedKey(),
                EnchantmentService.VOIDSTRIDING_BLESSING.getTypedKey(),
                EnchantmentService.REPLENISHING.getTypedKey(),
                EnchantmentService.TELEKINESIS_BLESSING.getTypedKey()
        );
    }

    @EventHandler
    private void __onAbilityCast(AbilityCastEvent event){
        if (event.getAbilityCost().resource == AbilityCost.Resource.MANA) {
            var ench = event.getItem().getEnchantmentLevel(this.getEnchantment());
            if (ench > 0)
                event.setAbilityCost(event.getAbilityCost().reduce(getManaCostReduction(ench) / 100.0));
        }
    }
}
