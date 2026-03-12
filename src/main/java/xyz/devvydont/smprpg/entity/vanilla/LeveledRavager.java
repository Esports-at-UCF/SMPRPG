package xyz.devvydont.smprpg.entity.vanilla;

import org.bukkit.entity.Ravager;
import xyz.devvydont.smprpg.entity.MobType;
import xyz.devvydont.smprpg.entity.base.VanillaEntity;

public class LeveledRavager extends VanillaEntity<Ravager> {

    public LeveledRavager(Ravager entity) {
        super(entity);
    }

    @Override
    public void setup() {
        mobTypes.add(MobType.RARE);
        mobTypes.add(MobType.ILLAGER);
        mobTypes.add(MobType.ANIMAL);

        super.setup();
    }
}
