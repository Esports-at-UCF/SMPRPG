package xyz.devvydont.smprpg.block;

import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import xyz.devvydont.smprpg.util.BlockDataBuilders;

import javax.annotation.Nullable;

public enum CustomBlock {

    //<editor-fold desc="Noteblocks: Harp, unpowered">
    SILVER_BLOCK(Material.NOTE_BLOCK, BlockDataBuilders.newNoteBlockData(Instrument.PIANO, new Note(0), false)),
    RAW_SILVER_BLOCK(Material.NOTE_BLOCK, BlockDataBuilders.newNoteBlockData(Instrument.PIANO, new Note(1), false)),
    TIN_BLOCK(Material.NOTE_BLOCK, BlockDataBuilders.newNoteBlockData(Instrument.PIANO, new Note(2), false)),
    RAW_TIN_BLOCK(Material.NOTE_BLOCK, BlockDataBuilders.newNoteBlockData(Instrument.PIANO, new Note(3), false)),
    ROSE_GOLD_BLOCK(Material.NOTE_BLOCK, BlockDataBuilders.newNoteBlockData(Instrument.PIANO, new Note(4), false)),
    BRONZE_BLOCK(Material.NOTE_BLOCK, BlockDataBuilders.newNoteBlockData(Instrument.PIANO, new Note(5), false)),
    MITHRIL_BLOCK(Material.NOTE_BLOCK, BlockDataBuilders.newNoteBlockData(Instrument.PIANO, new Note(6), false)),
    RAW_MITHRIL_BLOCK(Material.NOTE_BLOCK, BlockDataBuilders.newNoteBlockData(Instrument.PIANO, new Note(7), false)),
    STEEL_BLOCK(Material.NOTE_BLOCK, BlockDataBuilders.newNoteBlockData(Instrument.PIANO, new Note(8), false)),
    RAW_TITANIUM_BLOCK(Material.NOTE_BLOCK, BlockDataBuilders.newNoteBlockData(Instrument.PIANO, new Note(9), false)),
    TITANIUM_BLOCK(Material.NOTE_BLOCK, BlockDataBuilders.newNoteBlockData(Instrument.PIANO, new Note(10), false)),
    ADAMANTIUM_BLOCK(Material.NOTE_BLOCK, BlockDataBuilders.newNoteBlockData(Instrument.PIANO, new Note(11), false)),
    RAW_ADAMANTIUM_BLOCK(Material.NOTE_BLOCK, BlockDataBuilders.newNoteBlockData(Instrument.PIANO, new Note(12), false)),
    SULFUR_BLOCK(Material.NOTE_BLOCK, BlockDataBuilders.newNoteBlockData(Instrument.PIANO, new Note(13), false)),
    RAW_TUNGSTEN_BLOCK(Material.NOTE_BLOCK, BlockDataBuilders.newNoteBlockData(Instrument.PIANO, new Note(14), false)),
    TUNGSTEN_BLOCK(Material.NOTE_BLOCK, BlockDataBuilders.newNoteBlockData(Instrument.PIANO, new Note(15), false)),
    RAW_COBALT_BLOCK(Material.NOTE_BLOCK, BlockDataBuilders.newNoteBlockData(Instrument.PIANO, new Note(16), false)),
    COBALT_BLOCK(Material.NOTE_BLOCK, BlockDataBuilders.newNoteBlockData(Instrument.PIANO, new Note(17), false)),
    RAW_ORICHALCUM_BLOCK(Material.NOTE_BLOCK, BlockDataBuilders.newNoteBlockData(Instrument.PIANO, new Note(18), false)),
    ORICHALCUM_BLOCK(Material.NOTE_BLOCK, BlockDataBuilders.newNoteBlockData(Instrument.PIANO, new Note(19), false)),
    ONYX_BLOCK(Material.NOTE_BLOCK, BlockDataBuilders.newNoteBlockData(Instrument.PIANO, new Note(20), false)),
    DRAGONSTEEL_BLOCK(Material.NOTE_BLOCK, BlockDataBuilders.newNoteBlockData(Instrument.PIANO, new Note(22), false)),
    REFORGE_TABLE(Material.NOTE_BLOCK, BlockDataBuilders.newNoteBlockData(Instrument.PIANO, new Note(24), false)),
    //</editor-fold>

