package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.material.Fluids;

@Environment(EnvType.CLIENT)
public class DripParticle$HoneyHangProvider implements ParticleProvider<SimpleParticleType> {
   private final SpriteSet sprite;

   public DripParticle$HoneyHangProvider(final SpriteSet sprite) {
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
      DripParticle$DripHangParticle particle = new DripParticle$DripHangParticle(
         level, x, y, z, Fluids.EMPTY, ParticleTypes.FALLING_HONEY, this.sprite.get(random)
      );
      particle.gravity *= 0.01F;
      particle.lifetime = 100;
      particle.setColor(0.622F, 0.508F, 0.082F);
      return particle;
   }
}
