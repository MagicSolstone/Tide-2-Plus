package net.minecraft.client.renderer.block.dispatch;

import com.mojang.math.Transformation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Direction;
import org.joml.Matrix4fc;

@Environment(EnvType.CLIENT)
record BlockModelRotation$WithUvLock(BlockModelRotation parent) implements ModelState {
   @Override
   public Transformation transformation() {
      return this.parent.transformation;
   }

   @Override
   public Matrix4fc faceTransformation(final Direction face) {
      return this.parent.faceMapping.getOrDefault(face, NO_TRANSFORM);
   }

   @Override
   public Matrix4fc inverseFaceTransformation(final Direction face) {
      return this.parent.inverseFaceMapping.getOrDefault(face, NO_TRANSFORM);
   }

   @Override
   public String toString() {
      return "uvLocked[" + this.parent.orientation.getSerializedName() + "]";
   }
}
