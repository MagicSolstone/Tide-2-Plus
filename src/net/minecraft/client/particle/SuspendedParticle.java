package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

@Environment(EnvType.CLIENT)
public class SuspendedParticle extends SingleQuadParticle {
   private SuspendedParticle(final ClientLevel level, final double x, final double y, final double z, final TextureAtlasSprite sprite) {
      super(level, x, y - 0.125, z, sprite);
      this.setSize(0.01F, 0.01F);
      this.quadSize = this.quadSize * (this.random.nextFloat() * 0.6F + 0.2F);
      this.lifetime = (int)(16.0 / (this.random.nextFloat() * 0.8 + 0.2));
      this.hasPhysics = false;
      this.friction = 1.0F;
      this.gravity = 0.0F;
   }

   private SuspendedParticle(
      final ClientLevel level,
      final double x,
      final double y,
      final double z,
      final double xd,
      final double yd,
      final double zd,
      final TextureAtlasSprite sprite
   ) {
      super(level, x, y - 0.125, z, xd, yd, zd, sprite);
      this.setSize(0.01F, 0.01F);
      this.quadSize = this.quadSize * (this.random.nextFloat() * 0.6F + 0.6F);
      this.lifetime = (int)(16.0 / (this.random.nextFloat() * 0.8 + 0.2));
      this.hasPhysics = false;
      this.friction = 1.0F;
      this.gravity = 0.0F;
   }

   @Override
   public SingleQuadParticle$Layer getLayer() {
      return SingleQuadParticle$Layer.OPAQUE;
   }
}
