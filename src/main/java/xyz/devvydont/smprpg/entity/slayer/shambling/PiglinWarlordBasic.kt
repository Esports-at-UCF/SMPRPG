package xyz.devvydont.smprpg.entity.slayer.shambling

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.PiglinBrute
import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.entity.CustomEntityType
import xyz.devvydont.smprpg.entity.slayer.piglin.PiglinWarlordParent
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.items.ChancedItemDrop
import xyz.devvydont.smprpg.util.items.LootDrop
import xyz.devvydont.smprpg.util.items.QuantityLootDrop

class PiglinWarlordBasic(entity: LivingEntity, entityType: CustomEntityType) : PiglinWarlordParent(entity as PiglinBrute, entityType) {
    override fun getItemDrops(): List<LootDrop> {
        return listOf(
            QuantityLootDrop(ItemService.Companion.generate(CustomItemType.HOG_SKIN), 1, 3, this),
            QuantityLootDrop(ItemService.Companion.generate(Material.GOLD_INGOT), 5, 16, this),
            ChancedItemDrop(ItemService.Companion.generate(Material.GOLD_BLOCK), 20, this),
            ChancedItemDrop(ItemService.Companion.generate(CustomItemType.ENCHANTED_GOLD), 100, this),

            //ChancedItemDrop(ItemService.Companion.generate(CustomItemType.REVILED_VISCERA), 100, this)
        )
    }

    override fun updateAttributes() {
        super.updateAttributes()
        updateBaseAttribute(AttributeWrapper.KNOCKBACK_RESISTANCE, 0.25)
        updateBaseAttribute(AttributeWrapper.DEFENSE, 25.0)
    }

    override fun setup() {
        super.setup()
        val piglin = entity as PiglinBrute
        val mobGoals = Bukkit.getMobGoals()
        mobGoals.removeAllGoals(piglin)

    }
}