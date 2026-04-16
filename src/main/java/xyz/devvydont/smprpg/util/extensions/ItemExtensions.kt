package xyz.devvydont.smprpg.util.extensions

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe
import xyz.devvydont.smprpg.items.base.SMPItemBlueprint
import xyz.devvydont.smprpg.items.base.VanillaItemBlueprint
import xyz.devvydont.smprpg.items.blueprints.resources.VanillaResource
import xyz.devvydont.smprpg.items.interfaces.ICompressible
import xyz.devvydont.smprpg.items.interfaces.ICompressible.CompressionStep
import xyz.devvydont.smprpg.items.interfaces.ISellable
import xyz.devvydont.smprpg.util.crafting.MaterialWrapper

/**
 * Finds the first item in the compression chain. Useful for figuring out what is the base item of this chain.
 * It may seem counterintuitive that we need to pass in a material wrapper as a parameter, but we need to do so
 * so that we can properly set a fallback if we call this method when we are already at the first item in the chain.
 */
fun ICompressible.resolveFirstItemInCompressionChain(type: MaterialWrapper): ICompressible {

    // Traverse the "linked list" backwards until we reach a state where we cannot compress into anything else.
    var step = this
    while (step.decompressor != null) {
        step = step.decompressor!!.blueprint
    }

    return step
}

/**
 * Calculates a list of materials in a compression chain for this material.
 * The input amount in the compression step represents the amount of items it took to make one of these items.
 * The result amount will always be 1.
 */
fun ICompressible.calculateCompressionChain(type: MaterialWrapper): List<CompressionStep>{

    // First, loop all the way to the first item in the chain. We will start there.
    var current = this.resolveFirstItemInCompressionChain(type).compressor
    var totalItemsNeeded = 0
    var levelsDeep = 1
    val chain = mutableListOf<CompressionStep>()

    while (current != null) {
        totalItemsNeeded += current.inputAmount * levelsDeep
        levelsDeep += 1
        chain.add(CompressionStep(current.blueprint, totalItemsNeeded, 1))
        current = current.blueprint.compressor
    }

    return chain
}

/**
 * Shortcut method to register a recipe to the server using the compression recipe rules and utilities.
 * If null is returned, the recipe was not registered due to not existing or already being registered.
 */
fun ICompressible.setupCompressionRecipe(id: Int, doDecompress: Boolean): Recipe? {

    if (this !is SMPItemBlueprint)
        throw IllegalStateException("Only SMPItemBlueprint is supported")

    val key = this.getGenericMaterial().key() + "-comp-id-$id"
    var result = ItemStack.of(Material.AIR)
    val compressor = if (doDecompress) this.decompressor else this.compressor
    if (compressor == null)
        return null

    val compressorBlueprint = compressor.blueprint
    if (compressorBlueprint is SMPItemBlueprint)
        result = compressorBlueprint.generate(compressor.resultAmount)
    val recipe = ShapedRecipe(NamespacedKey("smprpg", key), result)
    recipe.shape(*ICompressible.shape(compressor.inputAmount).toTypedArray())
    recipe.setIngredient(ICompressible.MATERIAL_CHAR, this.generate())
    val firstBlueprint = this.resolveFirstItemInCompressionChain(this.getGenericMaterial())
    if (firstBlueprint is SMPItemBlueprint)
        recipe.group = firstBlueprint.getGenericMaterial().key()

    if (Bukkit.addRecipe(recipe))
        return recipe
    return null
}

/**
 * Calculates the worth of a compressed item by walking up the decompressor chain to find the base item's
 * per-item sell value, then multiplying by the total compression ratio and the stack size.
 * Returns the stack amount as a fallback when no base worth can be determined.
 */
fun ICompressible.calculateCompressedWorth(itemStack: ItemStack): Int {
    if (decompressor == null) return itemStack.amount
    var compressedAmount = 1
    var current: ICompressible = this
    while (current.decompressor != null) {
        compressedAmount *= current.decompressor!!.resultAmount
        current = current.decompressor!!.blueprint
    }
    val basePerItemWorth = when {
        current is VanillaItemBlueprint -> VanillaResource.getMaterialValue(current.material)
        current is ISellable -> (current as ISellable).getWorth((current as SMPItemBlueprint).generate())
        else -> return itemStack.amount
    }
    return compressedAmount * basePerItemWorth * itemStack.amount
}
