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
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.services.ItemService.Companion.generate
import xyz.devvydont.smprpg.util.items.ChancedItemDrop
import xyz.devvydont.smprpg.util.items.LootDrop
import xyz.devvydont.smprpg.util.items.QuantityLootDrop
import java.util.List

class ShamblingAbominationAdvanced(entity: LivingEntity?, entityType: CustomEntityType?) : ShamblingAbominationParent(entity as Zombie?, entityType) {
    override fun getItemDrops(): MutableCollection<LootDrop?>? {
        return List.of<LootDrop?>(
            QuantityLootDrop(generate(Material.ROTTEN_FLESH), 24, 48, this),
            QuantityLootDrop(generate(CustomItemType.PREMIUM_FLESH), 1, 2, this),
            ChancedItemDrop(generate(CustomItemType.ENCHANTED_FLESH), 50, this)
        )
    }

    override fun updateAttributes() {
        super.updateAttributes()
        updateBaseAttribute(AttributeWrapper.KNOCKBACK_RESISTANCE, 0.5)
        updateBaseAttribute(AttributeWrapper.SCALE, 1.1)
        updateBaseAttribute(AttributeWrapper.DEFENSE, 150.0)
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
            mobGoals.addGoal(zombie, 3, ShamblingAbominationChaseGoal(this, null, 1.7))
            mobGoals.addGoal(zombie, 4, ShamblingAbominationEnrageGoal(this, null))
        }
    }
}