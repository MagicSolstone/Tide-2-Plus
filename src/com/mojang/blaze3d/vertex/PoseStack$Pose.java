package com.mojang.blaze3d.vertex;

import com.mojang.math.Axis;
import com.mojang.math.MatrixUtil;
import com.mojang.math.Transformation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

@Environment(EnvType.CLIENT)
public final class PoseStack$Pose {
   private final Matrix4f pose = new Matrix4f();
   private final Matrix3f normal = new Matrix3f();
   private boolean trustedNormals = true;

   private void computeNormalMatrix() {
      this.normal.set(this.pose).invert().transpose();
      this.trustedNormals = false;
   }

   public void set(final PoseStack$Pose pose) {
      this.pose.set(pose.pose);
      this.normal.set(pose.normal);
      this.trustedNormals = pose.trustedNormals;
   }

   public Matrix4f pose() {
      return this.pose;
   }

   public Matrix3f normal() {
      return this.normal;
   }

   public Vector3f transformNormal(final Vector3fc normal, final Vector3f destination) {
      return this.transformNormal(normal.x(), normal.y(), normal.z(), destination);
   }

   public Vector3f transformNormal(final float x, final float y, final float z, final Vector3f destination) {
      Vector3f result = this.normal.transform(x, y, z, destination);
      return this.trustedNormals ? result : result.normalize();
   }

   public Matrix4f translate(final float xo, final float yo, final float zo) {
      return this.pose.translate(xo, yo, zo);
   }

   public void scale(final float xScale, final float yScale, final float zScale) {
      this.pose.scale(xScale, yScale, zScale);
      if (Math.abs(xScale) == Math.abs(yScale) && Math.abs(yScale) == Math.abs(zScale)) {
         if (xScale < 0.0F || yScale < 0.0F || zScale < 0.0F) {
            this.normal.scale(Math.signum(xScale), Math.signum(yScale), Math.signum(zScale));
         }
      } else {
         this.normal.scale(1.0F / xScale, 1.0F / yScale, 1.0F / zScale);
         this.trustedNormals = false;
      }
   }

   public void rotate(final Quaternionfc by) {
      this.pose.rotate(by);
      this.normal.rotate(by);
   }

   public void rotate(final Axis axis, final float angle) {
      axis.rotate(this.pose, angle);
      axis.rotate(this.normal, angle);
   }

   public void rotateDegrees(final Axis axis, final float angle) {
      axis.rotateDegrees(this.pose, angle);
      axis.rotateDegrees(this.normal, angle);
   }

   public void rotateAround(final Quaternionfc rotation, final float pivotX, final float pivotY, final float pivotZ) {
      this.pose.rotateAround(rotation, pivotX, pivotY, pivotZ);
      this.normal.rotate(rotation);
   }

   public void setIdentity() {
      this.pose.identity();
      this.normal.identity();
      this.trustedNormals = true;
   }

   public void mulPose(final Matrix4fc matrix) {
      this.pose.mul(matrix);
      if (!MatrixUtil.isPureTranslation(matrix)) {
         if (MatrixUtil.checkPropertyRaw(matrix, 16)) {
            this.normal.mul(new Matrix3f(matrix));
         } else {
            this.computeNormalMatrix();
         }
      }
   }

   public void mulPose(final Transformation transformation) {
      this.mulPose(transformation.getMatrix());
   }

   public PoseStack$Pose copy() {
      PoseStack$Pose pose = new PoseStack$Pose();
      pose.set(this);
      return pose;
   }
}
