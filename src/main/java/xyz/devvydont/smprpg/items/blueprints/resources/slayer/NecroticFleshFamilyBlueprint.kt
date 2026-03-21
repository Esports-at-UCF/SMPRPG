package xyz.devvydont.smprpg.items.blueprints.resources.slayer

import io.papermc.paper.datacomponent.item.Consumable
import io.papermc.paper.datacomponent.item.consumable.ConsumeEffect
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.base.CustomCompressableBlueprint
import xyz.devvydont.smprpg.items.interfaces.IConsumable
import xyz.devvydont.smprpg.items.interfaces.IEdible
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.util.crafting.CompressionRecipeMember
import xyz.devvydont.smprpg.util.crafting.MaterialWrapper
import xyz.devvydont.smprpg.util.time.TickTime
import java.util.List

class NecroticFleshFamilyBlueprint(itemService: ItemService?, type: CustomItemType?) :
    CustomCompressableBlueprint(itemService, type), IEdible, IConsumable {
    override fun getCompressionFlow(): MutableList<CompressionRecipeMember?> {
        return COMPRESSION_FLOW
    }

    override fun getNutrition(item: ItemStack?): Int {
        return 0
    }

    override fun getSaturation(item: ItemStack?): Float {
        return 0.0f
    }

    override fun canAlwaysEat(item: ItemStack?): Boolean {
        return true
    }

    override fun getConsumableComponent(item: ItemStack?): Consumable {
        val effects = ArrayList<ConsumeEffect>()

        if (getCustomItemType() == CustomItemType.NECROTIC_FLESH_SINGULARITY) effects.add(
            ConsumeEffect.applyStatusEffects(
                listOf(
                    PotionEffect(PotionEffectType.WITHER, TickTime.minutes(5).toInt(), 9),
                    PotionEffect(PotionEffectType.BLINDNESS, TickTime.minutes(5).toInt(), 1),
                    PotionEffect(PotionEffectType.DARKNESS, TickTime.minutes(5).toInt(), 1)
                ), 1f
            )
        )

        if (getCustomItemType() == CustomItemType.ENCHANTED_NECROTIC_FLESH) effects.add(
            ConsumeEffect.applyStatusEffects(
                listOf(
                    PotionEffect(PotionEffectType.WITHER, TickTime.minutes(5).toInt(), 4),
                    PotionEffect(PotionEffectType.BLINDNESS, TickTime.minutes(5).toInt(), 0)
                ), 1f
            )
        )

        if (getCustomItemType() == CustomItemType.PREMIUM_NECROTIC_FLESH) effects.add(
            ConsumeEffect.applyStatusEffects(
                listOf(
                    PotionEffect(PotionEffectType.WITHER, TickTime.minutes(5).toInt(), 2)
                ), 1f
            )
        )

        return Consumable.consumable()
            .consumeSeconds(10f)
            .addEffects(effects)
            .build()
    }

    override val itemClassification: ItemClassification get() = ItemClassification.CONSUMABLE

    companion object {
        val COMPRESSION_FLOW: MutableList<CompressionRecipeMember?> = List.of<CompressionRecipeMember?>(
            CompressionRecipeMember(MaterialWrapper(CustomItemType.NECROTIC_FLESH)),
            CompressionRecipeMember(MaterialWrapper(CustomItemType.PREMIUM_NECROTIC_FLESH)),
            CompressionRecipeMember(MaterialWrapper(CustomItemType.ENCHANTED_NECROTIC_FLESH)),
            CompressionRecipeMember(MaterialWrapper(CustomItemType.NECROTIC_FLESH_SINGULARITY))
        )
    }
}
