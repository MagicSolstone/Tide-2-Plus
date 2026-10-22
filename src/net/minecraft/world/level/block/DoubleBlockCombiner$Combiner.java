package net.minecraft.world.level.block;

public interface DoubleBlockCombiner$Combiner<S, T> {
   T acceptDouble(S first, S second);

   T acceptSingle(S single);

   T acceptNone();
}
