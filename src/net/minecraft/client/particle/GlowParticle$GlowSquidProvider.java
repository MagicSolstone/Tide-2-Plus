package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class GlowParticle$GlowSquidProvider implements ParticleProvider<SimpleParticleType> {
   private final SpriteSet sprite;

   public GlowParticle$GlowSquidProvider(final SpriteSet sprite) {
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
      GlowParticle glowParticle = new GlowParticle(level, x, y, z, 0.5 - random.nextDouble(), yAux, 0.5 - random.nextDouble(), this.sprite);
      if (random.nextBoolean()) {
         glowParticle.setColor(0.6F, 1.0F, 0.8F);
      } else {
         glowParticle.setColor(0.08F, 0.4F, 0.4F);
      }

      glowParticle.yd *= 0.2F;
      if (xAux == 0.0 && zAux == 0.0) {
         glowParticle.xd *= 0.1F;
         glowParticle.zd *= 0.1F;
      }

      glowParticle.setLifetime((int)(8.0 / (random.nextDouble() * 0.8 + 0.2)));
      return glowParticle;
   }
}
