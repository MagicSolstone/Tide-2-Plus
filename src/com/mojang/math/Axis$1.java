package com.mojang.math;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

class Axis$1 implements Axis {
   @Override
   public Quaternionf rotation(final float angle) {
      return new Quaternionf().rotationX(-angle);
   }

   @Override
   public Matrix3f rotate(final Matrix3f matrix, final float angle) {
      return matrix.rotateX(-angle);
   }

   @Override
   public Matrix4f rotate(final Matrix4f matrix, final float angle) {
      return matrix.rotateX(-angle);
   }

   @Override
   public String toString() {
      return "<rotation around -X>";
   }
}
