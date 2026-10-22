package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ARGB;

@Environment(EnvType.CLIENT)
public class SquidInkParticle extends SimpleAnimatedParticle {
   private SquidInkParticle(
      final ClientLevel level,
      final double x,
      final double y,
      final double z,
      final double xa,
      final double ya,
      final double za,
      final int color,
      final SpriteSet sprites
   ) {
      super(level, x, y, z, sprites, 0.0F);
      this.friction = 0.92F;
      this.quadSize = 0.5F;
      this.setAlpha(1.0F);
      this.setColor(ARGB.redFloat(color), ARGB.greenFloat(color), ARGB.blueFloat(color));
      this.lifetime = (int)(this.quadSize * 12.0F / (this.random.nextFloat() * 0.8F + 0.2F));
      this.setSpriteFromAge(sprites);
      this.hasPhysics = false;
      this.xd = xa;
      this.yd = ya;
      this.zd = za;
   }

   @Override
   public void tick() {
      super.tick();
      if (!this.removed) {
         this.setSpriteFromAge(this.sprites);
         if (this.age > this.lifetime / 2) {
            this.setAlpha(1.0F - ((float)this.age - this.lifetime / 2) / this.lifetime);
         }

         if (this.level.getBlockState(BlockPos.containing(this.x, this.y, this.z)).isAir()) {
            this.yd -= 0.0074F;
         }
      }
   }
}
