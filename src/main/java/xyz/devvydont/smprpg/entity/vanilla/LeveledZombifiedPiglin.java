package xyz.devvydont.smprpg.entity.vanilla;

import org.bukkit.entity.Entity;
import xyz.devvydont.smprpg.entity.MobType;
import xyz.devvydont.smprpg.entity.base.VanillaEntity;

public class LeveledZombifiedPiglin extends VanillaEntity<Entity> {

    public LeveledZombifiedPiglin(Entity entity) {
        super(entity);
    }

    @Override
    public void setup() {
        mobTypes.add(MobType.NETHER);
        mobTypes.add(MobType.UNDEAD);
        mobTypes.add(MobType.ANIMAL);
        mobTypes.add(MobType.HUMANOID);

        super.setup();
    }
}
