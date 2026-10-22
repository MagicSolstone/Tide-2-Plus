package net.minecraft.client.renderer.state.level;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface QuadParticleRenderState$ParticleConsumer {
   void consume(
      final float x,
      final float y,
      final float z,
      final float xRot,
      final float yRot,
      final float zRot,
      final float wRot,
      final float scale,
      final float u0,
      final float u1,
      final float v0,
      final float v1,
      final int color,
      final int lightCoords
   );
}
