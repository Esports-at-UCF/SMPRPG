package xyz.devvydont.smprpg.items.blueprints.vanilla;

import org.bukkit.Material;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.base.VanillaAttributeItem;
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.items.ToolGlobals;

import java.util.Collection;
import java.util.List;


public class ItemSpear extends VanillaAttributeItem implements IBreakableEquipment {

    public static double getSpearDamage(Material material) {
        return switch (material) {
            case NETHERITE_SPEAR -> 40;
            case DIAMOND_SPEAR -> 25;
            case GOLDEN_SPEAR -> 18;
            case IRON_SPEAR -> 15;
            case STONE_SPEAR, COPPER_SPEAR -> 10;
            case WOODEN_SPEAR -> 8;
            default -> 0;
        };
    }

    public static double getSpearDamage(CustomItemType itemType) {
        return switch (itemType) {
            case CustomItemType.TIN_SPEAR -> 9.0;
            case CustomItemType.SILVER_SPEAR -> 13.0;
            case CustomItemType.BRONZE_SPEAR -> 15.0;
            case CustomItemType.STEEL_SPEAR -> 18.0;
            case CustomItemType.ROSE_GOLD_SPEAR, CustomItemType.MITHRIL_SPEAR, CustomItemType.COBALT_SPEAR -> 20.0;
            case CustomItemType.TITANIUM_SPEAR, CustomItemType.TUNGSTEN_SPEAR -> 25.0;
            case CustomItemType.ADAMANTIUM_SPEAR -> 33.0;
            case CustomItemType.ORICHALCUM_SPEAR -> 35.0;
            case CustomItemType.DRAGONSTEEL_SPEAR -> 60.0;
            default -> 0;
        };
    }

    public static int getSpearRating(Material material) {
        return switch (material) {
            case NETHERITE_SPEAR -> ToolGlobals.NETHERITE_TOOL_POWER;
            case DIAMOND_SPEAR -> ToolGlobals.DIAMOND_TOOL_POWER;
            case GOLDEN_SPEAR -> ToolGlobals.GOLD_TOOL_POWER;
            case TRIDENT, IRON_SPEAR -> ToolGlobals.IRON_TOOL_POWER;
            case STONE_SPEAR -> ToolGlobals.STONE_TOOL_POWER;
            case WOODEN_SPEAR -> ToolGlobals.WOOD_TOOL_POWER;
            case COPPER_SPEAR -> ToolGlobals.COPPER_TOOL_POWER;
            default -> 1;
        };
    }

    public static double getSpearRecovery(Material material) {
        return switch (material) {
            case NETHERITE_SPEAR -> -0.85;
            case DIAMOND_SPEAR -> -0.8;
            case IRON_SPEAR, GOLDEN_SPEAR -> -0.75;
            case COPPER_SPEAR -> -0.7;
            case STONE_SPEAR -> -.65;
            case WOODEN_SPEAR -> -.6;
            default -> -1.0;
        };
    }

    public static double getSpearRecovery(CustomItemType itemType) {
        return switch (itemType) {
            case TIN_SPEAR -> -0.6;
            case SILVER_SPEAR -> -0.65;
            case BRONZE_SPEAR -> -0.7;
            case STEEL_SPEAR, MITHRIL_SPEAR, TITANIUM_SPEAR, ADAMANTIUM_SPEAR -> -0.75;
            case COBALT_SPEAR -> -0.75;
            case TUNGSTEN_SPEAR -> -0.8;
            case ORICHALCUM_SPEAR -> -0.85;
            case DRAGONSTEEL_SPEAR -> -0.9;
            default -> -1.0;
        };
    }

    public ItemSpear(ItemService itemService, Material material) {
        super(itemService, material);
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.SPEAR;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.STRENGTH, getSpearDamage(material)),
                new MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, getSpearRecovery(material))
        );
    }

    @Override
    public int getPowerRating() {
        return getSpearRating(material);
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.MAINHAND;
    }

    @Override
    public int getMaxDurability() {
        return switch (material) {
            case NETHERITE_SPEAR -> ToolGlobals.NETHERITE_TOOL_DURABILITY;
            case DIAMOND_SPEAR -> ToolGlobals.DIAMOND_TOOL_DURABILITY;
            case GOLDEN_SPEAR -> ToolGlobals.GOLD_TOOL_DURABILITY;
            case IRON_SPEAR -> ToolGlobals.IRON_TOOL_DURABILITY;
            case STONE_SPEAR -> ToolGlobals.STONE_TOOL_DURABILITY;
            case WOODEN_SPEAR -> ToolGlobals.WOOD_TOOL_DURABILITY;
            case COPPER_SPEAR -> ToolGlobals.COPPER_TOOL_DURABILITY;
            default -> 50_000;
        };
    }
}
