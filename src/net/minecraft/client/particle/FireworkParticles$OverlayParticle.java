package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class FireworkParticles$OverlayParticle extends SingleQuadParticle {
   private FireworkParticles$OverlayParticle(final ClientLevel level, final double x, final double y, final double z, final TextureAtlasSprite sprite) {
      super(level, x, y, z, sprite);
      this.lifetime = 4;
   }

   @Override
   public SingleQuadParticle$Layer getLayer() {
      return SingleQuadParticle$Layer.TRANSLUCENT;
   }

   @Override
   public void extract(final QuadParticleRenderState particleTypeRenderState, final Camera camera, final float partialTickTime) {
      this.setAlpha(0.6F - (this.age + partialTickTime - 1.0F) * 0.25F * 0.5F);
      super.extract(particleTypeRenderState, camera, partialTickTime);
   }

   @Override
   public float getQuadSize(final float a) {
      return 7.1F * Mth.sin((this.age + a - 1.0F) * 0.25F * (float) Math.PI);
   }
}
