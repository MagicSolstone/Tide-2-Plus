package net.minecraft.world.entity.vehicle.minecart;

class NewMinecartBehavior$TrackIteration {
   public double movementLeft = 0.0;
   public boolean firstIteration = true;
   public boolean hasGainedSlopeSpeed = false;
   public boolean hasHalted = false;
   public boolean hasBoosted = false;

   private NewMinecartBehavior$TrackIteration() {
   }

   public boolean shouldIterate() {
      return this.firstIteration || this.movementLeft > 1.0E-5F;
   }
}
