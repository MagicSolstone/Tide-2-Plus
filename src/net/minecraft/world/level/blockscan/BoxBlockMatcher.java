package net.minecraft.world.level.blockscan;

import java.util.function.BiConsumer;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Continuation;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;

public class BoxBlockMatcher extends BlockMatcher {
   private final BlockPos from;
   private final BlockPos to;

   public BoxBlockMatcher(final LevelReader level, final BlockPos from, final BlockPos to) {
      super(level);
      this.from = from;
      this.to = to;
   }

   public BoxBlockMatcher filterState(final Predicate<BlockState> predicate) {
      super.filterState(predicate);
      return this;
   }

   @Override
   public boolean atLeastMatched(final int n) {
      return BlockScanUtils.findBlocks(this.level, this.from, this.to, this.statePredicate, new BoxBlockMatcher$1(this, n));
   }

   @Override
   protected boolean anyMatched(final Predicate<BlockState> statePredicate) {
      return BlockScanUtils.findBlocks(this.level, this.from, this.to, statePredicate, (var0, var1) -> Continuation.ABORT);
   }

   @Override
   public void forEach(final BiConsumer<BlockPos, BlockState> consumer) {
      BlockScanUtils.findBlocks(this.level, this.from, this.to, this.statePredicate, (pos, state) -> {
         consumer.accept(pos, state);
         return Continuation.CONTINUE;
      });
   }

   @Override
   public boolean forEachUntil(final BlockStateConsumer consumer) {
      return BlockScanUtils.findBlocks(this.level, this.from, this.to, this.statePredicate, consumer);
   }
}
