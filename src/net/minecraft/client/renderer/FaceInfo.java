package net.minecraft.client.renderer;

import java.util.EnumMap;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Direction;
import net.minecraft.util.Util;

@Environment(EnvType.CLIENT)
public enum FaceInfo {
   DOWN(
      new FaceInfo$VertexInfo(FaceInfo$Extent.MIN_X, FaceInfo$Extent.MIN_Y, FaceInfo$Extent.MAX_Z),
      new FaceInfo$VertexInfo(FaceInfo$Extent.MIN_X, FaceInfo$Extent.MIN_Y, FaceInfo$Extent.MIN_Z),
      new FaceInfo$VertexInfo(FaceInfo$Extent.MAX_X, FaceInfo$Extent.MIN_Y, FaceInfo$Extent.MIN_Z),
      new FaceInfo$VertexInfo(FaceInfo$Extent.MAX_X, FaceInfo$Extent.MIN_Y, FaceInfo$Extent.MAX_Z)
   ),
   UP(
      new FaceInfo$VertexInfo(FaceInfo$Extent.MIN_X, FaceInfo$Extent.MAX_Y, FaceInfo$Extent.MIN_Z),
      new FaceInfo$VertexInfo(FaceInfo$Extent.MIN_X, FaceInfo$Extent.MAX_Y, FaceInfo$Extent.MAX_Z),
      new FaceInfo$VertexInfo(FaceInfo$Extent.MAX_X, FaceInfo$Extent.MAX_Y, FaceInfo$Extent.MAX_Z),
      new FaceInfo$VertexInfo(FaceInfo$Extent.MAX_X, FaceInfo$Extent.MAX_Y, FaceInfo$Extent.MIN_Z)
   ),
   NORTH(
      new FaceInfo$VertexInfo(FaceInfo$Extent.MAX_X, FaceInfo$Extent.MAX_Y, FaceInfo$Extent.MIN_Z),
      new FaceInfo$VertexInfo(FaceInfo$Extent.MAX_X, FaceInfo$Extent.MIN_Y, FaceInfo$Extent.MIN_Z),
      new FaceInfo$VertexInfo(FaceInfo$Extent.MIN_X, FaceInfo$Extent.MIN_Y, FaceInfo$Extent.MIN_Z),
      new FaceInfo$VertexInfo(FaceInfo$Extent.MIN_X, FaceInfo$Extent.MAX_Y, FaceInfo$Extent.MIN_Z)
   ),
   SOUTH(
      new FaceInfo$VertexInfo(FaceInfo$Extent.MIN_X, FaceInfo$Extent.MAX_Y, FaceInfo$Extent.MAX_Z),
      new FaceInfo$VertexInfo(FaceInfo$Extent.MIN_X, FaceInfo$Extent.MIN_Y, FaceInfo$Extent.MAX_Z),
      new FaceInfo$VertexInfo(FaceInfo$Extent.MAX_X, FaceInfo$Extent.MIN_Y, FaceInfo$Extent.MAX_Z),
      new FaceInfo$VertexInfo(FaceInfo$Extent.MAX_X, FaceInfo$Extent.MAX_Y, FaceInfo$Extent.MAX_Z)
   ),
   WEST(
      new FaceInfo$VertexInfo(FaceInfo$Extent.MIN_X, FaceInfo$Extent.MAX_Y, FaceInfo$Extent.MIN_Z),
      new FaceInfo$VertexInfo(FaceInfo$Extent.MIN_X, FaceInfo$Extent.MIN_Y, FaceInfo$Extent.MIN_Z),
      new FaceInfo$VertexInfo(FaceInfo$Extent.MIN_X, FaceInfo$Extent.MIN_Y, FaceInfo$Extent.MAX_Z),
      new FaceInfo$VertexInfo(FaceInfo$Extent.MIN_X, FaceInfo$Extent.MAX_Y, FaceInfo$Extent.MAX_Z)
   ),
   EAST(
      new FaceInfo$VertexInfo(FaceInfo$Extent.MAX_X, FaceInfo$Extent.MAX_Y, FaceInfo$Extent.MAX_Z),
      new FaceInfo$VertexInfo(FaceInfo$Extent.MAX_X, FaceInfo$Extent.MIN_Y, FaceInfo$Extent.MAX_Z),
      new FaceInfo$VertexInfo(FaceInfo$Extent.MAX_X, FaceInfo$Extent.MIN_Y, FaceInfo$Extent.MIN_Z),
      new FaceInfo$VertexInfo(FaceInfo$Extent.MAX_X, FaceInfo$Extent.MAX_Y, FaceInfo$Extent.MIN_Z)
   );

   private static final Map<Direction, FaceInfo> BY_FACING = Util.make(new EnumMap<>(Direction.class), map -> {
      map.put(Direction.DOWN, DOWN);
      map.put(Direction.UP, UP);
      map.put(Direction.NORTH, NORTH);
      map.put(Direction.SOUTH, SOUTH);
      map.put(Direction.WEST, WEST);
      map.put(Direction.EAST, EAST);
   });
   private final FaceInfo$VertexInfo[] infos;

   public static FaceInfo fromFacing(final Direction direction) {
      return BY_FACING.get(direction);
   }

   private FaceInfo(final FaceInfo$VertexInfo... infos) {
      this.infos = infos;
   }

   public FaceInfo$VertexInfo getVertexInfo(final int index) {
      return this.infos[index];
   }
}
