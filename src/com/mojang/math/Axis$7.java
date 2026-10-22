package com.mojang.math;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3fc;

class Axis$7 implements Axis {
   Axis$7(final Vector3fc val$axis) {
      this.val$axis = val$axisx;
   }

   @Override
   public Quaternionf rotation(final float angle) {
      return new Quaternionf().rotationAxis(angle, this.val$axis);
   }

   @Override
   public Matrix3f rotate(final Matrix3f matrix, final float angle) {
      return matrix.rotate(angle, this.val$axis);
   }

   @Override
   public Matrix4f rotate(final Matrix4f matrix, final float angle) {
      return matrix.rotate(angle, this.val$axis);
   }

   @Override
   public String toString() {
      return "<rotation around " + this.val$axis + ">";
   }
}