    //<editor-fold desc="Noteblocks: Harp, powered">
    SILVER_ORE(Material.NOTE_BLOCK, BlockDataBuilders.newNoteBlockData(Instrument.PIANO, new Note(0), true)),
    DEEPSLATE_SILVER_ORE(Material.NOTE_BLOCK, BlockDataBuilders.newNoteBlockData(Instrument.PIANO, new Note(1), true)),
    TIN_ORE(Material.NOTE_BLOCK, BlockDataBuilders.newNoteBlockData(Instrument.PIANO, new Note(2), true)),
    DEEPSLATE_TIN_ORE(Material.NOTE_BLOCK, BlockDataBuilders.newNoteBlockData(Instrument.PIANO, new Note(3), true)),
    SPARSE_MITHRIL_ORE(Material.NOTE_BLOCK, BlockDataBuilders.newNoteBlockData(Instrument.PIANO, new Note(4), true)),
    MITHRIL_ORE(Material.NOTE_BLOCK, BlockDataBuilders.newNoteBlockData(Instrument.PIANO, new Note(5), true)),
    DENSE_MITHRIL_ORE(Material.NOTE_BLOCK, BlockDataBuilders.newNoteBlockData(Instrument.PIANO, new Note(6), true)),
    TITANIUM_ORE(Material.NOTE_BLOCK, BlockDataBuilders.newNoteBlockData(Instrument.PIANO, new Note(7), true)),
    ADAMANTIUM_ORE(Material.NOTE_BLOCK, BlockDataBuilders.newNoteBlockData(Instrument.PIANO, new Note(8), true)),
    SULFUR_ORE(Material.NOTE_BLOCK, BlockDataBuilders.newNoteBlockData(Instrument.PIANO, new Note(9), true)),
    TUNGSTEN_ORE(Material.NOTE_BLOCK, BlockDataBuilders.newNoteBlockData(Instrument.PIANO, new Note(10), true)),
    COBALT_ORE(Material.NOTE_BLOCK, BlockDataBuilders.newNoteBlockData(Instrument.PIANO, new Note(11), true)),
    ORICHALCUM_ORE(Material.NOTE_BLOCK, BlockDataBuilders.newNoteBlockData(Instrument.PIANO, new Note(12), true)),
    ONYX_ORE(Material.NOTE_BLOCK, BlockDataBuilders.newNoteBlockData(Instrument.PIANO, new Note(13), true)),
    GRIMSTONE(Material.NOTE_BLOCK, BlockDataBuilders.newNoteBlockData(Instrument.PIANO, new Note(14), true)),
    COBBLED_GRIMSTONE(Material.NOTE_BLOCK, BlockDataBuilders.newNoteBlockData(Instrument.PIANO, new Note(15), true));
    //</editor-fold>

    public final Material BlockMaterial;
    public final BlockData BlockData;

    CustomBlock(Material material, BlockData blockData) {
        BlockMaterial = material;
        BlockData = blockData;
    }

    public static @Nullable CustomBlock resolve(Block block) {
        for (var customBlock : CustomBlock.values())
            if (customBlock.BlockMaterial.equals(block.getType()) && customBlock.BlockData.matches(block.getBlockData()))
                return customBlock;
        return null;
    }

    public static @Nullable CustomBlock resolve(BlockState block) {
        for (var customBlock : CustomBlock.values())
            if (customBlock.BlockMaterial.equals(block.getType()) && customBlock.BlockData.matches(block.getBlockData()))
                return customBlock;
        return null;
    }
}
