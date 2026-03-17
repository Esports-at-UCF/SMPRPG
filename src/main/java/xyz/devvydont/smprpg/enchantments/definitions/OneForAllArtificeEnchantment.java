package xyz.devvydont.smprpg.enchantments.definitions;

import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.registry.set.RegistrySet;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.enchantments.EnchantmentRarity;
import xyz.devvydont.smprpg.enchantments.base.AttributeEnchantment;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeModifierType;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.Collection;
import java.util.List;

public class OneForAllArtificeEnchantment extends CustomEnchantment implements AttributeEnchantment {

    public OneForAllArtificeEnchantment(String id) {
        super(id);
    }

    @Override
    public @NotNull Component getDisplayName() {
        // Roman Numeral is hardcoded for this one.
        return ComponentUtils.create("One for All I");
    }

    @Override
    public @NotNull TextColor getEnchantColor() {
        return ARTIFICE_COLOR;
    }

    @Override
    public @NotNull Collection<Component> getLongDescription() {
        return List.of(ComponentUtils.merge(
        ComponentUtils.create("Multiplies "),
                    ComponentUtils.create(AttributeWrapper.STRENGTH.DisplayName, NamedTextColor.GOLD),
                    ComponentUtils.create(" by "),
                    ComponentUtils.create("10x", NamedTextColor.GREEN),
                    ComponentUtils.create(", but no other")
                ),
                ComponentUtils.create("enchantments may be present on this weapon.")
        );
    }

    @Override
    public @NotNull Component getDescription() {
        return ComponentUtils.merge(
                ComponentUtils.create("Multiplies "),
                ComponentUtils.create(AttributeWrapper.STRENGTH.DisplayName, NamedTextColor.GOLD),
                ComponentUtils.create(" by "),
                ComponentUtils.create("10x", NamedTextColor.GREEN),
                ComponentUtils.create(", but no other enchantments may be present on this weapon.")
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
        return 1;
    }

    @Override
    public int getWeight() {
        return EnchantmentRarity.ARTIFICE.getWeight();
    }

    @Override
    public EquipmentSlotGroup getEquipmentSlotGroup() {
        return EquipmentSlotGroup.ANY;
    }

    @Override
    public int getSkillRequirement() {
        return 60;
    }

    @Override
    public AttributeModifierType getAttributeModifierType() {
        return AttributeModifierType.ENCHANTMENT;
    }

    @Override
    public Collection<AttributeEntry> getHeldAttributes() {
        return List.of(
                new MultiplicativeAttributeEntry(AttributeWrapper.STRENGTH, 10.0)
        );
    }

    @Override
    public int getPowerRating() {
        return getLevel() / 2 + 1;
    }

    @Override
    public RegistryKeySet<@NotNull Enchantment> getConflictingEnchantments() {
        return RegistrySet.keySet(RegistryKey.ENCHANTMENT);

        // \/ So this code *should* work, the issue is that this is called during bootstrap.
        //    We will just manually check for conflicts on enchant in this case since it's a LOT of enchantments and tech debt otherwise.

        // var registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
        // List<Enchantment> allEnchants = registry.stream().toList();
        // allEnchants.remove(getEnchantment());
        // return RegistrySet.keySetFromValues(RegistryKey.ENCHANTMENT, allEnchants);
    }
}
