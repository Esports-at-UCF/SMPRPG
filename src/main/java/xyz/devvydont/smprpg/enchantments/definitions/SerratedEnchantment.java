package xyz.devvydont.smprpg.enchantments.definitions;

import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.enchantments.EnchantmentRarity;
import xyz.devvydont.smprpg.enchantments.base.AttributeEnchantment;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeModifierType;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.Collection;
import java.util.List;

public class SerratedEnchantment extends CustomEnchantment implements AttributeEnchantment {

    public static int getAdditionalPercentageIncrease(int level) {
        return switch (level) {
            case 0 -> 0;
            case 1 -> 10;
            case 2 -> 20;
            case 3 -> 30;
            case 4 -> 45;
            case 5 -> 60;
            case 6 -> 75;
            case 7 -> 100;
            default -> getAdditionalPercentageIncrease(5) + 25 * (level-7);
        };
    }

    public SerratedEnchantment(String id) {
        super(id);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return ComponentUtils.create("Serrated");
    }

    @Override
    public @NotNull Component getDescription() {
        return ComponentUtils.merge(
            ComponentUtils.create("Increases "),
            ComponentUtils.create("Critical Rating", NamedTextColor.BLUE),
            ComponentUtils.create(" by "),
            ComponentUtils.create("+" + getAdditionalPercentageIncrease(getLevel()), NamedTextColor.GREEN)
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
        return 7;
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
        return 11;
    }

    @Override
    public AttributeModifierType getAttributeModifierType() {
        return AttributeModifierType.ENCHANTMENT;
    }

    @Override
    public Collection<AttributeEntry> getHeldAttributes() {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.CRITICAL_DAMAGE, getAdditionalPercentageIncrease(this.getLevel()))
        );
    }

    @Override
    public int getPowerRating() {
        return this.getLevel() / 3;
    }
}
