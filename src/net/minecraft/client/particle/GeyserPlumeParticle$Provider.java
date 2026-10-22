package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.GeyserParticleOptions;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class GeyserPlumeParticle$Provider implements ParticleProvider<GeyserParticleOptions> {
   private final SpriteSet sprites;

   public GeyserPlumeParticle$Provider(final SpriteSet sprites) {
      this.sprites = sprites;
   }

   @Nullable
   public Particle createParticle(
      final GeyserParticleOptions options,
      final ClientLevel level,
      final double x,
      final double y,
      final double z,
      final double xAux,
      final double yAux,
      final double zAux,
      final RandomSource random
   ) {
      double randomX = x + (random.nextFloat() - 0.5F) * 0.2F;
      double randomY = y + random.nextFloat();
      double randomZ = z + (random.nextFloat() - 0.5F) * 0.2F;
      return new GeyserPlumeParticle(level, randomX, randomY, randomZ, xAux, yAux, zAux, options, this.sprites);
   }
}
