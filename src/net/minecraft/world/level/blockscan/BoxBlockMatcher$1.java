package net.minecraft.world.level.blockscan;

import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Continuation;
import net.minecraft.world.level.block.state.BlockState;

class BoxBlockMatcher$1 implements BlockStateConsumer {
   private int count;

   BoxBlockMatcher$1(final BoxBlockMatcher this$0, final int val$n) {
      this.val$n = val$nx;
      Objects.requireNonNull(this$0);
      super();
      this.count = 0;
   }

   @Override
   public Continuation apply(final BlockPos pos, final BlockState state) {
      return Continuation.abortIf(++this.count >= this.val$n);
   }
}
