package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import org.joml.Quaternionf;

@Environment(EnvType.CLIENT)
public interface SingleQuadParticle$FacingCameraMode {
   SingleQuadParticle$FacingCameraMode LOOKAT_XYZ = (target, camera, partialTickTime) -> target.set(camera.rotation());
   SingleQuadParticle$FacingCameraMode LOOKAT_Y = (target, camera, partialTickTime) -> target.set(0.0F, camera.rotation().y, 0.0F, camera.rotation().w);

   void setRotation(final Quaternionf target, final Camera camera, final float partialTickTime);
}
