package xyz.devvydont.smprpg.market.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.tree.LiteralCommandNode
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import org.bukkit.entity.Player
import xyz.devvydont.smprpg.commands.ICommand
import xyz.devvydont.smprpg.market.gui.auction.MenuAuctionBrowser
import xyz.devvydont.smprpg.market.gui.auction.MenuAuctionClaimBox
import xyz.devvydont.smprpg.market.gui.auction.MenuAuctionCreate
import xyz.devvydont.smprpg.market.gui.auction.MenuAuctionManage

class CommandAuctionHouse : ICommand {

    override fun getRoot(): LiteralCommandNode<CommandSourceStack> {
        return Commands.literal("ah")
            .executes { ctx ->
                val sender = ctx.source.executor
                if (sender is Player) {
                    MenuAuctionBrowser(sender).openMenu()
                }
                Command.SINGLE_SUCCESS
            }
            .then(
                Commands.literal("sell")
                    .executes { ctx ->
                        val sender = ctx.source.executor
                        if (sender is Player) {
                            MenuAuctionCreate(sender).openMenu()
                        }
                        Command.SINGLE_SUCCESS
                    }
            )
            .then(
                Commands.literal("manage")
                    .executes { ctx ->
                        val sender = ctx.source.executor
                        if (sender is Player) {
                            MenuAuctionManage(sender).openMenu()
                        }
                        Command.SINGLE_SUCCESS
                    }
            )
            .then(
                Commands.literal("claim")
                    .executes { ctx ->
                        val sender = ctx.source.executor
                        if (sender is Player) {
                            MenuAuctionClaimBox(sender).openMenu()
                        }
                        Command.SINGLE_SUCCESS
                    }
            )
            .then(
                Commands.literal("search")
                    .then(
                        Commands.argument("query", StringArgumentType.greedyString())
                            .executes { ctx ->
                                val sender = ctx.source.executor
                                if (sender is Player) {
                                    val query = StringArgumentType.getString(ctx, "query")
                                    MenuAuctionBrowser(sender, null, query).openMenu()
                                }
                                Command.SINGLE_SUCCESS
                            }
                    )
            )
            .build()
    }
}
