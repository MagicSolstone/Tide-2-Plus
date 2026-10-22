package net.minecraft.gametest.framework;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import org.jspecify.annotations.Nullable;

record TestCommand$Info(@Nullable ServerPlayer player, BlockPos playerPos, BlockPos testPos, int surfaceY) {
}
