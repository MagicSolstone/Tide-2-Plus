package net.minecraft.util;

import net.minecraft.core.BlockPos;

public class BlockUtil$FoundRectangle {
   public final BlockPos minCorner;
   public final int axis1Size;
   public final int axis2Size;

   public BlockUtil$FoundRectangle(final BlockPos minCorner, final int axis1Size, final int axis2Size) {
      this.minCorner = minCorner;
      this.axis1Size = axis1Size;
      this.axis2Size = axis2Size;
   }
}
