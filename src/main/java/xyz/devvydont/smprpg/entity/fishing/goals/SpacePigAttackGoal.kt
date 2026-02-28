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
import java.util.*


class SpacePigAttackGoal(val pig : Pig) : Goal<Pig> {

    val goalKey : GoalKey<Pig> = GoalKey.of(Pig::class.java, NamespacedKey(SMPRPG.plugin, "space_pig_attack"))
    var attackCooldown = 0

    override fun shouldActivate(): Boolean {
        if (getClosestPlayer() != null) {
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

    private fun getClosestPlayer(): Player? {
        val nearbyPlayers: MutableCollection<Player> = pig.getWorld().getNearbyPlayers(
            pig.getLocation(),
            20.0,
            { player -> !player.isDead() && (player.getGameMode() !== GameMode.SPECTATOR && player.getGameMode() !== GameMode.CREATIVE) && player.isValid() })
        var closestDistance = -1.0
        var closestPlayer: Player? = null
        for (player in nearbyPlayers) {
            val distance = player.getLocation().distanceSquared(pig.getLocation())
            if (closestDistance != -1.0 && !(distance < closestDistance)) {
                continue
            }
            closestDistance = distance
            closestPlayer = player
        }
        return closestPlayer
    }

    override fun shouldStayActive(): Boolean {
        return shouldActivate();
    }

    override fun start() {
        pig.target = getClosestPlayer()
    }

    override fun stop() {
        pig.target = null
        pig.pathfinder.stopPathfinding()
    }

    override fun tick() {
        var closestPlayer = getClosestPlayer() as LivingEntity
        pig.setTarget(closestPlayer)
        if (closestPlayer in pig.world.getNearbyPlayers(pig.location, 2.0) && attackCooldown <= 0) {
            pig.attack(closestPlayer)
            attackCooldown = 10
        }
        attackCooldown--
        if (pig.getLocation().distanceSquared(closestPlayer.getLocation()) < 6.25) {
            pig.getPathfinder().stopPathfinding()
        } else {
            pig.getPathfinder().moveTo(closestPlayer, 3.0)
        }
    }

}