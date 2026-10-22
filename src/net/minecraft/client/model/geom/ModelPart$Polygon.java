package net.minecraft.client.model.geom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction$Axis;
import org.joml.Vector3fc;

@Environment(EnvType.CLIENT)
public record ModelPart$Polygon(ModelPart$Vertex[] vertices, Vector3fc normal) {
   public ModelPart$Polygon(
      final ModelPart$Vertex[] vertices,
      final float u0,
      final float v0,
      final float u1,
      final float v1,
      final float xTexSize,
      final float yTexSize,
      final boolean mirror,
      final Direction facing
   ) {
      this(vertices, (mirror ? mirrorFacing(facing) : facing).getUnitVec3f());
      float us = 0.0F / xTexSize;
      float vs = 0.0F / yTexSize;
      vertices[0] = vertices[0].remap(u1 / xTexSize - us, v0 / yTexSize + vs);
      vertices[1] = vertices[1].remap(u0 / xTexSize + us, v0 / yTexSize + vs);
      vertices[2] = vertices[2].remap(u0 / xTexSize + us, v1 / yTexSize - vs);
      vertices[3] = vertices[3].remap(u1 / xTexSize - us, v1 / yTexSize - vs);
      if (mirror) {
         int length = vertices.length;

         for (int i = 0; i < length / 2; i++) {
            ModelPart$Vertex tmp = vertices[i];
            vertices[i] = vertices[length - 1 - i];
            vertices[length - 1 - i] = tmp;
         }
      }
   }

   private static Direction mirrorFacing(final Direction facing) {
      return facing.getAxis() == Direction$Axis.X ? facing.getOpposite() : facing;
   }
}
