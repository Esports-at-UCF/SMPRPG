package xyz.devvydont.smprpg.entity.vanilla;

import org.bukkit.entity.Vindicator;
import xyz.devvydont.smprpg.entity.MobType;
import xyz.devvydont.smprpg.entity.base.VanillaEntity;

public class LeveledVindicator extends VanillaEntity<Vindicator> {

    public LeveledVindicator(Vindicator entity) {
        super(entity);
    }

    @Override
    public void setup() {
        mobTypes.add(MobType.HUMANOID);
        mobTypes.add(MobType.ILLAGER);

        super.setup();
    }
}
