package net.minecraft.world.level.block;

public final class DoubleBlockCombiner$NeighborCombineResult$Single<S> implements DoubleBlockCombiner$NeighborCombineResult<S> {
   private final S single;

   public DoubleBlockCombiner$NeighborCombineResult$Single(final S single) {
      this.single = single;
   }

   @Override
   public <T> T apply(final DoubleBlockCombiner$Combiner<? super S, T> callback) {
      return callback.acceptSingle(this.single);
   }
}
