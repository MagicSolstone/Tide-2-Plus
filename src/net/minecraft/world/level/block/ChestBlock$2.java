package net.minecraft.world.level.block;

import java.util.Optional;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.ChestBlockEntity;

class ChestBlock$2 implements DoubleBlockCombiner$Combiner<ChestBlockEntity, Optional<MenuProvider>> {
   public Optional<MenuProvider> acceptDouble(final ChestBlockEntity first, final ChestBlockEntity second) {
      Container container = new CompoundContainer(first, second);
      return Optional.of(new ChestBlock$2$1(this, first, second, container));
   }

   public Optional<MenuProvider> acceptSingle(final ChestBlockEntity single) {
      return Optional.of(single);
   }

   public Optional<MenuProvider> acceptNone() {
      return Optional.empty();
   }
}
