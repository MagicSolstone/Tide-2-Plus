package net.minecraft.world.level.block.entity;

public class BeaconBeamOwner$Section {
   private final int color;
   private int height;

   public BeaconBeamOwner$Section(final int color) {
      this.color = color;
      this.height = 1;
   }

   public void increaseHeight() {
      this.height++;
   }

   public int getColor() {
      return this.color;
   }

   public int getHeight() {
      return this.height;
   }
}
