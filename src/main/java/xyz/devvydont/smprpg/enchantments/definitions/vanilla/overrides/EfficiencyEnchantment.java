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
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.enchantments.EnchantmentRarity;
import xyz.devvydont.smprpg.enchantments.base.AttributeEnchantment;
import xyz.devvydont.smprpg.enchantments.definitions.vanilla.VanillaEnchantment;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeModifierType;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.Collection;
import java.util.List;

public class EfficiencyEnchantment extends VanillaEnchantment implements AttributeEnchantment {

    public EfficiencyEnchantment(TypedKey<Enchantment> key) {
        super(key);
    }

    public static int getMiningEfficiency(int level) {
        return level * 100;
    }

    @Override
    public AttributeModifierType getAttributeModifierType() {
        return AttributeModifierType.ENCHANTMENT;
    }

    @Override
    public Collection<AttributeEntry> getHeldAttributes() {
        return List.of(
                AttributeEntry.additive(AttributeWrapper.MINING_SPEED, getMiningEfficiency(getLevel()))
        );
    }

    @Override
    public int getPowerRating() {
        return getLevel() / 2;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return ComponentUtils.create("Efficiency");
    }

    @Override
    public @NotNull Component getDescription() {
        return ComponentUtils.merge(
                ComponentUtils.create("Increases mining speed by "),
                ComponentUtils.create("+" + getMiningEfficiency(getLevel()), NamedTextColor.GREEN)
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
}
