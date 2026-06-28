package xyz.devvydont.smprpg.slayer.quest

import com.google.common.collect.ImmutableList
import xyz.devvydont.smprpg.entity.CustomEntityType

enum class SlayerClassification(val slayerType : SlayerType,
                                val entityType : CustomEntityType,
                                val xpToSpawn : Int,
                                val cost : Int,
                                val slayerXpReward : Int,
                                val specialSpawns : ImmutableList<CustomEntityType>?) {

    //<editor-fold desc="Shambling Abomination">
    SHAMBLING_HORROR_1(SlayerType.SHAMBLING_ABOMINATION,
        CustomEntityType.SHAMBLING_ABOMINATION_1,
        300,
        1_000,
        50,
        null),
    SHAMBLING_HORROR_2(SlayerType.SHAMBLING_ABOMINATION,
        CustomEntityType.SHAMBLING_ABOMINATION_2,
        750,
        2_500,
        200,
        null),
    SHAMBLING_HORROR_3(SlayerType.SHAMBLING_ABOMINATION,
        CustomEntityType.SHAMBLING_ABOMINATION_3,
        1_875,
        10_000,
        800,
        ImmutableList.of(CustomEntityType.SINFUL_SHAMBLER)
    ),
    SHAMBLING_HORROR_4(SlayerType.SHAMBLING_ABOMINATION,
        CustomEntityType.SHAMBLING_ABOMINATION_4,
        4_700,
        50_000,
        3_200,
        ImmutableList.of(CustomEntityType.REMORSELESS_ABOMINATION, CustomEntityType.WRETCHED_ABOMINATION)
    ),
    SHAMBLING_HORROR_5(SlayerType.SHAMBLING_ABOMINATION,
        CustomEntityType.SHAMBLING_ABOMINATION_5,
        11_750,
        100_000,
        12_800,
        ImmutableList.of(CustomEntityType.SHAMBLING_MALFEASANT, CustomEntityType.REMORSEFUL_ABOMINATION)
    ),
    //</editor-fold>

    //<editor-fold desc="Piglin Warlord">
    PIGLIN_WARLORD_1(SlayerType.PIGLIN_WARLORD,
        CustomEntityType.PIGLIN_WARLORD_1,
        450,
        1_000,
        100,
        null),
    PIGLIN_WARLORD_2(SlayerType.PIGLIN_WARLORD,
        CustomEntityType.PIGLIN_WARLORD_2,
        1_125,
        2_500,
        400,
        null),
    PIGLIN_WARLORD_3(SlayerType.PIGLIN_WARLORD,
        CustomEntityType.PIGLIN_WARLORD_3,
        2_820,
        10_000,
        1_600,
        ImmutableList.of(CustomEntityType.ILLAGER_POPPET)
    ),
    PIGLIN_WARLORD_4(SlayerType.PIGLIN_WARLORD,
        CustomEntityType.PIGLIN_WARLORD_4,
        7_050,
        50_000,
        6_400,
        ImmutableList.of(CustomEntityType.RAVAGER_FAMILIAR, CustomEntityType.HEXED_VEX)
    ),
    PIGLIN_WARLORD_5(SlayerType.PIGLIN_WARLORD,
        CustomEntityType.PIGLIN_WARLORD_5,
        17_625,
        100_000,
        25_600,
        ImmutableList.of(CustomEntityType.WARLOCK_APPRENTICE, CustomEntityType.WARLOCK_SHADOW)
    ),
    //</editor-fold>

    //<editor-fold desc="Illager Warlock">
    ILLAGER_WARLOCK_1(SlayerType.ILLAGER_WARLOCK,
    CustomEntityType.ILLAGER_WARLOCK_1,
    750,
    1_000,
    200,
    null),
    ILLAGER_WARLOCK_2(SlayerType.ILLAGER_WARLOCK,
    CustomEntityType.ILLAGER_WARLOCK_2,
    1_875,
    2_500,
    800,
    null),
    ILLAGER_WARLOCK_3(SlayerType.ILLAGER_WARLOCK,
    CustomEntityType.ILLAGER_WARLOCK_3,
    4_690,
    10_000,
    3_200,
    ImmutableList.of(CustomEntityType.ILLAGER_POPPET)
    ),
    ILLAGER_WARLOCK_4(SlayerType.ILLAGER_WARLOCK,
    CustomEntityType.ILLAGER_WARLOCK_4,
    11_720,
    50_000,
    12_800,
    ImmutableList.of(CustomEntityType.RAVAGER_FAMILIAR, CustomEntityType.HEXED_VEX)
    ),
    ILLAGER_WARLOCK_5(SlayerType.ILLAGER_WARLOCK,
    CustomEntityType.ILLAGER_WARLOCK_5,
    29_300,
    100_000,
    51_200,
    ImmutableList.of(CustomEntityType.WARLOCK_APPRENTICE, CustomEntityType.WARLOCK_SHADOW)
    )
    //</editor-fold>
}