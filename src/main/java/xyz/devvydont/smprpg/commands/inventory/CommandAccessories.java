package xyz.devvydont.smprpg.commands.inventory;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.devvydont.smprpg.commands.CommandSimplePlayer;
import xyz.devvydont.smprpg.gui.player.MenuAccessoryInventory;
import xyz.devvydont.smprpg.gui.player.MenuInventoryPeek;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;

public class CommandAccessories extends CommandSimplePlayer {
    public CommandAccessories(String name) {
        super(name);
    }

    @Override
    protected void playerInvoked(@NotNull Player player, @NotNull CommandSourceStack ctx, @NotNull String @NotNull [] args) {
        new MenuAccessoryInventory(player).openMenu();
    }
}
