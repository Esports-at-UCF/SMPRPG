package xyz.devvydont.smprpg.entity.slayer

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.LivingEntity
import xyz.devvydont.smprpg.entity.CustomEntityType
import xyz.devvydont.smprpg.entity.base.CustomBossInstance
import xyz.devvydont.smprpg.util.formatting.ComponentUtils

open class SlayerBossInstance<T>(T : LivingEntity?, entityType: CustomEntityType?) : CustomBossInstance<LivingEntity?>(T, entityType) {

    override fun getNameColor(): TextColor? {
        return NamedTextColor.DARK_PURPLE
    }

    override fun getNameComponent(): Component {
        return ComponentUtils.create(getEntityName(), getNameColor(), TextDecoration.BOLD)
    }
}