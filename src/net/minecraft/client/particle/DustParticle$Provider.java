package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class DustParticle$Provider implements ParticleProvider<DustParticleOptions> {
   private final SpriteSet sprites;

   public DustParticle$Provider(final SpriteSet sprites) {
      this.sprites = sprites;
   }

   public Particle createParticle(
      final DustParticleOptions options,
      final ClientLevel level,
      final double x,
      final double y,
      final double z,
      final double xAux,
      final double yAux,
      final double zAux,
      final RandomSource random
   ) {
      return new DustParticle(level, x, y, z, xAux, yAux, zAux, options, this.sprites);
   }
}
