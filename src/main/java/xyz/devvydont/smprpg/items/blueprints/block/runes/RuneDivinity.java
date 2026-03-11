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

public class RuneDivinity extends BlockBlueprint implements IFooterDescribable {

    public RuneDivinity(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public CustomBlock getCustomBlock() {
        return CustomBlock.RUNE_DIVINITY;
    }

    @Override
    public List<Component> getFooter(ItemStack itemStack) {
        return List.of(
                ComponentUtils.create("When 4 Runes are placed under an Enchanting Table:", NamedTextColor.LIGHT_PURPLE),
                ComponentUtils.merge(
                        ComponentUtils.create("Unlocks the ability to apply ", NamedTextColor.GRAY),
                        ComponentUtils.create("Blessings ", NamedTextColor.YELLOW),
                        ComponentUtils.create("to items.", NamedTextColor.GRAY)
                )
        );
    }
}
