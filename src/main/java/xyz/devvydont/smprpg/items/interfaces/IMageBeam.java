package xyz.devvydont.smprpg.items.interfaces;

import org.bukkit.Particle;

public interface IMageBeam {

    Particle getHitParticle();
    Particle getMissParticle();
    int getParticleDensity();
    int getParticleRange();
    int getManaCost();
}
