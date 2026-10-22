package net.minecraft.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

@Environment(EnvType.CLIENT)
record ItemPickupParticleGroup$ParticleInstance(EntityRenderState itemRenderState, double xOffset, double yOffset, double zOffset) {
   public static ItemPickupParticleGroup$ParticleInstance fromParticle(final ItemPickupParticle particle, final Camera camera, final float partialTickTime) {
      float time = (particle.life + partialTickTime) / 3.0F;
      time *= time;
      double xt = Mth.lerp((double)partialTickTime, particle.targetXOld, particle.targetX);
      double yt = Mth.lerp((double)partialTickTime, particle.targetYOld, particle.targetY);
      double zt = Mth.lerp((double)partialTickTime, particle.targetZOld, particle.targetZ);
      double xx = Mth.lerp((double)time, particle.itemRenderState.x, xt);
      double yy = Mth.lerp((double)time, particle.itemRenderState.y, yt);
      double zz = Mth.lerp((double)time, particle.itemRenderState.z, zt);
      Vec3 pos = camera.position();
      return new ItemPickupParticleGroup$ParticleInstance(particle.itemRenderState, xx - pos.x(), yy - pos.y(), zz - pos.z());
   }
}
