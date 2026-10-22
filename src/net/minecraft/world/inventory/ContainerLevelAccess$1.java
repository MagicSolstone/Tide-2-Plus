package net.minecraft.world.inventory;

import java.util.Optional;
import java.util.function.BiFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

class ContainerLevelAccess$1 implements ContainerLevelAccess {
   @Override
   public <T> Optional<T> evaluate(final BiFunction<Level, BlockPos, T> action) {
      return Optional.empty();
   }
}
