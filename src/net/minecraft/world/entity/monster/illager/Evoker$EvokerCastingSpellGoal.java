package net.minecraft.world.entity.monster.illager;

import java.util.Objects;

class Evoker$EvokerCastingSpellGoal extends SpellcasterIllager$SpellcasterCastingSpellGoal {
   private Evoker$EvokerCastingSpellGoal(final Evoker this$0) {
      Objects.requireNonNull(this$0x);
      this.this$0 = this$0x;
      super(this$0x);
   }

   @Override
   public void tick() {
      if (this.this$0.getTarget() != null) {
         this.this$0.getLookControl().setLookAt(this.this$0.getTarget(), this.this$0.getMaxHeadYRot(), this.this$0.getMaxHeadXRot());
      } else if (this.this$0.getWololoTarget() != null) {
         this.this$0.getLookControl().setLookAt(this.this$0.getWololoTarget(), this.this$0.getMaxHeadYRot(), this.this$0.getMaxHeadXRot());
      }
   }
}
