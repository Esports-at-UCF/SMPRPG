package xyz.devvydont.smprpg.recipe.postprocessors

import net.momirealms.craftengine.bukkit.api.CraftEngineItems
import net.momirealms.craftengine.core.item.CustomItem
import net.momirealms.craftengine.core.item.Item
import net.momirealms.craftengine.core.item.ItemBuildContext
import net.momirealms.craftengine.core.item.recipe.result.PostProcessor
import net.momirealms.craftengine.core.item.recipe.result.PostProcessorFactory
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.services.ItemService

class UpdateItemDataPostProcessor : PostProcessor {

    override fun <I : Any?> process(item: Item<I?>?, context: ItemBuildContext?): Item<I?>? {
        val customItem : CustomItem<ItemStack> = CraftEngineItems.byId(item!!.id())!!
        val itemStack = customItem.buildItemStack()
        val bp = ItemService.blueprint(itemStack)
        bp.updateItemData(itemStack)
        val retItem : Item<I>? = CraftEngineItems.byItemStack(itemStack)?.buildItem(ItemBuildContext.empty()) as Item<I>?
        return retItem as Item<I?>?
    }

    companion object {
        val FACTORY = Factory()

        class Factory() : PostProcessorFactory<UpdateItemDataPostProcessor> {
            override fun create(args: Map<String?, Any?>?): UpdateItemDataPostProcessor? {
                return UpdateItemDataPostProcessor()
            }
        }
    }
}