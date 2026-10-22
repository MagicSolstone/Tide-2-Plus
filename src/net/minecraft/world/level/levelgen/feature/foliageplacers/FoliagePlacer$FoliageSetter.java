package net.minecraft.world.level.levelgen.feature.foliageplacers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public interface FoliagePlacer$FoliageSetter {
   void set(final BlockPos pos, final BlockState state);

   boolean isSet(final BlockPos pos);
}
