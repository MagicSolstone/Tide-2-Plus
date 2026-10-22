package net.minecraft.server.commands;

import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.jspecify.annotations.Nullable;

@FunctionalInterface
public interface FillCommand$Filter {
   FillCommand$Filter NOOP = (r, p, b, l) -> b;

   @Nullable
   BlockInput filter(final BoundingBox region, final BlockPos pos, final BlockInput block, final ServerLevel level);
}
