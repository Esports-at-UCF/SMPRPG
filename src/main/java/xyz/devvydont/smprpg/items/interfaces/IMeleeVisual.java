package xyz.devvydont.smprpg.items.interfaces;

import org.bukkit.Particle;

public interface IMeleeVisual {

    Particle getHitParticle();
    Particle getMissParticle();
    int getParticleDensity();
    int getParticleRange();
}
