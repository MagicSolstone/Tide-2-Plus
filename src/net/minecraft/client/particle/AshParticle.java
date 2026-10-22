package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;

@Environment(EnvType.CLIENT)
public class AshParticle extends BaseAshSmokeParticle {
   protected AshParticle(
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
      super(level, x, y, z, 0.1F, -0.1F, 0.1F, xa, ya, za, scale, sprites, 0.5F, 20, 0.1F, false);
   }
}
