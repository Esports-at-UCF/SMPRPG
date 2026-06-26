package xyz.devvydont.smprpg.services

import org.bukkit.Bukkit
import org.bukkit.Keyed
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.event.Listener
import org.bukkit.inventory.BlastingRecipe
import org.bukkit.inventory.CampfireRecipe
import org.bukkit.inventory.FurnaceRecipe
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.RecipeChoice.ExactChoice
import org.bukkit.inventory.RecipeChoice.MaterialChoice
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.ShapelessRecipe
import org.bukkit.inventory.SmokingRecipe
import org.bukkit.inventory.recipe.CraftingBookCategory
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemRarity
import xyz.devvydont.smprpg.items.blueprints.fishing.FishBlueprint
import xyz.devvydont.smprpg.listeners.crafting.CustomCampfireController
import xyz.devvydont.smprpg.listeners.crafting.CustomFurnaceController
import net.momirealms.craftengine.core.util.Key
import xyz.devvydont.smprpg.recipe.campfire.FishTeardown
import xyz.devvydont.smprpg.recipe.CompressionGraph
import xyz.devvydont.smprpg.recipe.core.CompressionRecipe
import xyz.devvydont.smprpg.recipe.core.ItemIdentifier
import xyz.devvydont.smprpg.recipe.cookingpot.CookingPotRecipe
import xyz.devvydont.smprpg.recipe.core.CookingPotRecipe as CoreCookingPotRecipe
import xyz.devvydont.smprpg.recipe.core.RecipeLoader
import xyz.devvydont.smprpg.recipe.core.RecipeRegistry
import xyz.devvydont.smprpg.recipe.core.RecipeStationType
import xyz.devvydont.smprpg.recipe.core.SmeltingCookType
import xyz.devvydont.smprpg.recipe.core.SmeltingRecipe
import xyz.devvydont.smprpg.recipe.cuttingboard.CuttingBoardRecipe
import xyz.devvydont.smprpg.recipe.cuttingboard.CuttingBoardToolTags
import xyz.devvydont.smprpg.recipe.core.CuttingBoardRecipe as CoreCuttingBoardRecipe
import xyz.devvydont.smprpg.recipe.freezer.FreezerRecipe
import xyz.devvydont.smprpg.recipe.core.FreezerRecipe as CoreFreezerRecipe
import xyz.devvydont.smprpg.recipe.core.ShapedRecipe as CoreShapedRecipe
import xyz.devvydont.smprpg.recipe.core.ShapelessRecipe as CoreShapelessRecipe
import xyz.devvydont.smprpg.services.ItemService.Companion.blueprint
import xyz.devvydont.smprpg.services.ItemService.Companion.generate
import xyz.devvydont.smprpg.util.listeners.ToggleableListener
import java.io.File
import java.util.jar.JarFile


/**
 * Handles all recipe logic on the server. This includes crafting, smelting, etc.
 */
class RecipeService : IService, Listener {
    private val listeners: MutableList<ToggleableListener> = ArrayList()

    /**
     * The unified, data-driven recipe registry built from the recipe YAML files. Rebuilt and swapped
     * atomically on [reload]. Nothing consumes this yet — station drivers are migrated onto it incrementally.
     */
    private var registry: RecipeRegistry = RecipeRegistry()

    /** Keys of the Bukkit compression crafting recipes we registered from the registry, removed on reload. */
    private val registeredCompressionKeys: MutableList<NamespacedKey> = ArrayList()

    /** Keys of the Bukkit crafting-table recipes we registered from the registry, removed on reload. */
    private val registeredCraftingKeys: MutableList<NamespacedKey> = ArrayList()

    fun getRegistry(): RecipeRegistry = registry

    @Throws(RuntimeException::class)
    override fun setup() {

        saveDefaultRecipes()
        registry = RecipeLoader.load()
        registerCompressionRecipes()
        registerCraftingRecipes()
        Bukkit.updateRecipes()

        // Start listeners.
        listeners.add(CustomFurnaceController())
        listeners.add(CustomCampfireController())
        for (listener in listeners)
            listener.start()
    }

