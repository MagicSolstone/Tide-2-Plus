package net.minecraft.world.level.material;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

record FlowingFluid$BlockStatePairKey(BlockState first, BlockState second, Direction direction) {
   @Override
   public boolean equals(final Object o) {
      return o instanceof FlowingFluid$BlockStatePairKey that && this.first == that.first && this.second == that.second && this.direction == that.direction;
   }

   @Override
   public int hashCode() {
      int result = System.identityHashCode(this.first);
      result = 31 * result + System.identityHashCode(this.second);
      return 31 * result + this.direction.hashCode();
   }
}
