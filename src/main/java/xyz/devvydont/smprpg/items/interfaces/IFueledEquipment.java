package xyz.devvydont.smprpg.items.interfaces;

import org.bukkit.NamespacedKey;
import xyz.devvydont.smprpg.SMPRPG;

public interface IFueledEquipment {

    NamespacedKey fuelKey = new NamespacedKey(SMPRPG.getInstance(), "fuel");
    NamespacedKey maxFuelKey = new NamespacedKey(SMPRPG.getInstance(), "max_fuel");
    int FUEL_OFFSET = 1;

    int getMaxFuel();

}
