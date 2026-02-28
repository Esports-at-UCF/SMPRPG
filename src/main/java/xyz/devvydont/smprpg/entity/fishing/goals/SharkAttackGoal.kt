package xyz.devvydont.smprpg.entity.fishing.goals

import com.destroystokyo.paper.entity.ai.Goal
import com.destroystokyo.paper.entity.ai.GoalKey
import com.destroystokyo.paper.entity.ai.GoalType
import org.bukkit.GameMode
import org.bukkit.NamespacedKey
import org.bukkit.entity.Dolphin
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import xyz.devvydont.smprpg.SMPRPG
import java.util.*


class SharkAttackGoal(val shark : Dolphin) : Goal<Dolphin> {

    val goalKey : GoalKey<Dolphin> = GoalKey.of(Dolphin::class.java, NamespacedKey(SMPRPG.plugin, "shark_attack"))

    override fun shouldActivate(): Boolean {
        if (getClosestPlayer() != null) {
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

    private fun getClosestPlayer(): Player? {
        val nearbyPlayers: MutableCollection<Player> = shark.getWorld().getNearbyPlayers(
            shark.getLocation(),
            20.0,
            { player -> !player.isDead() && (player.getGameMode() !== GameMode.SPECTATOR && player.getGameMode() !== GameMode.CREATIVE) && player.isValid() })
        var closestDistance = -1.0
        var closestPlayer: Player? = null
        for (player in nearbyPlayers) {
            val distance = player.getLocation().distanceSquared(shark.getLocation())
            if (closestDistance != -1.0 && !(distance < closestDistance)) {
                continue
            }
            println(distance)
            closestDistance = distance
            closestPlayer = player
        }
        return closestPlayer
    }

    override fun shouldStayActive(): Boolean {
        return shouldActivate();
    }

    override fun start() {
        shark.target = getClosestPlayer()
    }

    override fun stop() {
        shark.target = null
        shark.pathfinder.stopPathfinding()
    }

    override fun tick() {
        shark.remainingAir = 2000
        var closestPlayer = getClosestPlayer() as LivingEntity
        shark.setTarget(closestPlayer)
        if (shark.getLocation().distanceSquared(closestPlayer.getLocation()) < 6.25) {
            shark.getPathfinder().stopPathfinding()
        } else {
            shark.getPathfinder().moveTo(closestPlayer, 10.0)
        }
    }

}