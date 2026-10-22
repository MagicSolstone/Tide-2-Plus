package net.minecraft.core;

public enum AxisCycle {
   NONE,
   FORWARD,
   BACKWARD;

   public static final Direction$Axis[] AXIS_VALUES = Direction$Axis.values();
   public static final AxisCycle[] VALUES = values();

   public abstract int cycle(final int x, final int y, final int z, final Direction$Axis axis);

   public abstract double cycle(final double x, final double y, final double z, final Direction$Axis axis);

   public abstract Direction$Axis cycle(final Direction$Axis axis);

   public abstract AxisCycle inverse();

   public static AxisCycle between(final Direction$Axis from, final Direction$Axis to) {
      return VALUES[Math.floorMod(to.ordinal() - from.ordinal(), 3)];
   }
}
