package xyz.devvydont.smprpg.items.blueprints.storage

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.CraftingRecipe
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.recipe.CraftingBookCategory
import xyz.devvydont.smprpg.SMPRPG.Companion.plugin
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint
import xyz.devvydont.smprpg.items.interfaces.ICraftable
import xyz.devvydont.smprpg.items.interfaces.ICustomTextured
import xyz.devvydont.smprpg.items.interfaces.IHeaderDescribable
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.services.ItemService.Companion.generate
import xyz.devvydont.smprpg.util.formatting.ComponentUtils
import xyz.devvydont.smprpg.util.items.AbilityUtil

/**
 * An item to access your ender chest!
 */
class EnderPack(itemService: ItemService, type: CustomItemType) : CustomItemBlueprint(itemService, type),
    IHeaderDescribable, Listener, ICraftable, ICustomTextured {

    override val itemClassification: ItemClassification get() = ItemClassification.EQUIPMENT

    override fun getHeader(itemStack: ItemStack?): MutableList<Component?> {
        return mutableListOf(
            ComponentUtils.merge(
                AbilityUtil.getAbilityComponent("Open!"),
                ComponentUtils.create(" (Right Click)", NamedTextColor.DARK_GRAY)
            ),
            ComponentUtils.create("Portable ender storage!"),
            ComponentUtils.create("Right click while holding", NamedTextColor.GRAY),
            ComponentUtils.create("to open your ender chest", NamedTextColor.GRAY)
        )
    }

    override fun getRecipeKey(): NamespacedKey {
        return NamespacedKey(plugin, this.customItemType.key + "_recipe")
    }

    override fun getCustomRecipe(): CraftingRecipe {
        val recipe = ShapedRecipe(this.recipeKey, generate())
        recipe.shape(" p ", "lcl", " l ")
        recipe.setIngredient('l', generate(CustomItemType.ENCHANTED_LEATHER))
        recipe.setIngredient('c', generate(Material.ENDER_CHEST))
        recipe.setIngredient('p', generate(CustomItemType.ENCHANTED_ENDER_PEARL))
        recipe.setCategory(CraftingBookCategory.MISC)
        return recipe
    }

    /**
     * A collection of items that will unlock the recipe for this item. Typically will be one of the components
     * of the recipe itself, but can be set to whatever is desired
     *
     * @return
     */
    override fun unlockedBy(): MutableCollection<ItemStack?> {
        return mutableListOf(
            generate(Material.ENDER_CHEST)
        )
    }

    /**
     * Retrieve the URL to use for the custom head texture of this item.
     * The link that is set here should follow the following format:
     * Let's say you have the following link to a skin;
     * [...](https://textures.minecraft.net/texture/18813764b2abc94ec3c3bc67b9147c21be850cdf996679703157f4555997ea63a)
     * You should only use the very last component of the URL, as the backend will fill in the rest.
     * Meaning we would end up using: "18813764b2abc94ec3c3bc67b9147c21be850cdf996679703157f4555997ea63a"
     *
     * @return The URL to the skin.
     */
    override fun getTextureUrl(): String {
        return "7f977e69164ecdddbd1f9353653b69035ad2517b0822ca6be40cfbb7aa2227e4"
    }

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        if (!event.action.isRightClick) return

        val item = event.item
        if (item == null) return
        if (!isItemOfType(item)) return

        event.player.world.playSound(event.player.location, Sound.BLOCK_ENDER_CHEST_OPEN, .5f, 1.5f)
        event.player.openInventory(event.player.enderChest)
    }
}
