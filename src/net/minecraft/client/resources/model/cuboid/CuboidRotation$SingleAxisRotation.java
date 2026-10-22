package net.minecraft.client.resources.model.cuboid;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Direction$Axis;
import org.joml.Matrix4f;
import org.joml.Vector3fc;

@Environment(EnvType.CLIENT)
public record CuboidRotation$SingleAxisRotation(Direction$Axis axis, float angle) implements CuboidRotation$RotationValue {
   @Override
   public Matrix4f transformation() {
      Matrix4f result = new Matrix4f();
      if (this.angle == 0.0F) {
         return result;
      } else {
         Vector3fc rotateAround = this.axis.getPositive().getUnitVec3f();
         result.rotation(this.angle * (float) (Math.PI / 180.0), rotateAround);
         return result;
      }
   }
}
