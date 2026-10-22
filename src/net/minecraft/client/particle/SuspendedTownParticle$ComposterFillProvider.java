package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class SuspendedTownParticle$ComposterFillProvider implements ParticleProvider<SimpleParticleType> {
   private final SpriteSet sprite;

   public SuspendedTownParticle$ComposterFillProvider(final SpriteSet sprite) {
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
      SuspendedTownParticle particle = new SuspendedTownParticle(level, x, y, z, xAux, yAux, zAux, this.sprite.get(random));
      particle.setColor(1.0F, 1.0F, 1.0F);
      particle.setLifetime(3 + level.getRandom().nextInt(5));
      return particle;
   }
}
