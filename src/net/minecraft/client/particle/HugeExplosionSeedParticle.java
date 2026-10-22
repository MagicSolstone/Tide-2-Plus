package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;

@Environment(EnvType.CLIENT)
public class HugeExplosionSeedParticle extends NoRenderParticle {
   private HugeExplosionSeedParticle(final ClientLevel level, final double x, final double y, final double z) {
      super(level, x, y, z, 0.0, 0.0, 0.0);
      this.lifetime = 8;
   }

   @Override
   public void tick() {
      for (int i = 0; i < 6; i++) {
         double xx = this.x + (this.random.nextDouble() - this.random.nextDouble()) * 4.0;
         double yy = this.y + (this.random.nextDouble() - this.random.nextDouble()) * 4.0;
         double zz = this.z + (this.random.nextDouble() - this.random.nextDouble()) * 4.0;
         this.level.addParticle(ParticleTypes.EXPLOSION, xx, yy, zz, (float)this.age / this.lifetime, 0.0, 0.0);
      }

      this.age++;
      if (this.age == this.lifetime) {
         this.remove();
      }
   }
}
