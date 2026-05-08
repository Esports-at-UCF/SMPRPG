package xyz.devvydont.smprpg.block.entity

import net.momirealms.craftengine.bukkit.api.BukkitAdaptors
import net.momirealms.craftengine.bukkit.entity.data.ItemDisplayEntityData
import net.momirealms.craftengine.bukkit.item.BukkitItemManager
import net.momirealms.craftengine.bukkit.util.ItemStackUtils
import net.momirealms.craftengine.core.block.ImmutableBlockState
import net.momirealms.craftengine.core.block.entity.BlockEntity
import net.momirealms.craftengine.core.plugin.config.Config
import net.momirealms.craftengine.core.util.HorizontalDirection
import net.momirealms.craftengine.core.util.VersionHelper
import net.momirealms.craftengine.core.world.BlockPos
import net.momirealms.craftengine.core.world.Vec3d
import net.momirealms.craftengine.libraries.nbt.CompoundTag
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.joml.Quaternionf
import org.joml.Vector3f
import xyz.devvydont.smprpg.SMPRPG.Companion.plugin
import xyz.devvydont.smprpg.block.entity.renderers.CuttingBoardEntityRenderer
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.services.ItemService

class CuttingBoardBlockEntity(val pos: BlockPos, val blockState: ImmutableBlockState) : BlockEntity(SMPRPGBlockEntityTypes.CUTTING_BOARD, pos, blockState), Listener {

    var item: ItemStack
    var cacheMetadata: List<Object> = listOf()
    val version = VersionHelper.WORLD_VERSION

    init {
        plugin.server.pluginManager.registerEvents(this, plugin)

        item = BukkitItemManager.instance().uniqueEmptyItem().item().item
        blockEntityRenderer = CuttingBoardEntityRenderer(this, pos)
        updateMetadata()
    }

    override fun saveCustomData(tag: CompoundTag) {
        tag.putInt("data_version", version)
        try {
            if (item.isEmpty) return
            val itemTag = ItemStackUtils.saveItemStackAsTag(item)
            if (itemTag == null) return
            tag.put("item", itemTag)
        }
        catch (e: IllegalStateException) {
            plugin.logger.warning("Cutting Board item failed to save: ${e.message}")}  // TODO: On shutdown, we don't have access to saveItemStackAsTag (reason: reflection) so we have a wipe potential here.
    }

    override fun loadCustomData(tag: CompoundTag) {
        val dataVersion = tag.getInt("data_version", Config.itemDataFixerUpperFallbackVersion())
        val itemTag = tag.get("item")
        if (itemTag == null) return
        val itemStack = ItemStackUtils.parseItemStack(itemTag, dataVersion)
        if (itemStack == null) return
        item = itemStack
        updateMetadata()

        setItemStack(item)
    }

    fun setItemStack(itemStack: ItemStack?) {
        if (itemStack == null) item = BukkitItemManager.instance().uniqueEmptyItem().item().item
        else item = itemStack

        if (blockEntityRenderer == null) return
        val chunk = world.getChunkAtIfLoaded(pos.x shr 4, pos.z shr 4)
        if (chunk == null) return
        updateMetadata()
        for (player in chunk.trackedBy) {
            blockEntityRenderer()!!.update(player)
        }
        world.blockEntityChanged(pos)
    }

    fun updateMetadata() {
        val facing = blockState.customBlockState().getProperty<HorizontalDirection>("facing").toString()
        val metadataValues: List<Object> = ArrayList()
        ItemDisplayEntityData.DisplayedItem.addEntityData(BukkitAdaptors.adapt(item).literalObject, metadataValues)
        ItemDisplayEntityData.DisplayType.addEntityData(7, metadataValues)  // GROUND
        when (facing) {
            "NORTH" -> {
                ItemDisplayEntityData.Translation.addEntityData(Vector3f(0.5f, 0.08f, 0.5f), metadataValues)
                ItemDisplayEntityData.RotationLeft.addEntityData(Quaternionf(0.7f, 0.0f, 0.0f, 0.7f), metadataValues)
            }
            "EAST" -> {
                ItemDisplayEntityData.Translation.addEntityData(Vector3f(0.5f, 0.08f, 0.5f), metadataValues)
                ItemDisplayEntityData.RotationLeft.addEntityData(Quaternionf(0.7f, 0.0f, 0.0f, 0.7f), metadataValues)
                ItemDisplayEntityData.RotationRight.addEntityData(Quaternionf(0.0f, 0.0f, 0.7f, 0.7f), metadataValues)
            }
            "SOUTH" -> {
                ItemDisplayEntityData.Translation.addEntityData(Vector3f(0.5f, 0.08f, 0.5f), metadataValues)
                ItemDisplayEntityData.RotationLeft.addEntityData(Quaternionf(-0.7f, 0.0f, 0.0f, 0.7f), metadataValues)
            }
            else -> {
                ItemDisplayEntityData.Translation.addEntityData(Vector3f(0.5f, 0.08f, 0.5f), metadataValues)
                ItemDisplayEntityData.RotationLeft.addEntityData(Quaternionf(-0.7f, 0.0f, 0.0f, 0.7f), metadataValues)
                ItemDisplayEntityData.RotationRight.addEntityData(Quaternionf(-0.0f, 0.0f, -0.7f, 0.7f), metadataValues)
            }
        }
        cacheMetadata = metadataValues
    }

    override fun preRemove() {
        val pos = Vec3d.atCenterOf(this.pos)
        super.world.world().dropItemNaturally(pos, BukkitItemManager.instance().wrap(item))
        super.world.world().dropItemNaturally(pos, BukkitItemManager.instance().wrap(ItemService.generate(CustomItemType.CUTTING_BOARD)))
        item = ItemStack.empty()

        HandlerList.unregisterAll(this)
    }
}