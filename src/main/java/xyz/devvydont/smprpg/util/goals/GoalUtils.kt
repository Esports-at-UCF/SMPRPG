package xyz.devvydont.smprpg.util.goals

import org.bukkit.GameMode
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Mob
import org.bukkit.entity.Player

class GoalUtils {

    fun getClosestPlayer(mob: Mob, radius: Double, priorityPlayer: Player? = null): Player? {
        val nearbyPlayers: MutableCollection<Player> = mob.getWorld().getNearbyPlayers(
            mob.getLocation(),
            radius,
            { player -> !player.isDead() && (player.getGameMode() !== GameMode.SPECTATOR && player.getGameMode() !== GameMode.CREATIVE) && player.isValid() })
        var closestDistance = -1.0
        var closestPlayer: Player? = null
        for (player in nearbyPlayers) {
            val distance = player.getLocation().distanceSquared(mob.getLocation())
            if ((priorityPlayer != null) && player == priorityPlayer) {
                closestDistance = player.getLocation().distanceSquared(mob.getLocation())
                if (closestDistance != -1.0 && !(distance < closestDistance)) {
                    continue
                }
                return player
            }
            if (closestDistance != -1.0 && !(distance < closestDistance)) {
                continue
            }
            closestDistance = distance
            closestPlayer = player
        }
        return closestPlayer
    }

    companion object {
        val inst = GoalUtils()
        fun chaseClosestPlayer(mob: Mob, radius: Double, speed: Double, priorityPlayer : Player? = null): Player {
            var closestPlayer = inst.getClosestPlayer(mob, radius, priorityPlayer) as LivingEntity
            mob.setTarget(closestPlayer)
            if (mob.getLocation().distanceSquared(closestPlayer.getLocation()) < 0.25) {
                mob.getPathfinder().stopPathfinding()
            } else {
                mob.getPathfinder().moveTo(closestPlayer, speed)
            }
            return closestPlayer as Player
        }
    }
}