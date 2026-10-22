package net.minecraft.world.inventory;

import java.util.Optional;
import java.util.function.BiFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

class ContainerLevelAccess$2 implements ContainerLevelAccess {
   ContainerLevelAccess$2(final Level val$level, final BlockPos val$pos) {
      this.val$level = val$levelx;
      this.val$pos = val$posx;
   }

   @Override
   public <T> Optional<T> evaluate(final BiFunction<Level, BlockPos, T> action) {
      return Optional.of(action.apply(this.val$level, this.val$pos));
   }
}
