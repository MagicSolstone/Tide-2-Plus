package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class PlayerCloudParticle$SneezeProvider implements ParticleProvider<SimpleParticleType> {
   private final SpriteSet sprites;

   public PlayerCloudParticle$SneezeProvider(final SpriteSet sprites) {
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
      PlayerCloudParticle particle = new PlayerCloudParticle(level, x, y, z, xAux, yAux, zAux, this.sprites);
      particle.setColor(0.22F, 1.0F, 0.53F);
      particle.setAlpha(0.4F);
      return particle;
   }
}
