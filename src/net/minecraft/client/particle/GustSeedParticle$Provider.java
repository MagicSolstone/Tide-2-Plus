package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class GustSeedParticle$Provider implements ParticleProvider<SimpleParticleType> {
   private final double scale;
   private final int lifetime;
   private final int tickDelayInBetween;

   public GustSeedParticle$Provider(final double scale, final int lifetime, final int tickDelayInBetween) {
      this.scale = scale;
      this.lifetime = lifetime;
      this.tickDelayInBetween = tickDelayInBetween;
   }

   public Particle createParticle(
      final SimpleParticleType options,
      final ClientLevel level,
      final double x,
      final double y,
      final double z,
      final double xAux,
      final double yAux,
      final double zAux,
      final RandomSource random
   ) {
      return new GustSeedParticle(level, x, y, z, this.scale, this.lifetime, this.tickDelayInBetween);
   }
}
