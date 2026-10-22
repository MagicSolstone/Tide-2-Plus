package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.material.Fluid;

@Environment(EnvType.CLIENT)
class DripParticle$DripHangParticle extends DripParticle {
   private final ParticleOptions fallingParticle;

   private DripParticle$DripHangParticle(
      final ClientLevel level,
      final double x,
      final double y,
      final double z,
      final Fluid type,
      final ParticleOptions fallingParticle,
      final TextureAtlasSprite sprite
   ) {
      super(level, x, y, z, type, sprite);
      this.fallingParticle = fallingParticle;
      this.gravity *= 0.02F;
      this.lifetime = 40;
   }

   @Override
   protected void preMoveUpdate() {
      if (this.lifetime-- <= 0) {
         this.remove();
         this.level.addParticle(this.fallingParticle, this.x, this.y, this.z, this.xd, this.yd, this.zd);
      }
   }

   @Override
   protected void postMoveUpdate() {
      this.xd *= 0.02;
      this.yd *= 0.02;
      this.zd *= 0.02;
   }
}
