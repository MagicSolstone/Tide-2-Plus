package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class DustColorTransitionParticle$Provider implements ParticleProvider<DustColorTransitionOptions> {
   private final SpriteSet sprites;

   public DustColorTransitionParticle$Provider(final SpriteSet sprites) {
      this.sprites = sprites;
   }

   public Particle createParticle(
      final DustColorTransitionOptions options,
      final ClientLevel level,
      final double x,
      final double y,
      final double z,
      final double xAux,
      final double yAux,
      final double zAux,
      final RandomSource random
   ) {
      return new DustColorTransitionParticle(level, x, y, z, xAux, yAux, zAux, options, this.sprites);
   }
}
