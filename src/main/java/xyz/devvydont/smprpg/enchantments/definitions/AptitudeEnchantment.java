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
import xyz.devvydont.smprpg.util.formatting.Symbols;

import java.util.Collection;
import java.util.List;

public class AptitudeEnchantment extends CustomEnchantment implements AttributeEnchantment {

    public static int getIntelligenceIncrease(int level) {
        return switch (level) {
            case 0 -> 0;
            case 1 -> 20;
            case 2 -> 40;
            case 3 -> 60;
            case 4 -> 80;
            case 5 -> 100;
            case 6 -> 120;
            case 7 -> 140;
            case 8 -> 160;
            case 9 -> 180;
            case 10 -> 200;
            default -> getIntelligenceIncrease(10) + 20*(level - 10);
        };
    }

    public AptitudeEnchantment(String id) {
        super(id);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return ComponentUtils.create("Aptitude");
    }

    @Override
    public @NotNull Component getDescription() {
        return ComponentUtils.merge(
            ComponentUtils.create("Increases intelligence by "),
            ComponentUtils.create("+" + getIntelligenceIncrease(getLevel()), NamedTextColor.GREEN),
            ComponentUtils.create(Symbols.MANA, NamedTextColor.AQUA)
        );
    }

    @Override
    public TagKey<ItemType> getItemTypeTag() {
        return ItemTypeTagKeys.ENCHANTABLE_VANISHING;
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
        return EquipmentSlotGroup.OFFHAND;
    }

    @Override
    public int getSkillRequirement() {
        return 10;
    }

    @Override
    public AttributeModifierType getAttributeModifierType() {
        return AttributeModifierType.ENCHANTMENT;
    }

    @Override
    public Collection<AttributeEntry> getHeldAttributes() {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.INTELLIGENCE, getIntelligenceIncrease(getLevel()))
        );
    }

    @Override
    public int getPowerRating() {
        return getLevel() / 2 + 1;
    }
}
