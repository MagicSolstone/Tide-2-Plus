package net.minecraft.client.animation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Mth;
import org.joml.Vector3fc;

@Environment(EnvType.CLIENT)
public class AnimationChannel$Interpolations {
   public static final AnimationChannel$Interpolation LINEAR = (vector, alpha, keyframes, prev, next, targetScale) -> {
      Vector3fc point0 = keyframes[prev].postTarget();
      Vector3fc point1 = keyframes[next].preTarget();
      return point0.lerp(point1, alpha, vector).mul(targetScale);
   };
   public static final AnimationChannel$Interpolation CATMULLROM = (vector, alpha, keyframes, prev, next, targetScale) -> {
      Vector3fc point0 = keyframes[Math.max(0, prev - 1)].postTarget();
      Vector3fc point1 = keyframes[prev].postTarget();
      Vector3fc point2 = keyframes[next].postTarget();
      Vector3fc point3 = keyframes[Math.min(keyframes.length - 1, next + 1)].postTarget();
      vector.set(
         Mth.catmullrom(alpha, point0.x(), point1.x(), point2.x(), point3.x()) * targetScale,
         Mth.catmullrom(alpha, point0.y(), point1.y(), point2.y(), point3.y()) * targetScale,
         Mth.catmullrom(alpha, point0.z(), point1.z(), point2.z(), point3.z()) * targetScale
      );
      return vector;
   };
}
