package net.minecraft.world.entity.animal.bee;

import java.util.Objects;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;

class Bee$BeeEnterHiveGoal extends Bee$BaseBeeGoal {
   private Bee$BeeEnterHiveGoal(final Bee this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(this$0x);
   }

   @Override
   public boolean canBeeUse() {
      if (this.this$0.hivePos != null && this.this$0.wantsToEnterHive() && this.this$0.hivePos.closerToCenterThan(this.this$0.position(), 2.0)) {
         BeehiveBlockEntity beehiveBlockEntity = this.this$0.getBeehiveBlockEntity();
         if (beehiveBlockEntity != null) {
            if (!beehiveBlockEntity.isFull()) {
               return true;
            }

            this.this$0.hivePos = null;
         }
      }

      return false;
   }

   @Override
   public boolean canBeeContinueToUse() {
      return false;
   }

   @Override
   public void start() {
      BeehiveBlockEntity beehiveBlockEntity = this.this$0.getBeehiveBlockEntity();
      if (beehiveBlockEntity != null) {
         beehiveBlockEntity.addOccupant(this.this$0);
      }
   }
}
