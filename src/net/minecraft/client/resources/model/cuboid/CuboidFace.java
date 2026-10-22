package net.minecraft.client.resources.model.cuboid;

import com.mojang.math.Quadrant;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Direction;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public record CuboidFace(@Nullable Direction cullForDirection, int tintIndex, String texture, @Nullable CuboidFace$UVs uvs, Quadrant rotation) {
   public static final int NO_TINT = -1;

   public static float getU(final CuboidFace$UVs uvs, final Quadrant rotation, final int vertex) {
      return uvs.getVertexU(rotation.rotateVertexIndex(vertex)) / 16.0F;
   }

   public static float getV(final CuboidFace$UVs uvs, final Quadrant rotation, final int index) {
      return uvs.getVertexV(rotation.rotateVertexIndex(index)) / 16.0F;
   }
}
