package net.minecraft.world.entity.animal.fox;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal$Flag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

class Fox$FoxSearchForItemsGoal extends Goal {
   public Fox$FoxSearchForItemsGoal(final Fox this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.setFlags(EnumSet.of(Goal$Flag.MOVE));
   }

   @Override
   public boolean canUse() {
      if (!this.this$0.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
         return false;
      } else if (this.this$0.getTarget() != null || this.this$0.getLastHurtByMob() != null) {
         return false;
      } else if (!this.this$0.canMove()) {
         return false;
      } else if (this.this$0.getRandom().nextInt(reducedTickDelay(10)) != 0) {
         return false;
      } else {
         List<ItemEntity> items = this.this$0
            .level()
            .getEntitiesOfClass(ItemEntity.class, this.this$0.getBoundingBox().inflate(8.0, 8.0, 8.0), Fox.ALLOWED_ITEMS);
         return !items.isEmpty() && this.this$0.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty();
      }
   }

   @Override
   public void tick() {
      List<ItemEntity> items = this.this$0.level().getEntitiesOfClass(ItemEntity.class, this.this$0.getBoundingBox().inflate(8.0, 8.0, 8.0), Fox.ALLOWED_ITEMS);
      ItemStack itemStack = this.this$0.getItemBySlot(EquipmentSlot.MAINHAND);
      if (itemStack.isEmpty() && !items.isEmpty()) {
         this.this$0.getNavigation().moveTo(items.get(0), 1.2F);
      }
   }

   @Override
   public void start() {
      List<ItemEntity> items = this.this$0.level().getEntitiesOfClass(ItemEntity.class, this.this$0.getBoundingBox().inflate(8.0, 8.0, 8.0), Fox.ALLOWED_ITEMS);
      if (!items.isEmpty()) {
         this.this$0.getNavigation().moveTo(items.get(0), 1.2F);
      }
   }
}
