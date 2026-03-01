package xyz.devvydont.smprpg.entity.slayer.shambling.goals

import com.destroystokyo.paper.entity.ai.Goal
import com.destroystokyo.paper.entity.ai.GoalKey
import com.destroystokyo.paper.entity.ai.GoalType
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.entity.Zombie
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.entity.slayer.shambling.ShamblingAbominationParent
import xyz.devvydont.smprpg.util.goals.GoalUtils
import java.util.EnumSet

class ShamblingAbominationChaseGoal(val slayer : ShamblingAbominationParent, val spawnPlayer : Player?, val chaseSpeed : Double) : Goal<Zombie> {

    var attackClock = 0
    val zombie = slayer.entity as Zombie
    var stopped = false

    override fun shouldActivate(): Boolean {
        if (GoalUtils.inst.getClosestPlayer(zombie, 20.0) != null) {
            return true
        }
        else
            return false
    }

    override fun getKey(): GoalKey<Zombie> {
        return GOAL_KEY
    }

    override fun getTypes(): EnumSet<GoalType> {
        return EnumSet.of(GoalType.TARGET, GoalType.MOVE)
    }

    override fun shouldStayActive(): Boolean {
        return shouldActivate();
    }

    override fun start() {
        stopped = false
        zombie.target = GoalUtils.inst.getClosestPlayer(zombie, 20.0)
    }

    override fun stop() {
        zombie.target = null
        stopped = true
        zombie.pathfinder.stopPathfinding()
    }

    override fun tick() {
        if (!stopped) {
            var closestPlayer = GoalUtils.chaseClosestPlayer(zombie, 20.0, chaseSpeed, spawnPlayer)
            if (closestPlayer in zombie.world.getNearbyPlayers(zombie.location, 0.5) && attackClock <= 0) {
                zombie.attack(closestPlayer)
                attackClock = slayer.attackCooldown
            }
            attackClock--
        }
    }

    companion object {
        val GOAL_KEY : GoalKey<Zombie> = GoalKey.of(Zombie::class.java, NamespacedKey(SMPRPG.Companion.plugin, "shambling_abomination_brain_goal"))
    }

}