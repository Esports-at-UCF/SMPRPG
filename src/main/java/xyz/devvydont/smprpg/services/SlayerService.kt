package xyz.devvydont.smprpg.services

import com.destroystokyo.paper.ParticleBuilder
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
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
import xyz.devvydont.smprpg.util.persistence.KeyStore
import xyz.devvydont.smprpg.util.time.TickTime
import java.util.UUID

class SlayerService : IService, Listener {

    private var playersToQuests : HashMap<UUID, SlayerQuest> = HashMap()

    @Throws(RuntimeException::class)
    override fun setup() {
    }

    override fun cleanup() {
    }

    fun spawnSlayerBoss(quest : SlayerQuest, location : Location) {
        // Update the FSM
        quest.questState = SlayerQuest.SlayerQuestState.BOSS_ACTIVE

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
                    player.world.spawnParticle(Particle.FLAME, locCopy, 20, 0.05, 0.01, 0.05, 0.01)
                    player.world.spawnParticle(Particle.TRIAL_SPAWNER_DETECTION_OMINOUS, locCopy, 40, 0.1, 0.2, 0.1, 0.02)
                    player.world.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, locCopy, 10, 0.0, 0.0, 0.0, 0.0175)

                }
                clock++
                if (clock > 40) {
                    val entity = SMPRPG.getService(EntityService::class.java).spawnCustomEntity(quest.bossToSpawn, location);
                    val slayer = entity as SlayerBossInstance<*>
                    player.world.spawnParticle(Particle.EXPLOSION, locCopy, 5, -4.0, 0.0, 0.0, 0.0)
                    player.world.playSound(location, Sound.ENTITY_WITHER_SPAWN, 0.5f, 2f)
                    player.world.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1f, 0.5f)
                    slayer.quest = quest
                    quest.bossEntity = entity

                    // Fire off a slayer spawn boss event for bukkit tracking
                    SlayerSpawnBossEvent(entity, quest.owner.player).callEvent()
                    this.cancel()
                }
            }
        }.runTaskTimer(plugin, TickTime.INSTANTANEOUSLY, TickTime.INSTANTANEOUSLY)
    }

    fun registerQuest(quest : SlayerQuest) : Boolean {

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

        // They can afford it, take their money and start the quest.
        ecoService.spendMoney(quest.owner.player, quest.cost.toLong())
        playersToQuests[quest.owner.player.player!!.uniqueId] = quest
        return true
    }

    @EventHandler
    fun onSlayerDeath(event: SlayerBossDeathEvent) {
        val quest = event.slayer.quest
        val player = quest!!.owner.player.player!!
        if (quest in playersToQuests.values) {
            playersToQuests.remove(player.uniqueId)
        }
        player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1f, 2f)
    }

    @EventHandler
    fun removeSlayerOnLogout(event: PlayerQuitEvent) {
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
                quest.xpEarned += event.experience

                println("Experience: " + quest.xpEarned + "/" + quest.xpRequired)

                // Have we hit the threshold for this boss yet? If so, spawn it.
                spawnSlayerBoss(quest, event.mobKilled.entity.location)
            }
        }
    }
}
