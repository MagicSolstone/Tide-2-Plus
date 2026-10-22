package net.minecraft.client.renderer.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
enum BlockModelLighter$SizeInfo {
   DOWN(0),
   UP(1),
   NORTH(2),
   SOUTH(3),
   WEST(4),
   EAST(5),
   FLIP_DOWN(6),
   FLIP_UP(7),
   FLIP_NORTH(8),
   FLIP_SOUTH(9),
   FLIP_WEST(10),
   FLIP_EAST(11);

   public static final int COUNT = values().length;
   private final int index;

   private BlockModelLighter$SizeInfo(final int index) {
      this.index = index;
   }
}
