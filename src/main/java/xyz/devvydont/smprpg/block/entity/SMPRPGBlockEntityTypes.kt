package xyz.devvydont.smprpg.block.entity

import net.momirealms.craftengine.core.block.entity.BlockEntityType
import net.momirealms.craftengine.core.block.entity.BlockEntityTypes

class SMPRPGBlockEntityTypes : BlockEntityTypes() {
    companion object {
        val FREEZER : BlockEntityType<FreezerBlockEntity> = register(SMPRPGBlockEntityTypeKeys.FREEZER)
        val CUTTING_BOARD : BlockEntityType<CuttingBoardBlockEntity> = register(SMPRPGBlockEntityTypeKeys.CUTTING_BOARD)
        val COOKING_POT : BlockEntityType<CookingPotBlockEntity> = register(SMPRPGBlockEntityTypeKeys.COOKING_POT)
    }
}