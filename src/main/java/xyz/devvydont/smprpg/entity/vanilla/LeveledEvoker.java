package xyz.devvydont.smprpg.entity.vanilla;

import org.bukkit.entity.Evoker;
import xyz.devvydont.smprpg.entity.MobType;
import xyz.devvydont.smprpg.entity.base.VanillaEntity;

public class LeveledEvoker extends VanillaEntity<Evoker> {

    public LeveledEvoker(Evoker entity) {
        super(entity);
    }

    @Override
    public void setup() {
        mobTypes.add(MobType.HUMANOID);
        mobTypes.add(MobType.ILLAGER);

        super.setup();
    }
}
