package net.minecraft.world.level.levelgen.feature.foliageplacers;

import net.minecraft.core.BlockPos;

public record FoliagePlacer$FoliageAttachment(BlockPos pos, int radiusOffsetXZ, int foliageHeightOffset, int sizeX, int sizeZ) {
   public FoliagePlacer$FoliageAttachment(final BlockPos pos, final int radiusOffsetXZ, final boolean doubleTrunk) {
      int sizeXZ = doubleTrunk ? 2 : 1;
      this(pos, radiusOffsetXZ, sizeXZ, sizeXZ);
   }

   public FoliagePlacer$FoliageAttachment(final BlockPos pos, final int radiusOffsetXZ, final int sizeX, final int sizeZ) {
      this(pos, radiusOffsetXZ, 0, sizeX, sizeZ);
   }

   public boolean doubleTrunk() {
      return this.sizeX == 2 && this.sizeZ == 2;
   }
}
