package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class SuspendedParticle$CrimsonSporeProvider implements ParticleProvider<SimpleParticleType> {
   private final SpriteSet sprite;

   public SuspendedParticle$CrimsonSporeProvider(final SpriteSet sprite) {
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
      double xa = random.nextGaussian() * 1.0E-6F;
      double ya = random.nextGaussian() * 1.0E-4F;
      double za = random.nextGaussian() * 1.0E-6F;
      SuspendedParticle particle = new SuspendedParticle(level, x, y, z, xa, ya, za, this.sprite.get(random));
      particle.setColor(0.9F, 0.4F, 0.5F);
      return particle;
   }
}
