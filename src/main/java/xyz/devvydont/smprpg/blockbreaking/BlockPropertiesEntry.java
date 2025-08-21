package xyz.devvydont.smprpg.blockbreaking;

import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.items.ItemClassification;

import java.util.Set;

/**
 * A member of the {@link BlockPropertiesRegistry}. Holds a material and loot to drop under certain contexts.
 */
public class BlockPropertiesEntry {

    // What equipment type is preferable for breaking this block.
    private final @NotNull Set<ItemClassification> preferredTool;

    // How much health does this block have?
    private final float hardness;

    // What breaking power is required to destroy this block?
    private final float breakingPower;

    // Is a tool absolutely required to destroy this block effectively?
    private final boolean softRequirement;

    private final @Nullable String blockData;

    public BlockPropertiesEntry(
            float hardness,
            float breakingPower,
            boolean softRequirement,
            @Nullable String blockData,
            @Nullable Set<ItemClassification> preferredTools) {
        this.preferredTool = ImmutableSet.copyOf(preferredTools);
        this.hardness = hardness;
        this.breakingPower = breakingPower;
        this.softRequirement = softRequirement;
        this.blockData = blockData;
    }

    /**
     * Get the preferred tool for this block properties entry.
     * @return The preferred tool.
     */
    public @Nullable Set<ItemClassification> getPreferredTools() {
        return preferredTool;
    }

    /**
     * Get the hardness of a block, in ticks to break assuming 1 damage per tick.
     * @return A float representation of a block's "hp"
     */
    public float getHardness() {
        return hardness;
    }

    /**
     * Get the breaking power of a block.
     * @return A float representation of a block's break requirement
     */
    public float getBreakingPower() {
        return breakingPower;
    }

    /**
     * Get the soft breaking requirement flag of a block.
     * @return A boolean representation of a block's soft break flag.
     */
    public boolean getSoftRequirement() {
        return softRequirement;
    }

    /**
     * Get the block data of this block. Useful for specialty noteblock ores.
     * @return A string representation of the block's BlockData.
     */
    public @Nullable String getBlockData() {
        return blockData;
    }

    /**
     * Gets a builder for ease of creation for properties definitions. This version of the builder should be
     * used when a certain tool is required to break a block.
     * @param preferredTools The preferred tool to break a block.
     * @return A new builder instance.
     */
    public static Builder builder(ItemClassification...preferredTools) {
        return new Builder(preferredTools);
    }

    /**
     * Gets a builder for ease of creation for properties definitions. This version of the builder should be
     * used when no tool is necessary to break this block. Similar to dirt, where our fist is enough.
     * @return A new builder instance.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * A builder for ease of creation of {@link BlockPropertiesEntry} instances.
     */
    public static class Builder {

        private @Nullable Set<ItemClassification> preferredTool = null;
        private float hardness = 0;
        private float breakingPower = 0;
        private boolean softRequirement = false;
        private @Nullable String blockData = null;

        private Builder(ItemClassification...preferredTool) {
            this.preferredTool = ImmutableSet.copyOf(preferredTool);
        }

        /**
         * Set the hardness of a block.
         * @param blockHp The Block's "hp" in ticks, assuming 1 damage per tick.
         * @return The same builder instance for proper builder pattern calls.
         */
        public Builder hardness(float blockHp) {
            this.hardness = blockHp;
            return this;
        }

        /**
         * Set the breaking power of a block.
         * @param breakingPower The Block's breaking power.
         * @return The same builder instance for proper builder pattern calls.
         */
        public Builder breakingPower(float breakingPower) {
            this.breakingPower = breakingPower;
            return this;
        }

        /**
         * Set the BlockData, as a string, of a block entry.
         * @param blockData The Block's stringified block data.
         * @return The same builder instance for proper builder pattern calls.
         */
        public Builder data(String blockData) {
            this.blockData = blockData;
            return this;
        }

        /**
         * Sets a flag to determine if this block *requires* a tool to break effectively. (i.e. dirt)
         * @param softRequirement Boolean flag for this toggle.
         * @return The same builder instance for proper builder pattern calls.
         */
        public Builder softRequirement(boolean softRequirement) {
            this.softRequirement = softRequirement;
            return this;
        }

        /**
         * Finish construction of the entry and build a {@link BlockPropertiesEntry} instance.
         * @return The new entry.
         */
        public BlockPropertiesEntry build() {
            return new BlockPropertiesEntry(this.hardness, this.breakingPower, this.softRequirement, this.blockData, this.preferredTool);
        }

    }

}
