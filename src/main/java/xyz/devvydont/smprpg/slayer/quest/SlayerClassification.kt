package xyz.devvydont.smprpg.slayer.quest

import com.google.common.collect.ImmutableList
import xyz.devvydont.smprpg.entity.CustomEntityType
import xyz.devvydont.smprpg.entity.slayer.shambling.ShamblingAbominationParent

enum class SlayerClassification(val slayerType : SlayerType,
                                val entityType : CustomEntityType,
                                val spawnFlag : String,
                                val xpToSpawn : Int,
                                val cost : Int,
                                val slayerXpReward : Int,
                                val specialSpawns : ImmutableList<CustomEntityType>?) {
    SHAMBLING_HORROR_1(SlayerType.SHAMBLING_ABOMINATION,
        CustomEntityType.SHAMBLING_ABOMINATION_1,
        ShamblingAbominationParent.SPAWN_MOB_FLAG,
        300,
        1000,
        50,
        null),
    SHAMBLING_HORROR_2(SlayerType.SHAMBLING_ABOMINATION,
        CustomEntityType.SHAMBLING_ABOMINATION_2,
        ShamblingAbominationParent.SPAWN_MOB_FLAG,
        750,
        2500,
        200,
        null),
    SHAMBLING_HORROR_3(SlayerType.SHAMBLING_ABOMINATION,
        CustomEntityType.SHAMBLING_ABOMINATION_3,
        ShamblingAbominationParent.SPAWN_MOB_FLAG,
        1875,
        10000,
        800,
        ImmutableList.of(CustomEntityType.SINFUL_SHAMBLER)
    ),
    SHAMBLING_HORROR_4(SlayerType.SHAMBLING_ABOMINATION,
        CustomEntityType.SHAMBLING_ABOMINATION_4,
        ShamblingAbominationParent.SPAWN_MOB_FLAG,
        4700,
        50000,
        3200,
        ImmutableList.of(CustomEntityType.REMORSELESS_ABOMINATION, CustomEntityType.WRETCHED_ABOMINATION)
    ),
    SHAMBLING_HORROR_5(SlayerType.SHAMBLING_ABOMINATION,
        CustomEntityType.SHAMBLING_ABOMINATION_5,
        ShamblingAbominationParent.SPAWN_MOB_FLAG,
        11750,
        100000,
        12800,
        ImmutableList.of(CustomEntityType.SHAMBLING_MALFEASANT, CustomEntityType.REMORSEFUL_ABOMINATION)
    )
}