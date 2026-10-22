package net.minecraft.gametest.framework;

import java.util.ArrayList;
import java.util.Collection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos$MutableBlockPos;
import net.minecraft.world.phys.AABB;

final class StructureGridSpawner$DimensionGridState {
   private final BlockPos firstTestNorthWestCorner;
   private final BlockPos$MutableBlockPos nextCorner;
   private AABB rowBounds;
   private int currentRowCount;
   private final Collection<GameTestInfo> testsInLastBatch = new ArrayList<>();

   private StructureGridSpawner$DimensionGridState(final BlockPos start) {
      this.firstTestNorthWestCorner = start;
      this.nextCorner = start.mutable();
      this.rowBounds = new AABB(start);
      this.currentRowCount = 0;
   }
}
