package xyz.devvydont.smprpg.slayer.quest

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.persistence.PersistentDataType
import org.bukkit.scheduler.BukkitRunnable
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.entity.CustomEntityType
import xyz.devvydont.smprpg.entity.player.LeveledPlayer
import xyz.devvydont.smprpg.entity.slayer.SlayerBossInstance
import xyz.devvydont.smprpg.events.slayer.SlayerQuestEarnExperienceEvent
import xyz.devvydont.smprpg.events.slayer.SlayerSpawnBossEvent
import xyz.devvydont.smprpg.services.EntityService
import xyz.devvydont.smprpg.util.formatting.ComponentUtils
import xyz.devvydont.smprpg.util.persistence.KeyStore
import xyz.devvydont.smprpg.util.time.TickTime

class SlayerQuest(val owner: LeveledPlayer,  val classification: SlayerClassification) : Listener
{
    enum class SlayerQuestState {
        PREQUEST,  // Used in confirmation state
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
    var questState : SlayerQuestState = SlayerQuestState.PREQUEST

    var progressBar : BossBar = BossBar.bossBar(ComponentUtils.EMPTY,
        0f,
        BossBar.Color.PURPLE,
        BossBar.Overlay.NOTCHED_10)

    init {
        progressBar.addViewer(owner.player)
        object : BukkitRunnable() {
            override fun run() {
                if (questState != SlayerQuestState.XP_COLLECTION) {
                    progressBar.removeViewer(owner.player)
                    this.cancel()
                    return
                }

                heartbeat()
            }
        }.runTaskTimer(SMPRPG.plugin, TickTime.INSTANTANEOUSLY, TickTime.INSTANTANEOUSLY)
    }

    fun heartbeat() {
        progressBar.name(
            ComponentUtils.merge(
                ComponentUtils.create(bossToSpawn.Name, NamedTextColor.DARK_PURPLE, TextDecoration.BOLD),
                ComponentUtils.create("     "),
                ComponentUtils.create(xpEarned.toString() + "XP", NamedTextColor.AQUA),
                ComponentUtils.create("/"),
                ComponentUtils.create(xpRequired.toString() + "XP", NamedTextColor.DARK_AQUA)
            )
        )
        progressBar.progress((xpEarned.toFloat() / xpRequired.toFloat()))  // We need to cast both values, otherwise integer division rounds it to death
    }

    fun cleanup() {
        progressBar.removeViewer(owner.player)
    }

}