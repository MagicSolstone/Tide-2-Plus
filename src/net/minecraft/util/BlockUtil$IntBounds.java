package net.minecraft.util;

public class BlockUtil$IntBounds {
   public final int min;
   public final int max;

   public BlockUtil$IntBounds(final int min, final int max) {
      this.min = min;
      this.max = max;
   }

   @Override
   public String toString() {
      return "IntBounds{min=" + this.min + ", max=" + this.max + "}";
   }
}
