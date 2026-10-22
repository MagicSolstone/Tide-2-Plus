package net.minecraft.world.entity.animal.dolphin;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.goal.Goal$Flag;
import net.minecraft.world.entity.item.ItemEntity;

class Dolphin$MoveToItemGoal extends Dolphin$ItemGoal {
   private int cooldown;

   public Dolphin$MoveToItemGoal(final Dolphin this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(this$0x);
      this.setFlags(EnumSet.of(Goal$Flag.MOVE));
   }

   @Override
   public boolean canUse() {
      if (this.cooldown > this.this$0.tickCount) {
         return false;
      } else {
         List<ItemEntity> items = this.this$0
            .level()
            .getEntitiesOfClass(ItemEntity.class, this.this$0.getBoundingBox().inflate(8.0, 8.0, 8.0), Dolphin.ALLOWED_ITEMS);
         return !items.isEmpty();
      }
   }

   @Override
   public void stop() {
      if (this.dropItem()) {
         this.cooldown = this.this$0.tickCount + Dolphin.access$200(this.this$0).nextInt(100);
      }
   }

   @Override
   public void start() {
      List<ItemEntity> items = this.this$0
         .level()
         .getEntitiesOfClass(ItemEntity.class, this.this$0.getBoundingBox().inflate(8.0, 8.0, 8.0), Dolphin.ALLOWED_ITEMS);
      if (!items.isEmpty()) {
         this.this$0.getNavigation().moveTo(items.get(0), 1.2F);
         this.this$0.playSound(SoundEvents.DOLPHIN_PLAY, 1.0F, 1.0F);
      }

      this.cooldown = 0;
   }

   @Override
   public void tick() {
      List<ItemEntity> items = this.this$0
         .level()
         .getEntitiesOfClass(ItemEntity.class, this.this$0.getBoundingBox().inflate(8.0, 8.0, 8.0), Dolphin.ALLOWED_ITEMS);
      if (!items.isEmpty()) {
         this.this$0.getNavigation().moveTo(items.get(0), 1.2F);
      }
   }
}
