package com.mojang.blaze3d.vertex;

import com.mojang.math.Axis;
import com.mojang.math.Transformation;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4fc;
import org.joml.Quaternionfc;

@Environment(EnvType.CLIENT)
public class PoseStack {
   private final List<PoseStack$Pose> poses = new ArrayList<>(16);
   private int lastIndex;

   public PoseStack() {
      this.poses.add(new PoseStack$Pose());
   }

   public void translate(final double xo, final double yo, final double zo) {
      this.translate((float)xo, (float)yo, (float)zo);
   }

   public void translate(final float xo, final float yo, final float zo) {
      this.last().translate(xo, yo, zo);
   }

   public void translate(final Vec3 offset) {
      this.translate(offset.x, offset.y, offset.z);
   }

   public void scale(final float xScale, final float yScale, final float zScale) {
      this.last().scale(xScale, yScale, zScale);
   }

   public void rotate(final Quaternionfc by) {
      this.last().rotate(by);
   }

   public void rotate(final Axis axis, final float angle) {
      this.last().rotate(axis, angle);
   }

   public void rotateDegrees(final Axis axis, final float angle) {
      this.last().rotateDegrees(axis, angle);
   }

   public void rotateAround(final Quaternionfc rotation, final float pivotX, final float pivotY, final float pivotZ) {
      this.last().rotateAround(rotation, pivotX, pivotY, pivotZ);
   }

   public void pushPose() {
      PoseStack$Pose lastPose = this.last();
      this.lastIndex++;
      if (this.lastIndex >= this.poses.size()) {
         this.poses.add(lastPose.copy());
      } else {
         this.poses.get(this.lastIndex).set(lastPose);
      }
   }

   public void popPose() {
      if (this.lastIndex == 0) {
         throw new NoSuchElementException();
      } else {
         this.lastIndex--;
      }
   }

   public PoseStack$Pose last() {
      return this.poses.get(this.lastIndex);
   }

   public boolean isEmpty() {
      return this.lastIndex == 0;
   }

   public void setIdentity() {
      this.last().setIdentity();
   }

   public void mulPose(final Matrix4fc matrix) {
      this.last().mulPose(matrix);
   }

   public void mulPose(final Transformation matrix) {
      this.last().mulPose(matrix);
   }
}
