package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.material.Fluid;

@Environment(EnvType.CLIENT)
class DripParticle$FallingParticle extends DripParticle {
   private DripParticle$FallingParticle(
      final ClientLevel level, final double x, final double y, final double z, final Fluid type, final TextureAtlasSprite sprite
   ) {
      super(level, x, y, z, type, sprite);
   }

   @Override
   protected void postMoveUpdate() {
      if (this.onGround) {
         this.remove();
      }
   }
}
