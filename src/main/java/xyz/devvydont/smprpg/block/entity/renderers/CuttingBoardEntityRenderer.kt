package xyz.devvydont.smprpg.block.entity.renderers

import it.unimi.dsi.fastutil.ints.IntList
import net.momirealms.craftengine.bukkit.plugin.reflection.minecraft.CoreReflections
import net.momirealms.craftengine.bukkit.plugin.reflection.minecraft.MEntityTypes
import net.momirealms.craftengine.bukkit.plugin.reflection.minecraft.NetworkReflections
import net.momirealms.craftengine.core.block.entity.render.DynamicBlockEntityRenderer
import net.momirealms.craftengine.core.entity.player.Player
import net.momirealms.craftengine.core.world.BlockPos
import xyz.devvydont.smprpg.block.entity.CuttingBoardBlockEntity
import java.util.*

class CuttingBoardEntityRenderer(val boardEntity: CuttingBoardBlockEntity, val pos: BlockPos) : DynamicBlockEntityRenderer {

    var entityId: Int = -1
    var cachedSpawnPacket: Any? = null
    var cachedDespawnPacket: Any? = null

    init {
        entityId = CoreReflections.`instance$Entity$ENTITY_COUNTER`.incrementAndGet()
        val spawnPacketClass = NetworkReflections.`clazz$ClientboundAddEntityPacket`
        val spawnConstructor = spawnPacketClass.getConstructor(
            Int::class.java,    // entityId
            UUID::class.java,   // uuid
            Double::class.java, // x
            Double::class.java, // y
            Double::class.java, // z
            Float::class.java,  // pitch
            Float::class.java,  // yaw
            net.minecraft.world.entity.EntityType::class.java,  // entityType
            Int::class.java,  // data
            net.minecraft.world.phys.Vec3::class.java,  // velocity
            Double::class.java,  // extraData
        )
        cachedSpawnPacket = spawnConstructor.newInstance(entityId,
            UUID.randomUUID(),
            pos.x.toDouble(),
            pos.y.toDouble(),
            pos.z.toDouble(),
            0.0f,
            0.0f,
            MEntityTypes.ITEM_DISPLAY,
            0,
            CoreReflections.`instance$Vec3$Zero`,
            0.0)

        val despawnPacketClass = NetworkReflections.`clazz$ClientboundRemoveEntitiesPacket`
        val despawnConstructor = despawnPacketClass.getConstructor(
            IntList::class.java
        )
        cachedDespawnPacket = despawnConstructor.newInstance(IntList.of(entityId))
    }

    override fun show(player: Player) {
        player.sendPacket(cachedSpawnPacket, false)
        update(player)
    }

    override fun hide(player: Player) {
        player.sendPacket(cachedDespawnPacket, false)
    }

    override fun update(player: Player) {
        val metadata = boardEntity.cacheMetadata
        val setEntityDataClass = NetworkReflections.`clazz$ClientboundSetEntityDataPacket`
        val setEntityDataConstructor = setEntityDataClass.getConstructor(
            Int::class.java,
            List::class.java
        )
        val packet = setEntityDataConstructor.newInstance(entityId, metadata)
        player.sendPacket(packet, false)
    }
}