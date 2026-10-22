package net.minecraft.world.level;

import net.minecraft.core.BlockPos;

class PotentialCalculator$PointCharge {
   private final BlockPos pos;
   private final double charge;

   public PotentialCalculator$PointCharge(final BlockPos pos, final double charge) {
      this.pos = pos;
      this.charge = charge;
   }

   public double getPotentialChange(final BlockPos pos) {
      double distSqr = this.pos.distSqr(pos);
      return distSqr == 0.0 ? Double.POSITIVE_INFINITY : this.charge / Math.sqrt(distSqr);
   }
}
