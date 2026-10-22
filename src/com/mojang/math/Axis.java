package com.mojang.math;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3fc;

public interface Axis {
   Axis XN = new Axis$1();
   Axis XP = new Axis$2();
   Axis YN = new Axis$3();
   Axis YP = new Axis$4();
   Axis ZN = new Axis$5();
   Axis ZP = new Axis$6();

   static Axis of(final Vector3fc axis) {
      return new Axis$7(axis);
   }

   Quaternionf rotation(float angle);

   default Quaternionf rotationDegrees(final float angle) {
      return this.rotation(angle * (float) (Math.PI / 180.0));
   }

   Matrix3f rotate(Matrix3f matrix, float angle);

   default Matrix3f rotateDegrees(final Matrix3f matrix, final float angle) {
      return this.rotate(matrix, angle * (float) (Math.PI / 180.0));
   }

   Matrix4f rotate(Matrix4f matrix, float angle);

   default Matrix4f rotateDegrees(final Matrix4f matrix, final float angle) {
      return this.rotate(matrix, angle * (float) (Math.PI / 180.0));
   }
}
