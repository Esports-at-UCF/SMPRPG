package xyz.devvydont.smprpg.entity.fishing

import kr.toxicity.model.api.BetterModel
import kr.toxicity.model.api.bukkit.platform.BukkitAdapter
import kr.toxicity.model.api.data.renderer.ModelRenderer
import kr.toxicity.model.api.tracker.EntityTracker
import kr.toxicity.model.api.tracker.ModelScaler
import kr.toxicity.model.api.tracker.TrackerModifier
import kr.toxicity.model.api.util.function.BonePredicate
import org.bukkit.Bukkit
import org.bukkit.damage.DamageType
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Pig
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.entity.CustomEntityType
import xyz.devvydont.smprpg.entity.MobType
import xyz.devvydont.smprpg.entity.fishing.goals.SharkAttackGoal
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.services.ItemService.Companion.generate
import xyz.devvydont.smprpg.util.items.ChancedItemDrop
import xyz.devvydont.smprpg.util.items.LootDrop
import java.util.List
import java.util.function.Function

class Shark(entity: LivingEntity?, entityType: CustomEntityType?) : SeaCreature<Pig?>(entity as Pig?, entityType),
    Listener {

    override fun setup() {
        mobTypes.add(MobType.SEA_CREATURE)
        mobTypes.add(MobType.AQUATIC)
        mobTypes.add(MobType.ANIMAL)

        entityTracker = BetterModel.model("shark")
            .map(Function { r: ModelRenderer? -> r!!.getOrCreate(BukkitAdapter.adapt(_entity!!)) })
            .orElse(null)
        entityTracker.scaler(ModelScaler.value(2.0f))
        entityTracker.forceUpdate(true)
        brightenNametag()

        super.setup()
        val mobGoals = Bukkit.getMobGoals()
        mobGoals.removeAllGoals(_entity!!)
        mobGoals.addGoal(_entity, 3, SharkAttackGoal(_entity, this))
    }

    override fun updateAttributes() {
        super.updateAttributes()
    }

    override fun getItemDrops(): MutableCollection<LootDrop> {
        return mutableListOf(
            ChancedItemDrop(generate(CustomItemType.SHARK_FIN), 1, this),
            ChancedItemDrop(generate(CustomItemType.PREDATOR_TOOTH), 50, this),
            ChancedItemDrop(lureScroll, 800, this),
            ChancedItemDrop(abyssalInstinctScroll, 800, this),
            ChancedItemDrop(impalingScroll, 800, this)
        )
    }

    @EventHandler
    fun onSharkDrownOrCram(event: EntityDamageEvent) {
        if (event.getEntity() == _entity) {
            val dmgType = event.damageSource.damageType
            if (dmgType === DamageType.IN_WALL || dmgType === DamageType.DROWN) {
                event.isCancelled = true
            }
        }
    }

    companion object {
        const val RATING_REQUIREMENT: Int = 120
    }
}
