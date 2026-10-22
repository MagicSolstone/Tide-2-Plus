package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.GeyserBaseParticleOptions;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class GeyserBaseParticle$Provider implements ParticleProvider<GeyserBaseParticleOptions> {
   private final SpriteSet sprites;

   public GeyserBaseParticle$Provider(final SpriteSet sprites) {
      this.sprites = sprites;
   }

   public Particle createParticle(
      final GeyserBaseParticleOptions options,
      final ClientLevel level,
      final double x,
      final double y,
      final double z,
      final double xAux,
      final double yAux,
      final double zAux,
      final RandomSource random
   ) {
      double randomX = x + (random.nextFloat() - 0.5F) * 0.5F;
      double randomY = y + (random.nextFloat() - 0.5F) * 0.5F + 0.2F;
      double randomZ = z + (random.nextFloat() - 0.5F) * 0.5F;
      return new GeyserBaseParticle(level, randomX, randomY, randomZ, xAux, yAux, zAux, options.waterBlocks(), options.burstImpulseBase(), this.sprites);
   }
}
