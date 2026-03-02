package xyz.devvydont.smprpg.entity.slayer.shambling

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Zombie
import org.bukkit.event.EventHandler
import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.entity.CustomEntityType
import xyz.devvydont.smprpg.entity.slayer.shambling.goals.ShamblingAbominationChaseGoal
import xyz.devvydont.smprpg.entity.slayer.shambling.goals.ShamblingAbominationEnrageGoal
import xyz.devvydont.smprpg.entity.slayer.shambling.goals.ShamblingAbominationImplodeGoal
import xyz.devvydont.smprpg.entity.slayer.shambling.goals.ShamblingAbominationSyphonGoal
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.services.ItemService.Companion.generate
import xyz.devvydont.smprpg.util.items.ChancedItemDrop
import xyz.devvydont.smprpg.util.items.LootDrop
import xyz.devvydont.smprpg.util.items.QuantityLootDrop
import xyz.devvydont.smprpg.util.time.TickTime
import java.util.List

class ShamblingAbominationBrutal(entity: LivingEntity?, entityType: CustomEntityType?) : ShamblingAbominationParent(entity as Zombie?, entityType) {
    override var enrageThreshold = 0.35

    override fun getItemDrops(): MutableCollection<LootDrop?>? {
        return List.of<LootDrop?>(
            QuantityLootDrop(generate(CustomItemType.ENCHANTED_NECROTIC_FLESH), 1, 2, this),
            ChancedItemDrop(generate(CustomItemType.NECROTIC_FLESH_SINGULARITY), 50, this),
            QuantityLootDrop(generate(CustomItemType.PREMIUM_FLESH), 12, 20, this),
            QuantityLootDrop(generate(CustomItemType.ENCHANTED_FLESH), 1, 3, this)
        )
    }

    override fun updateAttributes() {
        super.updateAttributes()
        updateBaseAttribute(AttributeWrapper.KNOCKBACK_RESISTANCE, 1.0)
        updateBaseAttribute(AttributeWrapper.SCALE, 1.25)
        updateBaseAttribute(AttributeWrapper.DEFENSE, 600.0)
    }

    @EventHandler
    private fun onShamblingAbominationSpawn(event: EntityAddToWorldEvent) {
        // TODO: Replace with SlayerSpawnBossEvent when proper spawning tech is in.
        val entity = this.entity
        if (event.getEntity() == entity) {
            val zombie = entity as Zombie
            zombie.setAdult()
            val mobGoals = Bukkit.getMobGoals()
            mobGoals.removeAllGoals(zombie)
            mobGoals.addGoal(zombie, 0, ShamblingAbominationChaseGoal(this, null, 2.0))
            mobGoals.addGoal(zombie, 1, ShamblingAbominationEnrageGoal(this, null))
            mobGoals.addGoal(zombie, 2, ShamblingAbominationImplodeGoal(this, null, TickTime.seconds(10).toInt(), 1000.0))
            mobGoals.addGoal(zombie, 3, ShamblingAbominationSyphonGoal(this, null, 0.1))
        }
    }
}