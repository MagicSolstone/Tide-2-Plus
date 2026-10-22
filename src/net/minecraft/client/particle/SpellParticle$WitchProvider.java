package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class SpellParticle$WitchProvider implements ParticleProvider<SimpleParticleType> {
   private final SpriteSet sprite;

   public SpellParticle$WitchProvider(final SpriteSet sprite) {
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
      SpellParticle particle = new SpellParticle(level, x, y, z, xAux, yAux, zAux, this.sprite);
      float randBrightness = random.nextFloat() * 0.5F + 0.35F;
      particle.setColor(1.0F * randBrightness, 0.0F * randBrightness, 1.0F * randBrightness);
      return particle;
   }
}
