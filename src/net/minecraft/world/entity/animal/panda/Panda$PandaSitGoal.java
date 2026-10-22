package net.minecraft.world.entity.animal.panda;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal$Flag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

class Panda$PandaSitGoal extends Goal {
   private int cooldown;

   public Panda$PandaSitGoal(final Panda this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super();
      this.setFlags(EnumSet.of(Goal$Flag.MOVE));
   }

   @Override
   public boolean canUse() {
      if (this.cooldown > this.this$0.tickCount
         || this.this$0.isBaby()
         || this.this$0.isInWater()
         || !this.this$0.canPerformAction()
         || this.this$0.getUnhappyCounter() > 0) {
         return false;
      } else {
         return !this.this$0.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()
            ? true
            : !this.this$0.level().getEntitiesOfClass(ItemEntity.class, this.this$0.getBoundingBox().inflate(6.0, 6.0, 6.0), Panda::canPickUpAndEat).isEmpty();
      }
   }

   @Override
   public boolean canContinueToUse() {
      return !this.this$0.isInWater() && (this.this$0.isLazy() || Panda.access$400(this.this$0).nextInt(reducedTickDelay(600)) != 1)
         ? Panda.access$500(this.this$0).nextInt(reducedTickDelay(2000)) != 1
         : false;
   }

   @Override
   public void tick() {
      if (!this.this$0.isSitting() && !this.this$0.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
         this.this$0.tryToSit();
      }
   }

   @Override
   public void start() {
      if (this.this$0.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
         List<ItemEntity> items = this.this$0
            .level()
            .getEntitiesOfClass(ItemEntity.class, this.this$0.getBoundingBox().inflate(8.0, 8.0, 8.0), Panda::canPickUpAndEat);
         if (!items.isEmpty()) {
            this.this$0.getNavigation().moveTo(items.getFirst(), 1.2F);
         }
      } else {
         this.this$0.tryToSit();
      }

      this.cooldown = 0;
   }

   @Override
   public void stop() {
      ItemStack itemStack = this.this$0.getItemBySlot(EquipmentSlot.MAINHAND);
      if (!itemStack.isEmpty()) {
         this.this$0.spawnAtLocation(getServerLevel(this.this$0.level()), itemStack);
         this.this$0.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
         int waitSeconds = this.this$0.isLazy() ? Panda.access$600(this.this$0).nextInt(50) + 10 : Panda.access$700(this.this$0).nextInt(150) + 10;
         this.cooldown = this.this$0.tickCount + waitSeconds * 20;
      }

      this.this$0.sit(false);
   }
}
