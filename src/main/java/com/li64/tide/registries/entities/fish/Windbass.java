package com.li64.tide.registries.entities.fish;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class Windbass extends TideFishEntity {
    public Windbass(EntityType<? extends TideFishEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (random.nextFloat() < 0.8f) {
            this.level().addParticle(ParticleTypes.WHITE_SMOKE,
                    this.getRandomX(0.6), this.getRandomY(), this.getRandomZ(0.6),
                    0.0, 0.0, 0.0);
        }
    }
}