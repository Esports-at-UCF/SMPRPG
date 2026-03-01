package xyz.devvydont.smprpg.events

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import xyz.devvydont.smprpg.entity.base.LeveledEntity
import xyz.devvydont.smprpg.items.base.SMPItemBlueprint

class SlayerSpawnBossEvent(@JvmField val entity: LeveledEntity<*>, val spawner : Player) : Event() {
    override fun getHandlers(): HandlerList {
        return handlerList
    }

    companion object {
        @JvmStatic
        val handlerList: HandlerList = HandlerList()
    }
}
