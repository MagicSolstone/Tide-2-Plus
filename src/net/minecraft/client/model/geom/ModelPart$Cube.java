package net.minecraft.client.model.geom;

import com.mojang.blaze3d.vertex.PoseStack$Pose;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Direction;
import org.joml.Matrix4f;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public class ModelPart$Cube {
   public final ModelPart$Polygon[] polygons;
   public final float minX;
   public final float minY;
   public final float minZ;
   public final float maxX;
   public final float maxY;
   public final float maxZ;

   public ModelPart$Cube(
      final int xTexOffs,
      final int yTexOffs,
      float minX,
      float minY,
      float minZ,
      final float width,
      final float height,
      final float depth,
      final float growX,
      final float growY,
      final float growZ,
      final boolean mirror,
      final float xTexSize,
      final float yTexSize,
      final Set<Direction> visibleFaces
   ) {
      this.minX = minX;
      this.minY = minY;
      this.minZ = minZ;
      this.maxX = minX + width;
      this.maxY = minY + height;
      this.maxZ = minZ + depth;
      this.polygons = new ModelPart$Polygon[visibleFaces.size()];
      float maxX = minX + width;
      float maxY = minY + height;
      float maxZ = minZ + depth;
      minX -= growX;
      minY -= growY;
      minZ -= growZ;
      maxX += growX;
      maxY += growY;
      maxZ += growZ;
      if (mirror) {
         float tmp = maxX;
         maxX = minX;
         minX = tmp;
      }

      ModelPart$Vertex t0 = new ModelPart$Vertex(minX, minY, minZ, 0.0F, 0.0F);
      ModelPart$Vertex t1 = new ModelPart$Vertex(maxX, minY, minZ, 0.0F, 8.0F);
      ModelPart$Vertex t2 = new ModelPart$Vertex(maxX, maxY, minZ, 8.0F, 8.0F);
      ModelPart$Vertex t3 = new ModelPart$Vertex(minX, maxY, minZ, 8.0F, 0.0F);
      ModelPart$Vertex l0 = new ModelPart$Vertex(minX, minY, maxZ, 0.0F, 0.0F);
      ModelPart$Vertex l1 = new ModelPart$Vertex(maxX, minY, maxZ, 0.0F, 8.0F);
      ModelPart$Vertex l2 = new ModelPart$Vertex(maxX, maxY, maxZ, 8.0F, 8.0F);
      ModelPart$Vertex l3 = new ModelPart$Vertex(minX, maxY, maxZ, 8.0F, 0.0F);
      float u0 = xTexOffs;
      float u1 = xTexOffs + depth;
      float u2 = xTexOffs + depth + width;
      float u22 = xTexOffs + depth + width + width;
      float u3 = xTexOffs + depth + width + depth;
      float u4 = xTexOffs + depth + width + depth + width;
      float v0 = yTexOffs;
      float v1 = yTexOffs + depth;
      float v2 = yTexOffs + depth + height;
      int pos = 0;
      if (visibleFaces.contains(Direction.DOWN)) {
         this.polygons[pos++] = new ModelPart$Polygon(new ModelPart$Vertex[]{l1, l0, t0, t1}, u1, v0, u2, v1, xTexSize, yTexSize, mirror, Direction.DOWN);
      }

      if (visibleFaces.contains(Direction.UP)) {
         this.polygons[pos++] = new ModelPart$Polygon(new ModelPart$Vertex[]{t2, t3, l3, l2}, u2, v1, u22, v0, xTexSize, yTexSize, mirror, Direction.UP);
      }

      if (visibleFaces.contains(Direction.WEST)) {
         this.polygons[pos++] = new ModelPart$Polygon(new ModelPart$Vertex[]{t0, l0, l3, t3}, u0, v1, u1, v2, xTexSize, yTexSize, mirror, Direction.WEST);
      }

      if (visibleFaces.contains(Direction.NORTH)) {
         this.polygons[pos++] = new ModelPart$Polygon(new ModelPart$Vertex[]{t1, t0, t3, t2}, u1, v1, u2, v2, xTexSize, yTexSize, mirror, Direction.NORTH);
      }

      if (visibleFaces.contains(Direction.EAST)) {
         this.polygons[pos++] = new ModelPart$Polygon(new ModelPart$Vertex[]{l1, t1, t2, l2}, u2, v1, u3, v2, xTexSize, yTexSize, mirror, Direction.EAST);
      }

      if (visibleFaces.contains(Direction.SOUTH)) {
         this.polygons[pos] = new ModelPart$Polygon(new ModelPart$Vertex[]{l0, l1, l2, l3}, u3, v1, u4, v2, xTexSize, yTexSize, mirror, Direction.SOUTH);
      }
   }

   public void compile(final PoseStack$Pose pose, final VertexConsumer builder, final int lightCoords, final int overlayCoords, final int color) {
      Matrix4f matrix = pose.pose();
      Vector3f scratchVector = new Vector3f();

      for (ModelPart$Polygon polygon : this.polygons) {
         Vector3f normal = pose.transformNormal(polygon.normal, scratchVector);
         float nx = normal.x();
         float ny = normal.y();
         float nz = normal.z();

         for (ModelPart$Vertex vertex : polygon.vertices) {
            float x = vertex.worldX();
            float y = vertex.worldY();
            float z = vertex.worldZ();
            Vector3f pos = matrix.transformPosition(x, y, z, scratchVector);
            builder.addVertex(pos.x(), pos.y(), pos.z(), color, vertex.u, vertex.v, overlayCoords, lightCoords, nx, ny, nz);
         }
      }
   }
}
