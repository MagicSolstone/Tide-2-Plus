package net.minecraft.world.level.levelgen.material.condition;

import java.util.Objects;
import net.minecraft.world.level.levelgen.material.MaterialRuleContext;
import net.minecraft.world.level.levelgen.material.MaterialRuleContext$LazyYCondition;

class YCondition$1 extends MaterialRuleContext$LazyYCondition {
   YCondition$1(final YCondition this$0, final MaterialRuleContext context) {
      Objects.requireNonNull(this$0);
      this.this$0 = this$0;
      super(context);
   }

   @Override
   protected boolean compute() {
      return this.context.blockY() + (this.this$0.addStoneDepth ? this.context.stoneDepthAbove() : 0)
         >= this.context.resolveAnchorY(this.this$0.anchor) + this.context.surfaceDepth() * this.this$0.surfaceDepthMultiplier;
   }
}
