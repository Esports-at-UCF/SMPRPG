package xyz.devvydont.smprpg.items.interfaces

import xyz.devvydont.smprpg.items.base.SMPItemBlueprint
import xyz.devvydont.smprpg.util.extensions.buildCompressionRecipe
import xyz.devvydont.smprpg.util.extensions.resolveFirstItemInCompressionChain

/**
 * Represents an item that may "compress" or "decompress" into another item.
 * When setup correctly, members using this interface automatically set up a linked list style
 * pattern of item + amount links in order to register compression chains.
 * A "Decompressor" property represents the result that this item would decompress into, whereas the "Compressor"
 * property represents the result that this item can compress into.
 */
interface ICompressible : IRecipeProvider {

    companion object {

        const val MATERIAL_CHAR = 'm'

        /**
         * Utility method to quickly generate a compression shape based on the amount
         * of items required. In theory, we should only really use 1, 4, and 9 but the
         * options to use other weird numbers are there.
         */
        fun shape(amount: Int): List<String> {
            return when (amount) {
                1 -> listOf("$MATERIAL_CHAR")
                2 -> listOf("$MATERIAL_CHAR$MATERIAL_CHAR")
                3 -> listOf("$MATERIAL_CHAR$MATERIAL_CHAR$MATERIAL_CHAR")
                4 -> listOf("$MATERIAL_CHAR$MATERIAL_CHAR", "$MATERIAL_CHAR$MATERIAL_CHAR")
                5 -> listOf("$MATERIAL_CHAR$MATERIAL_CHAR$MATERIAL_CHAR", "$MATERIAL_CHAR$MATERIAL_CHAR")
                6 -> listOf("$MATERIAL_CHAR$MATERIAL_CHAR$MATERIAL_CHAR", "$MATERIAL_CHAR$MATERIAL_CHAR$MATERIAL_CHAR")
                7 -> listOf("$MATERIAL_CHAR$MATERIAL_CHAR$MATERIAL_CHAR", "$MATERIAL_CHAR$MATERIAL_CHAR$MATERIAL_CHAR", "$MATERIAL_CHAR")
                8 -> listOf("$MATERIAL_CHAR$MATERIAL_CHAR$MATERIAL_CHAR", "$MATERIAL_CHAR$MATERIAL_CHAR$MATERIAL_CHAR", "$MATERIAL_CHAR$MATERIAL_CHAR")
                else -> listOf("$MATERIAL_CHAR$MATERIAL_CHAR$MATERIAL_CHAR", "$MATERIAL_CHAR$MATERIAL_CHAR$MATERIAL_CHAR", "$MATERIAL_CHAR$MATERIAL_CHAR$MATERIAL_CHAR")
            }
        }
    }

    /**
     * A result of an item being compressed or decompressed. Used to resolve compression flows both forward and backward.
     */
    class CompressionStep(val blueprint: ICompressible, val inputAmount: Int = 1, val resultAmount: Int = 1) {

    }

    /**
     * The result when this item is "decompressed". A simple example would be if this item is enchanted cobblestone,
     * this result would be CompressionResult(COBBLESTONE, 1, 9).
     * If this result ends up being null, it means that it is the end of the compression chain and there is nothing
     * that this item can decompress into making it the base material in the chain.
     */
    val decompressor: CompressionStep?

    /**
     * The result when this item is "compressed". A simple example would be if this item is rotten flesh,
     * this result would be CompressionResult(PREMIUM_FLESH, 9, 1).
     * If this result ends up being null, it means that it is the end of the compression chain and there is nothing
     * that this item can compress into making it the most expensive material in the chain.
     */
    val compressor: CompressionStep?

    /**
     * A compression member contributes up to two recipes (compressing into its [compressor] and decompressing into its
     * [decompressor]). Every recipe in the chain is unlocked by acquiring the base material at the root of the chain,
     * so obtaining the raw resource reveals the entire compression ladder in the recipe book.
     */
    override fun getProvidedRecipes(): Collection<IRecipeProvider.UnlockableRecipe> {
        val self = this as? SMPItemBlueprint ?: return emptyList()
        val root = resolveFirstItemInCompressionChain(self.getGenericMaterial())
        val rootItem = (root as SMPItemBlueprint).generate()

        val recipes = ArrayList<IRecipeProvider.UnlockableRecipe>()
        buildCompressionRecipe(doDecompress = false)
            ?.let { recipes.add(IRecipeProvider.UnlockableRecipe(it, listOf(rootItem))) }
        buildCompressionRecipe(doDecompress = true)
            ?.let { recipes.add(IRecipeProvider.UnlockableRecipe(it, listOf(rootItem))) }
        return recipes
    }
}
