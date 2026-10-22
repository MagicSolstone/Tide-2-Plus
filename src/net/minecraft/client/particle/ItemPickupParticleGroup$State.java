package net.minecraft.client.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.ParticleGroupRenderState;

@Environment(EnvType.CLIENT)
record ItemPickupParticleGroup$State(List<ItemPickupParticleGroup$ParticleInstance> instances) implements ParticleGroupRenderState {
   @Override
   public void submit(final SubmitNodeCollector submitNodeCollector, final CameraRenderState camera) {
      PoseStack poseStack = new PoseStack();
      EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();

      for (ItemPickupParticleGroup$ParticleInstance instance : this.instances) {
         entityRenderDispatcher.submit(instance.itemRenderState, camera, instance.xOffset, instance.yOffset, instance.zOffset, poseStack, submitNodeCollector);
      }
   }
}
