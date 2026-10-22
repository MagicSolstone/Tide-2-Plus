package net.minecraft.server.commands;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

record CloneCommands$DimensionAndPosition(ServerLevel dimension, BlockPos position) {
}
