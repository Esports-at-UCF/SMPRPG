package xyz.devvydont.smprpg.slayer.quest

import xyz.devvydont.smprpg.entity.CustomEntityType
import xyz.devvydont.smprpg.entity.slayer.shambling.ShamblingAbominationParent

enum class SlayerClassification(val slayerType : SlayerType, val entityType : CustomEntityType, val spawnFlag : String, val xpToSpawn : Int, val cost : Int) {
    SHAMBLING_HORROR_1(SlayerType.SHAMBLING_ABOMINATION,
        CustomEntityType.SHAMBLING_ABOMINATION_1,
        ShamblingAbominationParent.SPAWN_MOB_FLAG,
        30,
        1000),
    SHAMBLING_HORROR_2(SlayerType.SHAMBLING_ABOMINATION,
        CustomEntityType.SHAMBLING_ABOMINATION_2,
        ShamblingAbominationParent.SPAWN_MOB_FLAG,
        30,
        2500),
    SHAMBLING_HORROR_3(SlayerType.SHAMBLING_ABOMINATION,
        CustomEntityType.SHAMBLING_ABOMINATION_3,
        ShamblingAbominationParent.SPAWN_MOB_FLAG,
        30,
        10000),
    SHAMBLING_HORROR_4(SlayerType.SHAMBLING_ABOMINATION,
        CustomEntityType.SHAMBLING_ABOMINATION_4,
        ShamblingAbominationParent.SPAWN_MOB_FLAG,
        30,
        50000),
    SHAMBLING_HORROR_5(SlayerType.SHAMBLING_ABOMINATION,
        CustomEntityType.SHAMBLING_ABOMINATION_5,
        ShamblingAbominationParent.SPAWN_MOB_FLAG,
        30,
        100000)
}