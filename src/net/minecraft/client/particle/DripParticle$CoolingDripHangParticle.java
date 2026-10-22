package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.material.Fluid;

@Environment(EnvType.CLIENT)
class DripParticle$CoolingDripHangParticle extends DripParticle$DripHangParticle {
   private DripParticle$CoolingDripHangParticle(
      final ClientLevel level,
      final double x,
      final double y,
      final double z,
      final Fluid type,
      final ParticleOptions fallingParticle,
      final TextureAtlasSprite sprite
   ) {
      super(level, x, y, z, type, fallingParticle, sprite);
   }

   @Override
   protected void preMoveUpdate() {
      this.rCol = 1.0F;
      this.gCol = 16.0F / (40 - this.lifetime + 16);
      this.bCol = 4.0F / (40 - this.lifetime + 8);
      super.preMoveUpdate();
   }
}
