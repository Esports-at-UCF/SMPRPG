package xyz.devvydont.smprpg.items.blueprints.resources.scrolls

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.CustomModelData
import io.papermc.paper.datacomponent.item.ItemEnchantments
import io.papermc.paper.datacomponent.item.TooltipDisplay
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import xyz.devvydont.smprpg.SMPRPG
import xyz.devvydont.smprpg.enchantments.CustomEnchantment
import xyz.devvydont.smprpg.enchantments.EnchantmentRarity
import xyz.devvydont.smprpg.items.CustomItemType
import xyz.devvydont.smprpg.items.ItemClassification
import xyz.devvydont.smprpg.items.ItemRarity
import xyz.devvydont.smprpg.items.base.CustomItemBlueprint
import xyz.devvydont.smprpg.items.interfaces.IFooterDescribable
import xyz.devvydont.smprpg.items.interfaces.IHeaderDescribable
import xyz.devvydont.smprpg.items.interfaces.IModelOverridden
import xyz.devvydont.smprpg.items.interfaces.ISellable
import xyz.devvydont.smprpg.services.EnchantmentService
import xyz.devvydont.smprpg.services.ItemService
import xyz.devvydont.smprpg.services.ItemService.Companion.blueprint
import xyz.devvydont.smprpg.services.ItemService.Companion.generate
import xyz.devvydont.smprpg.util.formatting.ComponentUtils
import xyz.devvydont.smprpg.util.persistence.KeyStore
import java.util.function.Consumer

