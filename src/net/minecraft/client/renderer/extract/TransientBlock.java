package net.minecraft.client.renderer.extract;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

@Environment(EnvType.CLIENT)
public record TransientBlock(BlockPos pos, BlockState state, double timeToLive, long createTimeNs) {
   public static double DEFAULT_TIME_TO_LIVE = 1.0;
}
