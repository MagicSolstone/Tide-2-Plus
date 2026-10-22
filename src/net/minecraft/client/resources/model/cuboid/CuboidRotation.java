package net.minecraft.client.resources.model.cuboid;

import com.mojang.math.MatrixUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Direction$Axis;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

@Environment(EnvType.CLIENT)
public record CuboidRotation(Vector3fc origin, CuboidRotation$RotationValue value, boolean rescale, Matrix4fc transform) {
   public CuboidRotation(final Vector3fc origin, final CuboidRotation$RotationValue value, final boolean rescale) {
      this(origin, value, rescale, computeTransform(value, rescale));
   }

   private static Matrix4f computeTransform(final CuboidRotation$RotationValue value, final boolean rescale) {
      Matrix4f result = value.transformation();
      if (rescale && !MatrixUtil.isIdentity(result)) {
         Vector3fc scale = computeRescale(result);
         result.scale(scale);
      }

      return result;
   }

   private static Vector3fc computeRescale(final Matrix4fc rotation) {
      Vector3f scratch = new Vector3f();
      float scaleX = scaleFactorForAxis(rotation, Direction$Axis.X, scratch);
      float scaleY = scaleFactorForAxis(rotation, Direction$Axis.Y, scratch);
      float scaleZ = scaleFactorForAxis(rotation, Direction$Axis.Z, scratch);
      return scratch.set(scaleX, scaleY, scaleZ);
   }

   private static float scaleFactorForAxis(final Matrix4fc rotation, final Direction$Axis axis, final Vector3f scratch) {
      Vector3f axisUnit = scratch.set(axis.getPositive().getUnitVec3f());
      Vector3f transformedAxisUnit = rotation.transformDirection(axisUnit);
      float absX = Math.abs(transformedAxisUnit.x);
      float absY = Math.abs(transformedAxisUnit.y);
      float absZ = Math.abs(transformedAxisUnit.z);
      float maxComponent = Math.max(Math.max(absX, absY), absZ);
      return 1.0F / maxComponent;
   }
}
