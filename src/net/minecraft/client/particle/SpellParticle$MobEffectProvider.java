package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class SpellParticle$MobEffectProvider implements ParticleProvider<ColorParticleOption> {
   private final SpriteSet sprite;

   public SpellParticle$MobEffectProvider(final SpriteSet sprite) {
      this.sprite = sprite;
   }

   public Particle createParticle(
      final ColorParticleOption options,
      final ClientLevel level,
      final double x,
      final double y,
      final double z,
      final double xAux,
      final double yAux,
      final double zAux,
      final RandomSource random
   ) {
      SpellParticle particle = new SpellParticle(level, x, y, z, xAux, yAux, zAux, this.sprite);
      particle.setColor(options.getRed(), options.getGreen(), options.getBlue());
      particle.setAlpha(options.getAlpha());
      return particle;
   }
}
