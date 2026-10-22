package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.state.level.ParticleGroupRenderState;

@Environment(EnvType.CLIENT)
public class ItemPickupParticleGroup extends ParticleGroup<ItemPickupParticle> {
   public ItemPickupParticleGroup(final ParticleEngine engine) {
      super(engine);
   }

   @Override
   public ParticleGroupRenderState extractRenderState(final Frustum frustum, final Camera camera, final float partialTickTime) {
      return new ItemPickupParticleGroup$State(
         this.particles.stream().map(particle -> ItemPickupParticleGroup$ParticleInstance.fromParticle(particle, camera, partialTickTime)).toList()
      );
   }
}
