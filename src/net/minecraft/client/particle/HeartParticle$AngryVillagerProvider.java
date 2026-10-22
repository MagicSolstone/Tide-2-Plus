package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class HeartParticle$AngryVillagerProvider implements ParticleProvider<SimpleParticleType> {
   private final SpriteSet sprite;

   public HeartParticle$AngryVillagerProvider(final SpriteSet sprite) {
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
      HeartParticle particle = new HeartParticle(level, x, y + 0.5, z, this.sprite.get(random));
      particle.setColor(1.0F, 1.0F, 1.0F);
      return particle;
   }
}
