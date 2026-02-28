package xyz.devvydont.smprpg.entity.fishing;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.MobGoals;
import com.destroystokyo.paper.entity.ai.VanillaGoal;
import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Dolphin;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.entity.CustomEntityType;
import xyz.devvydont.smprpg.entity.fishing.goals.SharkAttackGoal;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.items.ChancedItemDrop;
import xyz.devvydont.smprpg.util.items.LootDrop;
import xyz.devvydont.smprpg.util.items.QuantityLootDrop;

import java.util.Collection;
import java.util.List;

public class Shark extends SeaCreature<LivingEntity> implements Listener {

    public static final int RATING_REQUIREMENT = 120;

    public Shark(LivingEntity entity, CustomEntityType entityType) {
        super(entity, entityType);
    }

    @Override
    public void updateAttributes() {
        super.updateAttributes();
        updateBaseAttribute(AttributeWrapper.SCALE, 5);
    }

    @Override
    public @Nullable Collection<LootDrop> getItemDrops() {
        return List.of(
            new ChancedItemDrop(ItemService.generate(CustomItemType.SHARK_FIN), 1, this),
            new ChancedItemDrop(ItemService.generate(CustomItemType.PREDATOR_TOOTH), 50, this)
        );
    }
}
