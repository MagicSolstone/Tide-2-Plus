package net.minecraft.world.level.block.entity;

import java.util.Objects;
import net.minecraft.world.inventory.ContainerData;

class AbstractFurnaceBlockEntity$1 implements ContainerData {
   AbstractFurnaceBlockEntity$1(final AbstractFurnaceBlockEntity this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
   }

   @Override
   public int get(final int dataId) {
      switch (dataId) {
         case 0:
            return this.this$0.litTimeRemaining;
         case 1:
            return this.this$0.litTotalTime;
         case 2:
            return this.this$0.cookingTimer;
         case 3:
            return this.this$0.cookingTotalTime;
         default:
            return 0;
      }
   }

   @Override
   public void set(final int dataId, final int value) {
      switch (dataId) {
         case 0:
            this.this$0.litTimeRemaining = value;
            break;
         case 1:
            this.this$0.litTotalTime = value;
            break;
         case 2:
            this.this$0.cookingTimer = value;
            break;
         case 3:
            this.this$0.cookingTotalTime = value;
      }
   }

   @Override
   public int getCount() {
      return 4;
   }
}
