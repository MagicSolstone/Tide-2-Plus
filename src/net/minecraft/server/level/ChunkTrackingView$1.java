package net.minecraft.server.level;

import java.util.function.Consumer;
import net.minecraft.world.level.ChunkPos;

class ChunkTrackingView$1 implements ChunkTrackingView {
   @Override
   public boolean contains(final int chunkX, final int chunkZ, final boolean includeNeighbors) {
      return false;
   }

   @Override
   public void forEach(final Consumer<ChunkPos> consumer) {
   }
}