    /**
     * Rebuild the recipe registry from disk without a server restart. Ensures the default files exist,
     * loads a fresh registry, then swaps it in. Existing files are never overwritten.
     */
    fun reload() {
        saveDefaultRecipes()
        registry = RecipeLoader.load()
        registerCompressionRecipes()
        registerCraftingRecipes()
        // A single resync after all the quiet add/remove calls, so online players' recipe books update with
        // one packet instead of one per recipe (which previously flooded clients into a packet-limit kick).
        // Furnace smelting is driven live by CustomFurnaceController, which reads the swapped-in registry each
        // tick, so a reload needs no furnace re-registration.
        Bukkit.updateRecipes()
        SMPRPG.plugin.logger.info("Reloaded custom recipe registry (${registry.size} recipes).")
    }

    /**
     * Dump the registry's compression recipes to editable YAML files under recipes/compression/, one file per
     * edge (the compress direction; the reverse is derived on load). Intended to be run once to turn the
     * bridge-derived compression recipes into a hand-editable source before the ICompressible bridge is removed.
     * @return the number of files written.
     */
    fun exportCompressionRecipes(): Int {
        val dir = File(SMPRPG.plugin.dataFolder, "recipes/compression")
        dir.mkdirs()

        var count = 0
        // One file per chain: walk each chain from its root and write its ordered tiers.
        for (root in CompressionGraph.roots()) {
            val flow = CompressionGraph.flowFromRoot(root)
            if (flow.size < 2) continue
            val family = CompressionGraph.family(root)?.takeIf { it.isNotEmpty() } ?: ItemIdentifier.parse(root).path
            // tiers[0] is the base (bare id); each later tier carries how many of the previous tier make one of it.
            val tiers = ArrayList<Any>()
            tiers.add(root)
            for (i in 1 until flow.size)
                tiers.add(linkedMapOf("item" to flow[i].first, "amount" to flow[i - 1].second))

            val cfg = YamlConfiguration()
            cfg.set("type", "compression")
            cfg.set("tiers", tiers)
            cfg.save(File(dir, "${family}_compression.yml"))
            count++
        }
        SMPRPG.plugin.logger.info("Exported $count compression chains to recipes/compression/.")
        return count
    }

    /**
     * Dump the enchantment recipes to editable YAML under recipes/enchanting/, one file per enchantment with
     * a `levels:` map holding each level's `power` + `ingredients`. Faithfully round-trips whatever
     * `CustomEnchantment.getRecipe` currently returns (today, the data-driven registry).
     * @return the number of files written (one per enchantment with at least one recipe).
     */
    fun exportEnchantingRecipes(): Int {
        val dir = File(SMPRPG.plugin.dataFolder, "recipes/enchanting")
        dir.mkdirs()
        val itemService = SMPRPG.getService(ItemService::class.java)
        var count = 0

        for (enchantment in EnchantmentService.CUSTOM_ENCHANTMENTS) {
            val levels = linkedMapOf<String, Any>()
            for (level in 1..enchantment.maxLevel) {
                val recipe = enchantment.getRecipe(level) ?: continue
                levels[level.toString()] = linkedMapOf(
                    "power" to recipe.power,
                    "ingredients" to recipe.ingredients.map {
                        linkedMapOf("item" to itemService.getIdentifier(it), "amount" to it.amount)
                    }
                )
            }
            if (levels.isEmpty()) continue
            val cfg = YamlConfiguration()
            cfg.set("type", "enchanting")
            cfg.set("enchantment", enchantment.id)
            cfg.set("levels", levels)
            cfg.save(File(dir, "${enchantment.id}.yml"))
            count++
        }
        SMPRPG.plugin.logger.info("Exported $count enchanting recipe files to recipes/enchanting/.")
        return count
    }

