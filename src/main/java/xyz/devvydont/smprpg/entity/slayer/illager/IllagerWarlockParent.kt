package xyz.devvydont.smprpg.entity.slayer.illager

import io.papermc.paper.datacomponent.DataComponentTypes
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeInstance
import org.bukkit.entity.Evoker
import org.bukkit.entity.Fireball
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.entity.EntityResurrectEvent
import org.bukkit.event.entity.EntityTransformEvent
import org.bukkit.persistence.PersistentDataType
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.SMPRPG.Companion.plugin
import xyz.devvydont.smprpg.entity.CustomEntityType
import xyz.devvydont.smprpg.entity.MobType
import xyz.devvydont.smprpg.entity.slayer.SlayerBossInstance
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.services.AttributeService
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.persistence.KeyStore
import xyz.devvydont.smprpg.util.time.TickTime

open class IllagerWarlockParent
/**
 * An unsafe constructor to use to allow dynamic creation of custom entities.
 * This is specifically used as a casting hack for the CustomEntityType enum in order to dynamically create
 * entities.
 *
 * @param entity     The entity that should map the T type parameter.
 * @param entityType The entity type.
 */
    (entity: LivingEntity?, entityType: CustomEntityType?) : SlayerBossInstance<Evoker>(entity as Evoker?, entityType) {

    val fireballs = ArrayList<Fireball>()

    override fun setup() {
        mobTypes.add(MobType.BOSS);
        mobTypes.add(MobType.ILLAGER);
        mobTypes.add(MobType.HUMANOID);

        super.setup()

        // Setup equipment for boss
        val equipment = _entity!!.equipment

        equipment?.setItemInOffHand(ItemService.generate(Material.TOTEM_OF_UNDYING))
        val helmet = ItemService.generate(Material.DISPENSER)
        helmet.setData(DataComponentTypes.ITEM_MODEL, HOOD_KEY)
        equipment?.setHelmet(helmet)
        equipment?.setItemInMainHand(ItemService.generate(CustomItemType.SIMPLE_TOME))

        equipment?.itemInMainHandDropChance = 0f
        equipment?.itemInOffHandDropChance = 0f
        equipment?.helmetDropChance = 0f
        equipment?.chestplateDropChance = 0f
        equipment?.leggingsDropChance = 0f
        equipment?.bootsDropChance = 0f

        // Illager Warlocks can also spawn other quests.
        // This encourages collaborative slayer gameplay.
        _entity.getPersistentDataContainer()
            .set(KeyStore.SLAYER_SPAWN_TYPE, PersistentDataType.STRING, SPAWN_MOB_FLAG)
    }

    override fun getSkillExperienceMultiplier(): Double {
        return 20.0
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onTransform(event : EntityTransformEvent) {
        if (event.entity == this.entity)
            event.isCancelled = true
    }

    @EventHandler
    fun onFireballExplode(event : EntityExplodeEvent) {
        if (event.entity in fireballs) {
            event.blockList().clear()
            fireballs.remove(event.entity)
        }
    }

    @EventHandler
    fun onBossResurrect(event : EntityResurrectEvent) {
        val entity = event.entity
        if (entity == this.entity) {
            Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                val maxHp: AttributeInstance? = entity.getAttribute(Attribute.MAX_HEALTH)
                entity.heal(maxHp!!.value)
            }, TickTime.TICK)
        }
    }

    companion object {
        const val SPAWN_MOB_FLAG : String = "illager_warlock"
        val HOOD_KEY = NamespacedKey(SMPRPG.plugin, "illager_warlock_hood")
    }
}