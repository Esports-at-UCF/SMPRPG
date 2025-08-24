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

    END_TEST_ORE(Material.NOTE_BLOCK, BlockDataBuilders.newNoteBlockData(Instrument.PIANO, new Note(24), false)),
    STEEL_BLOCK(Material.NOTE_BLOCK, BlockDataBuilders.newNoteBlockData(Instrument.PIANO, new Note(23), false)),
    DRAGONSTEEL_BLOCK(Material.NOTE_BLOCK, BlockDataBuilders.newNoteBlockData(Instrument.PIANO, new Note(22), false));

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
