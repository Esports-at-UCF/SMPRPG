package xyz.devvydont.smprpg.entity.slayer.shambling

import net.kyori.adventure.key.Key
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Zombie
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityTransformEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import xyz.devvydont.smprpg.entity.CustomEntityType
import xyz.devvydont.smprpg.entity.slayer.SlayerBossInstance
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.interfaces.ICustomTextured
import xyz.devvydont.smprpg.services.ItemService.Companion.generate

open class ShamblingAbominationParent
/**
 * An unsafe constructor to use to allow dynamic creation of custom entities.
 * This is specifically used as a casting hack for the CustomEntityType enum in order to dynamically create
 * entities.
 *
 * @param entity     The entity that should map the T type parameter.
 * @param entityType The entity type.
 */
    (entity: LivingEntity?, entityType: CustomEntityType?) : SlayerBossInstance<Zombie>(entity as Zombie?, entityType) {

    open var attackCooldown = 10
    open var enrageThreshold = 0.5

    override fun setup() {
        super.setup()

        // Setup equipment for boss
        val equipment = _entity!!.equipment
        val head = ItemStack(Material.PLAYER_HEAD)
        val meta = head.itemMeta as SkullMeta
        meta.playerProfile = ICustomTextured.getProfile(headTexture)
        head.setItemMeta(meta)
        equipment?.setHelmet(head)

        equipment?.setChestplate(generate(CustomItemType.SHAMBLING_CHESTPLATE))

        val leggings = generate(CustomItemType.SHAMBLING_LEGGINGS)
        equipment?.setLeggings(leggings)

        val boots = generate(CustomItemType.SHAMBLING_BOOTS)
        equipment?.setBoots(boots)

        equipment?.setItemInMainHandDropChance(0f)
        equipment?.setItemInOffHandDropChance(0f)
        equipment?.setHelmetDropChance(0f)
        equipment?.setChestplateDropChance(0f)
        equipment?.setLeggingsDropChance(0f)
        equipment?.setBootsDropChance(0f)

    }

    override fun getSkillExperienceMultiplier(): Double {
        return 10.0
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onTransform(event : EntityTransformEvent) {
        if (event.entity == this.entity)
            event.isCancelled = true
    }

    companion object {
        val headTexture : String = "http://textures.minecraft.net/texture/1fc0184473fe882d2895ce7cbc8197bd40ff70bf10d3745de97b6c2a9c5fc78f"
        val headTextureAngry : String = "http://textures.minecraft.net/texture/ba7bb34471508b4b20cb35c37a1dd6e8afa7f2d5388822138837caf59c2195d8"
        val enragedAssetId : Key = Key.key("shambling_boss_enrage");
    }
}