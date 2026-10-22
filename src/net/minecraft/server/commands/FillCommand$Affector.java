package net.minecraft.server.commands;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

@FunctionalInterface
public interface FillCommand$Affector {
   FillCommand$Affector NOOP = (l, p) -> false;

   boolean affect(final ServerLevel level, final BlockPos pos);
}
