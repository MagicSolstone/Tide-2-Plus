package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.properties.SideChainPart;

public record SideChainPartBlock$SideChainNeighbor(LevelAccessor level, SideChainPartBlock block, BlockPos pos, SideChainPart part)
   implements SideChainPartBlock$Neighbor {
   @Override
   public boolean isConnectable() {
      return true;
   }

   @Override
   public boolean isUnconnectableOrChainEnd() {
      return this.part.isChainEnd();
   }

   @Override
   public boolean connectsTowards(final SideChainPart endPart) {
      return this.part.isConnectionTowards(endPart);
   }

   @Override
   public void connectToTheRight() {
      this.block.setPart(this.level, this.pos, this.part.whenConnectedToTheRight());
   }

   @Override
   public void connectToTheLeft() {
      this.block.setPart(this.level, this.pos, this.part.whenConnectedToTheLeft());
   }

   @Override
   public void disconnectFromRight() {
      this.block.setPart(this.level, this.pos, this.part.whenDisconnectedFromTheRight());
   }

   @Override
   public void disconnectFromLeft() {
      this.block.setPart(this.level, this.pos, this.part.whenDisconnectedFromTheLeft());
   }
}
