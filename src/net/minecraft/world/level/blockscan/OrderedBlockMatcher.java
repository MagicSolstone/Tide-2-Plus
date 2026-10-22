package net.minecraft.world.level.blockscan;

import com.google.common.collect.Iterables;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Continuation;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.mutable.MutableObject;

public class OrderedBlockMatcher extends BlockMatcher {
   private Iterable<BlockPos> positions;

   public OrderedBlockMatcher(final LevelReader level, final Iterable<BlockPos> positions) {
      super(level);
      this.positions = positions;
   }

   public OrderedBlockMatcher filterPos(final Predicate<? super BlockPos> predicate) {
      this.positions = Iterables.filter(this.positions, predicate::test);
      return this;
   }

   public OrderedBlockMatcher filterState(final Predicate<BlockState> predicate) {
      super.filterState(predicate);
      return this;
   }

   @Override
   public boolean atLeastMatched(final int n) {
      return BlockScanUtils.findBlocksWithCache(this.level, this.positions, this.statePredicate, new OrderedBlockMatcher$1(this, n));
   }

   @Override
   protected boolean anyMatched(final Predicate<BlockState> statePredicate) {
      return BlockScanUtils.findBlocksWithCache(this.level, this.positions, statePredicate, (var0, var1) -> Continuation.ABORT);
   }

   @Override
   public void forEach(final BiConsumer<BlockPos, BlockState> consumer) {
      BlockScanUtils.findBlocksWithCache(this.level, this.positions, this.statePredicate, (pos, state) -> {
         consumer.accept(pos, state);
         return Continuation.CONTINUE;
      });
   }

   @Override
   public boolean forEachUntil(final BlockStateConsumer consumer) {
      return BlockScanUtils.findBlocksWithCache(this.level, this.positions, this.statePredicate, consumer);
   }

   public Optional<BlockPos> findFirst() {
      MutableObject<Optional<BlockPos>> result = new MutableObject(Optional.empty());
      BlockScanUtils.findBlocksWithCache(this.level, this.positions, this.statePredicate, (pos, var2) -> {
         result.setValue(Optional.of(pos.immutable()));
         return Continuation.ABORT;
      });
      return (Optional<BlockPos>)result.get();
   }

   public Optional<BlockPos> findFirst(final BiPredicate<BlockPos, BlockState> predicate) {
      MutableObject<Optional<BlockPos>> result = new MutableObject(Optional.empty());
      BlockScanUtils.findBlocksWithCache(this.level, this.positions, this.statePredicate, (pos, state) -> {
         if (predicate.test(pos, state)) {
            result.setValue(Optional.of(pos.immutable()));
            return Continuation.ABORT;
         } else {
            return Continuation.CONTINUE;
         }
      });
      return (Optional<BlockPos>)result.get();
   }
}
