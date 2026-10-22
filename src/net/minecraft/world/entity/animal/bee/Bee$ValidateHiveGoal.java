package net.minecraft.world.entity.animal.bee;

import java.util.Objects;
import net.minecraft.util.Mth;

class Bee$ValidateHiveGoal extends Bee$BaseBeeGoal {
   private final int VALIDATE_HIVE_COOLDOWN;
   private long lastValidateTick;

   private Bee$ValidateHiveGoal(final Bee this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(this$0x);
      this.VALIDATE_HIVE_COOLDOWN = Mth.nextInt(Bee.access$3100(this.this$0), 20, 40);
      this.lastValidateTick = -1L;
   }

   @Override
   public void start() {
      if (this.this$0.hivePos != null && this.this$0.level().isLoaded(this.this$0.hivePos) && !this.this$0.isHiveValid()) {
         this.this$0.dropHive();
      }

      this.lastValidateTick = this.this$0.level().getGameTime();
   }

   @Override
   public boolean canBeeUse() {
      return this.this$0.level().getGameTime() > this.lastValidateTick + this.VALIDATE_HIVE_COOLDOWN;
   }

   @Override
   public boolean canBeeContinueToUse() {
      return false;
   }
}
