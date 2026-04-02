package xyz.devvydont.smprpg.block

import net.momirealms.craftengine.bukkit.api.CraftEngineBlocks
import net.momirealms.craftengine.bukkit.api.event.CraftEngineReloadEvent
import net.momirealms.craftengine.core.util.Key
import org.bukkit.Material
import org.bukkit.block.BlockState
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import xyz.devvydont.smprpg.SMPRPG.Companion.plugin
import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.block.BlockLoot.Companion.of
import xyz.devvydont.smprpg.block.BlockLootEntry.Companion.builder
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.services.ItemService
import java.util.*

/**
 * The loot dropped from blocks is a very complex system. When blocks are dropped, there's conditions
 * that can be met that completely shift the result of what is dropped.
 * The idea with our plugin, is that we hook into specific events to override the drops of such events, and assume
 * vanilla (unchanged) behavior when our plugin doesn't have a case defined. Think of events as follows:
 * - Breaking a block with a preferred tool. (pickaxe breaking stone)
 * - Breaking a block with silk touch.
 * - Breaking a block with auto smelt.
 * - Breaking a block with incorrect tool. (fist and stone, or axe and stone)
 */
object BlockLootRegistry : Listener {
    private val entries: MutableMap<Material?, BlockLootEntry?> =
        EnumMap<Material?, BlockLootEntry?>(Material::class.java)
    private val specialEntries: MutableMap<Key?, BlockLootEntry?> = mutableMapOf()

    // Inputs necessary block drop overrides. Keep in mind, we only need to add OVERRIDES. If the vanilla behavior
    // is fine, then you can omit it :)
    // Just keep in mind, vanilla drops will NOT be affected by any fortune stats, so you do need to add all ores etc.
    init {
        plugin.server.pluginManager.registerEvents(this, plugin)
        registerBlockLoot()
    }

