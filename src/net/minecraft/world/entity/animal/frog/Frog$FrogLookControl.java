package net.minecraft.world.entity.animal.frog;

import java.util.Objects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;

class Frog$FrogLookControl extends LookControl {
   public Frog$FrogLookControl(final Frog this$0, final Mob mob) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(mob);
   }

   @Override
   protected boolean resetXRotOnTick() {
      return this.this$0.getTongueTarget().isEmpty();
   }
}
