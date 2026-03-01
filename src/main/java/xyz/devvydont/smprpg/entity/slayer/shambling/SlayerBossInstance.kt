package xyz.devvydont.smprpg.entity.slayer.shambling

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Sound
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import xyz.devvydont.smprpg.entity.CustomEntityType
import xyz.devvydont.smprpg.entity.base.CustomBossInstance
import xyz.devvydont.smprpg.util.formatting.ComponentUtils

open class SlayerBossInstance<T>(T : LivingEntity?, entityType: CustomEntityType?) : CustomBossInstance<LivingEntity?>(T, entityType), Listener {

    override fun getNameColor(): TextColor? {
        return NamedTextColor.DARK_PURPLE
    }

    override fun getNameComponent(): Component {
        return ComponentUtils.create(getEntityName(), getNameColor(), TextDecoration.BOLD)
    }

    @EventHandler()
    fun onSlayerBossDeath(event : EntityDeathEvent) {
        if (_entity != event.getEntity())
            return

        _entity.world.playSound(_entity.location, Sound.BLOCK_TRIAL_SPAWNER_SPAWN_MOB, 1f, 0.05f)
        _entity.world.playSound(_entity.location, Sound.BLOCK_TRIAL_SPAWNER_SPAWN_MOB, 1f, 0.2f)
        _entity.world.playSound(_entity.location, Sound.BLOCK_TRIAL_SPAWNER_SPAWN_MOB, 1f, 0.4f)
        _entity.world.playSound(_entity.location, Sound.ENTITY_WITHER_SPAWN, 1f, 2.0f)
    }
}