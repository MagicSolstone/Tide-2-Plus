package net.minecraft.client.resources.model.cuboid;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public record CuboidFace$UVs(float minU, float minV, float maxU, float maxV) {
   public float getVertexU(final int index) {
      return index != 0 && index != 1 ? this.maxU : this.minU;
   }

   public float getVertexV(final int index) {
      return index != 0 && index != 3 ? this.maxV : this.minV;
   }
}
