package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class SnowflakeParticle$Provider implements ParticleProvider<SimpleParticleType> {
   private final SpriteSet sprites;

   public SnowflakeParticle$Provider(final SpriteSet sprites) {
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
      SnowflakeParticle snowflakeParticle = new SnowflakeParticle(level, x, y, z, xAux, yAux, zAux, this.sprites);
      snowflakeParticle.setColor(0.923F, 0.964F, 0.999F);
      return snowflakeParticle;
   }
}
