package net.minecraft.world.level.redstone;

import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

interface CollectingNeighborUpdater$NeighborUpdates {
   boolean runNext(Level level);

   void forEachUpdatedPos(Consumer<BlockPos> output);
}
