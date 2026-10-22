package net.minecraft.world.level.block;

public final class DoubleBlockCombiner$NeighborCombineResult$Double<S> implements DoubleBlockCombiner$NeighborCombineResult<S> {
   private final S first;
   private final S second;

   public DoubleBlockCombiner$NeighborCombineResult$Double(final S first, final S second) {
      this.first = first;
      this.second = second;
   }

   @Override
   public <T> T apply(final DoubleBlockCombiner$Combiner<? super S, T> callback) {
      return callback.acceptDouble(this.first, this.second);
   }
}
