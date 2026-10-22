package net.minecraft.world.level.block;

import java.util.Objects;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

class RespawnAnchorBlock$1 extends ExplosionDamageCalculator {
   RespawnAnchorBlock$1(final RespawnAnchorBlock this$0, final BlockPos val$pos, final boolean val$inWater) {
      this.val$pos = val$posx;
      this.val$inWater = val$inWaterx;
      Objects.requireNonNull(this$0);
      super();
   }

   @Override
   public Optional<Float> getBlockExplosionResistance(
      final Explosion explosion, final BlockGetter level, final BlockPos testPos, final BlockState block, final FluidState fluid
   ) {
      return testPos.equals(this.val$pos) && this.val$inWater
         ? Optional.of(Blocks.WATER.getExplosionResistance())
         : super.getBlockExplosionResistance(explosion, level, testPos, block, fluid);
   }
}
