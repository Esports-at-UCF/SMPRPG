package xyz.devvydont.smprpg.util.goals

import org.bukkit.GameMode
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Mob
import org.bukkit.entity.Player

class GoalUtils {

    companion object {
        fun chaseClosestPlayer(mob: Mob, radius: Double, speed: Double, priorityPlayer : Player? = null): Player {
            var closestPlayer = getClosestPlayer(mob, radius, priorityPlayer) as LivingEntity
            mob.target = closestPlayer
            if (mob.location.distanceSquared(closestPlayer.location) < 0.25) {
                mob.pathfinder.stopPathfinding()
            } else {
                mob.pathfinder.moveTo(closestPlayer, speed)
            }
            return closestPlayer as Player
        }

        fun getClosestPlayer(mob: Mob, radius: Double, priorityPlayer: Player? = null): Player? {
            val nearbyPlayers: MutableCollection<Player> = mob.world.getNearbyPlayers(
                mob.location,
                radius,
                { player -> !player.isDead && (player.gameMode !== GameMode.SPECTATOR && player.gameMode !== GameMode.CREATIVE) && player.isValid })
            var closestDistance = -1.0
            var closestPlayer: Player? = null
            for (player in nearbyPlayers) {
                val distance = player.location.distanceSquared(mob.getLocation())
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
    }
}