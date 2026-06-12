package xyz.devvydont.smprpg.market.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.tree.LiteralCommandNode
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import org.bukkit.entity.Player
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.commands.ICommand
import xyz.devvydont.smprpg.market.MarketService
import xyz.devvydont.smprpg.market.gui.bazaar.MenuBazaarBrowser
import xyz.devvydont.smprpg.market.gui.bazaar.MenuBazaarCategory
import xyz.devvydont.smprpg.util.formatting.ComponentUtils

class CommandBazaar : ICommand {

    override fun getRoot(): LiteralCommandNode<CommandSourceStack> {
        return Commands.literal("bz")
            .executes { ctx ->
                val sender = ctx.source.executor
                if (sender is Player) {
                    MenuBazaarBrowser(sender).openMenu()
                }
                Command.SINGLE_SUCCESS
            }
            .then(
                Commands.literal("search")
                    .then(
                        Commands.argument("query", StringArgumentType.greedyString())
                            .executes { ctx ->
                                val sender = ctx.source.executor
                                if (sender is Player) {
                                    val query = StringArgumentType.getString(ctx, "query")
                                    openSearchResults(sender, query)
                                }
                                Command.SINGLE_SUCCESS
                            }
                    )
            )
            .build()
    }

    private fun openSearchResults(player: Player, query: String) {
        val bazaarManager = SMPRPG.getService(MarketService::class.java).bazaarManager
        val results = bazaarManager.searchItems(query)

        if (results.isEmpty()) {
            player.sendMessage(ComponentUtils.error("No bazaar items found for \"$query\""))
            return
        }

        MenuBazaarCategory(
            player,
            "Search: $query",
            { bazaarManager.searchItems(query) },
            null
        ).openMenu()
    }
}
