package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

enum MultifaceSpreader$SpreadType$3 {
   @Override
   public MultifaceSpreader$SpreadPos getSpreadPos(final BlockPos pos, final Direction spreadDirection, final Direction fromFace) {
      return new MultifaceSpreader$SpreadPos(pos.relative(spreadDirection).relative(fromFace), spreadDirection.getOpposite());
   }
}
