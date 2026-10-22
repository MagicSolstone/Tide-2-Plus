package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class SuspendedParticle$WarpedSporeProvider implements ParticleProvider<SimpleParticleType> {
   private final SpriteSet sprite;

   public SuspendedParticle$WarpedSporeProvider(final SpriteSet sprite) {
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
      double ya = random.nextFloat() * -1.9 * random.nextFloat() * 0.1;
      SuspendedParticle particle = new SuspendedParticle(level, x, y, z, 0.0, ya, 0.0, this.sprite.get(random));
      particle.setColor(0.1F, 0.1F, 0.3F);
      particle.setSize(0.001F, 0.001F);
      return particle;
   }
}
