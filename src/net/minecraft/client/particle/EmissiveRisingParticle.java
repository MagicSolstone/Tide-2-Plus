package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.LightCoordsUtil;

@Environment(EnvType.CLIENT)
public class EmissiveRisingParticle extends RisingParticle {
   private final SpriteSet sprites;
   protected boolean isGlowing;

   private EmissiveRisingParticle(
      final ClientLevel level, final double x, final double y, final double z, final double xd, final double yd, final double zd, final SpriteSet sprites
   ) {
      super(level, x, y, z, xd, yd, zd, sprites.first());
      this.sprites = sprites;
      this.scale(1.5F);
      this.setSpriteFromAge(sprites);
   }

   @Override
   public int getLightCoords(final float a) {
      return this.isGlowing ? LightCoordsUtil.withBlock(super.getLightCoords(a), 15) : super.getLightCoords(a);
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
