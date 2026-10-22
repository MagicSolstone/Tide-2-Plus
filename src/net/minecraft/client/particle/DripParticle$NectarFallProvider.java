package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.material.Fluids;

@Environment(EnvType.CLIENT)
public class DripParticle$NectarFallProvider implements ParticleProvider<SimpleParticleType> {
   private final SpriteSet sprite;

   public DripParticle$NectarFallProvider(final SpriteSet sprite) {
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
      particle.lifetime = (int)(16.0 / (random.nextFloat() * 0.8 + 0.2));
      particle.gravity = 0.007F;
      particle.setColor(0.92F, 0.782F, 0.72F);
      return particle;
   }
}
