package xyz.devvydont.smprpg.entity.slayer.illager.goals

import com.destroystokyo.paper.entity.ai.Goal
import com.destroystokyo.paper.entity.ai.GoalKey
import com.destroystokyo.paper.entity.ai.GoalType
import io.papermc.paper.entity.LookAnchor
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.*
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.SMPRPG.Companion.plugin
import xyz.devvydont.smprpg.ability.listeners.PlayerFreezeService
import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.entity.slayer.SlayerBossInstance
import xyz.devvydont.smprpg.entity.slayer.illager.IllagerWarlockParent
import xyz.devvydont.smprpg.events.slayer.SlayerSpawnBossEvent
import xyz.devvydont.smprpg.services.AttributeService
import xyz.devvydont.smprpg.services.EntityDamageCalculatorService
import xyz.devvydont.smprpg.services.EntityService
import xyz.devvydont.smprpg.slayer.quest.SlayerQuest
import xyz.devvydont.smprpg.slayer.quest.SlayerQuest.SlayerQuestState
import xyz.devvydont.smprpg.util.goals.GoalUtils
import xyz.devvydont.smprpg.util.persistence.KeyStore
import xyz.devvydont.smprpg.util.time.TickTime
import java.util.*
import kotlin.random.Random

class IllagerWarlockSpellGoal(val slayer : IllagerWarlockParent, val spawnPlayer : Player?, val spellCastFrequency: Int, spellOptions : Map<SpellType, Double>) :
    Goal<Evoker> {

    enum class SpellType {
        TELEPORT,
        FIREBALL,
        VEX,
        FANGS,
        TOSS
    }

    enum class TeleportType {
        BEHIND_PLAYER,
        SHORT_RANDOM,
        LONG_RANDOM
    }

    enum class FangType {
        LINE,
        ENCIRCLE,
        SELF_ENCIRCLE,
        // CHASE // Maybe reserve this one for a special attack at a threshold?
    }

    val evoker = slayer.entity as Evoker
    var spellClock = spellCastFrequency
    var nextSpell : SpellType = SpellType.TELEPORT
    val spells : ArrayList<SpellType> = ArrayList()
    var lastGroundedY : Double = 0.0
    val freezeService = SMPRPG.getService(PlayerFreezeService::class.java)

    init {
        for (spellOption in spellOptions) {
            val range = (spellOption.value * 100).toInt()
            for (i in 1..range) {
                spells.add(spellOption.key)
            }
        }
    }

    override fun shouldActivate(): Boolean {
        if (!slayer.activelyInvolvedPlayers.isEmpty())
            return true
        return false
    }

    override fun getKey(): GoalKey<Evoker> {
        return GOAL_KEY
    }

    override fun getTypes(): EnumSet<GoalType> {
        return EnumSet.of(GoalType.TARGET, GoalType.MOVE)
    }

    override fun shouldStayActive(): Boolean {
        return shouldActivate();
    }

    override fun start() {
        spellClock = spellCastFrequency
    }

    override fun stop() {
        spellClock = -1
    }

    override fun tick() {
        // Keep track of the last time we were grounded, and at what Y level we were at.
        // This is done in case we get knocked around in the air so things like fangs dont spawn mid air
        if (evoker.isOnGround)
            lastGroundedY = evoker.location.y

        // Keep an eye on our closest target.
        val closestPlayer : Player? = GoalUtils.inst.getClosestPlayer(evoker, 20.0)
        if (closestPlayer != null) {
            evoker.pathfinder.moveTo(closestPlayer, 0.25)
            evoker.lookAt(closestPlayer)
        }

        // As long as our spell clock is active, we need to be selecting generic spells.
        if (spellClock > 0) {
            spellClock--
            if (spellClock == 20) {
                // Time to pick a spell!
                nextSpell = spells.random()
                when (nextSpell) {
                    SpellType.TELEPORT, SpellType.VEX, SpellType.FANGS -> {
                        evoker.world.playSound(evoker.location, Sound.ENTITY_EVOKER_PREPARE_SUMMON, 1f, 1f)
                        evoker.world.playSound(evoker.location, Sound.ENTITY_EVOKER_CAST_SPELL, 1f, 1f)
                        if (nextSpell == SpellType.VEX)
                            evoker.spell = Spellcaster.Spell.SUMMON_VEX  // Set our spell to cast about 1 second before teleport
                        else if (nextSpell == SpellType.FANGS)
                            evoker.spell = Spellcaster.Spell.FANGS
                        else
                            evoker.spell = Spellcaster.Spell.DISAPPEAR
                    }
                    SpellType.FIREBALL -> {
                        evoker.world.playSound(evoker.location, Sound.ENTITY_GHAST_SHOOT, 1f, 0.5f)
                        evoker.world.playSound(evoker.location, Sound.ENTITY_EVOKER_CAST_SPELL, 1f, 1f)
                        evoker.spell = Spellcaster.Spell.WOLOLO  // Set our spell to cast about 1 second before teleport
                    }

                    SpellType.TOSS -> {
                        evoker.world.playSound(evoker.location, KeyStore.AUDIO_ILLAGER_WARLOCK_TOSS_CAST.toString(), 1f, 1f)
                        evoker.spell = Spellcaster.Spell.BLINDNESS

                        val nearbyPlayers = evoker.world.getNearbyPlayers(evoker.location, 20.0)
                        for (player in slayer.activelyInvolvedPlayers) {
                            // Throw nearby players
                            if (player in nearbyPlayers) {
                                // Start by launching our players up
                                player.velocity = Vector(0.0, 1.0, 0.0)

                                // 9 Ticks later, freeze them in air.
                                Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                                    player.velocity.y = 0.0
                                    player.lookAt(evoker, LookAnchor.EYES, LookAnchor.EYES)  // Lock player's eyes onto the evoker
                                }, TickTime.TICK * 9)

                                // 10 Ticks later, queue up our freeze watchers
                                Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                                    freezeService.addMovementWatch(player.uniqueId)  // Freeze player in place, and lock their mouse movement
                                    freezeService.addOrientationWatch(player.uniqueId)
                                    Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                                        launchPlayer(player, 4.0)
                                    }, TickTime.TICK * 30)
                                }, TickTime.TICK * 10)
                            }
                        }
                    }
                }
            }

            if (spellClock == 0) {
                // Time to cast the spell!
                spellClock = spellCastFrequency
                evoker.spell = Spellcaster.Spell.NONE  // Clear our spell

                when (nextSpell) {
                    SpellType.TELEPORT -> {
                        // Pick a random teleport type to do
                        val type = TeleportType.entries.toTypedArray().random()
                        when (type) {
                            TeleportType.BEHIND_PLAYER -> {
                                var player = slayer.activelyInvolvedPlayers.random()
                                val behindLocation =
                                    player.location.block // Block the player is currently positioned at (relative to feet)
                                        .getRelative(player.facing.oppositeFace) // Get block facing the OPPOSITE direction that the player is facing (facing east = get block west of the player)
                                        .location // Return the location of that block
                                teleportTo(behindLocation)
                            }

                            TeleportType.SHORT_RANDOM -> teleportRandomlySafe(3.0)
                            TeleportType.LONG_RANDOM -> teleportRandomlySafe(6.0)
                        }
                    }

                    SpellType.FIREBALL -> {
                        val fireball = evoker.launchProjectile(
                            Fireball::class.java,
                            evoker.location.getDirection().normalize().multiply(2)
                        )
                        SMPRPG.getService(EntityDamageCalculatorService::class.java)
                            .setBaseProjectileDamage(
                                fireball,
                                AttributeService.instance.getOrCreateAttribute(evoker, AttributeWrapper.STRENGTH).value
                            )
                        slayer.fireballs.add(fireball)
                    }

                    SpellType.VEX -> {
                        val evokerVec = evoker.location.toVector().normalize()
                        val vexVec = evokerVec.multiply(3)
                        for (i in 0..3) {
                            vexVec.rotateAroundY(90.0 * i)
                            val vex = evoker.world.spawnEntity(
                                evoker.eyeLocation.add(
                                    vexVec.toLocation(
                                        evoker.world,
                                        0.0f,
                                        30.0f
                                    )
                                ), EntityType.VEX, CreatureSpawnEvent.SpawnReason.CUSTOM
                            )
                            val leveledVex = SMPRPG.getService(EntityService::class.java).getEntityInstance(vex)
                            leveledVex.setup()
                            leveledVex.setLevel(slayer.level - 10)
                        }
                    }

                    SpellType.FANGS -> {
                        // Pick a random fang type attack to use.
                        var type = FangType.entries.toTypedArray().random()
                        if (closestPlayer == null)
                            type = FangType.SELF_ENCIRCLE  // Fallback in case we have a targeted fang attack, but no players nearby.
                        when (type) {
                            FangType.LINE -> {
                                evoker.lookAt(closestPlayer as Entity)  // Force an update to look at our closest player
                                val startLoc = evoker.location
                                startLoc.y = lastGroundedY
                                val endLoc = closestPlayer.location
                                val dirVector = evoker.eyeLocation.direction.normalize()

                                object : BukkitRunnable() {
                                    private var clock = 0

                                    override fun run() {
                                        val dist = startLoc.distance(endLoc) * (clock / 10.0)
                                        val currLocation = startLoc.clone().add(  // Clone our start location
                                            startLoc.toVector()
                                                .normalize()  // Normalize the start location as a vector to get our heading
                                                .multiply(dist)  // Multiply the vector to get how far from the start we are in this iteration
                                                .multiply(dirVector) // Then multiply by our direction vector to make it face the right way
                                                .multiply(2)  // Then multiply by 2 to make it extend a bit past the end point
                                                .toLocation(evoker.world)  // Convert to location
                                        )
                                        currLocation.y = lastGroundedY
                                        spawnFang(currLocation)
                                        clock++
                                        if (clock > 10) {
                                            this.cancel()
                                        }
                                    }
                                }.runTaskTimer(plugin, TickTime.INSTANTANEOUSLY, TickTime.INSTANTANEOUSLY)
                            }

                            FangType.ENCIRCLE -> {
                                val playerLoc = closestPlayer!!.location
                                spawnFang(playerLoc)

                                val rotateVec = Vector(4, 0, 0)
                                for (i in 0..10) {
                                    val fang = spawnFang(playerLoc.clone().add(rotateVec.clone().rotateAroundY(20.0 * i)))
                                    fang.attackDelay = i
                                }
                            }
                            FangType.SELF_ENCIRCLE -> {
                                val evokerLoc = evoker.location
                                evokerLoc.y = lastGroundedY

                                val rotateVec = Vector(4, 0, 0)
                                for (i in 0..2) {
                                    rotateVec.x = 4 + (i * 2.0)
                                    for (j in 0..10) {
                                        val fang = spawnFang(evokerLoc.clone().add(rotateVec.clone().rotateAroundY(20.0 * j)))
                                        fang.attackDelay = i * 5
                                    }
                                }
                            }
                        }
                    }

                    SpellType.TOSS -> {
                        spellClock += TickTime.seconds(1).toInt()  // Give 1 second extra to the spellClock to let this longer spell cast.
                    }
                }

            }
        }
    }

    fun teleportRandomlySafe(diameter : Double) : Boolean {
        var teleported = false
        val startLoc = evoker.location
        val destLoc = startLoc.clone()

        // Make 16 attempts to teleport, the chances of this failing are pretty low unless forced
        for (i in 0..15) {
            destLoc.x = startLoc.x + Random.nextDouble(-1 * diameter, diameter)
            destLoc.z = startLoc.z + Random.nextDouble(-1 * diameter, diameter)
            destLoc.y = startLoc.y
            var yAttempts = 0

            // Loop through (diameter) times until we find a collidable block to stand on (so block above should NOT be collidable)
            if (!evoker.world.getBlockAt(destLoc).isPassable) {
                while (yAttempts < diameter) {
                    destLoc.y++
                    yAttempts++
                    if (evoker.world.getBlockAt(destLoc).isPassable)
                        break
                }
            }

            // If the block we found isn't collidable, it means we found a safe place to teleport.
            var blockAt = evoker.world.getBlockAt(destLoc)
            if (blockAt.isPassable) {
                teleported = true
                break
            }
        }

        if (teleported) {
            teleportTo(destLoc)
            evoker.world.playSound(evoker.location, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f)
        }

        return teleported
    }

    fun teleportTo(location : Location) {
        evoker.world.spawnParticle(Particle.WITCH, evoker.location, 100, 0.2, 0.0, 0.2, 1.0)
        evoker.teleport(location)
        evoker.world.playSound(evoker.location, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f)
    }

    fun spawnFang(location : Location) : EvokerFangs {
        val fang: EvokerFangs = evoker.world.spawnEntity(
            location,
            EntityType.EVOKER_FANGS,
            CreatureSpawnEvent.SpawnReason.CUSTOM
        ) as EvokerFangs
        fang.owner = evoker
        return fang
    }

    fun launchPlayer(player : Player, power : Double) {
        freezeService.removeMovementWatch(player.uniqueId)
        freezeService.removeOrientationWatch(player.uniqueId)

        player.playSound(player.location, Sound.ENTITY_WITHER_SHOOT, 1f, 2f)
        player.playSound(player.location, Sound.ENTITY_ARROW_SHOOT, 1f, 0.5f)
        player.velocity = Vector(1, 1, 1).multiply(player.location.direction.normalize()).multiply(power * -1.0)
    }

    companion object {
        val GOAL_KEY : GoalKey<Evoker> = GoalKey.of(
            Evoker::class.java,
            NamespacedKey(plugin, "illager_warlock_teleport_goal")
        )
    }

}