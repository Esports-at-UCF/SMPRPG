package xyz.devvydont.smprpg.items.blueprints.vanilla

import org.bukkit.Material
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import xyz.devvydont.smprpg.attribute.AttributeWrapper
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry
import xyz.devvydont.smprpg.items.attribute.AttributeEntry
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry
import xyz.devvydont.smprpg.items.base.VanillaAttributeItem
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment
import xyz.devvydont.smprpg.items.interfaces.IRepairable
import xyz.devvydont.smprpg.items.interfaces.ISkillRequirement
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.skills.SkillType
import xyz.devvydont.smprpg.util.items.ToolGlobals
import xyz.devvydont.smprpg.util.items.ToolStats
import kotlin.math.max
import kotlin.math.roundToInt

class ItemArmor(itemService: ItemService, material: Material) : VanillaAttributeItem(itemService, material),
    IBreakableEquipment, IRepairable, ISkillRequirement {

    override val repairMaterial: MutableCollection<ItemStack> get() = when (material) {
        Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS -> mutableListOf(itemService.getCustomItem(Material.LEATHER))
        Material.COPPER_HELMET, Material.COPPER_CHESTPLATE, Material.COPPER_LEGGINGS, Material.COPPER_BOOTS -> mutableListOf(itemService.getCustomItem(Material.COPPER_INGOT))
        Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS -> mutableListOf(itemService.getCustomItem(Material.IRON_INGOT))
        Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS -> mutableListOf(itemService.getCustomItem(Material.GOLD_INGOT))
        Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS -> mutableListOf(itemService.getCustomItem(Material.NETHERITE_INGOT))
        else -> mutableListOf()
    }
    override val skillRequirements: MutableMap<SkillType, Int> get() = when (material) {
        Material.ELYTRA -> mutableMapOf(Pair(SkillType.COMBAT, 50))
        Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS -> mutableMapOf(Pair(SkillType.COMBAT, 35))
        Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS -> mutableMapOf(Pair(SkillType.COMBAT, 10))
        Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS -> mutableMapOf(Pair(SkillType.COMBAT, 7))
        Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS -> mutableMapOf(Pair(SkillType.COMBAT, 5))
        Material.COPPER_HELMET, Material.COPPER_CHESTPLATE, Material.COPPER_LEGGINGS, Material.COPPER_BOOTS -> mutableMapOf(Pair(SkillType.COMBAT, ToolStats.COPPER.skillReqLevel))
        else -> mutableMapOf()
    }

    override fun getPowerRating(): Int {
        return getArmorPowerRating(material)
    }

    override fun getAttributeModifiers(item: ItemStack?): MutableCollection<AttributeEntry?> {
        val modifiers: MutableList<AttributeEntry?> = ArrayList<AttributeEntry?>()

        // If we have true defense...
        val trueDef: Double = getArmorFromMaterial(material)
        if (trueDef > 0) modifiers.add(AdditiveAttributeEntry(AttributeWrapper.ARMOR, getArmorFromMaterial(material)))

        // If we have health...
        val health: Double = getHealthFromMaterial(material)
        if (health > 0) modifiers.add(AdditiveAttributeEntry(AttributeWrapper.HEALTH, health))

        // If we have defense...
        val defense = getDefenseFromMaterial(material).toDouble()
        if (defense > 0) modifiers.add(AdditiveAttributeEntry(AttributeWrapper.DEFENSE, defense))

        // If we have knockback resist...
        val kbResist: Double = getKnockbackResistanceFromMaterial(material)
        if (kbResist > 0) modifiers.add(AdditiveAttributeEntry(AttributeWrapper.KNOCKBACK_RESISTANCE, kbResist))

        // If we have damage...
        val dmg: Double = getDamageFromMaterial(material)
        if (dmg > 0) modifiers.add(ScalarAttributeEntry(AttributeWrapper.STRENGTH, dmg))

        // If we have intelligence...
        val intelligence: Double = getIntelligenceFromMaterial(material)
        if (intelligence > 0) modifiers.add(AdditiveAttributeEntry(AttributeWrapper.INTELLIGENCE, intelligence))

        // If we have mining speed...
        val miningSpeed: Double = getMiningSpeedFromMaterial(material)
        if (miningSpeed > 0) modifiers.add(AdditiveAttributeEntry(AttributeWrapper.MINING_SPEED, miningSpeed))

        // If we have no modifiers, we need to have something to get rid of the vanilla stats
        // Crappy armor won't have any attributes since defense isn't an attribute
        if (modifiers.isEmpty()) modifiers.add(AdditiveAttributeEntry(AttributeWrapper.ARMOR, 0.0))

        return modifiers
    }

    override fun getActiveSlot(): EquipmentSlotGroup {
        return EquipmentSlotGroup.ARMOR
    }

    override fun getMaxDurability(): Int {
        return getMaxDurability(material)
    }

    companion object {
        @JvmStatic
        fun getDefenseFromMaterial(material: Material): Int {
            return when (material) {
                Material.ELYTRA -> 100
                Material.LEATHER_HORSE_ARMOR -> 100
                Material.IRON_HORSE_ARMOR -> 250
                Material.GOLDEN_HORSE_ARMOR -> 500
                Material.DIAMOND_HORSE_ARMOR -> 1000
                Material.WOLF_ARMOR -> 500
                Material.TURTLE_HELMET -> 25
                Material.LEATHER_HELMET -> 10
                Material.LEATHER_CHESTPLATE -> 15
                Material.LEATHER_LEGGINGS -> 13
                Material.LEATHER_BOOTS -> 7
                Material.COPPER_HELMET -> 12
                Material.COPPER_CHESTPLATE -> 18
                Material.COPPER_LEGGINGS -> 14
                Material.COPPER_BOOTS -> 10
                Material.CHAINMAIL_HELMET -> 12
                Material.CHAINMAIL_CHESTPLATE -> 18
                Material.CHAINMAIL_LEGGINGS -> 14
                Material.CHAINMAIL_BOOTS -> 10
                Material.GOLDEN_HELMET -> 11
                Material.GOLDEN_CHESTPLATE -> 15
                Material.GOLDEN_LEGGINGS -> 13
                Material.GOLDEN_BOOTS -> 8
                Material.IRON_HELMET -> 30
                Material.IRON_CHESTPLATE -> 40
                Material.IRON_LEGGINGS -> 35
                Material.IRON_BOOTS -> 20
                Material.DIAMOND_HELMET -> 45
                Material.DIAMOND_CHESTPLATE -> 70
                Material.DIAMOND_LEGGINGS -> 60
                Material.DIAMOND_BOOTS -> 40
                Material.NETHERITE_HELMET -> 80
                Material.NETHERITE_CHESTPLATE -> 120
                Material.NETHERITE_LEGGINGS -> 100
                Material.NETHERITE_BOOTS -> 65
                else -> 0
            }
        }

        fun getDefenseFromItemType(itemType: CustomItemType): Int {
            return when (itemType) {
                CustomItemType.SILVER_HELMET -> 5
                CustomItemType.SILVER_CHESTPLATE -> 7
                CustomItemType.SILVER_LEGGINGS -> 6
                CustomItemType.SILVER_BOOTS -> 4
                CustomItemType.TIN_HELMET -> 10
                CustomItemType.TIN_CHESTPLATE -> 15
                CustomItemType.TIN_LEGGINGS -> 13
                CustomItemType.TIN_BOOTS -> 7
                CustomItemType.BRONZE_HELMET -> 30
                CustomItemType.BRONZE_CHESTPLATE -> 40
                CustomItemType.BRONZE_LEGGINGS -> 35
                CustomItemType.BRONZE_BOOTS -> 20
                CustomItemType.ROSE_GOLD_HELMET -> 15
                CustomItemType.ROSE_GOLD_CHESTPLATE -> 20
                CustomItemType.ROSE_GOLD_LEGGINGS -> 16
                CustomItemType.ROSE_GOLD_BOOTS -> 12
                CustomItemType.STEEL_HELMET -> 35
                CustomItemType.STEEL_CHESTPLATE -> 50
                CustomItemType.STEEL_LEGGINGS -> 40
                CustomItemType.STEEL_BOOTS -> 25
                CustomItemType.PLATINUM_HELMET -> 35
                CustomItemType.PLATINUM_CHESTPLATE -> 50
                CustomItemType.PLATINUM_LEGGINGS -> 40
                CustomItemType.PLATINUM_BOOTS -> 25
                CustomItemType.MITHRIL_HELMET -> 40
                CustomItemType.MITHRIL_CHESTPLATE -> 60
                CustomItemType.MITHRIL_LEGGINGS -> 45
                CustomItemType.MITHRIL_BOOTS -> 30
                CustomItemType.TITANIUM_HELMET -> 45
                CustomItemType.TITANIUM_CHESTPLATE -> 70
                CustomItemType.TITANIUM_LEGGINGS -> 60
                CustomItemType.TITANIUM_BOOTS -> 40
                CustomItemType.ADAMANTIUM_HELMET -> 60
                CustomItemType.ADAMANTIUM_CHESTPLATE -> 90
                CustomItemType.ADAMANTIUM_LEGGINGS -> 75
                CustomItemType.ADAMANTIUM_BOOTS -> 55
                CustomItemType.TUNGSTEN_HELMET -> 45
                CustomItemType.TUNGSTEN_CHESTPLATE -> 70
                CustomItemType.TUNGSTEN_LEGGINGS -> 60
                CustomItemType.TUNGSTEN_BOOTS -> 40
                CustomItemType.COBALT_HELMET, CustomItemType.PALLADIUM_HELMET -> 60
                CustomItemType.COBALT_CHESTPLATE, CustomItemType.PALLADIUM_CHESTPLATE -> 90
                CustomItemType.COBALT_LEGGINGS, CustomItemType.PALLADIUM_LEGGINGS -> 75
                CustomItemType.COBALT_BOOTS, CustomItemType.PALLADIUM_BOOTS -> 55
                CustomItemType.ORICHALCUM_HELMET, CustomItemType.AETHERIUM_HELMET -> 70
                CustomItemType.ORICHALCUM_CHESTPLATE, CustomItemType.AETHERIUM_CHESTPLATE -> 105
                CustomItemType.ORICHALCUM_LEGGINGS, CustomItemType.AETHERIUM_LEGGINGS -> 90
                CustomItemType.ORICHALCUM_BOOTS, CustomItemType.AETHERIUM_BOOTS -> 60
                else -> 0
            }
        }

        /**
         * Gets the armor rating for a vanilla material.
         *
         * Armor is the amount of additional i-frames that a player gets
         * after taking damage.
         *
         * @param material The material fallback for an item.
         * @return How much armor it gives.
         */
        fun getArmorFromMaterial(material: Material): Double {
            return when (material) {
                Material.NETHERITE_HELMET -> 2
                Material.NETHERITE_CHESTPLATE -> 3
                Material.NETHERITE_LEGGINGS -> 3
                Material.NETHERITE_BOOTS -> 2
                // Material.DIAMOND_CHESTPLATE -> 1
                // Material.DIAMOND_LEGGINGS -> 1
                else -> 0
            }.toDouble()
        }

        @JvmStatic
        fun getHealthFromMaterial(material: Material): Double {
            return when (material) {
                Material.ELYTRA -> 100
                else -> 0
            }.toDouble()
        }

        @JvmStatic
        fun getDamageFromMaterial(material: Material): Double {
            return when (material) {
                Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS -> .2
                Material.NETHERITE_LEGGINGS, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_BOOTS, Material.NETHERITE_HELMET -> .1
                else -> 0
            }.toDouble()
        }

        fun getIntelligenceFromMaterial(material: Material): Double {
            return when (material) {
                Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS -> 15
                else -> 0
            }.toDouble()
        }

        fun getKnockbackResistanceFromMaterial(material: Material): Double {
            return when (material) {
                Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS -> .1
                else -> 0
            }.toDouble()
        }

        fun getMiningSpeedFromMaterial(material: Material): Double {
            return when (material) {
                Material.COPPER_HELMET, Material.COPPER_CHESTPLATE, Material.COPPER_LEGGINGS, Material.COPPER_BOOTS -> 50
                else -> 0
            }.toDouble()
        }

        fun getArmorPowerRating(material: Material): Int {
            return when (material) {
                Material.ELYTRA, Material.WOLF_ARMOR -> 30
                Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS -> ToolStats.NETHERITE.power
                // Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS, Material.DIAMOND_HORSE_ARMOR -> ToolGlobals.DIAMOND_TOOL_POWER
                Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS, Material.GOLDEN_HORSE_ARMOR -> ToolStats.GOLD.power
                Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS, Material.IRON_HORSE_ARMOR -> ToolStats.IRON.power
                Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS, Material.TURTLE_HELMET -> ToolStats.COPPER.power + 1
                Material.COPPER_HELMET, Material.COPPER_CHESTPLATE, Material.COPPER_LEGGINGS, Material.COPPER_BOOTS, Material.COPPER_HORSE_ARMOR -> ToolStats.COPPER.power
                Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS, Material.LEATHER_HORSE_ARMOR -> ToolStats.WOOD.power
                else -> 1
            }
        }

        fun getMaxDurability(material: Material): Int {
            return when (material) {
                Material.ELYTRA -> 2500

                Material.NETHERITE_HELMET -> (ToolStats.NETHERITE.getArmorUnitDurability() * 5).roundToInt()
                Material.NETHERITE_CHESTPLATE -> (ToolStats.NETHERITE.getArmorUnitDurability() * 8).roundToInt()
                Material.NETHERITE_LEGGINGS -> (ToolStats.NETHERITE.getArmorUnitDurability() * 7).roundToInt()
                Material.NETHERITE_BOOTS -> (ToolStats.NETHERITE.getArmorUnitDurability() * 4).roundToInt()

                // Material.DIAMOND_HELMET -> (ToolStats.DIAMOND.getArmorUnitDurability() * 5).roundToInt()
                // Material.DIAMOND_CHESTPLATE -> (ToolStats.DIAMOND.getArmorUnitDurability() * 8).roundToInt()
                // Material.DIAMOND_LEGGINGS -> (ToolStats.DIAMOND.getArmorUnitDurability() * 7).roundToInt()
                // Material.DIAMOND_BOOTS -> (ToolStats.DIAMOND.getArmorUnitDurability() * 4).roundToInt()

                Material.GOLDEN_HELMET -> (ToolStats.GOLD.getArmorUnitDurability() * 5).roundToInt()
                Material.GOLDEN_CHESTPLATE -> (ToolStats.GOLD.getArmorUnitDurability() * 8).roundToInt()
                Material.GOLDEN_LEGGINGS -> (ToolStats.GOLD.getArmorUnitDurability() * 7).roundToInt()
                Material.GOLDEN_BOOTS -> (ToolStats.GOLD.getArmorUnitDurability() * 4).roundToInt()

                Material.IRON_HELMET -> (ToolStats.IRON.getArmorUnitDurability() * 5).roundToInt()
                Material.IRON_CHESTPLATE -> (ToolStats.IRON.getArmorUnitDurability() * 8).roundToInt()
                Material.IRON_LEGGINGS -> (ToolStats.IRON.getArmorUnitDurability() * 7).roundToInt()
                Material.IRON_BOOTS -> (ToolStats.IRON.getArmorUnitDurability() * 4).roundToInt()

                Material.CHAINMAIL_HELMET -> (ToolStats.COPPER.getArmorUnitDurability() * 6).roundToInt()
                Material.CHAINMAIL_CHESTPLATE -> (ToolStats.COPPER.getArmorUnitDurability() * 9).roundToInt()
                Material.CHAINMAIL_LEGGINGS -> (ToolStats.COPPER.getArmorUnitDurability() * 8).roundToInt()
                Material.CHAINMAIL_BOOTS -> (ToolStats.COPPER.getArmorUnitDurability() * 5).roundToInt()

                Material.COPPER_HELMET -> (ToolStats.COPPER.getArmorUnitDurability() * 5).roundToInt()
                Material.COPPER_CHESTPLATE -> (ToolStats.COPPER.getArmorUnitDurability() * 8).roundToInt()
                Material.COPPER_LEGGINGS -> (ToolStats.COPPER.getArmorUnitDurability() * 7).roundToInt()
                Material.COPPER_BOOTS -> (ToolStats.COPPER.getArmorUnitDurability() * 4).roundToInt()

                Material.LEATHER_HELMET -> (ToolStats.WOOD.getArmorUnitDurability() * 5).roundToInt()
                Material.LEATHER_CHESTPLATE -> (ToolStats.WOOD.getArmorUnitDurability() * 8).roundToInt()
                Material.LEATHER_LEGGINGS -> (ToolStats.WOOD.getArmorUnitDurability() * 7).roundToInt()
                Material.LEATHER_BOOTS -> (ToolStats.WOOD.getArmorUnitDurability() * 4).roundToInt()

                else -> max(1000, material.getMaxDurability() * 10)
            }
        }
    }
}
