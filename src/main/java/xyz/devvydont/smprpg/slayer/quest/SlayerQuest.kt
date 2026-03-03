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
import xyz.devvydont.smprpg.entity.slayer.SlayerBossInstance
import xyz.devvydont.smprpg.events.slayer.SlayerQuestEarnExperienceEvent
import xyz.devvydont.smprpg.events.slayer.SlayerSpawnBossEvent
import xyz.devvydont.smprpg.services.EntityService
import xyz.devvydont.smprpg.util.persistence.KeyStore

class SlayerQuest(val owner: LeveledPlayer,  val classification: SlayerClassification) : Listener
{
    enum class SlayerQuestState {
        XP_COLLECTION,
        BOSS_ACTIVE,
        CANCELLED,
        COMPLETE  // Not currently used, quests complete on kill
    }

    val xpRequired = classification.xpToSpawn
    val bossToSpawn = classification.entityType
    val spawnKeyValue = classification.spawnFlag
    val cost = classification.cost

    var bossEntity : SlayerBossInstance<*>? = null

    var xpEarned : Int = 0
    var questState : SlayerQuestState = SlayerQuestState.XP_COLLECTION

    init {
        println("SLAYER QUEST STARTED!")
        println("Mob to spawn: " + bossToSpawn.toString())
        println("XP Required: " + xpRequired)
    }

}