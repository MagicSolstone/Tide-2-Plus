package net.minecraft.world.entity;

import net.minecraft.core.Holder;
import net.minecraft.world.level.material.Fluid;

class EntityFluidInteraction$Tracker {
   private final Holder<Fluid> fluidType;
   private double height;
   private boolean eyesInside;

   public EntityFluidInteraction$Tracker(final Holder<Fluid> fluidType) {
      this.fluidType = fluidType;
   }

   public boolean reset() {
      if (this.height == 0.0 && !this.eyesInside) {
         return true;
      } else {
         this.height = 0.0;
         this.eyesInside = false;
         return false;
      }
   }
}