class DynamicEnchantingScroll(itemService: ItemService, type: CustomItemType) :
    CustomItemBlueprint(itemService, type), IHeaderDescribable, IFooterDescribable, ISellable, IModelOverridden {
    /**
     * Determine what type of item this is.
     */
    override val itemClassification: ItemClassification get() = ItemClassification.ITEM

    /**
     * Given this item stack, how much should it be able to sell for?
     * Keep in mind that the size of the stack needs to considered as well!
     *
     * @param item The item that can be sold.
     * @return The worth of the item.
     */
    override fun getWorth(item: ItemStack): Int {
        return 1000 * item.getAmount()
    }

    override fun updateItemData(itemStack: ItemStack) {
        // Fallback to Smite if enchantment isn't generated
        if (itemStack.getData(DataComponentTypes.STORED_ENCHANTMENTS) == null) itemStack.setData(
            DataComponentTypes.STORED_ENCHANTMENTS,
            ItemEnchantments.itemEnchantments().add(Enchantment.SMITE, 255).build()
        )

        itemStack.setData(
            DataComponentTypes.TOOLTIP_DISPLAY,
            TooltipDisplay.tooltipDisplay().addHiddenComponents(DataComponentTypes.STORED_ENCHANTMENTS).build()
        )

        val enchToUse = itemStack.getData(DataComponentTypes.STORED_ENCHANTMENTS)!!.enchantments().keys.first()
        val customEnch : CustomEnchantment = SMPRPG.getService(EnchantmentService::class.java).getEnchantment(enchToUse)!!
        super.updateItemData(itemStack)
        itemStack.setData(
            DataComponentTypes.CUSTOM_MODEL_DATA,
            CustomModelData.customModelData().addColors(listOf(customEnch.scrollColor, customEnch.scrollBindingColor)))
        itemStack.editMeta(Consumer { meta: ItemMeta? -> meta!!.setMaxStackSize(8) })
    }

    override fun getHeader(itemStack: ItemStack): MutableList<Component?> {
        val enchant = itemStack.getData(DataComponentTypes.STORED_ENCHANTMENTS)!!
            .enchantments().keys.toTypedArray()[0]
        var color = enchant.description().color()
        if (color != null) {
            if (getRarity(itemStack) == ItemRarity.ARTIFICE) {
                val textComp = SMPRPG.getService(EnchantmentService::class.java).getEnchantment(enchant)!!.displayName
                return mutableListOf(
                    ComponentUtils.gradient(
                        PlainTextComponentSerializer.plainText().serialize(textComp),
                        NamedTextColor.DARK_PURPLE,
                        TextColor.color(255, 0, 0)
                    ).decorate(TextDecoration.ITALIC)
                )
            }
            color = if (color == NamedTextColor.WHITE) NamedTextColor.GRAY else enchant.description().color()
        } else {
            color = NamedTextColor.DARK_RED
        }
        return mutableListOf(
            enchant.displayName(0).color(color)
        )
    }

    override fun getRarity(item: ItemStack): ItemRarity {
        val enchant = item.getData(DataComponentTypes.STORED_ENCHANTMENTS)!!.enchantments().keys.toTypedArray()[0] as Enchantment?
        if (enchant != null) {
            val weight = enchant.getWeight()
            if (weight == EnchantmentRarity.COMMON.getWeight()) return ItemRarity.RARE
            else if (weight == EnchantmentRarity.UNCOMMON.getWeight() || weight == EnchantmentRarity.CURSE.getWeight()) return ItemRarity.EPIC
            else if (weight == EnchantmentRarity.RARE.getWeight()) return ItemRarity.LEGENDARY
            else if (weight == EnchantmentRarity.ARTIFICE.getWeight()) return ItemRarity.ARTIFICE
            else if (weight == EnchantmentRarity.BLESSING.getWeight()) return ItemRarity.MYTHIC
            else if (weight == EnchantmentRarity.ARTIFACT.getWeight()) return ItemRarity.SPECIAL
        }
        return defaultRarity
    }

    override fun getDisplayKey(): Key? {
        return IModelOverridden.ofItemType(customItemType)
    }

    override fun getFooter(itemStack: ItemStack): List<Component> {
        val retComps = mutableListOf<Component>()
        val enchant = itemStack.getData(DataComponentTypes.STORED_ENCHANTMENTS)!!.enchantments().keys.toTypedArray()[0]
        val customEnch = SMPRPG.getService(EnchantmentService::class.java).getEnchantment(enchant)
        customEnch!!.level = 1
        val altasKey = Key.key("minecraft:items")
        val comp = when (customEnch.itemTypeTag) {
            ItemTypeTagKeys.ENCHANTABLE_WEAPON -> ComponentUtils.merge(
                ComponentUtils.atlasSprite(altasKey, Key.key("minecraft:item/iron_sword")),
                ComponentUtils.atlasSprite(altasKey, Key.key("minecraft:item/iron_axe")),
                ComponentUtils.atlasSprite(altasKey, Key.key("minecraft:item/iron_spear")),
                ComponentUtils.atlasSprite(altasKey, Key.key("minecraft:item/bow")),
                ComponentUtils.atlasSprite(altasKey, Key.key("minecraft:item/crossbow_standby")),
                ComponentUtils.atlasSprite(altasKey, Key.key("minecraft:item/trident")),
                ComponentUtils.atlasSprite(altasKey, Key.key("minecraft:item/mace")),
                ComponentUtils.atlasSprite(altasKey, Key.key("staffs:item/amethyst_staff")),
                ComponentUtils.atlasSprite(altasKey, Key.key("smprpg:item/tools/iron_knife")),
            )
            ItemTypeTagKeys.ENCHANTABLE_BOW -> ComponentUtils.atlasSprite(altasKey, Key.key("minecraft:item/bow"))
            ItemTypeTagKeys.ENCHANTABLE_MACE -> ComponentUtils.atlasSprite(altasKey, Key.key("minecraft:item/mace"))
            ItemTypeTagKeys.ENCHANTABLE_CROSSBOW -> ComponentUtils.atlasSprite(altasKey, Key.key("minecraft:item/crossbow_standby"))
            ItemTypeTagKeys.ENCHANTABLE_SWEEPING -> ComponentUtils.atlasSprite(altasKey, Key.key("smprpg:item/enchantable_icons/sweeping"))
            ItemTypeTagKeys.ENCHANTABLE_ARMOR -> ComponentUtils.merge(
                ComponentUtils.atlasSprite(altasKey, Key.key("minecraft:item/iron_helmet")),
                ComponentUtils.atlasSprite(altasKey, Key.key("minecraft:item/iron_chestplate")),
                ComponentUtils.atlasSprite(altasKey, Key.key("minecraft:item/iron_leggings")),
                ComponentUtils.atlasSprite(altasKey, Key.key("minecraft:item/iron_boots"))
            )
            ItemTypeTagKeys.ENCHANTABLE_CHEST_ARMOR -> ComponentUtils.atlasSprite(altasKey, Key.key("minecraft:item/iron_chestplate"))
            ItemTypeTagKeys.ENCHANTABLE_DURABILITY -> ItemService.COMMON_REPAIR_CORE_ATLAS_ICON.append(ComponentUtils.create(" Any durable item"))
            ItemTypeTagKeys.ENCHANTABLE_FIRE_ASPECT -> ComponentUtils.merge(
                ComponentUtils.atlasSprite(altasKey, Key.key("minecraft:item/iron_sword")),
                ComponentUtils.atlasSprite(altasKey, Key.key("minecraft:item/iron_axe")),
                ComponentUtils.atlasSprite(altasKey, Key.key("minecraft:item/iron_spear")),
                ComponentUtils.atlasSprite(altasKey, Key.key("minecraft:item/trident")),
                ComponentUtils.atlasSprite(altasKey, Key.key("minecraft:item/mace")),
                ComponentUtils.atlasSprite(altasKey, Key.key("staffs:item/amethyst_staff")),
                ComponentUtils.atlasSprite(altasKey, Key.key("smprpg:item/tools/iron_knife")),
                ComponentUtils.atlasSprite(altasKey, Key.key("minecraft:item/iron_pickaxe")),
                ComponentUtils.atlasSprite(altasKey, Key.key("minecraft:item/iron_hoe")),
                ComponentUtils.atlasSprite(altasKey, Key.key("minecraft:item/iron_shovel")),
                ComponentUtils.atlasSprite(altasKey, Key.key("minecraft:item/shears")),
                ComponentUtils.atlasSprite(altasKey, Key.key("tools:item/iron_hatchet"))
            )
            ItemTypeTagKeys.ENCHANTABLE_FISHING -> ComponentUtils.atlasSprite(altasKey, Key.key("minecraft:item/fishing_rod"))
            ItemTypeTagKeys.ENCHANTABLE_FOOT_ARMOR -> ComponentUtils.atlasSprite(altasKey, Key.key("minecraft:item/iron_boots"))
            ItemTypeTagKeys.ENCHANTABLE_HEAD_ARMOR -> ComponentUtils.atlasSprite(altasKey, Key.key("minecraft:item/iron_helmet"))
            ItemTypeTagKeys.ENCHANTABLE_LEG_ARMOR -> ComponentUtils.atlasSprite(altasKey, Key.key("minecraft:item/iron_leggings"))
            ItemTypeTagKeys.ENCHANTABLE_LUNGE -> ComponentUtils.atlasSprite(altasKey, Key.key("minecraft:item/iron_spear"))
            ItemTypeTagKeys.ENCHANTABLE_MELEE_WEAPON -> ComponentUtils.atlasSprite(altasKey, Key.key("minecraft:item/iron_sword"))
            ItemTypeTagKeys.ENCHANTABLE_MINING -> ComponentUtils.atlasSprite(altasKey, Key.key("minecraft:item/iron_pickaxe"))
            ItemTypeTagKeys.ENCHANTABLE_MINING_LOOT -> ComponentUtils.atlasSprite(altasKey, Key.key("minecraft:item/golden_pickaxe"))
            ItemTypeTagKeys.ENCHANTABLE_SHARP_WEAPON -> ComponentUtils.merge(
                ComponentUtils.atlasSprite(altasKey, Key.key("minecraft:item/iron_sword")),
                ComponentUtils.atlasSprite(altasKey, Key.key("minecraft:item/iron_axe")),
                ComponentUtils.atlasSprite(altasKey, Key.key("minecraft:item/iron_spear")),
                ComponentUtils.atlasSprite(altasKey, Key.key("minecraft:item/trident")),
                ComponentUtils.atlasSprite(altasKey, Key.key("staffs:item/amethyst_staff")),
                ComponentUtils.atlasSprite(altasKey, Key.key("smprpg:item/tools/iron_knife"))
            )
            KeyStore.ENCHANTABLE_TOME -> ComponentUtils.atlasSprite(altasKey, Key.key("tools:item/simple_tome_ui"))
            KeyStore.ENCHANTABLE_APTITUDE -> ComponentUtils.merge(
                ComponentUtils.atlasSprite(altasKey, Key.key("minecraft:item/iron_helmet")),
                ComponentUtils.atlasSprite(altasKey, Key.key("minecraft:item/iron_chestplate")),
                ComponentUtils.atlasSprite(altasKey, Key.key("minecraft:item/iron_leggings")),
                ComponentUtils.atlasSprite(altasKey, Key.key("minecraft:item/iron_boots")),
                ComponentUtils.atlasSprite(altasKey, Key.key("tools:item/simple_tome_ui"))
            )
            else -> ComponentUtils.create("Anything enchantable", NamedTextColor.LIGHT_PURPLE)
        }
        val applicationComp = ComponentUtils.merge(
            ComponentUtils.create("Applies to: ", NamedTextColor.GOLD),
            comp
        )
        return listOf(
            customEnch.description,
            ComponentUtils.create("(Values for level 1 enchantment shown.)", NamedTextColor.DARK_GRAY),
            ComponentUtils.EMPTY,
            applicationComp
        )
    }

    companion object {
        @JvmStatic
        fun getScrollWithEnchantment(enchant: CustomEnchantment): ItemStack {
            val scroll = generate(CustomItemType.ENCHANTING_SCROLL)
            scroll.setData(
                DataComponentTypes.STORED_ENCHANTMENTS,
                ItemEnchantments.itemEnchantments().add(enchant.enchantment, 255).build()
            )
            blueprint(scroll).updateItemData(scroll)
            return scroll
        }
    }
}
