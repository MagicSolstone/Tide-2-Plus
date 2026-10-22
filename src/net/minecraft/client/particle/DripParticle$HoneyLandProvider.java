package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.material.Fluids;

@Environment(EnvType.CLIENT)
public class DripParticle$HoneyLandProvider implements ParticleProvider<SimpleParticleType> {
   private final SpriteSet sprite;

   public DripParticle$HoneyLandProvider(final SpriteSet sprite) {
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
      DripParticle particle = new DripParticle$DripLandParticle(level, x, y, z, Fluids.EMPTY, this.sprite.get(random));
      particle.lifetime = (int)(128.0 / (random.nextFloat() * 0.8 + 0.2));
      particle.setColor(0.522F, 0.408F, 0.082F);
      return particle;
   }
}