    /**
     * Seed every bundled recipe file into the data folder on first run only. We only seed when the recipes
     * folder does not yet exist, so admin edits — and deletions — are respected on later restarts.
     */
    private fun saveDefaultRecipes() {
        val dir = File(SMPRPG.plugin.dataFolder, "recipes")
        if (dir.exists())
            return
        copyBundledRecipes(overwrite = false)
    }

    /**
     * Force every bundled recipe file from the jar back into the data folder, overwriting existing copies.
     * Unlike [saveDefaultRecipes], this ignores whether the folder already exists, so it refreshes the
     * hand-authored recipes (shaped/smelting/etc.) to match the current build — the counterpart of
     * [exportCompressionRecipes]/[exportEnchantingRecipes] for the file-authored recipe types.
     * @return the number of files written.
     */
    fun exportBundledRecipes(): Int = copyBundledRecipes(overwrite = true)

    /**
     * Copy every recipe file under the jar's `recipes/` directory into the data folder. With [overwrite] off,
     * existing files are left untouched (first-run seeding); with it on, they are replaced.
     * @return the number of files copied.
     */
    private fun copyBundledRecipes(overwrite: Boolean): Int {
        var count = 0
        try {
            val jar = File(SMPRPG.plugin.javaClass.protectionDomain.codeSource.location.toURI())
            JarFile(jar).use { jf ->
                for (entry in jf.entries()) {
                    if (entry.isDirectory) continue
                    val name = entry.name
                    if (!name.startsWith("recipes/")) continue
                    if (!name.endsWith(".yml") && !name.endsWith(".yaml")) continue
                    try {
                        SMPRPG.plugin.saveResource(name, overwrite)
                        count++
                    } catch (e: IllegalArgumentException) {
                        // Shouldn't happen for a jar entry we just enumerated; ignore defensively.
                    }
                }
            }
        } catch (e: Exception) {
            SMPRPG.plugin.logger.warning("Could not copy bundled recipe files: ${e.message}")
        }
        return count
    }

    override fun cleanup() {
        for (listener in listeners)
            listener.stop()
    }

    /**
     * Registers the vanilla-grid compression crafting recipes (compress and decompress) from the registry's
     * compression edges. Each edge is stored compress-direction (N -> 1); we build both Bukkit shaped recipes
     * from it. Custom items match exactly; vanilla items match by material. Previously-registered recipes are
     * removed first so this is safe to call again on reload, and both recipes are unlocked by the chain root.
     */
    private fun registerCompressionRecipes() {
        // Mutate quietly (no per-recipe client resend); callers resync once via Bukkit.updateRecipes().
        for (key in registeredCompressionKeys)
            Bukkit.removeRecipe(key, false)
        registeredCompressionKeys.clear()

        val itemService = SMPRPG.getService(ItemService::class.java)
        for (recipe in registry.byStation(RecipeStationType.COMPRESSOR).filterIsInstance<CompressionRecipe>()) {
            // Only build from the compress direction (N -> 1); the decompress recipe is derived here.
            if (recipe.input.amount <= recipe.result.amount) continue
            val lowerId = recipe.input.identifier
            val higherId = recipe.result.identifier
            val n = recipe.input.amount
            val lowerStack = itemService.resolveIdentifier(lowerId.asString()) ?: continue
            val higherStack = itemService.resolveIdentifier(higherId.asString()) ?: continue

            val group = CompressionGraph.baseOf(lowerId.asString())
            val rootStack = itemService.resolveIdentifier(group)

            // Compress: N lower -> 1 higher
            val compressKey = NamespacedKey("smprpg", recipe.key.value() + "_compress")
            val compress = ShapedRecipe(compressKey, higherStack.clone().apply { amount = 1 })
            compress.shape(*compressionShape(n).toTypedArray())
            compress.setIngredient(COMPRESSION_CHAR, compressionChoice(lowerId, lowerStack))
            compress.category = CraftingBookCategory.MISC
            compress.group = group

            // Decompress: 1 higher -> N lower
            val decompressKey = NamespacedKey("smprpg", recipe.key.value() + "_decompress")
            val decompress = ShapedRecipe(decompressKey, lowerStack.clone().apply { amount = n })
            decompress.shape(*compressionShape(1).toTypedArray())
            decompress.setIngredient(COMPRESSION_CHAR, compressionChoice(higherId, higherStack))
            decompress.category = CraftingBookCategory.MISC
            decompress.group = group

            if (Bukkit.addRecipe(compress, false)) registeredCompressionKeys.add(compressKey)
            if (Bukkit.addRecipe(decompress, false)) registeredCompressionKeys.add(decompressKey)
            if (rootStack != null) {
                itemService.addRecipeUnlock(rootStack, compressKey)
                itemService.addRecipeUnlock(rootStack, decompressKey)
            }
        }
    }

