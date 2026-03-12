package xyz.devvydont.smprpg.entity.vanilla;

import org.bukkit.entity.Illusioner;
import xyz.devvydont.smprpg.entity.MobType;
import xyz.devvydont.smprpg.entity.base.VanillaEntity;

public class LeveledIllusioner extends VanillaEntity<Illusioner> {

    public LeveledIllusioner(Illusioner entity) {
        super(entity);
    }

    @Override
    public void setup() {
        mobTypes.add(MobType.HUMANOID);
        mobTypes.add(MobType.ILLAGER);

        super.setup();
    }
}
