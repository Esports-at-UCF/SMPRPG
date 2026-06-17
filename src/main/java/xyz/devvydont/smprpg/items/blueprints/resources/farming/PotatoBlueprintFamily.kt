package xyz.devvydont.smprpg.items.blueprints.resources.farming

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint
import xyz.devvydont.smprpg.items.interfaces.ICompressible
import xyz.devvydont.smprpg.items.interfaces.ICompressible.CompressionStep
import xyz.devvydont.smprpg.items.interfaces.ISellable
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.extensions.calculateCompressedWorth

class PotatoBlueprintFamily(itemService: ItemService, type: CustomItemType) : CustomItemBlueprint(itemService, type),
    ICompressible, ISellable {
    override val itemClassification: ItemClassification get() = ItemClassification.MATERIAL

    override val decompressor: CompressionStep?
        get() = when (customItemType) {
            CustomItemType.PREMIUM_POTATO -> CompressionStep(itemService.getVanillaBlueprint(ItemStack.of(Material.POTATO)) as ICompressible, 1, 9)
            CustomItemType.PREMIUM_BAKED_POTATO -> CompressionStep(itemService.getBlueprint(CustomItemType.PREMIUM_POTATO) as ICompressible, 1, 9)
            CustomItemType.ENCHANTED_POTATO -> CompressionStep(itemService.getBlueprint(CustomItemType.PREMIUM_BAKED_POTATO) as ICompressible, 1, 9)
            CustomItemType.ENCHANTED_BAKED_POTATO -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_POTATO) as ICompressible, 1, 9)
            CustomItemType.POTATO_SINGULARITY -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_BAKED_POTATO) as ICompressible, 1, 9)
            else -> null
        }

    override val compressor: CompressionStep?
        get() = when (customItemType) {
            CustomItemType.PREMIUM_POTATO -> CompressionStep(itemService.getBlueprint(CustomItemType.PREMIUM_BAKED_POTATO) as ICompressible, 9, 1)
            CustomItemType.PREMIUM_BAKED_POTATO -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_POTATO) as ICompressible, 9, 1)
            CustomItemType.ENCHANTED_POTATO -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_BAKED_POTATO) as ICompressible, 9, 1)
            CustomItemType.ENCHANTED_BAKED_POTATO -> CompressionStep(itemService.getBlueprint(CustomItemType.POTATO_SINGULARITY) as ICompressible, 9, 1)
            else -> null
        }

    override fun getWorth(itemStack: ItemStack): Int {
        return this.calculateCompressedWorth(itemStack)
    }
}
