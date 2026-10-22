package net.minecraft.core;

enum AxisCycle$1 {
   @Override
   public int cycle(final int x, final int y, final int z, final Direction$Axis axis) {
      return axis.choose(x, y, z);
   }

   @Override
   public double cycle(final double x, final double y, final double z, final Direction$Axis axis) {
      return axis.choose(x, y, z);
   }

   @Override
   public Direction$Axis cycle(final Direction$Axis axis) {
      return axis;
   }

   @Override
   public AxisCycle inverse() {
      return this;
   }
}
