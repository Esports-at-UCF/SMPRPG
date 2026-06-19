package xyz.devvydont.smprpg.gui.anvil

import xyz.devvydont.smprpg.gui.anvil.operations.HotPotatoBookOperation
import xyz.devvydont.smprpg.gui.anvil.operations.NecronomiconExcerptOperation
import xyz.devvydont.smprpg.gui.anvil.operations.RepairCoreOperation
import xyz.devvydont.smprpg.gui.anvil.operations.ReforgeStoneOperation

/**
 * The ordered registry of every behavior the anvil can perform.
 *
 * Inputs are tested against each operation in order; the first to produce a result wins. Durability is
 * restored exclusively via repair cores ([RepairCoreOperation]); combining items to merge durability is
 * intentionally not supported.
 *
 * To add a new anvil behavior, implement [AnvilOperation] and add it to this list.
 */
object AnvilOperations {

    val ALL: List<AnvilOperation> = listOf(
        RepairCoreOperation,
        NecronomiconExcerptOperation,
        HotPotatoBookOperation,
        ReforgeStoneOperation
    )
}
