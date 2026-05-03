package xyz.devvydont.smprpg.entity.fishing.goals

import com.destroystokyo.paper.entity.ai.Goal
import com.destroystokyo.paper.entity.ai.GoalKey
import com.destroystokyo.paper.entity.ai.GoalType
import kr.toxicity.model.api.animation.AnimationModifier
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.entity.Pig
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.entity.fishing.Shark
import xyz.devvydont.smprpg.util.goals.GoalUtils
import java.util.*


class SharkAttackGoal(val pig : Pig, val customEntity : Shark) : Goal<Pig> {

    val goalKey : GoalKey<Pig> = GoalKey.of(Pig::class.java, NamespacedKey(SMPRPG.plugin, "shark_attack"))
    var attackClock = 0

    override fun shouldActivate(): Boolean {
        if (GoalUtils.getClosestPlayer(pig, 20.0) != null) {
            return true
        }
        else
            return false
    }

    override fun getKey(): GoalKey<Pig> {
        return goalKey
    }

    override fun getTypes(): EnumSet<GoalType> {
        return EnumSet.of(GoalType.TARGET, GoalType.MOVE)
    }

    override fun shouldStayActive(): Boolean {
        return shouldActivate();
    }

    override fun start() {
        pig.target = GoalUtils.chaseClosestPlayer(pig, 20.0, 1.0)
    }

    override fun stop() {
        pig.target = null
        pig.pathfinder.stopPathfinding()
    }

    override fun tick() {
        var closestPlayer = GoalUtils.chaseClosestPlayer(pig, 30.0, 1.0)
        pig.lookAt(closestPlayer)
        if (closestPlayer in pig.world.getNearbyPlayers(pig.location, 1.25) && attackClock <= 0) {
            pig.attack(closestPlayer)
            pig.world.playSound(pig.location, Sound.ENTITY_PHANTOM_BITE, 1.0f, 0.5f)
            pig.world.playSound(pig.location, Sound.ENTITY_ZOMBIE_DESTROY_EGG, 1.0f, 0.5f)
            customEntity.entityTracker!!.animate("attack", AnimationModifier.DEFAULT_WITH_PLAY_ONCE)
            attackClock = 10
        }
        attackClock--
    }

}