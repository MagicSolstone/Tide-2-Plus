package net.minecraft.network.protocol.game;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jspecify.annotations.Nullable;

@FunctionalInterface
public interface ClientboundLevelChunkPacketData$BlockEntityTagOutput {
   void accept(BlockPos pos, BlockEntityType<?> type, @Nullable CompoundTag tag);
}
