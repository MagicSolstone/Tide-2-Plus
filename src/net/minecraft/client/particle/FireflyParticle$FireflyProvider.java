package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class FireflyParticle$FireflyProvider implements ParticleProvider<SimpleParticleType> {
   private final SpriteSet sprite;

   public FireflyParticle$FireflyProvider(final SpriteSet sprite) {
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
      FireflyParticle particle = new FireflyParticle(
         level, x, y, z, 0.5 - random.nextDouble(), random.nextBoolean() ? yAux : -yAux, 0.5 - random.nextDouble(), this.sprite.get(random)
      );
      particle.setLifetime(random.nextIntBetweenInclusive(200, 300));
      particle.scale(1.5F);
      particle.setAlpha(0.0F);
      return particle;
   }
}
