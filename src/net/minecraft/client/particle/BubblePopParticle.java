package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;

@Environment(EnvType.CLIENT)
public class BubblePopParticle extends SingleQuadParticle {
   private final SpriteSet sprites;

   private BubblePopParticle(
      final ClientLevel level, final double x, final double y, final double z, final double xa, final double ya, final double za, final SpriteSet sprites
   ) {
      super(level, x, y, z, sprites.first());
      this.sprites = sprites;
      this.lifetime = 4;
      this.gravity = 0.008F;
      this.xd = xa;
      this.yd = ya;
      this.zd = za;
      this.setSpriteFromAge(sprites);
   }

   @Override
   public void tick() {
      this.xo = this.x;
      this.yo = this.y;
      this.zo = this.z;
      if (this.age++ >= this.lifetime) {
         this.remove();
      } else {
         this.yd = this.yd - this.gravity;
         this.move(this.xd, this.yd, this.zd);
         this.setSpriteFromAge(this.sprites);
      }
   }

   @Override
   public SingleQuadParticle$Layer getLayer() {
      return SingleQuadParticle$Layer.OPAQUE;
   }
}
