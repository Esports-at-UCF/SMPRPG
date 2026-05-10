package xyz.devvydont.smprpg.block.behaviors

import net.momirealms.craftengine.bukkit.api.BukkitAdaptors
import net.momirealms.craftengine.bukkit.block.behavior.BukkitBlockBehavior
import net.momirealms.craftengine.bukkit.item.BukkitItemManager
import net.momirealms.craftengine.core.block.CustomBlock
import net.momirealms.craftengine.core.block.ImmutableBlockState
import net.momirealms.craftengine.core.block.behavior.BlockBehaviorFactory
import net.momirealms.craftengine.core.block.behavior.EntityBlockBehavior
import net.momirealms.craftengine.core.block.entity.BlockEntity
import net.momirealms.craftengine.core.block.entity.BlockEntityType
import net.momirealms.craftengine.core.entity.EquipmentSlot
import net.momirealms.craftengine.core.entity.player.GameMode
import net.momirealms.craftengine.core.entity.player.InteractionHand
import net.momirealms.craftengine.core.entity.player.InteractionResult
import net.momirealms.craftengine.core.entity.player.Player
import net.momirealms.craftengine.core.item.Item
import net.momirealms.craftengine.core.sound.SoundData
import net.momirealms.craftengine.core.util.Key
import net.momirealms.craftengine.core.world.BlockPos
import net.momirealms.craftengine.core.world.Vec3d
import net.momirealms.craftengine.core.world.context.UseOnContext
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.block.entity.CuttingBoardBlockEntity
import xyz.devvydont.smprpg.block.entity.SMPRPGBlockEntityTypes
import xyz.devvydont.smprpg.items.interfaces.IKnife
import xyz.devvydont.smprpg.recipe.cuttingboard.CuttingBoardRecipes
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.persistence.KeyStore
import kotlin.random.Random

class CuttingBoardBehavior(customBlock: CustomBlock) : BukkitBlockBehavior(customBlock), EntityBlockBehavior {

    override fun <T : BlockEntity> blockEntityType(state: ImmutableBlockState): BlockEntityType<T?> {
        return EntityBlockBehavior.blockEntityTypeHelper(SMPRPGBlockEntityTypes.FREEZER)
    }

    override fun createBlockEntity(pos: BlockPos, state: ImmutableBlockState): BlockEntity {
        return CuttingBoardBlockEntity(pos, state)
    }

    override fun useOnBlock(context: UseOnContext, state: ImmutableBlockState): InteractionResult {
        var boardEntity = context.world.storageWorld().getBlockEntityAtIfLoaded(context.clickedPos)
        if (boardEntity == null || boardEntity !is CuttingBoardBlockEntity) return super.useOnBlock(context, state)

        val item = context.item
        val itemStack = item.item as ItemStack
        val isCreative = context.player?.gameMode() != GameMode.CREATIVE

        // Only allow one item to be sent over to the cutting board.

        // Nothing on the board currently, we can try to place an item
        val hand = context.hand
        val offhandItem = context.player?.getItemInHand(InteractionHand.OFF_HAND)
        // Try to place items in offhand on the board first, then we can place main hand items.
        if (hand == InteractionHand.OFF_HAND) {
            if (boardEntity.item.isEmpty) {
                placeItemOnBoard(itemStack, boardEntity, isCreative)
                return InteractionResult.SUCCESS
            }
        }
        // There is no offhand item, so let's place our main hand item instead
        if (hand == InteractionHand.MAIN_HAND) {
            var noItem = offhandItem == null
            if (!noItem)
                noItem = offhandItem!!.isEmpty
            if (noItem) {
                if (boardEntity.item.isEmpty) {
                    placeItemOnBoard(itemStack, boardEntity, isCreative)
                    return InteractionResult.SUCCESS
                } else if (item.isEmpty)  // Take back our item if we have an empty main hand
                {
                    val dropPos = Vec3d.atCenterOf(boardEntity.pos)
                    boardEntity.world.world().dropItemNaturally(dropPos, BukkitAdaptors.adapt(boardEntity.item))
                    boardEntity.setItemStack(ItemStack.empty())
                    context.player?.swingHand(InteractionHand.MAIN_HAND)
                    val bukkitWorld = boardEntity.world().world.platformWorld() as World
                    bukkitWorld.playSound(
                        Location(
                            bukkitWorld,
                            boardEntity.pos.x.toDouble(),
                            boardEntity.pos.y.toDouble(),
                            boardEntity.pos.z.toDouble()
                        ),
                        Sound.ENTITY_ITEM_FRAME_PLACE,
                        1.0f,
                        1.5f
                    )
                }
            }

            // There's an item on the board, so let's try to process it with our mainhand item
            processItem(item, boardEntity, context.player!!)
        }

        return super.useOnBlock(context, state)
    }

    fun placeItemOnBoard(item: ItemStack, board: CuttingBoardBlockEntity, takeItem: Boolean) {
        val placementStack = item.clone()
        placementStack.amount = 1
        if (takeItem)
            item.amount--
        board.setItemStack(placementStack)
        if (!placementStack.isEmpty) {
            val bukkitWorld = board.world().world.platformWorld() as World
            bukkitWorld.playSound(
                Location(
                    bukkitWorld,
                    board.pos.x.toDouble(),
                    board.pos.y.toDouble(),
                    board.pos.z.toDouble()
                ),
                Sound.BLOCK_WOOD_PLACE,
                1.0f,
                1.0f
            )
        }
    }

    fun placeItemOnBoard(item: ItemStack, board: CuttingBoardBlockEntity) {
        placeItemOnBoard(item, board, false)
    }

    fun processItem(toolToProcess: Item<*>, board: CuttingBoardBlockEntity, player: Player) : Boolean {
        val damage = toolToProcess.damage()
        val maxDamage = toolToProcess.maxDamage() - 1

        if (!damage.isEmpty) {
            if (damage.get() >= maxDamage)
                return false
        }
        val dropPos = Vec3d.atCenterOf(board.pos)
        for (entry in CuttingBoardRecipes.entries) {
            if (entry.recipe.input.isSimilar(board.item)) {
                // if (toolToProcess.hasItemTag(entry.recipe.processToolTag)) {  // TODO: This is the correct way to do this, we need to do a blueprint check until tags are fixed
                val bp = ItemService.blueprint(toolToProcess.item as ItemStack)
                if (ItemService.blueprint(toolToProcess.item as ItemStack) is IKnife) {
                    for (item in entry.recipe.recipeResult) {
                        val itemStack = item.first
                        val chance = item.second
                        if (Random.nextDouble() <= chance) {
                            board.world.world()
                                .dropItemNaturally(
                                    dropPos,
                                    BukkitItemManager.instance().wrap(itemStack)
                                )
                        }
                    }
                    if (!damage.isEmpty) {
                        toolToProcess.hurtAndBreak(1, player, EquipmentSlot.MAINHAND)
                        player.swingHand(InteractionHand.MAIN_HAND)
                    }
                    board.setItemStack(ItemStack.empty())
                    board.world.world.playBlockSound(dropPos,
                        SoundData(Key.of(KeyStore.AUDIO_CUTTING_BOARD_CUT.toString()),
                            SoundData.SoundValue.FIXED_1,
                            SoundData.SoundValue.FIXED_1))
                    return true
                }
            }
        }
        return false
    }

    companion object {
        val FACTORY = Factory()

        class Factory : BlockBehaviorFactory<CuttingBoardBehavior?> {
            override fun create(block: CustomBlock, arguments: MutableMap<String?, Any?>): CuttingBoardBehavior {
                return CuttingBoardBehavior(block)
            }
        }
    }
}