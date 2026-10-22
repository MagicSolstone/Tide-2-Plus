package net.minecraft.world.level.levelgen.material.condition;

import java.util.Objects;
import net.minecraft.world.level.levelgen.material.MaterialRuleContext;
import net.minecraft.world.level.levelgen.material.MaterialRuleContext$LazyXZCondition;

class SteepCondition$1 extends MaterialRuleContext$LazyXZCondition {
   SteepCondition$1(final SteepCondition this$0, final MaterialRuleContext context) {
      Objects.requireNonNull(this$0);
      super(context);
   }

   @Override
   protected boolean compute() {
      return this.context.surfaceGradientX() <= -4 || this.context.surfaceGradientZ() >= 4;
   }
}
