package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

@Environment(EnvType.CLIENT)
public class SimpleVerticalParticle extends SingleQuadParticle {
   private SimpleVerticalParticle(
      final ClientLevel level,
      final double x,
      final double y,
      final double z,
      final double xa,
      final double ya,
      final double za,
      final TextureAtlasSprite sprite,
      final boolean upwards
   ) {
      super(level, x, y, z, xa, ya, za, sprite);
      this.xd = xa;
      this.zd = za;
      this.yd = ya;
      this.gravity = 0.0F;
      this.yd += upwards ? 0.03 : -0.03;
      this.quadSize = this.quadSize * (this.random.nextFloat() * 0.6F + 0.5F);
      this.lifetime = 8;
   }

   @Override
   public SingleQuadParticle$Layer getLayer() {
      return SingleQuadParticle$Layer.OPAQUE;
   }
}
