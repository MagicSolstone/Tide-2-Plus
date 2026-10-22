package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;

@Environment(EnvType.CLIENT)
public class GeyserBaseParticle extends BaseAshSmokeParticle {
   private static final float BURST_IMPULSE_FACTOR = 0.25F;
   private static final float PARTICLE_SIZE_BASE = 3.0F;
   private static final float PARTICLE_SIZE_FACTOR = 0.125F;
   private static final float SPAWN_OFFSET_Y = 0.2F;
   private static final float RANDOM_SPAWN_SPREAD = 0.5F;
   private static final int MAX_LIFETIME = 25;
   private static final float FRICTION = 0.725F;

   private GeyserBaseParticle(
      final ClientLevel level,
      final double x,
      final double y,
      final double z,
      final double xAux,
      final double yAux,
      final double zAux,
      final int waterBlocks,
      final float burstImpulseBase,
      final SpriteSet sprites
   ) {
      float burstImpulse = burstImpulseBase + 0.25F * waterBlocks;
      float size = 3.0F + 0.125F * waterBlocks;
      super(level, x, y, z, burstImpulse, burstImpulse, burstImpulse, xAux, yAux, zAux, size, sprites, 0.0F, 0, 0.0F, true);
      this.friction = 0.725F;
      this.rCol = 1.0F;
      this.gCol = 1.0F;
      this.bCol = 1.0F;
      this.yd = Math.abs(this.yd);
      float lifetimeFactor = 0.8F + 0.2F * level.getRandom().nextFloat();
      this.lifetime = (int)(25.0F * lifetimeFactor);
   }
}
