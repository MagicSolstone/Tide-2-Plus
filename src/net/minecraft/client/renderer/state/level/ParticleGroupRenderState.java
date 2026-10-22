package net.minecraft.client.renderer.state.level;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;

@Environment(EnvType.CLIENT)
public interface ParticleGroupRenderState {
   void submit(SubmitNodeCollector submitNodeCollector, final CameraRenderState camera);

   default void clear() {
   }
}
