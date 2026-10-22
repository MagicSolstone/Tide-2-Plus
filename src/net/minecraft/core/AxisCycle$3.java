package net.minecraft.core;

enum AxisCycle$3 {
   @Override
   public int cycle(final int x, final int y, final int z, final Direction$Axis axis) {
      return axis.choose(y, z, x);
   }

   @Override
   public double cycle(final double x, final double y, final double z, final Direction$Axis axis) {
      return axis.choose(y, z, x);
   }

   @Override
   public Direction$Axis cycle(final Direction$Axis axis) {
      return AXIS_VALUES[Math.floorMod(axis.ordinal() - 1, 3)];
   }

   @Override
   public AxisCycle inverse() {
      return FORWARD;
   }
}
