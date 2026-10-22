package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.properties.SideChainPart;

public record SideChainPartBlock$EmptyNeighbor(BlockPos pos) implements SideChainPartBlock$Neighbor {
   @Override
   public boolean isConnectable() {
      return false;
   }

   @Override
   public boolean isUnconnectableOrChainEnd() {
      return true;
   }

   @Override
   public boolean connectsTowards(final SideChainPart endPart) {
      return false;
   }
}
