package xyz.devvydont.smprpg.entity.slayer.goals

import com.destroystokyo.paper.entity.ai.Goal
import com.destroystokyo.paper.entity.ai.GoalKey
import com.destroystokyo.paper.entity.ai.GoalType
import org.bukkit.GameMode
import org.bukkit.NamespacedKey
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Pig
import org.bukkit.entity.Player
import org.bukkit.entity.Zombie
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.util.goals.GoalUtils
import java.util.EnumSet
import kotlin.compareTo
import kotlin.dec

class ShamblingAbominationBrainGoal(val zombie : Zombie, val spawnPlayer : Player?) : Goal<Zombie> {

    val goalKey : GoalKey<Zombie> = GoalKey.of(Zombie::class.java, NamespacedKey(SMPRPG.Companion.plugin, "shambling_abomination_brain_goal"))
    var attackCooldown = 0
    val chaseSpeed = 1.5

    override fun shouldActivate(): Boolean {
        if (GoalUtils.inst.getClosestPlayer(zombie, 20.0) != null) {
            return true
        }
        else
            return false
    }

    override fun getKey(): GoalKey<Zombie> {
        return goalKey
    }

    override fun getTypes(): EnumSet<GoalType> {
        return EnumSet.of(GoalType.TARGET, GoalType.MOVE)
    }

    override fun shouldStayActive(): Boolean {
        return shouldActivate();
    }

    override fun start() {
        zombie.target = GoalUtils.inst.getClosestPlayer(zombie, 20.0)
    }

    override fun stop() {
        zombie.target = null
        zombie.pathfinder.stopPathfinding()
    }

    override fun tick() {
        var closestPlayer = GoalUtils.chaseClosestPlayer(zombie, 20.0, chaseSpeed, spawnPlayer)
        if (closestPlayer in zombie.world.getNearbyPlayers(zombie.location, 0.5) && attackCooldown <= 0) {
            zombie.attack(closestPlayer)
            attackCooldown = 10
        }
        attackCooldown--
    }

}