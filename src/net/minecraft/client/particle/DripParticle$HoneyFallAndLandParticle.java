package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.Fluid;

@Environment(EnvType.CLIENT)
class DripParticle$HoneyFallAndLandParticle extends DripParticle$FallAndLandParticle {
   private DripParticle$HoneyFallAndLandParticle(
      final ClientLevel level,
      final double x,
      final double y,
      final double z,
      final Fluid type,
      final ParticleOptions landParticle,
      final TextureAtlasSprite sprite
   ) {
      super(level, x, y, z, type, landParticle, sprite);
   }

   @Override
   protected void postMoveUpdate() {
      if (this.onGround) {
         this.remove();
         this.level.addParticle(this.landParticle, this.x, this.y, this.z, 0.0, 0.0, 0.0);
         float volume = Mth.randomBetween(this.random, 0.3F, 1.0F);
         this.level.playLocalSound(this.x, this.y, this.z, SoundEvents.BEEHIVE_DRIP, SoundSource.BLOCKS, volume, 1.0F, false);
      }
   }
}
