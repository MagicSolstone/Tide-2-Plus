package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

@Environment(EnvType.CLIENT)
public class PlayerCloudParticle extends SingleQuadParticle {
   private final SpriteSet sprites;

   private PlayerCloudParticle(
      final ClientLevel level, final double x, final double y, final double z, final double xa, final double ya, final double za, final SpriteSet sprites
   ) {
      super(level, x, y, z, 0.0, 0.0, 0.0, sprites.first());
      this.friction = 0.96F;
      this.sprites = sprites;
      float scale = 2.5F;
      this.xd *= 0.1F;
      this.yd *= 0.1F;
      this.zd *= 0.1F;
      this.xd += xa;
      this.yd += ya;
      this.zd += za;
      float col = 1.0F - this.random.nextFloat() * 0.3F;
      this.rCol = col;
      this.gCol = col;
      this.bCol = col;
      this.quadSize *= 1.875F;
      int baseLifetime = (int)(8.0 / (this.random.nextFloat() * 0.8 + 0.3));
      this.lifetime = (int)Math.max(baseLifetime * 2.5F, 1.0F);
      this.hasPhysics = false;
      this.setSpriteFromAge(sprites);
   }

   @Override
   public SingleQuadParticle$Layer getLayer() {
      return SingleQuadParticle$Layer.TRANSLUCENT;
   }

   @Override
   public float getQuadSize(final float a) {
      return this.quadSize * Mth.clamp((this.age + a) / this.lifetime * 32.0F, 0.0F, 1.0F);
   }

   @Override
   public void tick() {
      super.tick();
      if (!this.removed) {
         this.setSpriteFromAge(this.sprites);
         Player player = this.level.getNearestPlayer(this.x, this.y, this.z, 2.0, false);
         if (player != null) {
            double playerY = player.getY();
            if (this.y > playerY) {
               this.y = this.y + (playerY - this.y) * 0.2;
               this.yd = this.yd + (player.getDeltaMovement().y - this.yd) * 0.2;
               this.setPos(this.x, this.y, this.z);
            }
         }
      }
   }
}
