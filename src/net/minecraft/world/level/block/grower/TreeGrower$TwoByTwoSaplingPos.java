package net.minecraft.world.level.block.grower;

import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

record TreeGrower$TwoByTwoSaplingPos(int offsetX, int offsetZ, List<Pair<BlockState, BlockPos>> groundLevelSurroundingBlocks) {
}
