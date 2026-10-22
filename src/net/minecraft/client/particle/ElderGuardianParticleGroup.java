package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.state.level.ParticleGroupRenderState;

@Environment(EnvType.CLIENT)
public class ElderGuardianParticleGroup extends ParticleGroup<ElderGuardianParticle> {
   public ElderGuardianParticleGroup(final ParticleEngine engine) {
      super(engine);
   }

   @Override
   public ParticleGroupRenderState extractRenderState(final Frustum frustum, final Camera camera, final float partialTickTime) {
      return new ElderGuardianParticleGroup$State(
         this.particles
            .stream()
            .map(particle -> ElderGuardianParticleGroup$ElderGuardianParticleRenderState.fromParticle(particle, camera, partialTickTime))
            .toList()
      );
   }
}
