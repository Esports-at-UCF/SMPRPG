package xyz.devvydont.smprpg.entity.slayer.shambling.goals

import com.destroystokyo.paper.entity.ai.Goal
import com.destroystokyo.paper.entity.ai.GoalKey
import com.destroystokyo.paper.entity.ai.GoalType
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.Equippable
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Player
import org.bukkit.entity.Zombie
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.entity.slayer.shambling.ShamblingAbominationParent
import xyz.devvydont.smprpg.items.interfaces.ICustomTextured
import xyz.devvydont.smprpg.services.AttributeService
import java.util.*

class ShamblingAbominationEnrageGoal(val zombie : Zombie, val spawnPlayer : Player?) : Goal<Zombie> {

    val goalKey : GoalKey<Zombie> = GoalKey.of(Zombie::class.java, NamespacedKey(SMPRPG.Companion.plugin, "shambling_abomination_enrage_goal"))
    val multiplierKey : NamespacedKey = NamespacedKey(SMPRPG.plugin, "shambling_abomination_enrage")
    var activated = false

    override fun shouldActivate(): Boolean {
        val maxHp = zombie.getAttribute(Attribute.MAX_HEALTH)
        if (activated)  // We only want enrage to happen once.
            return false

        if (maxHp != null) {
           if (zombie.health <= (maxHp.value * 0.5)) {
                return true
           }
        }
        return false
    }

    override fun getKey(): GoalKey<Zombie> {
        return goalKey
    }

    override fun getTypes(): EnumSet<GoalType> {
        return EnumSet.of(GoalType.UNKNOWN_BEHAVIOR)
    }

    override fun shouldStayActive(): Boolean {
        return shouldActivate();
    }

    override fun start() {
    }

    override fun stop() {
    }

    override fun tick() {
        if (!activated) {
            activated = true

            // Multiply outgoing damage by 50%
            val attributeInst = AttributeService.instance.getOrCreateAttribute(zombie, AttributeWrapper.STRENGTH)
            attributeInst.addModifier(AttributeModifier(multiplierKey, 0.5, AttributeModifier.Operation.MULTIPLY_SCALAR_1))

            // Update asset ID on all of the equipment
            val zombieEq = zombie.equipment
            val head = ItemStack(Material.PLAYER_HEAD)
            val meta = head.itemMeta as SkullMeta
            meta.playerProfile = ICustomTextured.getProfile(ShamblingAbominationParent.headTextureAngry)
            head.setItemMeta(meta)
            zombieEq.setHelmet(head)

            val chestplate = zombieEq.chestplate
            chestplate.setData(DataComponentTypes.EQUIPPABLE, Equippable.equippable(EquipmentSlot.CHEST).assetId(ShamblingAbominationParent.enragedAssetId).build())
            zombieEq.chestplate = chestplate

            val leggings = zombieEq.leggings
            leggings.setData(DataComponentTypes.EQUIPPABLE, Equippable.equippable(EquipmentSlot.LEGS).assetId(ShamblingAbominationParent.enragedAssetId).build())
            zombieEq.leggings = leggings

            val boots = zombieEq.boots
            boots.setData(DataComponentTypes.EQUIPPABLE, Equippable.equippable(EquipmentSlot.FEET).assetId(ShamblingAbominationParent.enragedAssetId).build())
            zombieEq.boots = boots

            zombie.world.playSound(zombie, Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1f, 0.5f)
        }
    }

}