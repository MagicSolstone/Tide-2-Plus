package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;

@Environment(EnvType.CLIENT)
public class GustParticle extends SingleQuadParticle {
   private final SpriteSet sprites;

   protected GustParticle(final ClientLevel level, final double x, final double y, final double z, final SpriteSet sprites) {
      super(level, x, y, z, sprites.first());
      this.sprites = sprites;
      this.setSpriteFromAge(sprites);
      this.lifetime = 12 + this.random.nextInt(4);
      this.quadSize = 1.0F;
      this.setSize(1.0F, 1.0F);
   }

   @Override
   public SingleQuadParticle$Layer getLayer() {
      return SingleQuadParticle$Layer.OPAQUE;
   }

   @Override
   public int getLightCoords(final float a) {
      return 15728880;
   }

   @Override
   public void tick() {
      if (this.age++ >= this.lifetime) {
         this.remove();
      } else {
         this.setSpriteFromAge(this.sprites);
      }
   }
}
