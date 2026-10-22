package net.minecraft.world.entity.monster.illager;

import java.util.EnumSet;
import java.util.Objects;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.Goal$Flag;

public class SpellcasterIllager$SpellcasterCastingSpellGoal extends Goal {
   public SpellcasterIllager$SpellcasterCastingSpellGoal(final SpellcasterIllager this$0) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super();
      this.setFlags(EnumSet.of(Goal$Flag.MOVE, Goal$Flag.LOOK));
   }

   @Override
   public boolean canUse() {
      return this.this$0.getSpellCastingTime() > 0;
   }

   @Override
   public void start() {
      super.start();
      SpellcasterIllager.access$000(this.this$0).stop();
   }

   @Override
   public void stop() {
      super.stop();
      this.this$0.setIsCastingSpell(SpellcasterIllager$IllagerSpell.NONE);
   }

   @Override
   public void tick() {
      if (this.this$0.getTarget() != null) {
         this.this$0.getLookControl().setLookAt(this.this$0.getTarget(), this.this$0.getMaxHeadYRot(), this.this$0.getMaxHeadXRot());
      }
   }
}
