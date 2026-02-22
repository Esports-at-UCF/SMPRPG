package xyz.devvydont.smprpg

import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.datapack.DatapackRegistrar
import io.papermc.paper.plugin.bootstrap.BootstrapContext
import io.papermc.paper.plugin.bootstrap.PluginBootstrap
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager
import io.papermc.paper.plugin.lifecycle.event.handler.LifecycleEventHandler
import io.papermc.paper.plugin.lifecycle.event.registrar.RegistrarEvent
import io.papermc.paper.plugin.lifecycle.event.registrar.ReloadableRegistrarEvent
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import xyz.devvydont.smprpg.commands.ICommand
import xyz.devvydont.smprpg.commands.ICommandBase
import xyz.devvydont.smprpg.commands.LegacyCommand
import xyz.devvydont.smprpg.commands.admin.CommandAttribute
import xyz.devvydont.smprpg.commands.admin.CommandEcoAdmin
import xyz.devvydont.smprpg.commands.admin.CommandSimulateFishing
import xyz.devvydont.smprpg.commands.admin.CommandSteal
import xyz.devvydont.smprpg.commands.entity.CommandSummon
import xyz.devvydont.smprpg.commands.inventory.CommandPeek
import xyz.devvydont.smprpg.commands.items.CommandGiveItem
import xyz.devvydont.smprpg.commands.items.CommandReforge
import xyz.devvydont.smprpg.commands.items.CommandSearchItem
import xyz.devvydont.smprpg.commands.player.*
import xyz.devvydont.smprpg.fishing.gui.LootTypeChancesMenu
import xyz.devvydont.smprpg.gui.MainMenu
import xyz.devvydont.smprpg.gui.MenuReforgeBrowser
import xyz.devvydont.smprpg.gui.economy.MenuDeposit
import xyz.devvydont.smprpg.gui.economy.MenuWithdraw
import xyz.devvydont.smprpg.gui.enchantments.EnchantmentMenu
import xyz.devvydont.smprpg.gui.items.MenuTrashItems
import xyz.devvydont.smprpg.gui.player.MenuDifficultyChooser
import xyz.devvydont.smprpg.services.EnchantmentService
import java.io.IOException
import java.net.URI
import java.net.URISyntaxException


@Suppress("unused")
class SMPRPGBootstrapper : PluginBootstrap {
    private fun bootstrapCommands(context: BootstrapContext) {
        val commandsToRegister: Array<ICommandBase> = arrayOf(

            // Old commands to be moved to the new API.
            CommandSimulateFishing("simulatefishing"),
            CommandGiveItem("give"),
            CommandSearchItem("search"),
            CommandSummon("summon"),
            CommandPeek("peek"),

            // New commands that use the new API.
            CommandSteal(),
            CommandAttribute(),
            CommandEcoAdmin(),
            CommandSkill(),
            CommandBalance("balance"),
            CommandBalance("bal"),  // Effectively functions as an alias. We can register the same command multiple times!
            CommandStatistics("statistics"),
            CommandStatistics("stats"),
            CommandWhatAmIHolding("whatamiholding"),
            CommandWhatAmIHolding("waih"),
            CommandBalanceTop("balancetop"),
            CommandBalanceTop("topbalance"),
            CommandBalanceTop("baltop"),
            CommandReforge(),
            ICommand.SimplePlayerCommand("menu", { player -> MainMenu(player).openMenu()}),
            ICommand.SimplePlayerCommand("difficulty", { player -> MenuDifficultyChooser(player).openMenu()}),
            ICommand.SimplePlayerCommand("fishing", { player -> LootTypeChancesMenu(player).openMenu()}),
            ICommand.SimplePlayerCommand("deposit", { player -> MenuDeposit(player).openMenu()}),
            ICommand.SimplePlayerCommand("withdrawal", { player -> MenuWithdraw(player).openMenu()}),
            ICommand.SimplePlayerCommand("trash", { player -> MenuTrashItems(player).openMenu()}),
            ICommand.SimplePlayerCommand("enchantments", { player -> EnchantmentMenu(player).openMenu()}),
            ICommand.SimplePlayerCommand("reforges", { player -> MenuReforgeBrowser(player).openMenu()}),
        )

        val manager: LifecycleEventManager<BootstrapContext> = context.lifecycleManager
        manager.registerEventHandler<ReloadableRegistrarEvent<Commands>>(
            LifecycleEvents.COMMANDS,
            LifecycleEventHandler { event: ReloadableRegistrarEvent<Commands> ->
                val commands = event.registrar()
                for (command in commandsToRegister) {

                    if (command is LegacyCommand)
                        commands.register(
                        command.name,
                        command.description,
                        command.aliases,
                        command
                        )

                    if (command is ICommand)
                        commands.register(command.getRoot())
                }
            })
    }

    private fun bootstrapEnchantments(context: BootstrapContext) {
        for (enchantment in EnchantmentService.CUSTOM_ENCHANTMENTS)
            enchantment.bootstrap(context)
    }

    private fun bootstrapDatapack(context: BootstrapContext) {
        context.lifecycleManager.registerEventHandler(
            LifecycleEvents.DATAPACK_DISCOVERY.newHandler(
                LifecycleEventHandler { event: RegistrarEvent<DatapackRegistrar> ->
                    try {
                        // Retrieve the URI of the datapack folder.
                        val uri: URI = this.javaClass.getResource("/smprpg-data")!!.toURI()
                        // Discover the pack. The ID is set to "provided", which indicates to
                        // a server owner that your plugin includes this data pack.
                        event.registrar().discoverPack(uri, "provided")
                    } catch (e: URISyntaxException) {
                        throw RuntimeException(e)
                    } catch (e: IOException) {
                        throw RuntimeException(e)
                    }
                }
            ))
    }

    override fun bootstrap(bootstrapContext: BootstrapContext) {
        bootstrapCommands(bootstrapContext)
        bootstrapEnchantments(bootstrapContext)
        bootstrapDatapack(bootstrapContext)
    }


}
