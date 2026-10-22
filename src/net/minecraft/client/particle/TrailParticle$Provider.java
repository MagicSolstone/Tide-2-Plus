package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.TrailParticleOption;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class TrailParticle$Provider implements ParticleProvider<TrailParticleOption> {
   private final SpriteSet sprite;

   public TrailParticle$Provider(final SpriteSet sprite) {
      this.sprite = sprite;
   }

   public Particle createParticle(
      final TrailParticleOption options,
      final ClientLevel level,
      final double x,
      final double y,
      final double z,
      final double xAux,
      final double yAux,
      final double zAux,
      final RandomSource random
   ) {
      TrailParticle particle = new TrailParticle(level, x, y, z, xAux, yAux, zAux, options.target(), options.color(), this.sprite.get(random));
      particle.setLifetime(options.duration());
      return particle;
   }
}
