package xyz.devvydont.smprpg.entity.fishing;

import org.bukkit.Bukkit;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Dolphin;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.jetbrains.annotations.Nullable;
import xyz.devvydont.smprpg.attribute.AttributeWrapper;
import xyz.devvydont.smprpg.entity.CustomEntityType;
import xyz.devvydont.smprpg.entity.MobType;
import xyz.devvydont.smprpg.entity.fishing.goals.SharkAttackGoal;
import xyz.devvydont.smprpg.items.CustomItemType;
import xyz.devvydont.smprpg.services.ItemService;
import xyz.devvydont.smprpg.util.items.ChancedItemDrop;
import xyz.devvydont.smprpg.util.items.LootDrop;

import java.util.Collection;
import java.util.List;

public class Shark extends SeaCreature<Dolphin> implements Listener {

    public static final int RATING_REQUIREMENT = 120;

    public Shark(LivingEntity entity, CustomEntityType entityType) {
        super((Dolphin) entity, entityType);
    }

    @Override
    public void setup() {
        mobTypes.add(MobType.SEA_CREATURE);
        mobTypes.add(MobType.AQUATIC);
        mobTypes.add(MobType.ANIMAL);

        super.setup();
        var location = _entity.getLocation();
        var armorStand = location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND, CreatureSpawnEvent.SpawnReason.CUSTOM);
        armorStand.addPassenger(_entity);
        armorStand.setInvisible(true);

        var mobGoals = Bukkit.getMobGoals();
        mobGoals.removeAllGoals(_entity);
        mobGoals.addGoal(_entity, 3, new SharkAttackGoal(_entity));
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

    @EventHandler
    public void onSharkDrownOrCram(EntityDamageEvent event) {
        if (event.getEntity() == _entity) {
            var dmgType = event.getDamageSource().getDamageType();
            if (dmgType == DamageType.IN_WALL || dmgType == DamageType.DROWN) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onSharkDeath(EntityDeathEvent event) {
        if (event.getEntity() == _entity) {
            _entity.getVehicle().remove();
        }
    }
}
