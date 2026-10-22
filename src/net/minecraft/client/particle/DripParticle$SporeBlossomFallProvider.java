package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.material.Fluids;

@Environment(EnvType.CLIENT)
public class DripParticle$SporeBlossomFallProvider implements ParticleProvider<SimpleParticleType> {
   private final SpriteSet sprite;

   public DripParticle$SporeBlossomFallProvider(final SpriteSet sprite) {
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
      DripParticle particle = new DripParticle$FallingParticle(level, x, y, z, Fluids.EMPTY, this.sprite.get(random));
      particle.lifetime = (int)(64.0F / Mth.randomBetween(particle.random, 0.1F, 0.9F));
      particle.gravity = 0.005F;
      particle.setColor(0.32F, 0.5F, 0.22F);
      return particle;
   }
}
