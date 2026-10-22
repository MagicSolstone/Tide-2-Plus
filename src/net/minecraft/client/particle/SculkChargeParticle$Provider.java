package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SculkChargeParticleOptions;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public record SculkChargeParticle$Provider(SpriteSet sprite) implements ParticleProvider<SculkChargeParticleOptions> {
   public Particle createParticle(
      final SculkChargeParticleOptions options,
      final ClientLevel level,
      final double x,
      final double y,
      final double z,
      final double xAux,
      final double yAux,
      final double zAux,
      final RandomSource random
   ) {
      SculkChargeParticle particle = new SculkChargeParticle(level, x, y, z, xAux, yAux, zAux, this.sprite);
      particle.setAlpha(1.0F);
      particle.setParticleSpeed(xAux, yAux, zAux);
      particle.oRoll = options.roll();
      particle.roll = options.roll();
      particle.setLifetime(random.nextInt(12) + 8);
      return particle;
   }
}
