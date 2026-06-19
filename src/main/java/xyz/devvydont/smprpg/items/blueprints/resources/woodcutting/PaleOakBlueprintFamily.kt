package xyz.devvydont.smprpg.items.blueprints.resources.woodcutting

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

class PaleOakBlueprintFamily(itemService: ItemService, type: CustomItemType) : CustomItemBlueprint(itemService, type),
    ICompressible, ISellable {
    override val itemClassification: ItemClassification get() = ItemClassification.MATERIAL

    override val decompressor: CompressionStep?
        get() = when (customItemType) {
            CustomItemType.PREMIUM_PALE_OAK_LOG -> CompressionStep(itemService.getVanillaBlueprint(ItemStack.of(Material.PALE_OAK_LOG)) as ICompressible, 1, 9)
            CustomItemType.ENCHANTED_PALE_OAK_LOG -> CompressionStep(itemService.getBlueprint(CustomItemType.PREMIUM_PALE_OAK_LOG) as ICompressible, 1, 9)
            CustomItemType.PALE_OAK_LOG_SINGULARITY -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_PALE_OAK_LOG) as ICompressible, 1, 9)
            else -> null
        }

    override val compressor: CompressionStep?
        get() = when (customItemType) {
            CustomItemType.PREMIUM_PALE_OAK_LOG -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_PALE_OAK_LOG) as ICompressible, 9, 1)
            CustomItemType.ENCHANTED_PALE_OAK_LOG -> CompressionStep(itemService.getBlueprint(CustomItemType.PALE_OAK_LOG_SINGULARITY) as ICompressible, 9, 1)
            else -> null
        }

    override fun getWorth(itemStack: ItemStack): Int {
        return this.calculateCompressedWorth(itemStack)
    }
}
