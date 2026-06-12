package xyz.devvydont.smprpg.items.blueprints.resources

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.base.VanillaItemBlueprint
import xyz.devvydont.smprpg.items.interfaces.ICompressible
import xyz.devvydont.smprpg.items.interfaces.ICompressible.CompressionStep
import xyz.devvydont.smprpg.services.ItemService

/**
 * A VanillaItemBlueprint that participates in a compression chain via ICompressible.
 * Registered for every vanilla Material that appears as a member of a compression family.
 * Uses a single class (registered per material) rather than one class per material to avoid proliferation.
 */
class VanillaCompressibleBlueprint(itemService: ItemService, material: Material) :
    VanillaItemBlueprint(itemService, material), ICompressible {

    override val decompressor: CompressionStep?
        get() = when (material) {
            // Mining — vanilla blocks that decompress into vanilla ingots/dusts
            Material.IRON_BLOCK -> CompressionStep(itemService.getVanillaBlueprint(ItemStack.of(Material.IRON_INGOT)) as ICompressible, 1, 9)
            Material.GOLD_BLOCK -> CompressionStep(itemService.getVanillaBlueprint(ItemStack.of(Material.GOLD_INGOT)) as ICompressible, 1, 9)
            Material.DIAMOND_BLOCK -> CompressionStep(itemService.getVanillaBlueprint(ItemStack.of(Material.DIAMOND)) as ICompressible, 1, 9)
            Material.EMERALD_BLOCK -> CompressionStep(itemService.getVanillaBlueprint(ItemStack.of(Material.EMERALD)) as ICompressible, 1, 9)
            Material.COAL_BLOCK -> CompressionStep(itemService.getVanillaBlueprint(ItemStack.of(Material.COAL)) as ICompressible, 1, 9)
            Material.AMETHYST_BLOCK -> CompressionStep(itemService.getVanillaBlueprint(ItemStack.of(Material.AMETHYST_SHARD)) as ICompressible, 1, 4)
            Material.COPPER_BLOCK -> CompressionStep(itemService.getVanillaBlueprint(ItemStack.of(Material.COPPER_INGOT)) as ICompressible, 1, 9)
            Material.GLOWSTONE -> CompressionStep(itemService.getVanillaBlueprint(ItemStack.of(Material.GLOWSTONE_DUST)) as ICompressible, 1, 4)
            Material.LAPIS_BLOCK -> CompressionStep(itemService.getVanillaBlueprint(ItemStack.of(Material.LAPIS_LAZULI)) as ICompressible, 1, 9)
            Material.NETHERITE_BLOCK -> CompressionStep(itemService.getVanillaBlueprint(ItemStack.of(Material.NETHERITE_INGOT)) as ICompressible, 1, 9)
            Material.QUARTZ_BLOCK -> CompressionStep(itemService.getVanillaBlueprint(ItemStack.of(Material.QUARTZ)) as ICompressible, 1, 4)
            Material.REDSTONE_BLOCK -> CompressionStep(itemService.getVanillaBlueprint(ItemStack.of(Material.REDSTONE)) as ICompressible, 1, 9)
            // Mob — slime block decompresses to slime balls
            Material.SLIME_BLOCK -> CompressionStep(itemService.getVanillaBlueprint(ItemStack.of(Material.SLIME_BALL)) as ICompressible, 1, 9)
            // Farming — melon decompresses to melon slices
            Material.MELON -> CompressionStep(itemService.getVanillaBlueprint(ItemStack.of(Material.MELON_SLICE)) as ICompressible, 1, 9)
            else -> null
        }

    override val compressor: CompressionStep?
        get() = when (material) {
            // Mining — single-vanilla-base items
            Material.COBBLESTONE -> CompressionStep(itemService.getBlueprint(CustomItemType.COMPRESSED_COBBLESTONE) as ICompressible, 9, 1)
            Material.COBBLED_DEEPSLATE -> CompressionStep(itemService.getBlueprint(CustomItemType.COMPRESSED_DEEPSLATE) as ICompressible, 9, 1)
            Material.FLINT -> CompressionStep(itemService.getBlueprint(CustomItemType.COMPRESSED_FLINT) as ICompressible, 9, 1)
            Material.OBSIDIAN -> CompressionStep(itemService.getBlueprint(CustomItemType.COMPRESSED_OBSIDIAN) as ICompressible, 9, 1)
            Material.CHARCOAL -> CompressionStep(itemService.getBlueprint(CustomItemType.COMPRESSED_CHARCOAL) as ICompressible, 9, 1)
            // Mining — two-vanilla-member chains (ingot/dust → block → custom)
            Material.IRON_INGOT -> CompressionStep(itemService.getVanillaBlueprint(ItemStack.of(Material.IRON_BLOCK)) as ICompressible, 9, 1)
            Material.IRON_BLOCK -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_IRON) as ICompressible, 9, 1)
            Material.GOLD_INGOT -> CompressionStep(itemService.getVanillaBlueprint(ItemStack.of(Material.GOLD_BLOCK)) as ICompressible, 9, 1)
            Material.GOLD_BLOCK -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_GOLD) as ICompressible, 9, 1)
            Material.DIAMOND -> CompressionStep(itemService.getVanillaBlueprint(ItemStack.of(Material.DIAMOND_BLOCK)) as ICompressible, 9, 1)
            Material.DIAMOND_BLOCK -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_DIAMOND) as ICompressible, 9, 1)
            Material.EMERALD -> CompressionStep(itemService.getVanillaBlueprint(ItemStack.of(Material.EMERALD_BLOCK)) as ICompressible, 9, 1)
            Material.EMERALD_BLOCK -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_EMERALD) as ICompressible, 9, 1)
            Material.COAL -> CompressionStep(itemService.getVanillaBlueprint(ItemStack.of(Material.COAL_BLOCK)) as ICompressible, 9, 1)
            Material.COAL_BLOCK -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_COAL) as ICompressible, 9, 1)
            Material.AMETHYST_SHARD -> CompressionStep(itemService.getVanillaBlueprint(ItemStack.of(Material.AMETHYST_BLOCK)) as ICompressible, 4, 1)
            Material.AMETHYST_BLOCK -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_AMETHYST) as ICompressible, 9, 1)
            Material.COPPER_INGOT -> CompressionStep(itemService.getVanillaBlueprint(ItemStack.of(Material.COPPER_BLOCK)) as ICompressible, 9, 1)
            Material.COPPER_BLOCK -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_COPPER) as ICompressible, 9, 1)
            Material.GLOWSTONE_DUST -> CompressionStep(itemService.getVanillaBlueprint(ItemStack.of(Material.GLOWSTONE)) as ICompressible, 4, 1)
            Material.GLOWSTONE -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_GLOWSTONE) as ICompressible, 9, 1)
            Material.LAPIS_LAZULI -> CompressionStep(itemService.getVanillaBlueprint(ItemStack.of(Material.LAPIS_BLOCK)) as ICompressible, 9, 1)
            Material.LAPIS_BLOCK -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_LAPIS) as ICompressible, 9, 1)
            Material.NETHERITE_INGOT -> CompressionStep(itemService.getVanillaBlueprint(ItemStack.of(Material.NETHERITE_BLOCK)) as ICompressible, 9, 1)
            Material.NETHERITE_BLOCK -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_NETHERITE) as ICompressible, 9, 1)
            Material.QUARTZ -> CompressionStep(itemService.getVanillaBlueprint(ItemStack.of(Material.QUARTZ_BLOCK)) as ICompressible, 4, 1)
            Material.QUARTZ_BLOCK -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_QUARTZ) as ICompressible, 9, 1)
            Material.REDSTONE -> CompressionStep(itemService.getVanillaBlueprint(ItemStack.of(Material.REDSTONE_BLOCK)) as ICompressible, 9, 1)
            Material.REDSTONE_BLOCK -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_REDSTONE) as ICompressible, 9, 1)
            // Mob — single-vanilla-base items
            Material.BONE -> CompressionStep(itemService.getBlueprint(CustomItemType.PREMIUM_BONE) as ICompressible, 9, 1)
            Material.GUNPOWDER -> CompressionStep(itemService.getBlueprint(CustomItemType.PREMIUM_GUNPOWDER) as ICompressible, 9, 1)
            Material.ROTTEN_FLESH -> CompressionStep(itemService.getBlueprint(CustomItemType.PREMIUM_FLESH) as ICompressible, 9, 1)
            Material.LEATHER -> CompressionStep(itemService.getBlueprint(CustomItemType.PREMIUM_LEATHER) as ICompressible, 9, 1)
            Material.FEATHER -> CompressionStep(itemService.getBlueprint(CustomItemType.PREMIUM_FEATHER) as ICompressible, 9, 1)
            Material.COOKED_CHICKEN -> CompressionStep(itemService.getBlueprint(CustomItemType.PREMIUM_CHICKEN) as ICompressible, 9, 1)
            Material.COOKED_MUTTON -> CompressionStep(itemService.getBlueprint(CustomItemType.PREMIUM_MUTTON) as ICompressible, 9, 1)
            Material.COOKED_PORKCHOP -> CompressionStep(itemService.getBlueprint(CustomItemType.PREMIUM_PORKCHOP) as ICompressible, 9, 1)
            Material.COOKED_BEEF -> CompressionStep(itemService.getBlueprint(CustomItemType.PREMIUM_STEAK) as ICompressible, 9, 1)
            Material.SPIDER_EYE -> CompressionStep(itemService.getBlueprint(CustomItemType.PREMIUM_SPIDER_EYE) as ICompressible, 9, 1)
            Material.BLAZE_ROD -> CompressionStep(itemService.getBlueprint(CustomItemType.PREMIUM_BLAZE_ROD) as ICompressible, 9, 1)
            Material.ECHO_SHARD -> CompressionStep(itemService.getBlueprint(CustomItemType.PREMIUM_ECHO_SHARD) as ICompressible, 9, 1)
            Material.INK_SAC -> CompressionStep(itemService.getBlueprint(CustomItemType.PREMIUM_INK_SAC) as ICompressible, 9, 1)
            Material.MAGMA_CREAM -> CompressionStep(itemService.getBlueprint(CustomItemType.PREMIUM_MAGMA_CREAM) as ICompressible, 9, 1)
            Material.NAUTILUS_SHELL -> CompressionStep(itemService.getBlueprint(CustomItemType.PREMIUM_NAUTILUS_SHELL) as ICompressible, 9, 1)
            Material.NETHER_STAR -> CompressionStep(itemService.getBlueprint(CustomItemType.PREMIUM_NETHER_STAR) as ICompressible, 9, 1)
            Material.PHANTOM_MEMBRANE -> CompressionStep(itemService.getBlueprint(CustomItemType.PREMIUM_MEMBRANE) as ICompressible, 9, 1)
            Material.PRISMARINE_CRYSTALS -> CompressionStep(itemService.getBlueprint(CustomItemType.PREMIUM_PRISMARINE_CRYSTAL) as ICompressible, 9, 1)
            Material.PRISMARINE_SHARD -> CompressionStep(itemService.getBlueprint(CustomItemType.PREMIUM_PRISMARINE_SHARD) as ICompressible, 9, 1)
            Material.RABBIT_HIDE -> CompressionStep(itemService.getBlueprint(CustomItemType.PREMIUM_RABBIT_HIDE) as ICompressible, 9, 1)
            Material.SHULKER_SHELL -> CompressionStep(itemService.getBlueprint(CustomItemType.PREMIUM_SHULKER_SHELL) as ICompressible, 9, 1)
            Material.STRING -> CompressionStep(itemService.getBlueprint(CustomItemType.PREMIUM_STRING) as ICompressible, 9, 1)
            // Mob — two-vanilla-member chain (slime)
            Material.SLIME_BALL -> CompressionStep(itemService.getVanillaBlueprint(ItemStack.of(Material.SLIME_BLOCK)) as ICompressible, 9, 1)
            Material.SLIME_BLOCK -> CompressionStep(itemService.getBlueprint(CustomItemType.PREMIUM_SLIME) as ICompressible, 9, 1)
            // Farming — sugar cane and melon chain roots
            Material.SUGAR_CANE -> CompressionStep(itemService.getBlueprint(CustomItemType.PREMIUM_SUGAR) as ICompressible, 9, 1)
            Material.MELON_SLICE -> CompressionStep(itemService.getVanillaBlueprint(ItemStack.of(Material.MELON)) as ICompressible, 9, 1)
            Material.MELON -> CompressionStep(itemService.getBlueprint(CustomItemType.PREMIUM_MELON_SLICE) as ICompressible, 9, 1)
            else -> null
        }
}
