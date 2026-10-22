package net.minecraft.world.level.block;

import java.util.Optional;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.entity.ChestBlockEntity;

class ChestBlock$1 implements DoubleBlockCombiner$Combiner<ChestBlockEntity, Optional<Container>> {
   public Optional<Container> acceptDouble(final ChestBlockEntity first, final ChestBlockEntity second) {
      return Optional.of(new CompoundContainer(first, second));
   }

   public Optional<Container> acceptSingle(final ChestBlockEntity single) {
      return Optional.of(single);
   }

   public Optional<Container> acceptNone() {
      return Optional.empty();
   }
}
