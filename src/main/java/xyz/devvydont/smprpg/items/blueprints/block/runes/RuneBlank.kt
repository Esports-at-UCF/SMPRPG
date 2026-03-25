package xyz.devvydont.smprpg.items.blueprints.block.runes

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.block.CustomBlock
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.blueprints.block.BlockBlueprint
import xyz.devvydont.smprpg.items.interfaces.IFooterDescribable
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.formatting.ComponentUtils

class RuneBlank(itemService: ItemService, type: CustomItemType) : BlockBlueprint(itemService, type),
    IFooterDescribable {
    override fun getCustomBlock(): CustomBlock {
        return CustomBlock.RUNE_BLANK
    }

    override fun getFooter(itemStack: ItemStack?): MutableList<Component?> {
        return mutableListOf(
            ComponentUtils.create("When placed under an Enchanting Table:", NamedTextColor.LIGHT_PURPLE),
            ComponentUtils.create("Just looks neat, I guess.", NamedTextColor.GRAY)
        )
    }
}
