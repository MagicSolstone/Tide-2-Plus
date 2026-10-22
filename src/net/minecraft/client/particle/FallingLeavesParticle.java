package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

@Environment(EnvType.CLIENT)
public class FallingLeavesParticle extends FallingParticle {
   protected FallingLeavesParticle(
      final ClientLevel level,
      final double x,
      final double y,
      final double z,
      final TextureAtlasSprite sprite,
      final float fallAcceleration,
      final float sideAcceleration,
      final boolean swirl,
      final boolean flowAway,
      final float scale,
      final float startVelocity
   ) {
      super(level, x, y, z, sprite, fallAcceleration, sideAcceleration, swirl, flowAway, scale, startVelocity);
   }
}
