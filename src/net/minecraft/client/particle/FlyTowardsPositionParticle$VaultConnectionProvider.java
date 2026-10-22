package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class FlyTowardsPositionParticle$VaultConnectionProvider implements ParticleProvider<SimpleParticleType> {
   private final SpriteSet sprite;

   public FlyTowardsPositionParticle$VaultConnectionProvider(final SpriteSet sprite) {
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
      FlyTowardsPositionParticle particle = new FlyTowardsPositionParticle(
         level, x, y, z, xAux, yAux, zAux, true, new Particle$LifetimeAlpha(0.0F, 0.6F, 0.25F, 1.0F), this.sprite.get(random)
      );
      particle.scale(1.5F);
      return particle;
   }
}
