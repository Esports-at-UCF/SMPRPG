package xyz.devvydont.smprpg.entity.slayer.piglin

import net.kyori.adventure.key.Key
import org.bukkit.Material
import org.bukkit.damage.DamageType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.PiglinBrute
import org.bukkit.entity.Zombie
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityTransformEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.persistence.PersistentDataType
import xyz.devvydont.smprpg.entity.CustomEntityType
import xyz.devvydont.smprpg.entity.MobType
import xyz.devvydont.smprpg.entity.slayer.SlayerBossInstance
import xyz.devvydont.smprpg.events.CustomEntityDamageByEntityEvent
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.blueprints.resources.scrolls.DynamicEnchantingScroll
import xyz.devvydont.smprpg.items.interfaces.ICustomTextured
import xyz.devvydont.smprpg.services.EnchantmentService
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.persistence.KeyStore

open class PiglinWarlordParent
/**
 * An unsafe constructor to use to allow dynamic creation of custom entities.
 * This is specifically used as a casting hack for the CustomEntityType enum in order to dynamically create
 * entities.
 *
 * @param entity     The entity that should map the T type parameter.
 * @param entityType The entity type.
 */
    (entity: LivingEntity, val entityType: CustomEntityType) : SlayerBossInstance<PiglinBrute>(entity as PiglinBrute, entityType) {

    override fun setup() {
        mobTypes.add(MobType.BOSS);
        mobTypes.add(MobType.NETHER);
        mobTypes.add(MobType.ANIMAL);
        mobTypes.add(MobType.HUMANOID);

        super.setup()

        // Setup equipment for boss
        val equipment = _entity!!.equipment

        equipment?.itemInMainHandDropChance = 0f
        equipment?.itemInOffHandDropChance = 0f
        equipment?.helmetDropChance = 0f
        equipment?.chestplateDropChance = 0f
        equipment?.leggingsDropChance = 0f
        equipment?.bootsDropChance = 0f

        // Shambling Abominations can also spawn other quests.
        // This encourages collaborative slayer gameplay.
        _entity.getPersistentDataContainer()
            .set(KeyStore.SLAYER_SPAWN_TYPE, PersistentDataType.STRING, SPAWN_MOB_FLAG)
    }

    override fun getSkillExperienceMultiplier(): Double {
        return 10.0
    }

    /*
     * 5 min
     */
    override fun getTimeLimit(): Long {
        return 60L * 5L
    }

    companion object {
        val LOOT_SMITE_SCROLL : ItemStack = DynamicEnchantingScroll.Companion.getScrollWithEnchantment(
            EnchantmentService.Companion.SMITE)

        const val SPAWN_MOB_FLAG : String = "piglin_warlord"
    }
}