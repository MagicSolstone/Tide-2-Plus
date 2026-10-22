package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.ARGB;

@Environment(EnvType.CLIENT)
public class WhiteAshParticle extends BaseAshSmokeParticle {
   private static final int COLOR_RGB24 = 12235202;

   protected WhiteAshParticle(
      final ClientLevel level,
      final double x,
      final double y,
      final double z,
      final double xa,
      final double ya,
      final double za,
      final float scale,
      final SpriteSet sprites
   ) {
      super(level, x, y, z, 0.1F, -0.1F, 0.1F, xa, ya, za, scale, sprites, 0.0F, 20, 0.0125F, false);
      this.rCol = ARGB.red(12235202) / 255.0F;
      this.gCol = ARGB.green(12235202) / 255.0F;
      this.bCol = ARGB.blue(12235202) / 255.0F;
   }
}
