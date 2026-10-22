package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class WhiteAshParticle$Provider implements ParticleProvider<SimpleParticleType> {
   private final SpriteSet sprites;

   public WhiteAshParticle$Provider(final SpriteSet sprites) {
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
      double xa = random.nextFloat() * -1.9 * random.nextFloat() * 0.1;
      double ya = random.nextFloat() * -0.5 * random.nextFloat() * 0.1 * 5.0;
      double za = random.nextFloat() * -1.9 * random.nextFloat() * 0.1;
      return new WhiteAshParticle(level, x, y, z, xa, ya, za, 1.0F, this.sprites);
   }
}
