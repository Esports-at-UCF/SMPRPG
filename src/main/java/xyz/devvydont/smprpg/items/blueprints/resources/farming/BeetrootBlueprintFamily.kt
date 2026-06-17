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

class BeetrootBlueprintFamily(itemService: ItemService, type: CustomItemType) : CustomItemBlueprint(itemService, type),
    ICompressible, ISellable {
    override val itemClassification: ItemClassification get() = ItemClassification.MATERIAL

    override val decompressor: CompressionStep?
        get() = when (customItemType) {
            CustomItemType.PREMIUM_BEETROOT -> CompressionStep(itemService.getVanillaBlueprint(ItemStack.of(Material.BEETROOT)) as ICompressible, 1, 9)
            CustomItemType.ENCHANTED_BEETROOT -> CompressionStep(itemService.getBlueprint(CustomItemType.PREMIUM_BEETROOT) as ICompressible, 1, 9)
            CustomItemType.BEETROOT_SINGULARITY -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_BEETROOT) as ICompressible, 1, 9)
            else -> null
        }

    override val compressor: CompressionStep?
        get() = when (customItemType) {
            CustomItemType.PREMIUM_BEETROOT -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_BEETROOT) as ICompressible, 9, 1)
            CustomItemType.ENCHANTED_BEETROOT -> CompressionStep(itemService.getBlueprint(CustomItemType.BEETROOT_SINGULARITY) as ICompressible, 9, 1)
            else -> null
        }

    override fun getWorth(itemStack: ItemStack): Int {
        return this.calculateCompressedWorth(itemStack)
    }
}
