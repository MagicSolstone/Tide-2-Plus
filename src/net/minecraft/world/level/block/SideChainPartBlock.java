package net.minecraft.world.level.block;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SideChainPart;

public interface SideChainPartBlock {
   SideChainPart getSideChainPart(final BlockState state);

   BlockState setSideChainPart(final BlockState state, final SideChainPart newPart);

   Direction getFacing(final BlockState state);

   boolean isConnectable(final BlockState state);

   int getMaxChainLength();

   default List<BlockPos> getAllBlocksConnectedTo(final LevelAccessor level, final BlockPos pos) {
      BlockState state = level.getBlockState(pos);
      if (!this.isConnectable(state)) {
         return List.of();
      } else {
         SideChainPartBlock$Neighbors neighbors = this.getNeighbors(level, pos, this.getFacing(state));
         List<BlockPos> results = new LinkedList<>();
         results.add(pos);
         this.addBlocksConnectingTowards(neighbors::left, SideChainPart.LEFT, results::addFirst);
         this.addBlocksConnectingTowards(neighbors::right, SideChainPart.RIGHT, results::addLast);
         return results;
      }
   }

   private void addBlocksConnectingTowards(
      final IntFunction<SideChainPartBlock$Neighbor> getNeighbor, final SideChainPart endPart, final Consumer<BlockPos> accumulator
   ) {
      for (int steps = 1; steps < this.getMaxChainLength(); steps++) {
         SideChainPartBlock$Neighbor neighbor = getNeighbor.apply(steps);
         if (neighbor.connectsTowards(endPart)) {
            accumulator.accept(neighbor.pos());
         }

         if (neighbor.isUnconnectableOrChainEnd()) {
            break;
         }
      }
   }

   default void updateNeighborsAfterPoweringDown(final LevelAccessor level, final BlockPos pos, final BlockState state) {
      SideChainPartBlock$Neighbors neighbors = this.getNeighbors(level, pos, this.getFacing(state));
      neighbors.left().disconnectFromRight();
      neighbors.right().disconnectFromLeft();
   }

   default void updateSelfAndNeighborsOnPoweringUp(final LevelAccessor level, final BlockPos pos, final BlockState state, final BlockState oldState) {
      if (this.isConnectable(state)) {
         if (!this.isBeingUpdatedByNeighbor(state, oldState)) {
            SideChainPartBlock$Neighbors neighbors = this.getNeighbors(level, pos, this.getFacing(state));
            SideChainPart newPartForSelf = SideChainPart.UNCONNECTED;
            int existingChainOnTheLeft = neighbors.left().isConnectable() ? this.getAllBlocksConnectedTo(level, neighbors.left().pos()).size() : 0;
            int existingChainOnTheRight = neighbors.right().isConnectable() ? this.getAllBlocksConnectedTo(level, neighbors.right().pos()).size() : 0;
            int currentChainLength = 1;
            if (this.canConnect(existingChainOnTheLeft, currentChainLength)) {
               newPartForSelf = newPartForSelf.whenConnectedToTheLeft();
               neighbors.left().connectToTheRight();
               currentChainLength += existingChainOnTheLeft;
            }

            if (this.canConnect(existingChainOnTheRight, currentChainLength)) {
               newPartForSelf = newPartForSelf.whenConnectedToTheRight();
               neighbors.right().connectToTheLeft();
            }

            this.setPart(level, pos, newPartForSelf);
         }
      }
   }

   private boolean canConnect(final int newBlocksToConnectTo, final int currentChainLength) {
      return newBlocksToConnectTo > 0 && currentChainLength + newBlocksToConnectTo <= this.getMaxChainLength();
   }

   private boolean isBeingUpdatedByNeighbor(final BlockState state, final BlockState oldState) {
      boolean isGettingConnected = this.getSideChainPart(state).isConnected();
      boolean hasBeenConnectedBefore = this.isConnectable(oldState) && this.getSideChainPart(oldState).isConnected();
      return isGettingConnected || hasBeenConnectedBefore;
   }

   private SideChainPartBlock$Neighbors getNeighbors(final LevelAccessor level, final BlockPos center, final Direction facing) {
      return new SideChainPartBlock$Neighbors(this, level, facing, center, new HashMap<>());
   }

   private void setPart(final LevelAccessor level, final BlockPos pos, final SideChainPart newPart) {
      BlockState state = level.getBlockState(pos);
      if (this.getSideChainPart(state) != newPart) {
         level.setBlockAndUpdate(pos, this.setSideChainPart(state, newPart));
      }
   }
}
