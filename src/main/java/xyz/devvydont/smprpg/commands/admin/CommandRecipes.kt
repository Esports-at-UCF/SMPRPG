package xyz.devvydont.smprpg.commands.admin

import com.mojang.brigadier.Command
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.tree.LiteralCommandNode
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.commands.ICommand
import xyz.devvydont.smprpg.services.RecipeService
import xyz.devvydont.smprpg.util.formatting.ComponentUtils

/**
 * Admin command root for SMPRPG. Currently exposes recipe management.
 *
 * Usage: `/smprpg recipes reload` rebuilds the data-driven recipe registry from the recipe YAML
 * files without a server restart.
 */
class CommandRecipes : ICommand {

    override fun getRoot(): LiteralCommandNode<CommandSourceStack> {
        return Commands.literal("smprpg")
            .requires { it.sender.hasPermission("smprpg.command.admin") || it.sender.isOp }
            .then(
                Commands.literal("recipes")
                    .then(
                        Commands.literal("reload")
                            .executes(this::executeReload)
                    )
                    .then(
                        Commands.literal("export")
                            .executes(this::executeExport)
                    )
            )
            .build()
    }

    private fun executeReload(ctx: CommandContext<CommandSourceStack>): Int {
        val service = SMPRPG.getService(RecipeService::class.java)
        try {
            service.reload()
            ctx.source.sender.sendMessage(
                ComponentUtils.success("Reloaded custom recipes (${service.getRegistry().size} loaded). Check console for any skipped entries.")
            )
        } catch (e: Exception) {
            ctx.source.sender.sendMessage(ComponentUtils.error("Failed to reload recipes: ${e.message}"))
            SMPRPG.plugin.logger.severe("Failed to reload recipes: ${e.message}")
        }
        return Command.SINGLE_SUCCESS
    }

    private fun executeExport(ctx: CommandContext<CommandSourceStack>): Int {
        val service = SMPRPG.getService(RecipeService::class.java)
        try {
            val bundled = service.exportBundledRecipes()
            val compression = service.exportCompressionRecipes()
            val enchanting = service.exportEnchantingRecipes()
            ctx.source.sender.sendMessage(
                ComponentUtils.success("Exported $bundled bundled (overwritten) + $compression compression + $enchanting enchanting recipes to recipes/. Run /smprpg recipes reload to apply.")
            )
        } catch (e: Exception) {
            ctx.source.sender.sendMessage(ComponentUtils.error("Export failed: ${e.message}"))
            SMPRPG.plugin.logger.severe("Recipe export failed: ${e.message}")
        }
        return Command.SINGLE_SUCCESS
    }
}
