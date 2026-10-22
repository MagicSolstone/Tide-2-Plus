package net.minecraft.client.resources.model.cuboid;

import com.mojang.blaze3d.vertex.PoseStack$Pose;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector3fc;

@Environment(EnvType.CLIENT)
public record ItemTransform(Vector3fc rotation, Vector3fc translation, Vector3fc scale) {
   public static final ItemTransform NO_TRANSFORM = new ItemTransform(new Vector3f(), new Vector3f(), new Vector3f(1.0F, 1.0F, 1.0F));

   public void apply(final boolean applyLeftHandFix, final PoseStack$Pose pose) {
      if (this == NO_TRANSFORM) {
         pose.translate(-0.5F, -0.5F, -0.5F);
      } else {
         float translationX;
         float rotY;
         float rotZ;
         if (applyLeftHandFix) {
            translationX = -this.translation.x();
            rotY = -this.rotation.y();
            rotZ = -this.rotation.z();
         } else {
            translationX = this.translation.x();
            rotY = this.rotation.y();
            rotZ = this.rotation.z();
         }

         pose.translate(translationX, this.translation.y(), this.translation.z());
         pose.rotate(
            new Quaternionf().rotationXYZ(this.rotation.x() * (float) (Math.PI / 180.0), rotY * (float) (Math.PI / 180.0), rotZ * (float) (Math.PI / 180.0))
         );
         pose.scale(this.scale.x(), this.scale.y(), this.scale.z());
         pose.translate(-0.5F, -0.5F, -0.5F);
      }
   }
}
