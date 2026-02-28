package xyz.devvydont.smprpg.entity.slayer

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Zombie
import org.bukkit.event.EventHandler
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.SkullMeta
import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.entity.CustomEntityType
import xyz.devvydont.smprpg.entity.slayer.goals.ShamblingAbominationBrainGoal
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.interfaces.ICustomTextured
import xyz.devvydont.smprpg.services.AttributeService
import xyz.devvydont.smprpg.services.ItemService.Companion.generate
import xyz.devvydont.smprpg.util.items.ChancedItemDrop
import xyz.devvydont.smprpg.util.items.LootDrop
import xyz.devvydont.smprpg.util.items.QuantityLootDrop
import java.util.List
import java.util.function.Consumer

class ShamblingAbomination
/**
 * An unsafe constructor to use to allow dynamic creation of custom entities.
 * This is specifically used as a casting hack for the CustomEntityType enum in order to dynamically create
 * entities.
 *
 * @param entity     The entity that should map the T type parameter.
 * @param entityType The entity type.
 */
    (entity: LivingEntity?, entityType: CustomEntityType?) : SlayerBossInstance<Zombie>(entity as Zombie?, entityType) {
    override fun getItemDrops(): MutableCollection<LootDrop?>? {
        return List.of<LootDrop?>(
            QuantityLootDrop(generate(Material.ROTTEN_FLESH), 5, 16, this),
            ChancedItemDrop(generate(CustomItemType.PREMIUM_FLESH), 20, this),
            ChancedItemDrop(generate(CustomItemType.ENCHANTED_FLESH), 100, this)
        )
    }

    override fun setup() {
        super.setup()

        // Setup equipment for boss
        val head = ItemStack(Material.PLAYER_HEAD)
        val meta = head.itemMeta as SkullMeta
        meta.playerProfile = ICustomTextured.getProfile(headTexture)
        head.setItemMeta(meta)
        _entity!!.equipment?.setHelmet(head)

        _entity.equipment?.setChestplate(generate(CustomItemType.SHAMBLING_CHESTPLATE))

        val leggings = generate(CustomItemType.SHAMBLING_LEGGINGS)
        _entity.equipment?.setLeggings(leggings)

        val boots = generate(CustomItemType.SHAMBLING_BOOTS)
        _entity.equipment?.setBoots(boots)

    }

    override fun updateAttributes() {
        super.updateAttributes()
        updateBaseAttribute(AttributeWrapper.KNOCKBACK_RESISTANCE, 0.25)
        updateBaseAttribute(AttributeWrapper.SCALE, 1.25)
        updateBaseAttribute(AttributeWrapper.DEFENSE, 25.0)
        updateBaseAttribute(AttributeWrapper.ARMOR, 0.0)
    }

    override fun getSkillExperienceMultiplier(): Double {
        return 10.0
    }

    @EventHandler
    private fun onShamblingAbominationSpawn(event: EntityAddToWorldEvent) {
        val entity = this.getEntity()
        if (event.getEntity() == entity) {
            val zombie = entity
            val mobGoals = Bukkit.getMobGoals()
            mobGoals.removeAllGoals(zombie as Zombie)
            mobGoals.addGoal(zombie, 3, ShamblingAbominationBrainGoal(zombie, null))
        }
    }

    companion object {
        val headTexture : String = "http://textures.minecraft.net/texture/58a285acf8ae49b6c510718509293f109b7967751922bb860e3dc0fad77091c3"
        val headTextureAngry : String = "http://textures.minecraft.net/texture/1fc0184473fe882d2895ce7cbc8197bd40ff70bf10d3745de97b6c2a9c5fc78f"
    }
}