package xyz.devvydont.smprpg.items.blueprints.resources.woodcutting

import net.kyori.adventure.key.Key
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint
import xyz.devvydont.smprpg.items.interfaces.ICompressible
import xyz.devvydont.smprpg.items.interfaces.ICompressible.CompressionStep
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden
import xyz.devvydont.smprpg.items.interfaces.ISellable
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.extensions.calculateCompressedWorth

class SkyrootBlueprintFamily(itemService: ItemService, type: CustomItemType) : CustomItemBlueprint(itemService, type),
    ICompressible, ISellable, IModelOverridden {
    override val itemClassification: ItemClassification get() = ItemClassification.MATERIAL

    override val decompressor: CompressionStep?
        get() = when (customItemType) {
            CustomItemType.PREMIUM_SKYROOT_LOG -> CompressionStep(itemService.getBlueprint(CustomItemType.SKYROOT_LOG) as ICompressible, 1, 9)
            CustomItemType.ENCHANTED_SKYROOT_LOG -> CompressionStep(itemService.getBlueprint(CustomItemType.PREMIUM_SKYROOT_LOG) as ICompressible, 1, 9)
            CustomItemType.SKYROOT_LOG_SINGULARITY -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_SKYROOT_LOG) as ICompressible, 1, 9)
            else -> null
        }

    override val compressor: CompressionStep?
        get() = when (customItemType) {
            CustomItemType.PREMIUM_SKYROOT_LOG -> CompressionStep(itemService.getBlueprint(CustomItemType.ENCHANTED_SKYROOT_LOG) as ICompressible, 9, 1)
            CustomItemType.ENCHANTED_SKYROOT_LOG -> CompressionStep(itemService.getBlueprint(CustomItemType.SKYROOT_LOG_SINGULARITY) as ICompressible, 9, 1)
            else -> null
        }

    override fun getWorth(itemStack: ItemStack): Int {
        return this.calculateCompressedWorth(itemStack)
    }

    override fun getDisplayKey(): Key? {
        return NamespacedKey(SMPRPG.plugin, "skyroot_log")
    }
}
