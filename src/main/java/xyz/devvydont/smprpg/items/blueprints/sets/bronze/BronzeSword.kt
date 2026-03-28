package xyz.devvydont.smprpg.items.blueprints.sets.bronze

import org.bukkit.Material
import org.bukkit.inventory.CraftingRecipe
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeEntry
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemSword
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment
import xyz.devvydont.smprpg.items.interfaces.ICraftable
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.skills.SkillType
import xyz.devvydont.smprpg.util.crafting.builders.SwordRecipe

class BronzeSword(itemService: ItemService, type: CustomItemType) : BronzeAttributeItem(itemService, type),
    ICraftable, IBreakableEquipment {

    override val itemClassification: ItemClassification get() = ItemClassification.SWORD
    override val skillRequirements: MutableMap<SkillType, Int> get() = mutableMapOf(Pair(SkillType.COMBAT, toolStats.skillReqLevel))

    override fun getAttributeModifiers(item: ItemStack?): MutableCollection<AttributeEntry?> {
        return mutableListOf(
            AdditiveAttributeEntry(AttributeWrapper.STRENGTH, ItemSword.getSwordDamage(CustomItemType.BRONZE_SWORD)),
            MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, ItemSword.SWORD_ATTACK_SPEED_DEBUFF)
        )
    }

    override fun getCustomRecipe(): CraftingRecipe? {
        return SwordRecipe(
            this,
            itemService.getCustomItem(CustomItemType.BRONZE_INGOT),
            itemService.getCustomItem(Material.STICK),
            generate()
        ).build()
    }

}
