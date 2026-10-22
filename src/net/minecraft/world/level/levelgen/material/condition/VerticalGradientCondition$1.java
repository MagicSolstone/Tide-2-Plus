package net.minecraft.world.level.levelgen.material.condition;

import java.util.Objects;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import net.minecraft.world.level.levelgen.material.MaterialRuleContext;
import net.minecraft.world.level.levelgen.material.MaterialRuleContext$LazyYCondition;

class VerticalGradientCondition$1 extends MaterialRuleContext$LazyYCondition {
   VerticalGradientCondition$1(
      final VerticalGradientCondition this$0,
      final MaterialRuleContext context,
      final int val$trueAtAndBelow,
      final int val$falseAtAndAbove,
      final PositionalRandomFactory val$randomFactory
   ) {
      this.val$trueAtAndBelow = val$trueAtAndBelowx;
      this.val$falseAtAndAbove = val$falseAtAndAbovex;
      this.val$randomFactory = val$randomFactoryx;
      Objects.requireNonNull(this$0);
      super(context);
   }

   @Override
   protected boolean compute() {
      int blockY = this.context.blockY();
      if (blockY <= this.val$trueAtAndBelow) {
         return true;
      } else if (blockY >= this.val$falseAtAndAbove) {
         return false;
      } else {
         double probability = Mth.map((double)blockY, (double)this.val$trueAtAndBelow, (double)this.val$falseAtAndAbove, 1.0, 0.0);
         RandomSource random = this.val$randomFactory.at(this.context.blockX(), blockY, this.context.blockZ());
         return random.nextFloat() < probability;
      }
   }
}
