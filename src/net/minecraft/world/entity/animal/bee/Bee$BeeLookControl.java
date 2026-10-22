package net.minecraft.world.entity.animal.bee;

import java.util.Objects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;

class Bee$BeeLookControl extends LookControl {
   public Bee$BeeLookControl(final Bee this$0, final Mob mob) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(mob);
   }

   @Override
   public void tick() {
      if (!this.this$0.isAngry()) {
         super.tick();
      }
   }

   @Override
   protected boolean resetXRotOnTick() {
      return !this.this$0.beePollinateGoal.isPollinating();
   }
}
