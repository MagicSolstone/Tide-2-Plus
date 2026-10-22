package net.minecraft.world.entity.animal.dolphin;

import java.util.Objects;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

abstract class Dolphin$ItemGoal extends Goal {
   private Dolphin$ItemGoal(final Dolphin this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
   }

   protected void drop(final ItemStack itemStack) {
      if (!itemStack.isEmpty()) {
         double yHandPos = this.this$0.getEyeY() - 0.3F;
         ItemEntity thrownItem = new ItemEntity(this.this$0.level(), this.this$0.getX(), yHandPos, this.this$0.getZ(), itemStack);
         thrownItem.setPickUpDelay(40);
         thrownItem.setThrower(this.this$0);
         float pow = 0.3F;
         float dir = Dolphin.access$000(this.this$0).nextFloat() * (float) (Math.PI * 2);
         float pow2 = 0.02F * Dolphin.access$100(this.this$0).nextFloat();
         thrownItem.setDeltaMovement(
            0.3F * -Mth.sin(this.this$0.getYRot() * (float) (Math.PI / 180.0)) * Mth.cos(this.this$0.getXRot() * (float) (Math.PI / 180.0))
               + Mth.cos(dir) * pow2,
            0.3F * Mth.sin(this.this$0.getXRot() * (float) (Math.PI / 180.0)) * 1.5F,
            0.3F * Mth.cos(this.this$0.getYRot() * (float) (Math.PI / 180.0)) * Mth.cos(this.this$0.getXRot() * (float) (Math.PI / 180.0))
               + Mth.sin(dir) * pow2
         );
         this.this$0.level().addFreshEntity(thrownItem);
      }
   }

   protected boolean dropItem() {
      ItemStack itemStack = this.this$0.getItemBySlot(EquipmentSlot.MAINHAND);
      if (itemStack.isEmpty()) {
         return false;
      } else {
         this.drop(itemStack);
         this.this$0.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
         return true;
      }
   }
}
