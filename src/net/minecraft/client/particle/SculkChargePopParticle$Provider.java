package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public record SculkChargePopParticle$Provider(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {
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
      SculkChargePopParticle particle = new SculkChargePopParticle(level, x, y, z, xAux, yAux, zAux, this.sprite);
      particle.setAlpha(1.0F);
      particle.setParticleSpeed(xAux, yAux, zAux);
      particle.setLifetime(random.nextInt(4) + 6);
      return particle;
   }
}
