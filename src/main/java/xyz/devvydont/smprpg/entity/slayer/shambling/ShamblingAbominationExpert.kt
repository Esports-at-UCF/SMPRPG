package xyz.devvydont.smprpg.entity.slayer.shambling

import org.bukkit.Bukkit
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Zombie
import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.entity.CustomEntityType
import xyz.devvydont.smprpg.entity.slayer.shambling.goals.ShamblingAbominationChaseGoal
import xyz.devvydont.smprpg.entity.slayer.shambling.goals.ShamblingAbominationEnrageGoal
import xyz.devvydont.smprpg.entity.slayer.shambling.goals.ShamblingAbominationImplodeGoal
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.services.ItemService.Companion.generate
import xyz.devvydont.smprpg.util.items.ChancedItemDrop
import xyz.devvydont.smprpg.util.items.LootDrop
import xyz.devvydont.smprpg.util.items.QuantityLootDrop
import xyz.devvydont.smprpg.util.time.TickTime

class ShamblingAbominationExpert(entity: LivingEntity?, entityType: CustomEntityType?) : ShamblingAbominationParent(entity as Zombie?, entityType) {
    override fun getItemDrops(): List<LootDrop> {
        return listOf(
            QuantityLootDrop(generate(CustomItemType.PREMIUM_NECROTIC_FLESH), 5, 8, this),
            ChancedItemDrop(generate(CustomItemType.ENCHANTED_NECROTIC_FLESH), 5, this),
            QuantityLootDrop(generate(CustomItemType.PREMIUM_FLESH), 4, 7, this),
            ChancedItemDrop(generate(CustomItemType.ENCHANTED_FLESH), 5, this),

            ChancedItemDrop(LOOT_SMITE_SCROLL, 40, this),
            ChancedItemDrop(generate(CustomItemType.UNDIGESTED_BRAINS), 250, this),
            ChancedItemDrop(generate(CustomItemType.SYPHON_SPELL), 250, this),
            ChancedItemDrop(generate(CustomItemType.NECRONOMICON_EXCERPTS), 500, this),
            ChancedItemDrop(generate(CustomItemType.REVILED_VISCERA), 10, this)
        )
    }

    override fun updateAttributes() {
        super.updateAttributes()
        updateBaseAttribute(AttributeWrapper.KNOCKBACK_RESISTANCE, 0.75)
        updateBaseAttribute(AttributeWrapper.SCALE, 1.15)
        updateBaseAttribute(AttributeWrapper.DEFENSE, 300.0)
    }

    override fun setup() {
        super.setup()
        val zombie = entity as Zombie
        zombie.setAdult()
        if (zombie.vehicle != null)
            zombie.vehicle!!.removePassenger(zombie)
        val mobGoals = Bukkit.getMobGoals()
        mobGoals.removeAllGoals(zombie)
        mobGoals.addGoal(zombie, 3, ShamblingAbominationChaseGoal(this, null, 1.8))
        mobGoals.addGoal(zombie, 4, ShamblingAbominationEnrageGoal(this, null))
        mobGoals.addGoal(zombie, 5, ShamblingAbominationImplodeGoal(this, null, TickTime.seconds(10).toInt()))
    }
}