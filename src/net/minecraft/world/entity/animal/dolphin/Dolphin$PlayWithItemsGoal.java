package net.minecraft.world.entity.animal.dolphin;

import java.util.Objects;
import net.minecraft.world.entity.EquipmentSlot;

class Dolphin$PlayWithItemsGoal extends Dolphin$ItemGoal {
   private Dolphin$PlayWithItemsGoal(final Dolphin this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(this$0x);
   }

   @Override
   public boolean canUse() {
      return !this.this$0.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty();
   }

   @Override
   public void stop() {
      this.dropItem();
   }

   @Override
   public void tick() {
      this.dropItem();
   }
}
