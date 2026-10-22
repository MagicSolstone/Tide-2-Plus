package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class SquidInkParticle$Provider implements ParticleProvider<SimpleParticleType> {
   private final SpriteSet sprites;

   public SquidInkParticle$Provider(final SpriteSet sprites) {
      this.sprites = sprites;
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
      return new SquidInkParticle(level, x, y, z, xAux, yAux, zAux, -16777216, this.sprites);
   }
}
