package net.minecraft.client.animation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public interface AnimationChannel$Interpolation {
   Vector3f apply(final Vector3f vector, final float alpha, final Keyframe[] keyframes, final int prev, final int next, final float targetScale);
}
