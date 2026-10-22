package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class NoteParticle extends SingleQuadParticle {
   private NoteParticle(final ClientLevel level, final double x, final double y, final double z, final double color, final TextureAtlasSprite sprite) {
      super(level, x, y, z, 0.0, 0.0, 0.0, sprite);
      this.friction = 0.66F;
      this.speedUpWhenYMotionIsBlocked = true;
      this.xd *= 0.01F;
      this.yd *= 0.01F;
      this.zd *= 0.01F;
      this.yd += 0.2;
      this.rCol = Math.max(0.0F, Mth.sin(((float)color + 0.0F) * (float) (Math.PI * 2)) * 0.65F + 0.35F);
      this.gCol = Math.max(0.0F, Mth.sin(((float)color + 0.33333334F) * (float) (Math.PI * 2)) * 0.65F + 0.35F);
      this.bCol = Math.max(0.0F, Mth.sin(((float)color + 0.6666667F) * (float) (Math.PI * 2)) * 0.65F + 0.35F);
      this.quadSize *= 1.5F;
      this.lifetime = 6;
   }

   @Override
   public SingleQuadParticle$Layer getLayer() {
      return SingleQuadParticle$Layer.OPAQUE;
   }

   @Override
   public float getQuadSize(final float a) {
      return this.quadSize * Mth.clamp((this.age + a) / this.lifetime * 32.0F, 0.0F, 1.0F);
   }
}
