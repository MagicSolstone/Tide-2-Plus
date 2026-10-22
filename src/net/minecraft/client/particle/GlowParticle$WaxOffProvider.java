package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class GlowParticle$WaxOffProvider implements ParticleProvider<SimpleParticleType> {
   private static final double SPEED_FACTOR = 0.01;
   private final SpriteSet sprite;

   public GlowParticle$WaxOffProvider(final SpriteSet sprite) {
      this.sprite = sprite;
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
      GlowParticle glowParticle = new GlowParticle(level, x, y, z, 0.0, 0.0, 0.0, this.sprite);
      glowParticle.setColor(1.0F, 0.9F, 1.0F);
      glowParticle.setParticleSpeed(xAux * 0.01 / 2.0, yAux * 0.01, zAux * 0.01 / 2.0);
      int minLifespan = 10;
      int maxLifespan = 40;
      glowParticle.setLifetime(random.nextInt(30) + 10);
      return glowParticle;
   }
}
