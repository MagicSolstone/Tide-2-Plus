package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

@Environment(EnvType.CLIENT)
public class SplashParticle extends WaterDropParticle {
   private SplashParticle(
      final ClientLevel level,
      final double x,
      final double y,
      final double z,
      final double xa,
      final double ya,
      final double za,
      final TextureAtlasSprite sprite
   ) {
      super(level, x, y, z, sprite);
      this.gravity = 0.04F;
      if (ya == 0.0 && (xa != 0.0 || za != 0.0)) {
         this.xd = xa;
         this.yd = 0.1;
         this.zd = za;
      }
   }
}
