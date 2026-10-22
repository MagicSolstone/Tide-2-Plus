package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.VibrationParticleOption;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class VibrationSignalParticle$Provider implements ParticleProvider<VibrationParticleOption> {
   private final SpriteSet sprite;

   public VibrationSignalParticle$Provider(final SpriteSet sprite) {
      this.sprite = sprite;
   }

   public Particle createParticle(
      final VibrationParticleOption options,
      final ClientLevel level,
      final double x,
      final double y,
      final double z,
      final double xAux,
      final double yAux,
      final double zAux,
      final RandomSource random
   ) {
      VibrationSignalParticle particle = new VibrationSignalParticle(
         level, x, y, z, options.getDestination(), options.getArrivalInTicks(), this.sprite.get(random)
      );
      particle.setAlpha(1.0F);
      return particle;
   }
}