    /** Custom items must match exactly (shared base materials are ambiguous); vanilla items match by material. */
    private fun compressionChoice(id: ItemIdentifier, stack: ItemStack): RecipeChoice =
        if (id.namespace == "smprpg") ExactChoice(stack) else MaterialChoice(stack.type)

    /**
     * Registers the data-driven crafting-table recipes with Bukkit so they work in the player's 2x2 grid, the
     * recipe book, the recipe browser, and the transmute-upgrade fix. The custom crafting menu has its own
     * count-aware matching; this Bukkit registration covers everything else. Shaped recipes that use per-slot
     * counts greater than one cannot be represented as a Bukkit recipe and are skipped here (they still craft
     * in the custom menu). Removed and re-added on reload, and unlocked by each recipe's `unlocked_by` items.
     */
    private fun registerCraftingRecipes() {
        // Mutate quietly (no per-recipe client resend); callers resync once via Bukkit.updateRecipes().
        for (key in registeredCraftingKeys)
            Bukkit.removeRecipe(key, false)
        registeredCraftingKeys.clear()

        val itemService = SMPRPG.getService(ItemService::class.java)
        for (recipe in registry.byStation(RecipeStationType.CRAFTING_TABLE)) {
            val result = recipe.outputs.firstOrNull()?.generate() ?: continue
            val key = recipe.key
            val bukkit: Recipe = when (recipe) {
                is CoreShapedRecipe -> {
                    if (recipe.keyMap.values.any { it.amount > 1 }) {
                        SMPRPG.plugin.logger.info("Crafting recipe ${key.value()} uses per-slot counts; only the custom crafting menu will craft it.")
                        continue
                    }
                    val shaped = ShapedRecipe(key, result)
                    shaped.shape(*recipe.pattern.toTypedArray())
                    for ((ch, ingredient) in recipe.keyMap)
                        shaped.setIngredient(ch, craftingChoice(ingredient.identifier, itemService) ?: continue)
                    shaped.category = CraftingBookCategory.MISC
                    shaped
                }
                is CoreShapelessRecipe -> {
                    val shapeless = ShapelessRecipe(key, result)
                    for (ingredient in recipe.ingredients) {
                        val choice = craftingChoice(ingredient.identifier, itemService) ?: continue
                        repeat(ingredient.amount) { shapeless.addIngredient(choice) }
                    }
                    shapeless.category = CraftingBookCategory.MISC
                    shapeless
                }
                else -> continue
            }

            if (Bukkit.addRecipe(bukkit, false))
                registeredCraftingKeys.add(key)
            for (unlock in recipe.unlockedBy) {
                val stack = itemService.resolveIdentifier(unlock.asString()) ?: continue
                itemService.addRecipeUnlock(stack, key)
            }
        }
    }

    /** Resolve a recipe choice for a crafting ingredient: exact for custom items, material for vanilla. */
    private fun craftingChoice(id: ItemIdentifier, itemService: ItemService): RecipeChoice? {
        val stack = itemService.resolveIdentifier(id.asString()) ?: return null
        return if (id.namespace == "smprpg") ExactChoice(stack) else MaterialChoice(stack.type)
    }

