package xyz.devvydont.smprpg.items.blueprints.resources.slayer;

import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.consumable.ConsumeEffect;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jspecify.annotations.NonNull;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.items.ItemClassification;
import xyz.devvydont.smprpg.items.base.CustomCompressableBlueprint;
import xyz.devvydont.smprpg.items.interfaces.IEdible;
import xyz.devvydont.smprpg.items.interfaces.IConsumable;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.crafting.CompressionRecipeMember;
import xyz.devvydont.smprpg.util.crafting.MaterialWrapper;
import xyz.devvydont.smprpg.util.time.TickTime;

import java.util.ArrayList;
import java.util.List;

public class NecroticFleshFamilyBlueprint extends CustomCompressableBlueprint implements IEdible, IConsumable {

    public static final List<CompressionRecipeMember> COMPRESSION_FLOW = List.of(
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.NECROTIC_FLESH)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.PREMIUM_NECROTIC_FLESH)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.ENCHANTED_NECROTIC_FLESH)),
            new CompressionRecipeMember(new MaterialWrapper(CustomItemType.NECROTIC_FLESH_SINGULARITY))
    );

    public NecroticFleshFamilyBlueprint(ItemService itemService, CustomItemType type) {
        super(itemService, type);
    }

    @Override
    public List<CompressionRecipeMember> getCompressionFlow() {
        return COMPRESSION_FLOW;
    }

    @Override
    public int getNutrition(ItemStack item) {
        return 1;
    }

    @Override
    public float getSaturation(ItemStack item) {
        return 1;
    }

    @Override
    public boolean canAlwaysEat(ItemStack item) {
        return true;
    }

    @Override
    public @NonNull Consumable getConsumableComponent(ItemStack item) {

        var effects = new ArrayList<ConsumeEffect>();

        if (getCustomItemType().equals(CustomItemType.NECROTIC_FLESH_SINGULARITY))
            effects.add(ConsumeEffect.applyStatusEffects(List.of(
                    new PotionEffect(PotionEffectType.WITHER, (int) TickTime.minutes(5), 9),
                    new PotionEffect(PotionEffectType.BLINDNESS, (int) TickTime.minutes(5), 1),
                    new PotionEffect(PotionEffectType.DARKNESS, (int) TickTime.minutes(5), 1)
            ), 1f));

        if (getCustomItemType().equals(CustomItemType.ENCHANTED_NECROTIC_FLESH))
            effects.add(ConsumeEffect.applyStatusEffects(List.of(
                    new PotionEffect(PotionEffectType.WITHER, (int) TickTime.minutes(5), 4),
                    new PotionEffect(PotionEffectType.BLINDNESS, (int) TickTime.minutes(5), 0)
            ), 1f));

        if (getCustomItemType().equals(CustomItemType.PREMIUM_NECROTIC_FLESH))
            effects.add(ConsumeEffect.applyStatusEffects(List.of(
                    new PotionEffect(PotionEffectType.WITHER, (int) TickTime.minutes(5), 2)
            ), 1f));

        return Consumable.consumable()
                .consumeSeconds(10)
                .addEffects(effects)
                .build();
    }

    @Override
    public ItemClassification getItemClassification() {
        return ItemClassification.CONSUMABLE;
    }

}
