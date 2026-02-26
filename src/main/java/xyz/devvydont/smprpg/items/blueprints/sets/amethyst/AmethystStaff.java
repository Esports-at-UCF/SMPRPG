package xyz.devvydont.smprpg.items.blueprints.sets.amethyst;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.AttackRange;
import io.papermc.paper.datacomponent.item.PiercingWeapon;
import io.papermc.paper.datacomponent.item.SwingAnimation;
import io.papermc.paper.datacomponent.item.Weapon;
import io.papermc.paper.registry.keys.SoundEventKeys;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import xyz.devvydont.smprpg.SMPRPG;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.attribute.AdditiveAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.AttributeEntry;
import xyz.devvydont.smprpg.items.attribute.MultiplicativeAttributeEntry;
import xyz.devvydont.smprpg.items.attribute.ScalarAttributeEntry;
import xyz.devvydont.smprpg.items.base.CustomAttributeItem;
import xyz.devvydont.smprpg.items.blueprints.sets.inferno.InfernoArmorSet;
import xyz.devvydont.smprpg.items.blueprints.vanilla.ItemSword;
import xyz.devvydont.smprpg.items.interfaces.*;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.items.ToolGlobals;

import javax.xml.crypto.Data;
import java.util.Collection;
import java.util.List;

public class AmethystStaff extends CustomAttributeItem implements IBreakableEquipment, ICantCrit, IIntelligenceScaled, IMeleeVisual, ICraftable {

    public AmethystStaff(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public Collection<AttributeEntry> getAttributeModifiers(ItemStack item) {
        return List.of(
                new AdditiveAttributeEntry(AttributeWrapper.STRENGTH, 15),
                new MultiplicativeAttributeEntry(AttributeWrapper.ATTACK_SPEED, -.5),
                new AdditiveAttributeEntry(AttributeWrapper.INTELLIGENCE, 25)
        );
    }

    @Override
    public int getPowerRating() {
        return 12;
    }

    @Override
    public NamespacedKey getRecipeKey() {
        return new NamespacedKey(SMPRPG.getPlugin(), getCustomItemType().getKey() + "-recipe");
    }

    @Override
    public CraftingRecipe getCustomRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(getRecipeKey(), generate());
        recipe.shape(
                " as",
                " ss",
                "s  ");
        recipe.setIngredient('s', Material.STICK);
        recipe.setIngredient('a', ItemService.generate(CustomItemType.ENCHANTED_AMETHYST));
        recipe.setCategory(CraftingBookCategory.EQUIPMENT);
        return recipe;
    }

    @Override
    public Collection<ItemStack> unlockedBy() {
        return List.of(SMPRPG.getService(ItemService.class).getCustomItem(Material.AMETHYST_BLOCK));
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
        return ToolGlobals.IRON_TOOL_DURABILITY;
    }

    @Override
    public double getIntelligenceScaleFactor() {
        return 0.15;
    }

    @Override
    public int getManaCost() {
        return 10;
    }

    @Override
    public Particle getHitParticle() {
        return Particle.END_ROD;
    }

    @Override
    public Particle getMissParticle() {
        return Particle.CRIT;
    }

    @Override
    public int getParticleDensity() {
        return 20;
    }

    @Override
    public int getParticleRange() {
        return 10;
    }

    @Override
    public void updateItemData(ItemStack itemStack) {
        super.updateItemData(itemStack);
        itemStack.setData(DataComponentTypes.ATTACK_RANGE, AttackRange.attackRange()
                .hitboxMargin(0.15f)
                .maxReach(10.0f)
                .maxCreativeReach(10.0f)
                .build());
        itemStack.setData(DataComponentTypes.WEAPON, Weapon.weapon().build());
        itemStack.setData(DataComponentTypes.SWING_ANIMATION, SwingAnimation.swingAnimation()
                .type(SwingAnimation.Animation.STAB)
                .duration(10)
                .build());
        itemStack.setData(DataComponentTypes.PIERCING_WEAPON, PiercingWeapon.piercingWeapon()
                .dealsKnockback(false)
                .dismounts(false)
                .sound(SoundEventKeys.BLOCK_AMETHYST_BLOCK_RESONATE)
                .hitSound(SoundEventKeys.BLOCK_TRIAL_SPAWNER_EJECT_ITEM)
                .build());

    }
}
