package net.minecraft.client.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Vector3fc;

@Environment(EnvType.CLIENT)
public enum FaceInfo$Extent {
   MIN_X,
   MIN_Y,
   MIN_Z,
   MAX_X,
   MAX_Y,
   MAX_Z;

   public float select(final Vector3fc min, final Vector3fc max) {
      return switch (this) {
         case MIN_X -> min.x();
         case MIN_Y -> min.y();
         case MIN_Z -> min.z();
         case MAX_X -> max.x();
         case MAX_Y -> max.y();
         case MAX_Z -> max.z();
      };
   }

   public float select(final float minX, final float minY, final float minZ, final float maxX, final float maxY, final float maxZ) {
      return switch (this) {
         case MIN_X -> minX;
         case MIN_Y -> minY;
         case MIN_Z -> minZ;
         case MAX_X -> maxX;
         case MAX_Y -> maxY;
         case MAX_Z -> maxZ;
      };
   }
}
