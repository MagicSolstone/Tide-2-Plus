package net.minecraft.server.level;

import com.google.common.annotations.VisibleForTesting;
import java.util.function.Consumer;
import net.minecraft.world.level.ChunkPos;

public record ChunkTrackingView$Positioned(ChunkPos center, int viewDistance) implements ChunkTrackingView {
   private int minX() {
      return this.center.x() - this.viewDistance - 1;
   }

   private int minZ() {
      return this.center.z() - this.viewDistance - 1;
   }

   private int maxX() {
      return this.center.x() + this.viewDistance + 1;
   }

   private int maxZ() {
      return this.center.z() + this.viewDistance + 1;
   }

   @VisibleForTesting
   boolean squareIntersects(final ChunkTrackingView$Positioned other) {
      return this.minX() <= other.maxX() && this.maxX() >= other.minX() && this.minZ() <= other.maxZ() && this.maxZ() >= other.minZ();
   }

   @Override
   public boolean contains(final int chunkX, final int chunkZ, final boolean includeNeighbors) {
      return ChunkTrackingView.isWithinDistance(this.center.x(), this.center.z(), this.viewDistance, chunkX, chunkZ, includeNeighbors);
   }

   @Override
   public void forEach(final Consumer<ChunkPos> consumer) {
      for (int x = this.minX(); x <= this.maxX(); x++) {
         for (int z = this.minZ(); z <= this.maxZ(); z++) {
            if (this.contains(x, z)) {
               consumer.accept(new ChunkPos(x, z));
            }
         }
      }
   }
}
