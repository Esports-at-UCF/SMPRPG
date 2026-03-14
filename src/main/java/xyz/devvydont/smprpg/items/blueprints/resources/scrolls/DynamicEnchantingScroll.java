package xyz.devvydont.smprpg.items.blueprints.resources.scrolls;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemEnchantments;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.persistence.PersistentDataType;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.enchantments.CustomEnchantment;
import xyz.devvydont.smprpg.enchantments.EnchantmentRarity;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.ItemRarity;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.IHeaderDescribable;
import xyz.devvydont.smprpg.items.interfaces.ISellable;
import xyz.devvydont.smprpg.services.EnchantmentService;
import xyz.devvydont.smprpg.services.ItemService;

import java.util.List;

public class DynamicEnchantingScroll extends CustomItemBlueprint implements IHeaderDescribable, ISellable {

    public DynamicEnchantingScroll(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    /**
     * Determine what type of item this is.
     */
    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.ITEM;
    }

    /**
     * Given this item stack, how much should it be able to sell for?
     * Keep in mind that the size of the stack needs to considered as well!
     *
     * @param item The item that can be sold.
     * @return The worth of the item.
     */
    @Override
    public int getWorth(ItemStack item) {
        return 1_000 * item.getAmount();
    }

    @Override
    public void updateItemData(ItemStack item) {
        // Fallback to Smite if enchantment isn't generated
        if (item.getData(DataComponentTypes.STORED_ENCHANTMENTS) == null)
            item.setData(DataComponentTypes.STORED_ENCHANTMENTS, ItemEnchantments.itemEnchantments().add(Enchantment.SMITE, 255).build());

        item.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay().addHiddenComponents(DataComponentTypes.STORED_ENCHANTMENTS).build());
        super.updateItemData(item);
        item.editMeta(meta -> meta.setMaxStackSize(8));
    }

    @Override
    public List<Component> getHeader(ItemStack itemStack) {
        Enchantment enchant = (Enchantment) itemStack.getData(DataComponentTypes.STORED_ENCHANTMENTS).enchantments().keySet().toArray()[0];
        var color = enchant.description().color();
        if (color != null)
            color = color.equals(NamedTextColor.WHITE) ? NamedTextColor.GRAY : enchant.description().color();
        else {
            color = NamedTextColor.DARK_RED;
        }
        return List.of(
                enchant.displayName(0).color(color)
        );
    }

    @Override
    public ItemRarity getRarity(ItemStack item) {
        Enchantment enchant = (Enchantment) item.getData(DataComponentTypes.STORED_ENCHANTMENTS).enchantments().keySet().toArray()[0];
        if (enchant != null) {
            var weight = enchant.getWeight();
            if (weight == EnchantmentRarity.COMMON.getWeight())
                return ItemRarity.RARE;
            else if (weight == EnchantmentRarity.UNCOMMON.getWeight() || weight == EnchantmentRarity.CURSE.getWeight())
                return ItemRarity.EPIC;
            else if (weight == EnchantmentRarity.RARE.getWeight())
                return ItemRarity.LEGENDARY;
            else if (weight == EnchantmentRarity.BLESSING.getWeight())
                return ItemRarity.MYTHIC;
            else if (weight == EnchantmentRarity.ARTIFACT.getWeight())
                return ItemRarity.SPECIAL;
        }
        return getDefaultRarity();
    }

    public static ItemStack getScrollWithEnchantment(CustomEnchantment enchant) {
        var scroll = ItemService.generate(CustomItemType.ENCHANTING_SCROLL);
        scroll.setData(DataComponentTypes.STORED_ENCHANTMENTS, ItemEnchantments.itemEnchantments().add(enchant.getEnchantment(), 255).build());
        ItemService.blueprint(scroll).updateItemData(scroll);
        return scroll;
    }
}
