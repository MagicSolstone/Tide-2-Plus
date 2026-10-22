package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;

@Environment(EnvType.CLIENT)
public class GustSeedParticle extends NoRenderParticle {
   private final double scale;
   private final int tickDelayInBetween;

   private GustSeedParticle(
      final ClientLevel level, final double x, final double y, final double z, final double scale, final int lifetime, final int tickDelayInBetween
   ) {
      super(level, x, y, z, 0.0, 0.0, 0.0);
      this.scale = scale;
      this.lifetime = lifetime;
      this.tickDelayInBetween = tickDelayInBetween;
   }

   @Override
   public void tick() {
      if (this.age % (this.tickDelayInBetween + 1) == 0) {
         for (int i = 0; i < 3; i++) {
            double x = this.x + (this.random.nextDouble() - this.random.nextDouble()) * this.scale;
            double y = this.y + (this.random.nextDouble() - this.random.nextDouble()) * this.scale;
            double z = this.z + (this.random.nextDouble() - this.random.nextDouble()) * this.scale;
            this.level.addParticle(ParticleTypes.GUST, x, y, z, (float)this.age / this.lifetime, 0.0, 0.0);
         }
      }

      if (this.age++ == this.lifetime) {
         this.remove();
      }
   }
}