    companion object {

        /** The grid character used for the single repeated ingredient in a compression recipe. */
        private const val COMPRESSION_CHAR = 'm'

        /** Build a compression crafting grid shape that holds [amount] copies of the ingredient. */
        private fun compressionShape(amount: Int): List<String> = when (amount) {
            1 -> listOf("m")
            2 -> listOf("mm")
            3 -> listOf("mmm")
            4 -> listOf("mm", "mm")
            5 -> listOf("mmm", "mm")
            6 -> listOf("mmm", "mmm")
            7 -> listOf("mmm", "mmm", "m")
            8 -> listOf("mmm", "mmm", "mm")
            else -> listOf("mmm", "mmm", "mmm")
        }

        /** Whether the given item has a data-driven crafting-table recipe that produces it. */
        @JvmStatic
        fun isCraftable(item: ItemStack): Boolean {
            val id = SMPRPG.getService(ItemService::class.java).getIdentifier(item)
            return SMPRPG.getService(RecipeService::class.java).getRegistry()
                .byResult(id).any { it.station == RecipeStationType.CRAFTING_TABLE }
        }

        /** Map a cutting board recipe's `tool` string ("knives"/"axes"/"shovels") to its tool tag. */
        fun toolTagOf(tool: String?): Key = when (tool?.lowercase()) {
            "axes" -> CuttingBoardToolTags.AXES
            "shovels" -> CuttingBoardToolTags.SHOVELS
            else -> CuttingBoardToolTags.KNIVES
        }

        /**
         * Get a list of recipes for a specific item.
         * This operation will filter out vanilla recipes that think they can craft custom items, due to a
         * Material recipe match.
         * @param item
         * @return
         */
        @JvmStatic
        fun getRecipesFor(item: ItemStack): MutableList<Recipe> {
            val allRecipes = Bukkit.getRecipesFor(item)
            // Filter out recipes that have the minecraft namespace that think they can craft custom items.
            // Filter out items that do not match. This function has this lovely behavior of giving us ALL recipes that give us the same underlying vanilla material.
            // Another level of filtering. Filter out custom recipes that craft vanilla items. The only time this should
            // ever really be possible is with compression recipes, and they are annoying to display anyway...
            for (recipe in allRecipes.stream().toList()) {

                // Filter out items that simply do not match. An iron ingot cannot be crafted by a recipe that is a boiling ingot.
                if (!ItemService.isOfSameType(item, recipe.result)) {
                    allRecipes.remove(recipe)
                    continue
                }

                if (recipe !is Keyed)
                    continue

                // Filter out a recipe if it is vanilla, but thinks it can craft a custom item.
                //todo commented out to see if recipe browser behaves better with displaying recipes with strange
                //todo ways to make them (vanilla items making custom items)
//                val recipeIsVanilla = recipe.key.namespace == NamespacedKey.MINECRAFT_NAMESPACE
//
//                val resultBlueprint = blueprint(recipe.result)
//                if (recipeIsVanilla && resultBlueprint.isCustom) {
//                    allRecipes.remove(recipe)
//                    continue
//                }
//
//                // Filter out a recipe if it is one of our recipes, but a vanilla item is generated. This could potentially
//                // filter out recipes we want to consider valid, but there are more "lying" recipes if we allow them.
//                if (!recipeIsVanilla && resultBlueprint.isVanilla) {
//                    allRecipes.remove(recipe)
//                }
            }
            allRecipes.addAll(getCustomRecipesFor(item))
            return allRecipes
        }

        private fun getCustomRecipesFor(item: ItemStack): List<Recipe> {
            val results = mutableListOf<Recipe>()
            // Freezer, cutting board and cooking pot recipes now live in the data-driven registry. Build
            // lightweight display recipes from it so the recipe browser keeps working until it is fully
            // migrated to the core types.
            val registry = SMPRPG.getService(RecipeService::class.java).getRegistry()
            for (core in registry.byStation(RecipeStationType.COOKING_POT).filterIsInstance<CoreCookingPotRecipe>()) {
                val resultStack = core.result.generate() ?: continue
                if (resultStack.type == Material.BARRIER) continue
                if (resultStack.isSimilar(item)) {
                    val inputs = core.ingredients.mapNotNull { ing ->
                        ing.identifier.resolve()?.also { it.amount = ing.amount }
                    }
                    val plating = core.plating?.resolve()
                    results.add(CookingPotRecipe(core.key, inputs, core.time, resultStack, null, plating))
                }
            }
            for (core in registry.byStation(RecipeStationType.FREEZER).filterIsInstance<CoreFreezerRecipe>()) {
                val resultStack = core.result.generate() ?: continue
                if (resultStack.type == Material.BARRIER) continue
                if (resultStack.isSimilar(item)) {
                    val inputStack = core.input.identifier.resolve() ?: continue
                    results.add(FreezerRecipe(core.key, inputStack, core.time, resultStack))
                }
            }
            for (core in registry.byStation(RecipeStationType.CUTTING_BOARD).filterIsInstance<CoreCuttingBoardRecipe>()) {
                val outputs = core.results.mapNotNull { out -> out.generate()?.let { it to out.chance } }
                if (outputs.none { (stack, _) -> stack.type != Material.BARRIER && stack.isSimilar(item) }) continue
                val inputStack = core.input.identifier.resolve() ?: continue
                results.add(CuttingBoardRecipe(core.key, inputStack, outputs, toolTagOf(core.tool)))
            }
            // Furnace smelting is no longer registered with Bukkit (CustomFurnaceController drives it directly),
            // so build unregistered Bukkit cooking recipes purely so the recipe browser can still display them.
            for (core in registry.byStation(RecipeStationType.FURNACE).filterIsInstance<SmeltingRecipe>()) {
                val resultStack = core.result.generate() ?: continue
                if (resultStack.type == Material.BARRIER || !resultStack.isSimilar(item)) continue
                val inputStack = core.input.identifier.resolve() ?: continue
                val choice: RecipeChoice =
                    if (core.input.identifier.namespace == "smprpg") ExactChoice(inputStack)
                    else MaterialChoice(inputStack.type)
                results.add(when (core.cook) {
                    SmeltingCookType.FURNACE -> FurnaceRecipe(core.key, resultStack, choice, core.experience, core.time)
                    SmeltingCookType.BLASTING -> BlastingRecipe(core.key, resultStack, choice, core.experience, core.time)
                    SmeltingCookType.SMOKING -> SmokingRecipe(core.key, resultStack, choice, core.experience, core.time)
                    SmeltingCookType.CAMPFIRE -> CampfireRecipe(core.key, resultStack, choice, core.experience, core.time)
                })
            }
            results.addAll(fishTeardownDisplayRecipes(item))
            return results
        }

        /**
         * Fish teardown is driven by [CustomCampfireController] rather than a Bukkit recipe, so build
         * unregistered campfire display recipes (one per rarity, choosing any fish of that rarity) for the
         * recipe browser when the queried item is the essence that rarity produces.
         */
        private fun fishTeardownDisplayRecipes(item: ItemStack): List<Recipe> {
            val fishByRarity: MutableMap<ItemRarity, MutableList<ItemStack>> = HashMap()
            for (blueprint in SMPRPG.getService(ItemService::class.java).customBlueprints) {
                if (blueprint !is FishBlueprint) continue
                fishByRarity.getOrPut(blueprint.defaultRarity) { ArrayList() }.add(generate(blueprint.customItemType))
            }

            val display = mutableListOf<Recipe>()
            for ((rarity, fish) in fishByRarity) {
                if (fish.isEmpty()) continue
                val essence = generate(FishTeardown.essenceFor(rarity))
                if (!essence.isSimilar(item)) continue
                val key = NamespacedKey(SMPRPG.plugin, "${rarity}_fish_teardown")
                display.add(CampfireRecipe(key, essence, ExactChoice(fish), 0f, FishTeardown.cookTimeTicks(rarity)))
            }
            return display
        }
    }


}
