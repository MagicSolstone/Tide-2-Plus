package net.minecraft.client.animation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Vector3fc;

@Environment(EnvType.CLIENT)
public record Keyframe(float timestamp, Vector3fc preTarget, Vector3fc postTarget, AnimationChannel$Interpolation interpolation) {
   public Keyframe(final float timestamp, final Vector3fc postTarget, final AnimationChannel$Interpolation interpolation) {
      this(timestamp, postTarget, postTarget, interpolation);
   }
}
