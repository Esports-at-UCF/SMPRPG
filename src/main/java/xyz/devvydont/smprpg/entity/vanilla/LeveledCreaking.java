package xyz.devvydont.smprpg.entity.vanilla;

import org.bukkit.entity.Bee;
import xyz.devvydont.smprpg.entity.MobType;
import xyz.devvydont.smprpg.entity.base.VanillaEntity;

public class LeveledCreaking extends VanillaEntity<Bee> {

    public LeveledCreaking(Bee entity) {
        super(entity);
    }

    @Override
    public void setup() {
        mobTypes.add(MobType.RARE);
        mobTypes.add(MobType.PLANT);

        super.setup();
    }
}
