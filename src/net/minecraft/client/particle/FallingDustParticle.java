package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class FallingDustParticle extends SingleQuadParticle {
   private final float rotSpeed;
   private final SpriteSet sprites;

   private FallingDustParticle(
      final ClientLevel level, final double x, final double y, final double z, final float r, final float g, final float b, final SpriteSet sprites
   ) {
      super(level, x, y, z, sprites.first());
      this.sprites = sprites;
      this.rCol = r;
      this.gCol = g;
      this.bCol = b;
      float scale = 0.9F;
      this.quadSize *= 0.67499995F;
      int baseLifetime = (int)(32.0 / (this.random.nextFloat() * 0.8 + 0.2));
      this.lifetime = (int)Math.max(baseLifetime * 0.9F, 1.0F);
      this.setSpriteFromAge(sprites);
      this.rotSpeed = (this.random.nextFloat() - 0.5F) * 0.1F;
      this.roll = this.random.nextFloat() * (float) (Math.PI * 2);
   }

   @Override
   public SingleQuadParticle$Layer getLayer() {
      return SingleQuadParticle$Layer.OPAQUE;
   }

   @Override
   public float getQuadSize(final float a) {
      return this.quadSize * Mth.clamp((this.age + a) / this.lifetime * 32.0F, 0.0F, 1.0F);
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
         this.oRoll = this.roll;
         this.roll = this.roll + (float) Math.PI * this.rotSpeed * 2.0F;
         if (this.onGround) {
            this.oRoll = this.roll = 0.0F;
         }

         this.move(this.xd, this.yd, this.zd);
         this.yd -= 0.003F;
         this.yd = Math.max(this.yd, -0.14F);
      }
   }
}
