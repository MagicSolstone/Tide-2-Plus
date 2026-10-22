package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class CritParticle extends SingleQuadParticle {
   private CritParticle(
      final ClientLevel level,
      final double x,
      final double y,
      final double z,
      final double xa,
      final double ya,
      final double za,
      final TextureAtlasSprite sprite
   ) {
      super(level, x, y, z, 0.0, 0.0, 0.0, sprite);
      this.friction = 0.7F;
      this.gravity = 0.5F;
      this.xd *= 0.1F;
      this.yd *= 0.1F;
      this.zd *= 0.1F;
      this.xd += xa * 0.4;
      this.yd += ya * 0.4;
      this.zd += za * 0.4;
      float col = this.random.nextFloat() * 0.3F + 0.6F;
      this.rCol = col;
      this.gCol = col;
      this.bCol = col;
      this.quadSize *= 0.75F;
      this.lifetime = Math.max((int)(6.0 / (this.random.nextFloat() * 0.8 + 0.6)), 1);
      this.hasPhysics = false;
      this.tick();
   }

   @Override
   public float getQuadSize(final float a) {
      return this.quadSize * Mth.clamp((this.age + a) / this.lifetime * 32.0F, 0.0F, 1.0F);
   }

   @Override
   public void tick() {
      super.tick();
      this.gCol *= 0.96F;
      this.bCol *= 0.9F;
   }

   @Override
   public SingleQuadParticle$Layer getLayer() {
      return SingleQuadParticle$Layer.OPAQUE;
   }
}
