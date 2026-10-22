package net.minecraft.world.level.block;

import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SideChainPart;

public record SideChainPartBlock$Neighbors(
   SideChainPartBlock block, LevelAccessor level, Direction facing, BlockPos center, Map<BlockPos, SideChainPartBlock$Neighbor> cache
) {
   private boolean isConnectableToThisBlock(final BlockState neighbor) {
      return this.block.isConnectable(neighbor) && this.block.getFacing(neighbor) == this.facing;
   }

   private SideChainPartBlock$Neighbor createNewNeighbor(final BlockPos pos) {
      BlockState neighbor = this.level.getBlockState(pos);
      SideChainPart part = this.isConnectableToThisBlock(neighbor) ? this.block.getSideChainPart(neighbor) : null;
      return (SideChainPartBlock$Neighbor)(part == null
         ? new SideChainPartBlock$EmptyNeighbor(pos)
         : new SideChainPartBlock$SideChainNeighbor(this.level, this.block, pos, part));
   }

   private SideChainPartBlock$Neighbor getOrCreateNeighbor(final Direction dir, final Integer steps) {
      return this.cache.computeIfAbsent(this.center.relative(dir, steps), this::createNewNeighbor);
   }

   public SideChainPartBlock$Neighbor left(final int steps) {
      return this.getOrCreateNeighbor(this.facing.getClockWise(), steps);
   }

   public SideChainPartBlock$Neighbor right(final int steps) {
      return this.getOrCreateNeighbor(this.facing.getCounterClockWise(), steps);
   }

   public SideChainPartBlock$Neighbor left() {
      return this.left(1);
   }

   public SideChainPartBlock$Neighbor right() {
      return this.right(1);
   }
}
