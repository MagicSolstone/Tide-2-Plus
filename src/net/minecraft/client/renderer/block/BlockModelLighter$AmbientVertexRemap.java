package net.minecraft.client.renderer.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Direction;
import net.minecraft.util.Util;

@Environment(EnvType.CLIENT)
enum BlockModelLighter$AmbientVertexRemap {
   DOWN(0, 1, 2, 3),
   UP(2, 3, 0, 1),
   NORTH(3, 0, 1, 2),
   SOUTH(0, 1, 2, 3),
   WEST(3, 0, 1, 2),
   EAST(1, 2, 3, 0);

   private final int vert0;
   private final int vert1;
   private final int vert2;
   private final int vert3;
   private static final BlockModelLighter$AmbientVertexRemap[] BY_FACING = Util.make(new BlockModelLighter$AmbientVertexRemap[6], map -> {
      map[Direction.DOWN.get3DDataValue()] = DOWN;
      map[Direction.UP.get3DDataValue()] = UP;
      map[Direction.NORTH.get3DDataValue()] = NORTH;
      map[Direction.SOUTH.get3DDataValue()] = SOUTH;
      map[Direction.WEST.get3DDataValue()] = WEST;
      map[Direction.EAST.get3DDataValue()] = EAST;
   });

   private BlockModelLighter$AmbientVertexRemap(final int vert0, final int vert1, final int vert2, final int vert3) {
      this.vert0 = vert0;
      this.vert1 = vert1;
      this.vert2 = vert2;
      this.vert3 = vert3;
   }

   public static BlockModelLighter$AmbientVertexRemap fromFacing(final Direction direction) {
      return BY_FACING[direction.get3DDataValue()];
   }
}
