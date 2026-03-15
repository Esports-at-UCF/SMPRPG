package xyz.devvydont.smprpg.services

import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.persistence.PersistentDataType
import org.bukkit.scheduler.BukkitRunnable
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.SMPRPG.Companion.plugin
import xyz.devvydont.smprpg.entity.slayer.SlayerBossInstance
import xyz.devvydont.smprpg.events.slayer.SlayerBossDeathEvent
import xyz.devvydont.smprpg.events.slayer.SlayerQuestEarnExperienceEvent
import xyz.devvydont.smprpg.events.slayer.SlayerSpawnBossEvent
import xyz.devvydont.smprpg.slayer.quest.SlayerQuest
import xyz.devvydont.smprpg.util.formatting.ComponentUtils
import xyz.devvydont.smprpg.util.persistence.KeyStore
import xyz.devvydont.smprpg.util.time.TickTime
import java.util.*
import kotlin.math.roundToInt

class SlayerService : IService, Listener {

    private var playersToQuests : HashMap<UUID, SlayerQuest> = HashMap()

    @Throws(RuntimeException::class)
    override fun setup() {
    }

    override fun cleanup() {
    }

    fun spawnSlayerBoss(quest : SlayerQuest, location : Location) {
        // Update the FSM
        quest.questState = SlayerQuest.SlayerQuestState.BOSS_SPAWNING

        object : BukkitRunnable() {
            private var clock = 0
            private var pitch = 1.0f

            override fun run() {
                // If our quest got cancelled due to logout/other reason, cancel our spawn.
                if (quest.questState == SlayerQuest.SlayerQuestState.CANCELLED)
                    this.cancel()

                val player = quest.owner.player
                val locCopy = location.clone()
                locCopy.y += 1
                if (clock % 2 == 0) {
                    player.world.playSound(player.location, Sound.ENTITY_ZOMBIE_VILLAGER_CONVERTED, 1f, pitch)
                    player.world.playSound(player.location, Sound.ENTITY_WITHER_SHOOT, 0.5f, pitch - 0.5f)
                    pitch += .05f
                    player.world.spawnParticle(Particle.FLAME, location, 20, 0.25, 1.0, 0.25, 0.01)
                    player.world.spawnParticle(Particle.TRIAL_SPAWNER_DETECTION_OMINOUS, locCopy, 40, 0.1, 0.2, 0.1, 0.02)
                    player.world.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, locCopy, 10, 0.0, 0.0, 0.0, 0.0175)

                }
                clock++
                quest.spawnCountdown = clock
                if (clock > 40) {
                    val entity = SMPRPG.getService(EntityService::class.java).spawnCustomEntity(quest.bossToSpawn, location);
                    val slayer = entity as SlayerBossInstance<*>
                    player.world.spawnParticle(Particle.EXPLOSION, locCopy, 5, -4.0, 0.0, 0.0, 0.0)
                    player.world.playSound(location, Sound.ENTITY_WITHER_SPAWN, 0.5f, 2f)
                    player.world.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1f, 0.5f)
                    slayer.quest = quest
                    quest.bossEntity = entity
                    quest.questState = SlayerQuest.SlayerQuestState.BOSS_ACTIVE

                    // Fire off a slayer spawn boss event for bukkit tracking
                    SlayerSpawnBossEvent(entity, quest.owner.player).callEvent()
                    this.cancel()
                }
            }
        }.runTaskTimer(plugin, TickTime.INSTANTANEOUSLY, TickTime.INSTANTANEOUSLY)
    }

    fun canStartQuest(quest : SlayerQuest) : Boolean {

        // Don't register if we already have a quest active for this player.
        if (quest.owner.player.player!!.uniqueId in playersToQuests.keys) {
            return false
        }

        val ecoService = SMPRPG.getService(EconomyService::class.java)
        // Check if the player can afford to start a quest
        val bal = Math.toIntExact(
            ecoService.getMoney(quest.owner.player)
        )
        if (bal < quest.cost) {
            return false
        }

        // They can afford to start the quest, and aren't in a quest currently.
        return true
    }

    fun registerQuest(quest : SlayerQuest) {
        // This method is UNCHECKED!
        // Only call this directly if you know what you are doing,
        // or if you ran checks beforehand (see canStartQuest)
        SMPRPG.getService(EconomyService::class.java).spendMoney(quest.owner.player, quest.cost.toLong())
        playersToQuests[quest.owner.player.player!!.uniqueId] = quest
    }

    @EventHandler
    fun onSlayerBossDied(event: SlayerBossDeathEvent) {
        val quest = event.slayer.quest
        val player = quest!!.owner.player.player!!
        if (quest in playersToQuests.values) {
            playersToQuests.remove(player.uniqueId)
        }
        player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1f, 2f)

        event.slayer.generateSkillExperienceReward()
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)  // We ignoreCancelled=true so that in the event the death was cancelled, we don't stop the slayer.
    fun onPlayerDiedWhileSlayerActive(event: PlayerDeathEvent) {

        // If the player that died is not doing a quest, we don't care
        val playerQuest = playersToQuests[event.player.uniqueId]
        if (playerQuest == null)
            return

        // Attempt to despawn the entity and remove the quest. they failed.
        playerQuest.bossEntity?.entity?.remove()
        playersToQuests.remove(event.player.uniqueId)
        event.player.playSound(event.player.location, Sound.ENTITY_WARDEN_SONIC_BOOM, 1f, .1f)
        event.player.sendMessage(ComponentUtils.alert(ComponentUtils.create("You failed your slayer quest because you died!",
            NamedTextColor.RED)))
    }

    @EventHandler
    fun onPlayerLogoutWhileSlayerActive(event: PlayerQuitEvent) {
        val uuid = event.player.uniqueId
        if (uuid in playersToQuests.keys) {
            val quest : SlayerQuest? = playersToQuests.get(event.player.uniqueId)
            val slayerInst : SlayerBossInstance<*>? = quest!!.bossEntity
            if (slayerInst == null)
                quest.questState = SlayerQuest.SlayerQuestState.CANCELLED
            slayerInst?.entity?.remove()
            playersToQuests.remove(uuid)
        }
    }

    @EventHandler
    fun onGainCombatExperience(event : SlayerQuestEarnExperienceEvent) {
        // First, check if our player even has a quest active.
        val quest = playersToQuests.getOrDefault(event.player.player.uniqueId, null)
        if (quest == null)
            return

        // Are we currently in the XP tracking state of this quest?
        if (quest.questState == SlayerQuest.SlayerQuestState.XP_COLLECTION)
        {
            // Next, check that the mob killed is a valid mob for this quest.
            if (event.mobKilled.entity.persistentDataContainer.getOrDefault(
                    KeyStore.SLAYER_SPAWN_TYPE,
                    PersistentDataType.STRING,
                    ""
                ) == quest.spawnKeyValue
            ) {
                // Heck yeah, time to add xp to this quest.

                // But first, we need to cap the xp earned if it's far too high.
                // We don't want slayer bosses chaining one after another
                if (event.experience > (quest.xpRequired / 2))
                    quest.xpEarned += (quest.xpRequired / 2)
                else
                    quest.xpEarned += event.experience

                // Have we hit the threshold for this boss yet? If so, spawn it.
                // If we haven't, roll a 5% chance to spawn a special mob
                val specialSpawns = quest.classification.specialSpawns;
                if (quest.xpEarned >= quest.xpRequired) {
                    quest.xpEarned = quest.xpRequired  // Do this to prevent visual weirdness
                    spawnSlayerBoss(quest, event.mobKilled.entity.location)
                }
                else if (specialSpawns != null) {
                    if (Math.random() <= 0.05) {
                        object : BukkitRunnable() {
                            private var clock = 0
                            private var location = event.mobKilled.entity.location

                            override fun run() {
                                val player = quest.owner.player
                                val locCopy = event.mobKilled.entity.location
                                val randRoll = Math.random();
                                locCopy.y += 0.5 + randRoll
                                locCopy.x -= randRoll;
                                locCopy.z += randRoll;
                                if (clock % 2 == 0) {
                                    player.world.playSound(player.location, Sound.ENTITY_GENERIC_EXPLODE, 1f, (1f + randRoll.toFloat()))
                                    player.world.spawnParticle(Particle.EXPLOSION, locCopy, 0, -1.5, 0.0, 0.0, 0.0)
                                }
                                clock++
                                if (clock > 20) {
                                    SMPRPG.getService(EntityService::class.java).spawnCustomEntity(specialSpawns.random(), location);
                                    player.world.playSound(location, Sound.ENTITY_GHAST_HURT, 1f, 1f)
                                    this.cancel()
                                }
                            }
                        }.runTaskTimer(plugin, TickTime.INSTANTANEOUSLY, TickTime.INSTANTANEOUSLY)
                    }
                }
            }
        }
    }
}
