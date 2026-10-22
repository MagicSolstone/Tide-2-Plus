package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ShriekParticleOption;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class ShriekParticle$Provider implements ParticleProvider<ShriekParticleOption> {
   private final SpriteSet sprite;

   public ShriekParticle$Provider(final SpriteSet sprite) {
      this.sprite = sprite;
   }

   public Particle createParticle(
      final ShriekParticleOption options,
      final ClientLevel level,
      final double x,
      final double y,
      final double z,
      final double xAux,
      final double yAux,
      final double zAux,
      final RandomSource random
   ) {
      ShriekParticle particle = new ShriekParticle(level, x, y, z, options.getDelay(), this.sprite.get(random));
      particle.setAlpha(1.0F);
      return particle;
   }
}