    fun registerBlockLoot() {
        entries.clear()
        specialEntries.clear()
        // Mining.

        register(
            Material.STONE, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.STONE)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.STONE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.COBBLESTONE)))
                .build()
        )

        register(
            Material.COBBLESTONE, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.STONE)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.STONE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.COBBLESTONE)))
                .build()
        )

        register(
            Material.DEEPSLATE, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.DEEPSLATE)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.DEEPSLATE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.COBBLED_DEEPSLATE)))
                .build()
        )

        register(
            Material.COBBLED_DEEPSLATE, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.DEEPSLATE)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.COBBLED_DEEPSLATE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.COBBLED_DEEPSLATE)))
                .build()
        )

        register(
            Material.GRANITE, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.GRANITE)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.GRANITE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.GRANITE)))
                .build()
        )

        register(
            Material.DIORITE, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.DIORITE)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.DIORITE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.DIORITE)))
                .build()
        )

        register(
            Material.ANDESITE, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.ANDESITE)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.ANDESITE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.ANDESITE)))
                .build()
        )

        register(
            Material.SAND, BlockLootEntry.Companion.builder()
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.GLASS)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.SAND)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.SAND)))
                .uses(AttributeWrapper.MINING_FORTUNE)
                .build()
        )

        register(
            Material.GRAVEL, BlockLootEntry.Companion.builder()
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.GRAVEL)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.GRAVEL)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.GRAVEL)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.FLINT), .2))
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.FLINT), .2))
                .uses(AttributeWrapper.MINING_FORTUNE)
                .build()
        )

        register(
            Material.WET_SPONGE, BlockLootEntry.Companion.builder()
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.SPONGE)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.WET_SPONGE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.WET_SPONGE)))
                .add(BlockLootContext.INCORRECT_TOOL, of(ItemService.generate(Material.WET_SPONGE)))
                .uses(AttributeWrapper.MINING_FORTUNE)
                .build()
        )

        register(
            Material.END_STONE, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.END_STONE)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.END_STONE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.END_STONE)))
                .build()
        )

        register(
            Material.OBSIDIAN, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.OBSIDIAN)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.OBSIDIAN)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.OBSIDIAN)))
                .build()
        )

        register(
            Material.AMETHYST_BLOCK, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.AMETHYST_BLOCK)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.AMETHYST_BLOCK)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.AMETHYST_BLOCK)))
                .build()
        )

        register(
            Material.AMETHYST_CLUSTER, BlockLootEntry.Companion.builder()
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.AMETHYST_SHARD)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.AMETHYST_CLUSTER)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.AMETHYST_SHARD), 3.5))
                .uses(AttributeWrapper.MINING_FORTUNE)
                .build()
        )

        register(
            Material.CALCITE, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.CALCITE)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.CALCITE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.CALCITE)))
                .build()
        )

        register(
            Material.COAL_ORE, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.COAL), 1.5))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.COAL_ORE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.COAL), 1.5))
                .build()
        )

        register(
            Material.DEEPSLATE_COAL_ORE, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.COAL), 1.75))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.DEEPSLATE_COAL_ORE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.COAL), 1.75))
                .build()
        )

        register(
            Material.COPPER_ORE, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.COPPER_INGOT), 1.5))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.COPPER_ORE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.RAW_COPPER), 1.5))
                .build()
        )

        register(
            Material.DEEPSLATE_COPPER_ORE, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.COPPER_INGOT), 1.75))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.DEEPSLATE_COPPER_ORE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.RAW_COPPER), 1.75))
                .build()
        )

        register(
            Material.IRON_ORE, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.IRON_INGOT)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.IRON_ORE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.RAW_IRON)))
                .build()
        )

        register(
            Material.DEEPSLATE_IRON_ORE, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.IRON_INGOT)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.DEEPSLATE_IRON_ORE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.RAW_IRON)))
                .build()
        )

        register(
            Material.GOLD_ORE, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.GOLD_INGOT)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.GOLD_ORE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.RAW_GOLD)))
                .build()
        )

        register(
            Material.DEEPSLATE_GOLD_ORE, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.GOLD_INGOT)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.DEEPSLATE_GOLD_ORE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.RAW_GOLD)))
                .build()
        )

        register(
            Material.REDSTONE_ORE, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.REDSTONE), 4.0))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.REDSTONE_ORE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.REDSTONE), 4.0))
                .build()
        )

        register(
            Material.DEEPSLATE_REDSTONE_ORE, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.REDSTONE), 5.0))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.DEEPSLATE_REDSTONE_ORE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.REDSTONE), 5.0))
                .build()
        )

        register(
            Material.LAPIS_ORE, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.LAPIS_LAZULI), 4.0))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.LAPIS_ORE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.LAPIS_LAZULI), 4.0))
                .build()
        )

        register(
            Material.DEEPSLATE_LAPIS_ORE, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.LAPIS_LAZULI), 5.0))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.DEEPSLATE_LAPIS_ORE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.LAPIS_LAZULI), 5.0))
                .build()
        )

        register(
            Material.EMERALD_ORE, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.EMERALD)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.EMERALD_ORE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.EMERALD)))
                .build()
        )

        register(
            Material.DEEPSLATE_EMERALD_ORE, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.EMERALD)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.DEEPSLATE_EMERALD_ORE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.EMERALD)))
                .build()
        )

        register(
            Material.DIAMOND_ORE, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.DIAMOND)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.DIAMOND_ORE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.DIAMOND)))
                .build()
        )

        register(
            Material.DEEPSLATE_DIAMOND_ORE, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.DIAMOND)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.DEEPSLATE_DIAMOND_ORE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.DIAMOND)))
                .build()
        )

        register(
            Material.NETHER_GOLD_ORE, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.GOLD_NUGGET)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.NETHER_GOLD_ORE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.GOLD_NUGGET)))
                .build()
        )

        register(
            Material.NETHER_QUARTZ_ORE, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.QUARTZ)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.NETHER_QUARTZ_ORE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.QUARTZ)))
                .build()
        )

        register(
            Material.ANCIENT_DEBRIS, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.NETHERITE_SCRAP)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.ANCIENT_DEBRIS)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.NETHERITE_SCRAP)))
                .build()
        )

        register(
            Material.NETHERRACK, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.NETHER_BRICK)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.NETHERRACK)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.NETHERRACK)))
                .build()
        )

        // Woodcutting.
        register(
            Material.OAK_LOG, BlockLootEntry.Companion.builder()
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.CHARCOAL)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.OAK_LOG)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.OAK_LOG)))
                .add(BlockLootContext.INCORRECT_TOOL, of(ItemService.generate(Material.OAK_LOG)))
                .uses(AttributeWrapper.WOODCUTTING_FORTUNE)
                .build()
        )

        register(
            Material.ACACIA_LOG, BlockLootEntry.Companion.builder()
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.CHARCOAL)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.ACACIA_LOG)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.ACACIA_LOG)))
                .add(BlockLootContext.INCORRECT_TOOL, of(ItemService.generate(Material.ACACIA_LOG)))
                .uses(AttributeWrapper.WOODCUTTING_FORTUNE)
                .build()
        )

        register(
            Material.BIRCH_LOG, BlockLootEntry.Companion.builder()
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.CHARCOAL)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.BIRCH_LOG)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.BIRCH_LOG)))
                .add(BlockLootContext.INCORRECT_TOOL, of(ItemService.generate(Material.BIRCH_LOG)))
                .uses(AttributeWrapper.WOODCUTTING_FORTUNE)
                .build()
        )

        register(
            Material.DARK_OAK_LOG, BlockLootEntry.Companion.builder()
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.CHARCOAL)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.DARK_OAK_LOG)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.DARK_OAK_LOG)))
                .add(BlockLootContext.INCORRECT_TOOL, of(ItemService.generate(Material.DARK_OAK_LOG)))
                .uses(AttributeWrapper.WOODCUTTING_FORTUNE)
                .build()
        )

        register(
            Material.CHERRY_LOG, BlockLootEntry.Companion.builder()
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.CHARCOAL)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.CHERRY_LOG)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.CHERRY_LOG)))
                .add(BlockLootContext.INCORRECT_TOOL, of(ItemService.generate(Material.CHERRY_LOG)))
                .uses(AttributeWrapper.WOODCUTTING_FORTUNE)
                .build()
        )

        register(
            Material.JUNGLE_LOG, BlockLootEntry.Companion.builder()
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.CHARCOAL)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.JUNGLE_LOG)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.JUNGLE_LOG)))
                .add(BlockLootContext.INCORRECT_TOOL, of(ItemService.generate(Material.JUNGLE_LOG)))
                .uses(AttributeWrapper.WOODCUTTING_FORTUNE)
                .build()
        )

        register(
            Material.MANGROVE_LOG, BlockLootEntry.Companion.builder()
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.CHARCOAL)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.MANGROVE_LOG)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.MANGROVE_LOG)))
                .add(BlockLootContext.INCORRECT_TOOL, of(ItemService.generate(Material.MANGROVE_LOG)))
                .uses(AttributeWrapper.WOODCUTTING_FORTUNE)
                .build()
        )

        register(
            Material.SPRUCE_LOG, BlockLootEntry.Companion.builder()
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.CHARCOAL)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.SPRUCE_LOG)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.SPRUCE_LOG)))
                .add(BlockLootContext.INCORRECT_TOOL, of(ItemService.generate(Material.SPRUCE_LOG)))
                .uses(AttributeWrapper.WOODCUTTING_FORTUNE)
                .build()
        )

        register(
            Material.PALE_OAK_LOG, BlockLootEntry.Companion.builder()
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.CHARCOAL)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.PALE_OAK_LOG)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.PALE_OAK_LOG)))
                .add(BlockLootContext.INCORRECT_TOOL, of(ItemService.generate(Material.PALE_OAK_LOG)))
                .uses(AttributeWrapper.WOODCUTTING_FORTUNE)
                .build()
        )

        register(
            Material.WARPED_STEM, BlockLootEntry.Companion.builder()
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.WARPED_STEM)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.WARPED_STEM)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.WARPED_STEM)))
                .add(BlockLootContext.INCORRECT_TOOL, of(ItemService.generate(Material.WARPED_STEM)))
                .uses(AttributeWrapper.WOODCUTTING_FORTUNE)
                .build()
        )

        register(
            Material.CRIMSON_STEM, BlockLootEntry.Companion.builder()
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.CRIMSON_STEM)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.CRIMSON_STEM)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.CRIMSON_STEM)))
                .add(BlockLootContext.INCORRECT_TOOL, of(ItemService.generate(Material.CRIMSON_STEM)))
                .uses(AttributeWrapper.WOODCUTTING_FORTUNE)
                .build()
        )

        // Farming. Keep in mind, we actually set farming fortune overrides for a lot of entries.
        // The reason for this is so that fist breaking procs farming fortune.
        register(
            Material.MELON, builder(ItemClassification.AXE, ItemClassification.HATCHET)
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.MELON_SLICE), 3.5))
                .add(BlockLootContext.INCORRECT_TOOL, of(ItemService.generate(Material.MELON_SLICE), 3.5))
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.MELON_SLICE), 3.5))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.MELON)))
                .uses(AttributeWrapper.FARMING_FORTUNE)
                .build()
        )

        register(
            Material.PUMPKIN, builder(ItemClassification.AXE, ItemClassification.HATCHET)
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.PUMPKIN)))
                .add(BlockLootContext.INCORRECT_TOOL, of(ItemService.generate(Material.PUMPKIN)))
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.PUMPKIN)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.PUMPKIN)))
                .uses(AttributeWrapper.FARMING_FORTUNE)
                .build()
        )

        register(
            Material.WHEAT, BlockLootEntry.Companion.builder()
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.WHEAT), 1.5))
                .add(BlockLootContext.INCORRECT_TOOL, of(ItemService.generate(Material.WHEAT), 1.5))
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.WHEAT), 1.5))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.WHEAT), 1.5))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.WHEAT_SEEDS), 1.5))
                .add(BlockLootContext.INCORRECT_TOOL, of(ItemService.generate(Material.WHEAT_SEEDS), 1.5))
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.WHEAT_SEEDS), 1.5))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.WHEAT_SEEDS), 1.5))
                .uses(AttributeWrapper.FARMING_FORTUNE)
                .build()
        )

        register(
            Material.CARROTS, BlockLootEntry.Companion.builder()
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.CARROT), 2.5))
                .add(BlockLootContext.INCORRECT_TOOL, of(ItemService.generate(Material.CARROT), 2.5))
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.CARROT), 2.5))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.CARROT), 2.5))
                .uses(AttributeWrapper.FARMING_FORTUNE)
                .build()
        )

        register(
            Material.POTATOES, BlockLootEntry.Companion.builder()
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.POTATO), 2.5))
                .add(BlockLootContext.INCORRECT_TOOL, of(ItemService.generate(Material.POTATO), 2.5))
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.POTATO), 2.5))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.POTATO), 2.5))
                .uses(AttributeWrapper.FARMING_FORTUNE)
                .build()
        )

        register(
            Material.NETHER_WART, BlockLootEntry.Companion.builder()
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.NETHER_WART), 2.5))
                .add(BlockLootContext.INCORRECT_TOOL, of(ItemService.generate(Material.NETHER_WART), 2.5))
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.NETHER_WART), 2.5))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.NETHER_WART), 2.5))
                .uses(AttributeWrapper.FARMING_FORTUNE)
                .build()
        )

        register(
            Material.COCOA, BlockLootEntry.Companion.builder()
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.COCOA_BEANS), 2.5))
                .add(BlockLootContext.INCORRECT_TOOL, of(ItemService.generate(Material.COCOA_BEANS), 2.5))
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.COCOA_BEANS), 2.5))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.COCOA_BEANS), 2.5))
                .uses(AttributeWrapper.FARMING_FORTUNE)
                .build()
        )

        register(
            Material.BEETROOTS, BlockLootEntry.Companion.builder()
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.BEETROOT)))
                .add(BlockLootContext.INCORRECT_TOOL, of(ItemService.generate(Material.BEETROOT)))
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.BEETROOT)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.BEETROOT)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.BEETROOT_SEEDS), 2.5))
                .add(BlockLootContext.INCORRECT_TOOL, of(ItemService.generate(Material.BEETROOT_SEEDS), 2.5))
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.BEETROOT_SEEDS), 2.5))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(Material.BEETROOT_SEEDS), 2.5))
                .uses(AttributeWrapper.FARMING_FORTUNE)
                .build()
        )

        register(
            CraftEngineBlockEnums.SILVER_BLOCK.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.SILVER_BLOCK)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.SILVER_BLOCK)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.SILVER_BLOCK)))
                .ignoresFortune()
                .build()
        )

        register(
            CraftEngineBlockEnums.RAW_SILVER_BLOCK.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.SILVER_BLOCK)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.RAW_SILVER_BLOCK)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.RAW_SILVER_BLOCK)))
                .build()
        )

        register(
            CraftEngineBlockEnums.SILVER_ORE.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.SILVER_INGOT)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.SILVER_ORE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.RAW_SILVER)))
                .uses(AttributeWrapper.MINING_FORTUNE)
                .build()
        )

        register(
            CraftEngineBlockEnums.DEEPSLATE_SILVER_ORE.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.SILVER_INGOT)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.DEEPSLATE_SILVER_ORE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.RAW_SILVER)))
                .uses(AttributeWrapper.MINING_FORTUNE)
                .build()
        )

        register(
            CraftEngineBlockEnums.TIN_BLOCK.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.TIN_BLOCK)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.TIN_BLOCK)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.TIN_BLOCK)))
                .ignoresFortune()
                .build()
        )

        register(
            CraftEngineBlockEnums.RAW_TIN_BLOCK.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.TIN_BLOCK)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.RAW_TIN_BLOCK)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.RAW_TIN_BLOCK)))
                .build()
        )

        register(
            CraftEngineBlockEnums.TIN_ORE.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.TIN_INGOT), 1.5))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.TIN_ORE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.RAW_TIN), 1.5))
                .uses(AttributeWrapper.MINING_FORTUNE)
                .build()
        )

        register(
            CraftEngineBlockEnums.DEEPSLATE_TIN_ORE.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.TIN_INGOT), 1.75))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.DEEPSLATE_TIN_ORE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.RAW_TIN), 1.75))
                .uses(AttributeWrapper.MINING_FORTUNE)
                .build()
        )

        register(
            CraftEngineBlockEnums.STEEL_BLOCK.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.STEEL_BLOCK)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.STEEL_BLOCK)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.STEEL_BLOCK)))
                .ignoresFortune()
                .build()
        )

        register(
            CraftEngineBlockEnums.SPARSE_MITHRIL_ORE.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.MITHRIL_INGOT)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.SPARSE_MITHRIL_ORE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.RAW_MITHRIL)))
                .build()
        )

        register(
            CraftEngineBlockEnums.MITHRIL_ORE.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.MITHRIL_INGOT), 2.0))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.MITHRIL_ORE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.RAW_MITHRIL), 2.0))
                .build()
        )

        register(
            CraftEngineBlockEnums.DENSE_MITHRIL_ORE.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.MITHRIL_INGOT), 3.0))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.DENSE_MITHRIL_ORE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.RAW_MITHRIL), 3.0))
                .build()
        )

        register(
            CraftEngineBlockEnums.RAW_MITHRIL_BLOCK.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.MITHRIL_BLOCK)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.RAW_MITHRIL_BLOCK)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.RAW_MITHRIL_BLOCK)))
                .build()
        )

        register(
            CraftEngineBlockEnums.MITHRIL_BLOCK.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.MITHRIL_BLOCK)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.MITHRIL_BLOCK)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.MITHRIL_BLOCK)))
                .ignoresFortune()
                .build()
        )

        register(
            CraftEngineBlockEnums.TITANIUM_ORE.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.TITANIUM_INGOT)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.TITANIUM_ORE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.RAW_TITANIUM)))
                .build()
        )

        register(
            CraftEngineBlockEnums.RAW_TITANIUM_BLOCK.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.TITANIUM_BLOCK)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.RAW_TITANIUM_BLOCK)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.RAW_TITANIUM_BLOCK)))
                .build()
        )

        register(
            CraftEngineBlockEnums.TITANIUM_BLOCK.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.TITANIUM_BLOCK)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.TITANIUM_BLOCK)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.TITANIUM_BLOCK)))
                .ignoresFortune()
                .build()
        )

        register(
            CraftEngineBlockEnums.ADAMANTIUM_ORE.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.ADAMANTIUM_INGOT)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.ADAMANTIUM_ORE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.RAW_ADAMANTIUM)))
                .build()
        )

        register(
            CraftEngineBlockEnums.RAW_ADAMANTIUM_BLOCK.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.ADAMANTIUM_BLOCK)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.RAW_ADAMANTIUM_BLOCK)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.RAW_ADAMANTIUM_BLOCK)))
                .build()
        )

        register(
            CraftEngineBlockEnums.ADAMANTIUM_BLOCK.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.ADAMANTIUM_BLOCK)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.ADAMANTIUM_BLOCK)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.ADAMANTIUM_BLOCK)))
                .ignoresFortune()
                .build()
        )

        register(
            CraftEngineBlockEnums.GRIMSTONE.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.GRIMSTONE)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.GRIMSTONE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.COBBLED_GRIMSTONE)))
                .build()
        )

        register(
            CraftEngineBlockEnums.COBBLED_GRIMSTONE.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.GRIMSTONE)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.COBBLED_GRIMSTONE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.COBBLED_GRIMSTONE)))
                .build()
        )

        register(
            CraftEngineBlockEnums.GRIMSTONE_DIAMOND_ORE.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.DIAMOND), 3.0))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.GRIMSTONE_DIAMOND_ORE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.DIAMOND), 3.0))
                .build()
        )

        register(
            CraftEngineBlockEnums.GRIMSTONE_IRON_ORE.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.IRON_INGOT), 3.0))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.GRIMSTONE_IRON_ORE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.RAW_IRON), 3.0))
                .build()
        )

        register(
            CraftEngineBlockEnums.GRIMSTONE_GOLD_ORE.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.GOLD_INGOT), 3.0))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.GRIMSTONE_GOLD_ORE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.RAW_GOLD), 3.0))
                .build()
        )

        register(
            CraftEngineBlockEnums.GRIMSTONE_SILVER_ORE.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.SILVER_INGOT), 3.0))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.GRIMSTONE_SILVER_ORE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.RAW_SILVER), 3.0))
                .build()
        )

        register(
            CraftEngineBlockEnums.GRIMSTONE_LAPIS_ORE.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(Material.LAPIS_LAZULI), 6.0))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.GRIMSTONE_LAPIS_ORE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(Material.LAPIS_LAZULI), 6.0))
                .build()
        )

        register(
            CraftEngineBlockEnums.SULFUR_ORE.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.SULFUR), 6.0))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.SULFUR_ORE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.SULFUR), 6.0))
                .build()
        )

        register(
            CraftEngineBlockEnums.SULFUR_BLOCK.key, builder(ItemClassification.SHOVEL, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.SULFUR), 9.0))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.SULFUR_BLOCK)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.SULFUR), 9.0))
                .ignoresFortune()
                .build()
        )

        register(
            CraftEngineBlockEnums.TUNGSTEN_ORE.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.TUNGSTEN_INGOT)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.TUNGSTEN_ORE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.RAW_TUNGSTEN)))
                .build()
        )

        register(
            CraftEngineBlockEnums.RAW_TUNGSTEN_BLOCK.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.TUNGSTEN_BLOCK)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.RAW_TUNGSTEN_BLOCK)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.RAW_TUNGSTEN_BLOCK)))
                .build()
        )

        register(
            CraftEngineBlockEnums.TUNGSTEN_BLOCK.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.TUNGSTEN_BLOCK)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.TUNGSTEN_BLOCK)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.TUNGSTEN_BLOCK)))
                .ignoresFortune()
                .build()
        )

        register(
            CraftEngineBlockEnums.COBALT_ORE.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.COBALT_INGOT)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.COBALT_ORE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.RAW_COBALT)))
                .build()
        )

        register(
            CraftEngineBlockEnums.RAW_COBALT_BLOCK.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.COBALT_BLOCK)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.RAW_COBALT_BLOCK)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.RAW_COBALT_BLOCK)))
                .ignoresFortune()
                .build()
        )

        register(
            CraftEngineBlockEnums.COBALT_BLOCK.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.COBALT_BLOCK)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.COBALT_BLOCK)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.COBALT_BLOCK)))
                .ignoresFortune()
                .build()
        )

        register(
            CraftEngineBlockEnums.ORICHALCUM_ORE.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.ORICHALCUM_INGOT)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.ORICHALCUM_ORE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.RAW_ORICHALCUM)))
                .build()
        )

        register(
            CraftEngineBlockEnums.RAW_ORICHALCUM_BLOCK.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.ORICHALCUM_BLOCK)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.RAW_ORICHALCUM_BLOCK)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.RAW_ORICHALCUM_BLOCK)))
                .build()
        )

        register(
            CraftEngineBlockEnums.ORICHALCUM_BLOCK.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.ORICHALCUM_BLOCK)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.ORICHALCUM_BLOCK)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.ORICHALCUM_BLOCK)))
                .ignoresFortune()
                .build()
        )

        register(
            CraftEngineBlockEnums.ONYX_ORE.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.ONYX)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.ONYX_ORE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.ONYX)))
                .build()
        )

        register(
            CraftEngineBlockEnums.ONYX_BLOCK.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.ONYX_BLOCK)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.ONYX_BLOCK)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.ONYX_BLOCK)))
                .ignoresFortune()
                .build()
        )

        register(
            CraftEngineBlockEnums.DRAGONSTEEL_BLOCK.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.DRAGONSTEEL_BLOCK)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.DRAGONSTEEL_BLOCK)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.DRAGONSTEEL_BLOCK)))
                .ignoresFortune()
                .build()
        )

        register(
            CraftEngineBlockEnums.REFORGE_TABLE.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.REFORGE_TABLE)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.REFORGE_TABLE)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.REFORGE_TABLE)))
                .ignoresFortune()
                .build()
        )

        register(
            CraftEngineBlockEnums.RUNE_BLANK.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.RUNE_BLANK)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.RUNE_BLANK)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.RUNE_BLANK)))
                .ignoresFortune()
                .build()
        )

        register(
            CraftEngineBlockEnums.RUNE_POTENTIAL.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.RUNE_POTENTIAL)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.RUNE_POTENTIAL)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.RUNE_POTENTIAL)))
                .ignoresFortune()
                .build()
        )

        register(
            CraftEngineBlockEnums.RUNE_AMBITION.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.RUNE_AMBITION)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.RUNE_AMBITION)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.RUNE_AMBITION)))
                .ignoresFortune()
                .build()
        )

        register(
            CraftEngineBlockEnums.RUNE_MEMORIZATION.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.RUNE_MEMORIZATION)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.RUNE_MEMORIZATION)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.RUNE_MEMORIZATION)))
                .ignoresFortune()
                .build()
        )

        register(
            CraftEngineBlockEnums.RUNE_GREED.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.RUNE_GREED)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.RUNE_GREED)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.RUNE_GREED)))
                .ignoresFortune()
                .build()
        )

        register(
            CraftEngineBlockEnums.RUNE_INSIGHT.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.RUNE_INSIGHT)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.RUNE_INSIGHT)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.RUNE_INSIGHT)))
                .ignoresFortune()
                .build()
        )

        register(
            CraftEngineBlockEnums.RUNE_FORTUITY.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.RUNE_FORTUITY)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.RUNE_FORTUITY)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.RUNE_FORTUITY)))
                .ignoresFortune()
                .build()
        )

        register(
            CraftEngineBlockEnums.RUNE_DIVINITY.key, builder(ItemClassification.PICKAXE, ItemClassification.DRILL)
                .add(BlockLootContext.AUTO_SMELT, of(ItemService.generate(CustomItemType.RUNE_DIVINITY)))
                .add(BlockLootContext.SILK_TOUCH, of(ItemService.generate(CustomItemType.RUNE_DIVINITY)))
                .add(BlockLootContext.CORRECT_TOOL, of(ItemService.generate(CustomItemType.RUNE_DIVINITY)))
                .ignoresFortune()
                .build()
        )
    }

    fun register(material: Material?, entry: BlockLootEntry?) {
        entries.put(material, entry)
    }

    fun register(key: Key?, entry: BlockLootEntry?) {
        specialEntries.put(key, entry!!)
    }

    fun get(block: BlockState): BlockLootEntry? {
        if (CraftEngineBlocks.isCustomBlock(block.block)) {
            val resourceKey: Key = Key.of(CraftEngineBlocks.getCustomBlockState(block.blockData)!!.customBlockState().asString)
            return specialEntries[resourceKey]
        }
        else
            return entries[block.type]
    }

    @EventHandler
    fun onCraftEngineReload(event: CraftEngineReloadEvent?) {
        registerBlockLoot()
    }
}
