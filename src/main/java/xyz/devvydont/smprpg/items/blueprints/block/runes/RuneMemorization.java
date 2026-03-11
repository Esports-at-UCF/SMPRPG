package xyz.devvydont.smprpg.items.blueprints.block.runes;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.ItemStack;
import xyz.devvydont.smprpg.block.CustomBlock;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.blueprints.block.BlockBlueprint;
import xyz.devvydont.smprpg.items.interfaces.IFooterDescribable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

import java.util.List;

public class RuneMemorization extends BlockBlueprint implements IFooterDescribable {

    public RuneMemorization(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public CustomBlock getCustomBlock() {
        return CustomBlock.RUNE_MEMORIZATION;
    }

    @Override
    public List<Component> getFooter(ItemStack itemStack) {
        return List.of(
                ComponentUtils.create("When placed under an Enchanting Table:", NamedTextColor.LIGHT_PURPLE),
                ComponentUtils.merge(
                        ComponentUtils.create("Grants a ", NamedTextColor.GRAY),
                        ComponentUtils.create("+5%", NamedTextColor.DARK_PURPLE),
                        ComponentUtils.create(" chance to not consume ", NamedTextColor.GRAY),
                        ComponentUtils.create("Scrolls of Imbuement", NamedTextColor.BLUE),
                        ComponentUtils.create(".", NamedTextColor.GRAY)
                ),
                ComponentUtils.EMPTY,
                ComponentUtils.create("When placed directly under an Enchanting Table:", NamedTextColor.LIGHT_PURPLE),
                ComponentUtils.merge(
                        ComponentUtils.create("Grants a ", NamedTextColor.GRAY),
                        ComponentUtils.create("+10%", NamedTextColor.DARK_PURPLE),
                        ComponentUtils.create(" chance to not consume ", NamedTextColor.GRAY),
                        ComponentUtils.create("Scrolls of Imbuement", NamedTextColor.BLUE),
                        ComponentUtils.create(".", NamedTextColor.GRAY)
                )
        );
    }
}
