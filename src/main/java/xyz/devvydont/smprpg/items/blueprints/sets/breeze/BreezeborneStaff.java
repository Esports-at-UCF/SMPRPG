package xyz.devvydont.smprpg.items.blueprints.sets.breeze;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.AttackRange;
import io.papermc.paper.datacomponent.item.PiercingWeapon;
import io.papermc.paper.datacomponent.item.SwingAnimation;
import io.papermc.paper.datacomponent.item.Weapon;
import io.papermc.paper.event.player.PlayerArmSwingEvent;
import io.papermc.paper.registry.keys.SoundEventKeys;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.WindCharge;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.ability.Ability;
import xyz.devvydont.smprpg.ability.AbilityActivationMethod;
import xyz.devvydont.smprpg.ability.AbilityCost;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.events.CustomEntityDamageByEntityEvent;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomAttributeItem;
import xyz.devvydont.smprpg.items.interfaces.*;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.formatting.ComponentUtils;
import xyz.devvydont.smprpg.util.formatting.Symbols;
import xyz.devvydont.smprpg.util.items.AbilityUtil;
import xyz.devvydont.smprpg.util.items.ToolGlobals;
import xyz.devvydont.smprpg.util.time.TickTime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BreezeborneStaff extends CustomAttributeItem implements IBreakableEquipment, ICantCrit, IIntelligenceScaled, IMeleeVisual, ICraftable,
        IAbilityCaster, IModelOverridden, IHeaderDescribable, Listener {

    public BreezeborneStaff(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    private boolean windchargeCooldown = false;
    private double DAMAGE_MULT = 3.0;

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.STRENGTH, 180),
                new MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, -.5),
                new AdditiveAttributeEntry(AttributeWrapper.INTELLIGENCE, 160)
        );
    }

    @Override
    public int getPowerRating() {
        return 30;
    }

    @Override
    public List<Component> getHeader(ItemStack itemStack) {
        List<Component> components = new ArrayList<>();
        components.add(AbilityUtil.getAbilityComponent("Air Shot (Passive)"));
        components.add(ComponentUtils.create("Attacks deal ").append(ComponentUtils.create((int) DAMAGE_MULT + "x", NamedTextColor.GREEN)).append(ComponentUtils.create(" damage")));
        components.add(ComponentUtils.create("against mobs that are not grounded."));
        components.add(ComponentUtils.EMPTY);
        components.add(AbilityUtil.getAbilityComponent("Wind-Attuned (Passive)"));
        components.add(ComponentUtils.create("Your mage beam attack will ocassionally launch ").append(ComponentUtils.create("wind charges", NamedTextColor.AQUA).append(ComponentUtils.create("."))));

        return components;
    }

    @Override
    public NamespacedKey getRecipeKey() {
        return new NamespacedKey(SMPRPG.getPlugin(), getCustomItemType().getKey() + "-recipe");
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(getRecipeKey(), generate());
        recipe.shape(
                " hb",
                " bb",
                "b  ");
        recipe.setIngredient('h', ItemService.generate(Material.HEAVY_CORE));
        recipe.setIngredient('b', ItemService.generate(Material.BREEZE_ROD));
        recipe.setCategory(CraftingBookCategory.EQUIPMENT);
        return recipe;
    }

    @Override
    public Collection<ItemStack> unlockedBy() {
        return List.of(SMPRPG.getService(ItemService.class).getCustomItem(Material.BREEZE_ROD));
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.STAFF;
    }

    @Override
    public EquipmentSlotGroup getActiveSlot() {
        return EquipmentSlotGroup.MAINHAND;
    }

    @Override
    public int getMaxDurability() {
        return 10_000;
    }

    @Override
    public double getIntelligenceScaleFactor() {
        return 0.25;
    }

    @Override
    public int getManaCost() {
        return 30;
    }

    @Override
    public Particle getHitParticle() {
        return Particle.END_ROD;
    }

    @Override
    public Particle getMissParticle() {
        return Particle.ENCHANTED_HIT;
    }

    @Override
    public int getParticleDensity() {
        return 26;
    }

    @Override
    public int getParticleRange() {
        return 13;
    }

    @Override
    public void updateItemData(ItemStack itemStack) {
        super.updateItemData(itemStack);
        itemStack.setData(DataComponentTypes.ATTACK_RANGE, AttackRange.attackRange()
                .hitboxMargin(0.2f)
                .maxReach(13.0f)
                .maxCreativeReach(13.0f)
                .build());
        itemStack.setData(DataComponentTypes.WEAPON, Weapon.weapon().build());
        itemStack.setData(DataComponentTypes.SWING_ANIMATION, SwingAnimation.swingAnimation()
                .type(SwingAnimation.Animation.STAB)
                .duration(10)
                .build());
        itemStack.setData(DataComponentTypes.PIERCING_WEAPON, PiercingWeapon.piercingWeapon()
                .dealsKnockback(false)
                .dismounts(false)
                .sound(SoundEventKeys.ENTITY_BLAZE_SHOOT)
                .hitSound(SoundEventKeys.ENTITY_EXPERIENCE_ORB_PICKUP)
                .build());

    }

    @Override
    public Collection<IAbilityCaster.AbilityEntry> getAbilities(ItemStack item) {
        return List.of(
                new IAbilityCaster.AbilityEntry(
                        Ability.WIND_STORM,
                        AbilityActivationMethod.RIGHT_CLICK,
                        AbilityCost.of(AbilityCost.Resource.MANA, 200)
                )
        );
    }

    @Override
    public long getCooldown(ItemStack item) {
        return TickTime.seconds(3);
    }

    @EventHandler
    public void __onPlayerLeftClick(PlayerArmSwingEvent event) {
        var player = event.getPlayer();

        if (!isItemOfType(player.getEquipment().getItemInMainHand()))
            return;

        if (!windchargeCooldown) {
            var location = player.getLocation();
            player.getWorld().playSound(location, Sound.ENTITY_BREEZE_DEATH, 1f, 0.5f);
            player.launchProjectile(
                    WindCharge.class,
                    location.getDirection().normalize().multiply(2)
            );
            windchargeCooldown = true;
            Bukkit.getScheduler().runTaskLater(SMPRPG.getPlugin(), () -> this.windchargeCooldown = false, TickTime.HALF_SECOND);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void __onAirshotHit(CustomEntityDamageByEntityEvent event) {

        // Is the attacked target a living entity?
        if (!(event.damaged instanceof LivingEntity attacked))
            return;

        // Did the attacker use the cutlass?
        if (!(event.dealer instanceof LivingEntity living))
            return;

        if (living.getEquipment() == null)
            return;

        if (!isItemOfType(living.getEquipment().getItemInMainHand()))
            return;

        // Is this a direct event?
        if (event.isIndirect())
            return;

        // Increase our damage if they are in the air.
        if (!attacked.isOnGround()) {
            event.multiplyDamage(DAMAGE_MULT);
            attacked.getWorld().playSound(attacked.getLocation(), Sound.ENTITY_BREEZE_CHARGE, .5f, 2.0f);
            attacked.getWorld().spawnParticle(Particle.END_ROD, attacked.getEyeLocation(), 5);
        }
    }

    @Override
    public Key getDisplayKey() {
        return new NamespacedKey(SMPRPG.getPlugin(), _type.getKey());
    }
}
