package net.minecraft.world.level.levelgen.material.condition;

import java.util.Objects;
import net.minecraft.world.level.levelgen.material.MaterialRuleContext;
import net.minecraft.world.level.levelgen.material.MaterialRuleContext$LazyXZCondition;

class HoleCondition$1 extends MaterialRuleContext$LazyXZCondition {
   HoleCondition$1(final HoleCondition this$0, final MaterialRuleContext context) {
      Objects.requireNonNull(this$0);
      super(context);
   }

   @Override
   protected boolean compute() {
      return this.context.surfaceDepth() <= 0;
   }
}
