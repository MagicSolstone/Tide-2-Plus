package net.minecraft.world.entity.monster;

import java.util.Objects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.BodyRotationControl;

class Phantom$PhantomBodyRotationControl extends BodyRotationControl {
   public Phantom$PhantomBodyRotationControl(final Phantom this$0, final Mob mob) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(mob);
   }

   @Override
   public void clientTick() {
      this.this$0.yHeadRot = this.this$0.yBodyRot;
      this.this$0.yBodyRot = this.this$0.getYRot();
   }
}
