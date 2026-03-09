package xyz.devvydont.smprpg.items.blueprints.resources.scrolls;

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
import org.bukkit.persistence.PersistentDataType;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint;
import xyz.devvydont.smprpg.items.interfaces.IHeaderDescribable;
import xyz.devvydont.smprpg.items.interfaces.ISellable;
import xyz.devvydont.smprpg.services.EnchantmentService;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.List;

public class DynamicEnchantingScroll extends CustomItemBlueprint implements IHeaderDescribable, ISellable {

    public DynamicEnchantingScroll(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    public static final NamespacedKey SCROLL_ENCHANT_TYPE_KEY = new NamespacedKey(SMPRPG.getPlugin(), "enchant_key");

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
        super.updateItemData(item);
        item.editMeta(meta -> meta.setMaxStackSize(8));
    }

    @Override
    public List<Component> getHeader(ItemStack itemStack) {
        var enchantKey = itemStack.getPersistentDataContainer().getOrDefault(SCROLL_ENCHANT_TYPE_KEY, PersistentDataType.STRING, Enchantment.SHARPNESS.getKey().toString());
        var enchant = SMPRPG.getService(EnchantmentService.class).getEnchantment(TypedKey.create(RegistryKey.ENCHANTMENT, enchantKey));
        var color = enchant.description().color().equals(NamedTextColor.WHITE) ? NamedTextColor.DARK_GRAY : enchant.description().color();
        return List.of(
                enchant.displayName(0).decorate(TextDecoration.ITALIC).color(color)
        );
    }
}
