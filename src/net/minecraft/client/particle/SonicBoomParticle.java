package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;

@Environment(EnvType.CLIENT)
public class SonicBoomParticle extends HugeExplosionParticle {
   protected SonicBoomParticle(final ClientLevel level, final double x, final double y, final double z, final double size, final SpriteSet sprites) {
      super(level, x, y, z, size, sprites);
      this.lifetime = 16;
      this.quadSize = 1.5F;
      this.setSpriteFromAge(sprites);
   }
}
