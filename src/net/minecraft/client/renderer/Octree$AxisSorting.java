package net.minecraft.client.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
enum Octree$AxisSorting {
   XYZ(4, 2, 1),
   XZY(4, 1, 2),
   YXZ(2, 4, 1),
   YZX(1, 4, 2),
   ZXY(2, 1, 4),
   ZYX(1, 2, 4);

   private final int xShift;
   private final int yShift;
   private final int zShift;

   private Octree$AxisSorting(final int xShift, final int yShift, final int zShift) {
      this.xShift = xShift;
      this.yShift = yShift;
      this.zShift = zShift;
   }

   public static Octree$AxisSorting getAxisSorting(final int absXDiff, final int absYDiff, final int absZDiff) {
      if (absXDiff > absYDiff && absXDiff > absZDiff) {
         return absYDiff > absZDiff ? XYZ : XZY;
      } else if (absYDiff > absXDiff && absYDiff > absZDiff) {
         return absXDiff > absZDiff ? YXZ : YZX;
      } else {
         return absXDiff > absYDiff ? ZXY : ZYX;
      }
   }
}
