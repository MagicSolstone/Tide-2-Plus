package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class SuspendedParticle$SporeBlossomAirProvider implements ParticleProvider<SimpleParticleType> {
   private final SpriteSet sprite;

   public SuspendedParticle$SporeBlossomAirProvider(final SpriteSet sprite) {
      this.sprite = sprite;
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
      SuspendedParticle particle = new SuspendedParticle$SporeBlossomAirProvider$1(this, level, x, y, z, 0.0, -0.8F, 0.0, this.sprite.get(random));
      particle.lifetime = Mth.randomBetweenInclusive(random, 500, 1000);
      particle.gravity = 0.01F;
      particle.setColor(0.32F, 0.5F, 0.22F);
      return particle;
   }
}
