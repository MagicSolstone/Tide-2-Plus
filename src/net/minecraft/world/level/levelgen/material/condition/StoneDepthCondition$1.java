package net.minecraft.world.level.levelgen.material.condition;

import java.util.Objects;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.material.MaterialRuleContext;
import net.minecraft.world.level.levelgen.material.MaterialRuleContext$LazyYCondition;

class StoneDepthCondition$1 extends MaterialRuleContext$LazyYCondition {
   StoneDepthCondition$1(final StoneDepthCondition this$0, final MaterialRuleContext context, final boolean val$ceiling) {
      this.val$ceiling = val$ceilingx;
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(context);
   }

   @Override
   protected boolean compute() {
      int stoneDepth = this.val$ceiling ? this.context.stoneDepthBelow() : this.context.stoneDepthAbove();
      int surfaceDepth = this.this$0.addSurfaceDepth ? this.context.surfaceDepth() : 0;
      int secondarySurfaceDepth = this.this$0.secondaryDepthRange == 0
         ? 0
         : (int)Mth.map(this.context.getSurfaceSecondary(), -1.0, 1.0, 0.0, (double)this.this$0.secondaryDepthRange);
      return stoneDepth <= 1 + this.this$0.offset + surfaceDepth + secondarySurfaceDepth;
   }
}
