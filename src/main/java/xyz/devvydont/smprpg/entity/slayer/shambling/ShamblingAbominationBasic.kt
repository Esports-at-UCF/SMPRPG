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
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.services.ItemService.Companion.generate
import xyz.devvydont.smprpg.util.items.ChancedItemDrop
import xyz.devvydont.smprpg.util.items.LootDrop
import xyz.devvydont.smprpg.util.items.QuantityLootDrop
import java.util.List

class ShamblingAbominationBasic(entity: LivingEntity?, entityType: CustomEntityType?) : ShamblingAbominationParent(entity as Zombie?, entityType) {
    override fun getItemDrops(): MutableCollection<LootDrop?>? {
        return List.of<LootDrop?>(
            QuantityLootDrop(generate(CustomItemType.NECROTIC_FLESH), 1, 3, this),
            QuantityLootDrop(generate(Material.ROTTEN_FLESH), 5, 16, this),
            ChancedItemDrop(generate(CustomItemType.PREMIUM_FLESH), 20, this),
            ChancedItemDrop(generate(CustomItemType.ENCHANTED_FLESH), 100, this)
        )
    }

    override fun updateAttributes() {
        super.updateAttributes()
        updateBaseAttribute(AttributeWrapper.KNOCKBACK_RESISTANCE, 0.25)
        updateBaseAttribute(AttributeWrapper.SCALE, 1.0)
        updateBaseAttribute(AttributeWrapper.DEFENSE, 25.0)
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
            mobGoals.addGoal(zombie, 3, ShamblingAbominationChaseGoal(this, null, 1.5))
        }
    }
}