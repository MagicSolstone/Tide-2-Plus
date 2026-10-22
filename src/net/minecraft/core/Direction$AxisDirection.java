package net.minecraft.core;

public enum Direction$AxisDirection {
   POSITIVE(1, "Towards positive"),
   NEGATIVE(-1, "Towards negative");

   private final int step;
   private final String name;

   private Direction$AxisDirection(final int step, final String name) {
      this.step = step;
      this.name = name;
   }

   public int getStep() {
      return this.step;
   }

   public String getName() {
      return this.name;
   }

   @Override
   public String toString() {
      return this.name;
   }

   public Direction$AxisDirection opposite() {
      return this == POSITIVE ? NEGATIVE : POSITIVE;
   }
}
