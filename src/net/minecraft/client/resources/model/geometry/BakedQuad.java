package net.minecraft.client.resources.model.geometry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Direction;
import org.joml.Vector3fc;

@Environment(EnvType.CLIENT)
public record BakedQuad(
   Vector3fc position0,
   Vector3fc position1,
   Vector3fc position2,
   Vector3fc position3,
   long packedUV0,
   long packedUV1,
   long packedUV2,
   long packedUV3,
   Direction direction,
   BakedQuad$MaterialInfo materialInfo
) {
   public static final int VERTEX_COUNT = 4;
   public static final int FLAG_TRANSLUCENT = 1;
   public static final int FLAG_ANIMATED = 2;

   public Vector3fc position(final int vertex) {
      return switch (vertex) {
         case 0 -> this.position0;
         case 1 -> this.position1;
         case 2 -> this.position2;
         case 3 -> this.position3;
         default -> throw new IndexOutOfBoundsException(vertex);
      };
   }

   public long packedUV(final int vertex) {
      return switch (vertex) {
         case 0 -> this.packedUV0;
         case 1 -> this.packedUV1;
         case 2 -> this.packedUV2;
         case 3 -> this.packedUV3;
         default -> throw new IndexOutOfBoundsException(vertex);
      };
   }
}
