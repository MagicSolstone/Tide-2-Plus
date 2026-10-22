package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.properties.SideChainPart;

public sealed interface SideChainPartBlock$Neighbor permits SideChainPartBlock$EmptyNeighbor, SideChainPartBlock$SideChainNeighbor {
   BlockPos pos();

   boolean isConnectable();

   boolean isUnconnectableOrChainEnd();

   boolean connectsTowards(final SideChainPart endPart);

   default void connectToTheRight() {
   }

   default void connectToTheLeft() {
   }

   default void disconnectFromRight() {
   }

   default void disconnectFromLeft() {
   }
}
