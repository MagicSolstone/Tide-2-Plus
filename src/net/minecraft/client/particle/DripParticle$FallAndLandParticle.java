package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.material.Fluid;

@Environment(EnvType.CLIENT)
class DripParticle$FallAndLandParticle extends DripParticle$FallingParticle {
   protected final ParticleOptions landParticle;

   private DripParticle$FallAndLandParticle(
      final ClientLevel level,
      final double x,
      final double y,
      final double z,
      final Fluid type,
      final ParticleOptions landParticle,
      final TextureAtlasSprite sprite
   ) {
      super(level, x, y, z, type, sprite);
      this.lifetime = (int)(64.0 / (this.random.nextFloat() * 0.8 + 0.2));
      this.landParticle = landParticle;
   }

   @Override
   protected void postMoveUpdate() {
      if (this.onGround) {
         this.remove();
         this.level.addParticle(this.landParticle, this.x, this.y, this.z, 0.0, 0.0, 0.0);
      }
   }
}
