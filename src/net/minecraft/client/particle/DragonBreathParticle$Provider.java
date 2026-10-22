package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.PowerParticleOption;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class DragonBreathParticle$Provider implements ParticleProvider<PowerParticleOption> {
   private final SpriteSet sprites;

   public DragonBreathParticle$Provider(final SpriteSet sprites) {
      this.sprites = sprites;
   }

   public Particle createParticle(
      final PowerParticleOption options,
      final ClientLevel level,
      final double x,
      final double y,
      final double z,
      final double xAux,
      final double yAux,
      final double zAux,
      final RandomSource random
   ) {
      DragonBreathParticle particle = new DragonBreathParticle(level, x, y, z, xAux, yAux, zAux, this.sprites);
      particle.setPower(options.getPower());
      return particle;
   }
}
