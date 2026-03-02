package xyz.devvydont.smprpg.slayer.quest

import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.persistence.PersistentDataType
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.entity.CustomEntityType
import xyz.devvydont.smprpg.entity.player.LeveledPlayer
import xyz.devvydont.smprpg.events.slayer.SlayerQuestEarnExperienceEvent
import xyz.devvydont.smprpg.events.slayer.SlayerSpawnBossEvent
import xyz.devvydont.smprpg.services.EntityService
import xyz.devvydont.smprpg.util.persistence.KeyStore

class SlayerQuest(val owner: LeveledPlayer,  val xpRequired: Int,  val bossToSpawn: CustomEntityType,  val spawnKeyValue : String) : Listener
{
    var xpEarned : Int = 0
    var trackingXp = true

    init {
        println("SLAYER QUEST STARTED!")
        println("Mob to spawn: " + bossToSpawn.toString())
        println("XP Required: " + xpRequired)
    }

    fun spawnBoss(spawnLoc : Location) {
        var entity = SMPRPG.getService(EntityService::class.java).spawnCustomEntity(bossToSpawn, spawnLoc);

        // Fire off a slayer spawn boss event for bukkit tracking
        SlayerSpawnBossEvent(entity, owner.player).callEvent()
    }

    @EventHandler
    fun onGainCombatExperience(event : SlayerQuestEarnExperienceEvent) {
        // First, check if the slayer quest's owner gained this xp.
        if (event.player != owner)
            return

        // Next, check that the mob killed is a valid mob for this quest.
        if (event.mobKilled.entity.persistentDataContainer.getOrDefault(
                KeyStore.SLAYER_SPAWN_TYPE,
                PersistentDataType.STRING,
                "") == spawnKeyValue)
        {
            // Heck yeah, time to add xp to this quest.
            xpEarned += event.experience

            println("Experience: " + xpEarned + "/" + xpRequired)

            // Have we hit the threshold for this boss yet? If so, spawn it.
            if (trackingXp) {
                if (xpEarned >= xpRequired) {
                    spawnBoss(event.mobKilled.entity.location)
                    trackingXp = false
                }
            }
        }
    }
}