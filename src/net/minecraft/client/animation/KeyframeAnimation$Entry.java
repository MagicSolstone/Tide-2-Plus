package net.minecraft.client.animation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
record KeyframeAnimation$Entry(ModelPart part, AnimationChannel$Target target, Keyframe[] keyframes) {
   public void apply(final float secondsSinceStart, final float targetScale, final Vector3f scratchVector) {
      int prev = Math.max(0, Mth.binarySearch(0, this.keyframes.length, i -> secondsSinceStart <= this.keyframes[i].timestamp()) - 1);
      int next = Math.min(this.keyframes.length - 1, prev + 1);
      Keyframe previousFrame = this.keyframes[prev];
      Keyframe nextFrame = this.keyframes[next];
      float keyframeTimeDelta = secondsSinceStart - previousFrame.timestamp();
      float lerpAlpha;
      if (next != prev) {
         lerpAlpha = Mth.clamp(keyframeTimeDelta / (nextFrame.timestamp() - previousFrame.timestamp()), 0.0F, 1.0F);
      } else {
         lerpAlpha = 0.0F;
      }

      nextFrame.interpolation().apply(scratchVector, lerpAlpha, this.keyframes, prev, next, targetScale);
      this.target.apply(this.part, scratchVector);
   }
}
