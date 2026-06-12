package xyz.devvydont.smprpg.items.blueprints.vanilla

import io.papermc.paper.datacomponent.DataComponentTypes
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.checkerframework.common.value.qual.IntRange
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.base.VanillaItemBlueprint
import xyz.devvydont.smprpg.items.interfaces.ICompressible
import xyz.devvydont.smprpg.items.interfaces.ICompressible.CompressionStep
import xyz.devvydont.smprpg.services.ItemService

class EnderPearlBlueprint(itemService: ItemService, material: Material) :
    VanillaItemBlueprint(itemService, material), ICompressible {

    override val decompressor: CompressionStep? get() = null

    override val compressor: CompressionStep? get() =
        CompressionStep(itemService.getBlueprint(CustomItemType.PREMIUM_ENDER_PEARL) as ICompressible, 9, 1)

    override fun updateItemData(itemStack: ItemStack) {
        super.updateItemData(itemStack)
        itemStack.setData<@IntRange(from = 1L, to = 99L) Int?>(DataComponentTypes.MAX_STACK_SIZE, 64)
    }
}
