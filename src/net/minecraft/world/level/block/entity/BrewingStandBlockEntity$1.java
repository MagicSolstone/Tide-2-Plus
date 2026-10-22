package net.minecraft.world.level.block.entity;

import java.util.Objects;
import net.minecraft.world.inventory.ContainerData;

class BrewingStandBlockEntity$1 implements ContainerData {
   BrewingStandBlockEntity$1(final BrewingStandBlockEntity this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public int get(final int dataId) {
      return switch (dataId) {
         case 0 -> this.this$0.brewTime;
         case 1 -> this.this$0.fuel;
         case 2 -> this.this$0.totalBrewTime;
         case 3 -> this.this$0.totalFuel;
         default -> 0;
      };
   }

   @Override
   public void set(final int dataId, final int value) {
      switch (dataId) {
         case 0:
            this.this$0.brewTime = value;
            break;
         case 1:
            this.this$0.fuel = value;
            break;
         case 2:
            this.this$0.totalBrewTime = value;
            break;
         case 3:
            this.this$0.totalFuel = value;
      }
   }

   @Override
   public int getCount() {
      return 4;
   }
}
