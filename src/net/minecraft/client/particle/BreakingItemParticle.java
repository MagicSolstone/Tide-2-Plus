package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

@Environment(EnvType.CLIENT)
public class BreakingItemParticle extends SingleQuadParticle {
   private final float uo;
   private final float vo;
   private final SingleQuadParticle$Layer layer;

   private BreakingItemParticle(
      final ClientLevel level,
      final double x,
      final double y,
      final double z,
      final double xa,
      final double ya,
      final double za,
      final TextureAtlasSprite sprite
   ) {
      this(level, x, y, z, sprite);
      this.xd *= 0.1F;
      this.yd *= 0.1F;
      this.zd *= 0.1F;
      this.xd += xa;
      this.yd += ya;
      this.zd += za;
   }

   protected BreakingItemParticle(final ClientLevel level, final double x, final double y, final double z, final TextureAtlasSprite sprite) {
      super(level, x, y, z, 0.0, 0.0, 0.0, sprite);
      this.gravity = 1.0F;
      this.quadSize /= 2.0F;
      this.uo = this.random.nextFloat() * 3.0F;
      this.vo = this.random.nextFloat() * 3.0F;
      this.layer = SingleQuadParticle$Layer.bySprite(sprite);
   }

   @Override
   protected float getU0() {
      return this.sprite.getU((this.uo + 1.0F) / 4.0F);
   }

   @Override
   protected float getU1() {
      return this.sprite.getU(this.uo / 4.0F);
   }

   @Override
   protected float getV0() {
      return this.sprite.getV(this.vo / 4.0F);
   }

   @Override
   protected float getV1() {
      return this.sprite.getV((this.vo + 1.0F) / 4.0F);
   }

   @Override
   public SingleQuadParticle$Layer getLayer() {
      return this.layer;
   }
}
