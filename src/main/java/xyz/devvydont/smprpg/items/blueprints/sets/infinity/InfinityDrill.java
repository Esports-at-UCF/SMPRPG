package xyz.devvydont.smprpg.items.blueprints.sets.infinity;

import io.papermc.paper.datacomponent.item.Tool;
import org.bukkit.NamespacedKey;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.interfaces.IFueledEquipment;
import xyz.devvydont.smprpg.items.tools.ItemDrill;
import xyz.devvydont.smprpg.services.ItemService;

public class InfinityDrill extends ItemDrill implements IFueledEquipment {

    public static final Tool TOOL_COMP = Tool.tool()
            .defaultMiningSpeed(0.0001f)
            .build();

    public InfinityDrill(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public int getPowerRating() {
        return 1000;
    }

    @Override
    public double getDrillMiningPower() { return 99; }

    @Override
    public double getDrillDamage() { return 999_99; }

    @Override
    public double getDrillFortune() { return 1000; }

    @Override
    public double getDrillSpeed() { return 10000; }

    @Override
    public int getMaxFuel() {
        return 999_999 + IFueledEquipment.FUEL_OFFSET;
    }

}
