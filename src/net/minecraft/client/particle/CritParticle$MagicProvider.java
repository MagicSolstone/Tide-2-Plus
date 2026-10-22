package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class CritParticle$MagicProvider implements ParticleProvider<SimpleParticleType> {
   private final SpriteSet sprite;

   public CritParticle$MagicProvider(final SpriteSet sprite) {
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
      CritParticle particle = new CritParticle(level, x, y, z, xAux, yAux, zAux, this.sprite.get(random));
      particle.rCol *= 0.3F;
      particle.gCol *= 0.8F;
      return particle;
   }
}
