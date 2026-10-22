package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public enum MultifaceSpreader$SpreadType {
   SAME_POSITION,
   SAME_PLANE,
   WRAP_AROUND;

   public abstract MultifaceSpreader$SpreadPos getSpreadPos(final BlockPos pos, final Direction spreadDirection, final Direction fromFace);
}
