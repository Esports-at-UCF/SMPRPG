package xyz.devvydont.smprpg.entity.fishing.goals

import com.destroystokyo.paper.entity.ai.Goal
import com.destroystokyo.paper.entity.ai.GoalKey
import com.destroystokyo.paper.entity.ai.GoalType
import org.bukkit.GameMode
import org.bukkit.NamespacedKey
import org.bukkit.entity.Dolphin
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Pig
import org.bukkit.entity.Player
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.util.goals.GoalUtils
import java.util.*


class SharkAttackGoal(val dolphin: Dolphin) : Goal<Dolphin> {

    val goalKey : GoalKey<Dolphin> = GoalKey.of(Dolphin::class.java, NamespacedKey(SMPRPG.plugin, "shark_attack"))
    val armorStand = dolphin.vehicle!!
    var attackClock = 0
    var teleportClock = 10

    override fun shouldActivate(): Boolean {
        if (GoalUtils.inst.getClosestPlayer(dolphin, 20.0) != null) {
            return true
        }
        else
            return false
    }

    override fun getKey(): GoalKey<Dolphin> {
        return goalKey
    }

    override fun getTypes(): EnumSet<GoalType> {
        return EnumSet.of(GoalType.TARGET, GoalType.MOVE)
    }

    override fun shouldStayActive(): Boolean {
        return shouldActivate();
    }

    override fun start() {
        dolphin.target = GoalUtils.chaseClosestPlayer(dolphin, 20.0, 1.0)
    }

    override fun stop() {
        dolphin.target = null
        dolphin.pathfinder.stopPathfinding()
    }

    override fun tick() {
        val closestPlayer = GoalUtils.chaseClosestPlayer(dolphin, 20.0, 1.0)
        dolphin.lookAt(closestPlayer)
        if (closestPlayer in dolphin.world.getNearbyPlayers(dolphin.location, 0.125) && attackClock <= 0) {
            dolphin.attack(closestPlayer)
            attackClock = 10
        }
        if (teleportClock <= 0) {
            val locCopy = closestPlayer.location.clone()
            locCopy.y -= 2
            armorStand.teleport(locCopy)
            teleportClock = 4
        }
        teleportClock--
        attackClock--
    }

}