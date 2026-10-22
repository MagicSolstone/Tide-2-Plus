package net.minecraft.client.resources.model.cuboid;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Matrix4f;

@Environment(EnvType.CLIENT)
public record CuboidRotation$EulerXYZRotation(float x, float y, float z) implements CuboidRotation$RotationValue {
   @Override
   public Matrix4f transformation() {
      return new Matrix4f().rotationZYX(this.z * (float) (Math.PI / 180.0), this.y * (float) (Math.PI / 180.0), this.x * (float) (Math.PI / 180.0));
   }
}
