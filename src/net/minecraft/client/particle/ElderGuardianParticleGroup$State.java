package net.minecraft.client.particle;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.ParticleGroupRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Unit;

@Environment(EnvType.CLIENT)
record ElderGuardianParticleGroup$State(List<ElderGuardianParticleGroup$ElderGuardianParticleRenderState> states) implements ParticleGroupRenderState {
   @Override
   public void submit(final SubmitNodeCollector submitNodeCollector, final CameraRenderState camera) {
      for (ElderGuardianParticleGroup$ElderGuardianParticleRenderState state : this.states) {
         submitNodeCollector.submitModel(
            state.model, Unit.INSTANCE, state.poseStack, state.renderType, 15728880, OverlayTexture.NO_OVERLAY, state.color, null, 0
         );
      }
   }
}
