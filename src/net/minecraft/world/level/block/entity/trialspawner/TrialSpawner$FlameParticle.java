package net.minecraft.world.level.block.entity.trialspawner;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;

public enum TrialSpawner$FlameParticle {
   NORMAL(ParticleTypes.FLAME),
   OMINOUS(ParticleTypes.SOUL_FIRE_FLAME);

   public final SimpleParticleType particleType;

   private TrialSpawner$FlameParticle(final SimpleParticleType particleType) {
      this.particleType = particleType;
   }

   public static TrialSpawner$FlameParticle decode(final int data) {
      TrialSpawner$FlameParticle[] values = values();
      return data <= values.length && data >= 0 ? values[data] : NORMAL;
   }

   public int encode() {
      return this.ordinal();
   }
}
