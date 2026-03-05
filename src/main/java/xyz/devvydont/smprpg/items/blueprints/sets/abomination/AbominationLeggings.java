package xyz.devvydont.smprpg.items.blueprints.sets.abomination;

import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.entity.slayer.shambling.ShamblingAbominationParent;
import xyz.devvydont.smprpg.events.CustomEntityDamageByEntityEvent;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment;
import xyz.devvydont.smprpg.items.interfaces.ICraftable;
import xyz.devvydont.smprpg.items.interfaces.IFooterDescribable;
import xyz.devvydont.smprpg.services.EntityService;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.builders.ChestplateRecipe;
import xyz.devvydont.smprpg.util.crafting.builders.LeggingsRecipe;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.persistence.KeyStore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AbominationLeggings extends AbominationArmorSet implements IBreakableEquipment, ICraftable {

    public AbominationLeggings(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        List<AttributeEntry> attributes = new ArrayList<>();
        attributes.add(new AdditiveAttributeEntry(AttributeWrapper.DEFENSE, 150));
        attributes.add(new AdditiveAttributeEntry(AttributeWrapper.INTELLIGENCE, 10));
        attributes.add(new AdditiveAttributeEntry(AttributeWrapper.REGENERATION, 25));
        var strMult = getKillStrengthMultBoost(item);
        if (strMult > 0) {
            attributes.add(new ScalarAttributeEntry(AttributeWrapper.STRENGTH, strMult));
        }
        return attributes;
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.LEGS;
    }

    @Override
    public int getMaxDurability() {
        return 1_500;
    }

    @Override
    public NamespacedKey getRecipeKey() {
        return new NamespacedKey(SMPRPG.getPlugin(), getCustomItemType().getKey()+"-recipe");
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        return new LeggingsRecipe(this, itemService.getCustomItem(CustomItemType.REVILED_VISCERA), generate()).build();
    }

    @Override
    public Collection<ItemStack> unlockedBy() {
        return List.of(itemService.getCustomItem(CustomItemType.REVILED_VISCERA));
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.LEGGINGS;
    }

    @EventHandler
    public void __onValidEntityKill(EntityDeathEvent event) {
        var entity = event.getEntity();
        var killer = entity.getKiller();
        if (killer != null) {
            if (entity.getPersistentDataContainer().getOrDefault(KeyStore.SLAYER_SPAWN_TYPE, PersistentDataType.STRING,"").equals(ShamblingAbominationParent.SPAWN_MOB_FLAG)) {
                var item = killer.getInventory().getItem(EquipmentSlot.LEGS);
                int nextMilestone = getNextKillMilestone(item);
                int kills = item.getPersistentDataContainer().getOrDefault(killstoreKey, PersistentDataType.INTEGER, 0);
                item.editPersistentDataContainer(
                        pdc -> pdc.set(killstoreKey,
                                PersistentDataType.INTEGER,
                                kills + 1));
                if ((kills + 1) == nextMilestone) {
                    killer.sendMessage(ComponentUtils.merge(
                            ComponentUtils.create("Your "),
                            item.getData(DataComponentTypes.ITEM_NAME),
                            ComponentUtils.create(" has leveled up to "),
                            ComponentUtils.create("+" + (int) (getKillStrengthMultBoost(item) * 100.0) + "% Strength", NamedTextColor.RED),
                            ComponentUtils.create("!")
                    ));
                }
                ItemService.blueprint(item).updateItemData(item);
            }
        }
    }

    @EventHandler
    public void __onReceiveDamageFromBoss(CustomEntityDamageByEntityEvent event) {
        // Is the attacker a shambling abomination?
        var isBoss = (SMPRPG.getService(EntityService.class).getEntityInstance(event.dealer) instanceof ShamblingAbominationParent);
        if (isBoss) {
            // Is the attacker holding the halberd?
            if (!(event.damaged instanceof LivingEntity living))
                return;

            if (living.getEquipment() == null)
                return;

            if (!isItemOfType(living.getEquipment().getLeggings()))
                return;

            // Is this a direct event?
            if (event.isIndirect())
                return;

            // Reduce damage
            event.multiplyDamage(BOSS_DAMAGE_REDUCTION);
        }
    }
}
