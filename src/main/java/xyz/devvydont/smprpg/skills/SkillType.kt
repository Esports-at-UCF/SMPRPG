package xyz.devvydont.smprpg.skills

import org.bukkit.NamespacedKey
import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.skills.rewards.SkillRewardContainer
import xyz.devvydont.smprpg.skills.rewards.definitions.*
import xyz.devvydont.smprpg.util.formatting.MinecraftStringUtils

enum class SkillType(val rewards: SkillRewardContainer, val proficiencyAttribute : AttributeWrapper) {
    COMBAT(CombatSkillRewards(), AttributeWrapper.COMBAT_PROFICIENCY),
    WOODCUTTING(WoodcuttingSkillRewards(), AttributeWrapper.WOODCUTTING_PROFICIENCY),
    MINING(MiningSkillRewards(), AttributeWrapper.MINING_PROFICIENCY),
    MAGIC(MagicSkillRewards(), AttributeWrapper.MAGIC_PROFICIENCY),
    FARMING(FarmingSkillRewards(), AttributeWrapper.FARMING_PROFICIENCY),
    FISHING(FishingSkillRewards(), AttributeWrapper.FISHING_PROFICIENCY),
    SLAYER(SlayerSkillRewards(), AttributeWrapper.SLAYER_PROFICIENCY);

    val identifier: String = "skill_${name.lowercase()}"

    val key: NamespacedKey = NamespacedKey("smprpg", identifier)

    val displayName: String
        get() = MinecraftStringUtils.getTitledString(this.name)
}
