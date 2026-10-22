package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;

@Environment(EnvType.CLIENT)
public class HugeExplosionParticle extends SingleQuadParticle {
   private final SpriteSet sprites;

   protected HugeExplosionParticle(final ClientLevel level, final double x, final double y, final double z, final double size, final SpriteSet sprites) {
      super(level, x, y, z, 0.0, 0.0, 0.0, sprites.first());
      this.lifetime = 6 + this.random.nextInt(4);
      float col = this.random.nextFloat() * 0.6F + 0.4F;
      this.rCol = col;
      this.gCol = col;
      this.bCol = col;
      this.quadSize = 2.0F * (1.0F - (float)size * 0.5F);
      this.sprites = sprites;
      this.setSpriteFromAge(sprites);
   }

   @Override
   public int getLightCoords(final float a) {
      return 15728880;
   }

   @Override
   public void tick() {
      this.xo = this.x;
      this.yo = this.y;
      this.zo = this.z;
      if (this.age++ >= this.lifetime) {
         this.remove();
      } else {
         this.setSpriteFromAge(this.sprites);
      }
   }

   @Override
   public SingleQuadParticle$Layer getLayer() {
      return SingleQuadParticle$Layer.OPAQUE;
   }
}
