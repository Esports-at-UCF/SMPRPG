package xyz.devvydont.smprpg.services;

import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import xyz.devvydont.smprpg.blockbreaking.PacketManager;
import xyz.devvydont.smprpg.blockbreaking.SpeedConfigFileHandler;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.interfaces.IFueledEquipment;

public class BlockBreakingService implements IService, Listener {

    public SpeedConfigFileHandler filehandler;

    @Override
    public void setup() throws RuntimeException {

        filehandler = new SpeedConfigFileHandler();
        new PacketManager();
        System.out.println("we gaming");

    }

    @Override
    public void cleanup() {
    }
}
