package xyz.devvydont.smprpg.items.blueprints.sets.abomination;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.entity.slayer.shambling.ShamblingAbominationParent;
import xyz.devvydont.smprpg.events.CustomEntityDamageByEntityEvent;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomAttributeItem;
import xyz.devvydont.smprpg.items.blueprints.sets.forsaken.ForsakenArmorSet;
import xyz.devvydont.smprpg.items.interfaces.IBreakableEquipment;
import xyz.devvydont.smprpg.items.interfaces.ICraftable;
import xyz.devvydont.smprpg.items.interfaces.IHeaderDescribable;
import xyz.devvydont.smprpg.services.EntityService;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.formatting.Symbols;
import xyz.devvydont.smprpg.util.items.AbilityUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AbominableHalberd extends CustomAttributeItem implements Listener, IHeaderDescribable, ICraftable, IBreakableEquipment {

    public static final double DAMAGE_MULT = 8.0;
    public static final double HEAL_AMOUNT = 50.0;
    public static final double BOSS_DAMAGE_REDUCTION = 10.0;

    public AbominableHalberd(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public List<Component> getHeader(ItemStack itemStack) {
        List<Component> components = new ArrayList<>();
        components.add(AbilityUtil.getAbilityComponent("Divine Executioner (Passive)"));
        components.add(ComponentUtils.create("Attacks deal ").append(ComponentUtils.create((int) DAMAGE_MULT + "x", NamedTextColor.GREEN)).append(ComponentUtils.create(" damage")));
        components.add(ComponentUtils.create("against ").append(ComponentUtils.create("Shambling Abominations", NamedTextColor.DARK_PURPLE, TextDecoration.BOLD)).append(ComponentUtils.create(".")));
        components.add(ComponentUtils.create("Attacks deal ").append(ComponentUtils.create("25%", NamedTextColor.RED)).append(ComponentUtils.create(" damage to any other mobs.")));
        components.add(ComponentUtils.create("Attacks heal ").append(ComponentUtils.create("+" + (int) HEAL_AMOUNT, NamedTextColor.RED)).append(ComponentUtils.create(Symbols.HEART, NamedTextColor.RED)).append(ComponentUtils.create(" on critical hits.")));
        components.add(ComponentUtils.create("Receive ").append(ComponentUtils.create((int) BOSS_DAMAGE_REDUCTION + "%", NamedTextColor.GREEN)).append(ComponentUtils.create(" less damage from")).append(ComponentUtils.create(" Shambling Abominations", NamedTextColor.DARK_PURPLE, TextDecoration.BOLD)).append(ComponentUtils.create(".")));

        return components;
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.STRENGTH, 450),
                new MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, -.6),
                new AdditiveAttributeEntry(AttributeWrapper.CRITICAL_DAMAGE, 90)
        );
    }

    @Override
    public int getPowerRating() {
        return ForsakenArmorSet.POWER;
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.SWORD;
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.MAINHAND;
    }


    @Override
    public NamespacedKey getRecipeKey() {
        return new NamespacedKey(SMPRPG.getPlugin(), getCustomItemType().getKey() + "-recipe");
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(getRecipeKey(), generate());
        recipe.shape("fff", "fmf", "fff");
        recipe.setCategory(CraftingBookCategory.EQUIPMENT);
        recipe.setIngredient('m', ItemService.generate(CustomItemType.ABOMINABLE_MACHETE));
        recipe.setIngredient('f', ItemService.generate(CustomItemType.NECROTIC_FLESH_SINGULARITY));
        return recipe;
    }

    @Override
    public Collection<ItemStack> unlockedBy() {
        return List.of(
                ItemService.generate(Material.ROTTEN_FLESH)
        );
    }

    @Override
    public int getMaxDurability() {
        return 50_000;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void __onAttackWithHalberd(CustomEntityDamageByEntityEvent event) {

        // Did the attacker use the halberd?
        if (!(event.dealer instanceof LivingEntity living))
            return;

        if (living.getEquipment() == null)
            return;

        if (!isItemOfType(living.getEquipment().getItemInMainHand()))
            return;

        // Is this a direct event?
        if (event.isIndirect())
            return;

        // Is the attacked mob a shambling abomination?
        var isBoss = (SMPRPG.getService(EntityService.class).getEntityInstance(event.damaged) instanceof ShamblingAbominationParent);
        if (!isBoss) {
            // If it isn't, we quarter our damage instead of sextuple it.
            // This makes it not viable outside of slayer usage.
            event.multiplyDamage(0.25);
        } else {
            event.multiplyDamage(DAMAGE_MULT);
        }

        // We can safely heal the player now
        if (event.isCritical())
            living.heal(HEAL_AMOUNT);
    }

    @EventHandler
    public void __onReceiveDamageFromBoss(CustomEntityDamageByEntityEvent event) {
        // Is the attacker a shambling horror?
        var isBoss = (SMPRPG.getService(EntityService.class).getEntityInstance(event.dealer) instanceof ShamblingAbominationParent);
        if (isBoss) {
            // Is the attacker holding the halberd?
            if (!(event.damaged instanceof LivingEntity living))
                return;

            if (living.getEquipment() == null)
                return;

            if (!isItemOfType(living.getEquipment().getItemInMainHand()))
                return;

            // Is this a direct event?
            if (event.isIndirect())
                return;

            // Reduce damage
            event.multiplyDamage(1.0 - (BOSS_DAMAGE_REDUCTION / 100.0));
        }
    }

}
