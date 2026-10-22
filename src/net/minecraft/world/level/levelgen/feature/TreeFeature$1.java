package net.minecraft.world.level.levelgen.feature;

import java.util.Objects;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer$FoliageSetter;

class TreeFeature$1 implements FoliagePlacer$FoliageSetter {
   TreeFeature$1(final TreeFeature this$0, final Set val$foliage, final WorldGenLevel val$level) {
      this.val$foliage = val$foliagex;
      this.val$level = val$levelx;
      Objects.requireNonNull(this$0);
      super();
   }

   @Override
   public void set(final BlockPos pos, final BlockState state) {
      this.val$foliage.add(pos.immutable());
      this.val$level.setBlock(pos, state, 19);
   }

   @Override
   public boolean isSet(final BlockPos pos) {
      return this.val$foliage.contains(pos);
   }
}
