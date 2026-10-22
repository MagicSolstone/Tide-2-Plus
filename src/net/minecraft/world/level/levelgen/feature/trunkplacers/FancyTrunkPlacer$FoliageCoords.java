package net.minecraft.world.level.levelgen.feature.trunkplacers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer$FoliageAttachment;

class FancyTrunkPlacer$FoliageCoords {
   private final FoliagePlacer$FoliageAttachment attachment;
   private final int branchBase;

   public FancyTrunkPlacer$FoliageCoords(final BlockPos pos, final int branchBase) {
      this.attachment = new FoliagePlacer$FoliageAttachment(pos, 0, false);
      this.branchBase = branchBase;
   }

   public int getBranchBase() {
      return this.branchBase;
   }
}
