package net.minecraft.client.model.geom;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record ModelPart$Vertex(float x, float y, float z, float u, float v) {
   public static final float SCALE_FACTOR = 16.0F;

   public ModelPart$Vertex remap(final float u, final float v) {
      return new ModelPart$Vertex(this.x, this.y, this.z, u, v);
   }

   public float worldX() {
      return this.x / 16.0F;
   }

   public float worldY() {
      return this.y / 16.0F;
   }

   public float worldZ() {
      return this.z / 16.0F;
   }
}
