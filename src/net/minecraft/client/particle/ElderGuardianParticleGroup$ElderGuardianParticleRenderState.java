package net.minecraft.client.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;

@Environment(EnvType.CLIENT)
record ElderGuardianParticleGroup$ElderGuardianParticleRenderState(Model<Unit> model, PoseStack poseStack, RenderType renderType, int color) {
   public static ElderGuardianParticleGroup$ElderGuardianParticleRenderState fromParticle(
      final ElderGuardianParticle particle, final Camera camera, final float partialTickTime
   ) {
      float ageScale = (particle.age + partialTickTime) / particle.lifetime;
      float alpha = 0.05F + 0.5F * Mth.sin(ageScale * (float) Math.PI);
      int color = ARGB.colorFromFloat(alpha, 1.0F, 1.0F, 1.0F);
      PoseStack poseStack = new PoseStack();
      poseStack.pushPose();
      poseStack.rotate(camera.rotation());
      poseStack.rotateDegrees(Axis.XP, 60.0F - 150.0F * ageScale);
      float scale = 0.42553192F;
      poseStack.scale(0.42553192F, -0.42553192F, -0.42553192F);
      poseStack.translate(0.0F, -0.56F, 3.5F);
      return new ElderGuardianParticleGroup$ElderGuardianParticleRenderState(particle.model, poseStack, particle.renderType, color);
   }
}
