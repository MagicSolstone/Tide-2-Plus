package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.LightCoordsUtil;

@Environment(EnvType.CLIENT)
public class SculkChargeParticle extends SingleQuadParticle {
   private final SpriteSet sprites;

   private SculkChargeParticle(
      final ClientLevel level, final double x, final double y, final double z, final double xd, final double yd, final double zd, final SpriteSet sprites
   ) {
      super(level, x, y, z, xd, yd, zd, sprites.first());
      this.friction = 0.96F;
      this.sprites = sprites;
      this.scale(1.5F);
      this.hasPhysics = false;
      this.setSpriteFromAge(sprites);
   }

   @Override
   public int getLightCoords(final float a) {
      return LightCoordsUtil.withBlock(super.getLightCoords(a), 15);
   }

   @Override
   public SingleQuadParticle$Layer getLayer() {
      return SingleQuadParticle$Layer.TRANSLUCENT;
   }

   @Override
   public void tick() {
      super.tick();
      this.setSpriteFromAge(this.sprites);
   }
}
