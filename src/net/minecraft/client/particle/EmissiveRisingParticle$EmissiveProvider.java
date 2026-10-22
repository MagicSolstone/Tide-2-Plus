package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class EmissiveRisingParticle$EmissiveProvider implements ParticleProvider<SimpleParticleType> {
   private final SpriteSet sprite;

   public EmissiveRisingParticle$EmissiveProvider(final SpriteSet sprite) {
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
      EmissiveRisingParticle particle = new EmissiveRisingParticle(level, x, y, z, xAux, yAux, zAux, this.sprite);
      particle.setAlpha(1.0F);
      particle.isGlowing = true;
      return particle;
   }
}
