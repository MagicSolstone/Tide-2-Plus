package net.minecraft.world.entity.animal.bee;

import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;

class Bee$ValidateFlowerGoal extends Bee$BaseBeeGoal {
   private final int validateFlowerCooldown;
   private long lastValidateTick;

   private Bee$ValidateFlowerGoal(final Bee this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(this$0x);
      this.validateFlowerCooldown = Mth.nextInt(Bee.access$3000(this.this$0), 20, 40);
      this.lastValidateTick = -1L;
   }

   @Override
   public void start() {
      if (this.this$0.savedFlowerPos != null && this.this$0.level().isLoaded(this.this$0.savedFlowerPos) && !this.isFlower(this.this$0.savedFlowerPos)) {
         this.this$0.dropFlower();
      }

      this.lastValidateTick = this.this$0.level().getGameTime();
   }

   @Override
   public boolean canBeeUse() {
      return this.this$0.level().getGameTime() > this.lastValidateTick + this.validateFlowerCooldown;
   }

   @Override
   public boolean canBeeContinueToUse() {
      return false;
   }

   private boolean isFlower(final BlockPos flowerPos) {
      return Bee.attractsBees(this.this$0.level().getBlockState(flowerPos));
   }
}
