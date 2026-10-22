package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.LightCoordsUtil;

@Environment(EnvType.CLIENT)
public class LavaParticle extends SingleQuadParticle {
   private LavaParticle(final ClientLevel level, final double x, final double y, final double z, final TextureAtlasSprite sprite) {
      super(level, x, y, z, 0.0, 0.0, 0.0, sprite);
      this.gravity = 0.75F;
      this.friction = 0.999F;
      this.xd *= 0.8F;
      this.yd *= 0.8F;
      this.zd *= 0.8F;
      this.yd = this.random.nextFloat() * 0.4F + 0.05F;
      this.quadSize = this.quadSize * (this.random.nextFloat() * 2.0F + 0.2F);
      this.lifetime = (int)(16.0 / (this.random.nextFloat() * 0.8 + 0.2));
   }

   @Override
   public SingleQuadParticle$Layer getLayer() {
      return SingleQuadParticle$Layer.OPAQUE;
   }

   @Override
   public int getLightCoords(final float a) {
      return LightCoordsUtil.withBlock(super.getLightCoords(a), 15);
   }

   @Override
   public float getQuadSize(final float a) {
      float s = (this.age + a) / this.lifetime;
      return this.quadSize * (1.0F - s * s);
   }

   @Override
   public void tick() {
      super.tick();
      if (!this.removed) {
         float odds = (float)this.age / this.lifetime;
         if (this.random.nextFloat() > odds) {
            this.level.addParticle(ParticleTypes.SMOKE, this.x, this.y, this.z, this.xd, this.yd, this.zd);
         }
      }
   }
}
